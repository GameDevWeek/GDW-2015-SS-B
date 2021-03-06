package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;
import de.hochschuletrier.gdw.ss15.game.components.MagneticFieldComponent;
import de.hochschuletrier.gdw.ss15.game.components.MagneticInfluenceComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;

public class MagneticForceSystem extends IteratingSystem {
	/*
	 * Size of Magnets can be changed in jason/entities (line 123(range) & 128(sensor)) you have to change both values.
	 */
    private final float force = 75;
    
    public MagneticForceSystem() {
        this(0);
    }

    public MagneticForceSystem(int priority) {
        super(Family.all(PositionComponent.class, PhysixBodyComponent.class,
                MagneticInfluenceComponent.class, BallComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        MagneticInfluenceComponent magnets = ComponentMappers.magneticInfluence.get(entity);

        final Team ballTeam = entity.getComponent(TeamComponent.class).team;
        int i =0;
        for (Entity magneticField : magnets.magneticFields) {
            Vector2 otherPos = new Vector2();
            otherPos.x = magneticField.getComponent(PositionComponent.class).x;
            otherPos.y = magneticField.getComponent(PositionComponent.class).y;
   
            Vector2 magneticForce = new Vector2(otherPos.x - position.x, otherPos.y - position.y);
            float distance=magneticForce.len();
            float fieldLength= magneticField.getComponent(MagneticFieldComponent.class).range;
            magneticForce.nor();
            //vom Magnet weg, falls gleiche Polarität

            final Team fieldTeam = magneticField.getComponent(TeamComponent.class).team;
            if (fieldTeam == ballTeam) {
                magneticForce.scl(-1f);
            }

 
            magneticForce.scl(force*(fieldLength/distance));
            physix.simpleForceApply(magneticForce);
        }

    }

}
