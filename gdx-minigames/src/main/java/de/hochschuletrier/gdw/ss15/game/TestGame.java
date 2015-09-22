package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ss15.game.input.InputKeyboard;
import de.hochschuletrier.gdw.ss15.game.systems.InputBallSystem;
import de.hochschuletrier.gdw.ss15.game.utils.PhysixUtil;

public class TestGame extends AbstractGame {

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        setupPhysixWorld();
        
        Gdx.input.setInputProcessor( new InputKeyboard());

        Entity player = createEntity("player", 300, 300);
        player.add(engine.createComponent(LocalPlayerComponent.class));
        player.add(engine.createComponent(InputBallComponent.class));
    }

    @Override
    protected void addSystems() {
        super.addSystems();
        engine.addSystem(new InputBallSystem());
    }

    @Override
    protected void addContactListeners(PhysixComponentAwareContactListener contactListener) {
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 24);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(410, 500).fixedRotation(false);
        Body body = physixSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(800, 20));
        PhysixUtil.createHollowCircle(physixSystem, 180, 180, 150, 30, 6);

        createTrigger(410, 600, 3200, 40, (Entity entity) -> {
            engine.removeEntity(entity);
        });
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            createEntity("ball", screenX, screenY);
        } else {
            createEntity("box", screenX, screenY);
        }
        return true;
    }
}
