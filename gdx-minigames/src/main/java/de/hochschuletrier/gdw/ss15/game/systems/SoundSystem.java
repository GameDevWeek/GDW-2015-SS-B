package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SoundEmitterComponent;

public class SoundSystem extends IteratingSystem implements SoundEvent.Listener {

    private final AssetManagerX assetManager;
    private Engine engine;

    public SoundSystem(int priority) {
        super(Family.all(SoundEmitterComponent.class, PositionComponent.class).get(), priority);
        this.assetManager = Main.getInstance().getAssetManager();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
        SoundEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        SoundEvent.unregister(this);
        this.engine = null;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        LimitedSmoothCameraSystem camSys = engine.getSystem(LimitedSmoothCameraSystem.class);
        if(camSys != null) {
            LimitedSmoothCamera camera = camSys.getCamera();
            // Use Camera position as listener position
            Vector3 pos = camera.getPosition();
            SoundEmitter.setListenerPosition(pos.x, pos.y, 10, SoundEmitter.Mode.STEREO);
            SoundEmitter.updateGlobal();
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SoundEmitterComponent soundEmitter = ComponentMappers.soundEmitter.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        soundEmitter.emitter.setPosition(position.x, position.y, 0);
        soundEmitter.emitter.update();
    }

    @Override
    public void onSoundEvent(String sound, Entity entity) {
        if(entity != null) {
            SoundEmitterComponent soundEmitter = ComponentMappers.soundEmitter.get(entity);
            soundEmitter.emitter.play(assetManager.getSound(sound), false);
        } else {
            SoundEmitter.playGlobal(assetManager.getSound(sound), false);
        }
    }
}
