package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.events.ChangeBallOwnershipEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.MagneticFieldComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;

public class MagneticFieldRenderSystem extends IteratingSystem implements ChangeBallOwnershipEvent.Listener {

    private final AnimationExtended animMinus;
    private final AnimationExtended animMinusReverse;
    private final AnimationExtended animPlus;
    private final AnimationExtended animPlusReverse;
    private Team ballTeam = null;

    public MagneticFieldRenderSystem(AssetManagerX assetManager, int priority) {
        super(Family.all(MagneticFieldComponent.class, PositionComponent.class, TeamComponent.class).get(), priority);
        
        animMinus = assetManager.getAnimation("magnetic_field_minus");
        animMinusReverse = assetManager.getAnimation("magnetic_field_minus_reverse");
        animPlus = assetManager.getAnimation("magnetic_field_plus");
        animPlusReverse = assetManager.getAnimation("magnetic_field_plus_reverse");
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        ChangeBallOwnershipEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        ChangeBallOwnershipEvent.unregister(this);
        //fixme: gamestate event
    }

    @Override
    public void update(float deltaTime) {
        if(ballTeam != null)
            super.update(deltaTime);
    }

    
    private AnimationExtended getAnimation(Team team) {
        if(team == Team.BLUE) {
            if(ballTeam == team)
                return animMinus;
            else
                return animMinusReverse;
        } else {
            if(ballTeam == team)
                return animPlus;
            else
                return animPlusReverse;
        }
    }
    
    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MagneticFieldComponent magneticField = ComponentMappers.magneticField.get(entity);
        TeamComponent team = ComponentMappers.team.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);

        magneticField.stateTime += deltaTime;
        AnimationExtended animation = getAnimation(team.team);
        TextureRegion keyFrame = animation.getKeyFrame(magneticField.stateTime);
        float radius = magneticField.range;
        float size = radius * 2;
        DrawUtil.batch.draw(keyFrame, position.x - radius, position.y - radius, radius, radius, size, size, 1, 1, magneticField.stateTime * 30);
    }

    @Override
    public void onChangeBallOwnershipEvent(Entity owner) {
        if(owner != null)
            ballTeam = ComponentMappers.team.get(owner).team;
    }
}
