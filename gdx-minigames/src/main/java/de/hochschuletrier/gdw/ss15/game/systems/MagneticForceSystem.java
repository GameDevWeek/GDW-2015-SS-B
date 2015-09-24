package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss14.game.ComponentMappers;
import de.hochschuletrier.gdw.ss14.game.components.BallComponent;
import de.hochschuletrier.gdw.ss14.game.components.MagnetComponent;
import de.hochschuletrier.gdw.ss14.game.components.MagneticInfluenceComponent;
import de.hochschuletrier.gdw.ss14.game.components.PositionComponent;

public class MagneticForceSystem extends IteratingSystem{
	
	

	public MagneticForceSystem() {
        this(0);
    }

    public MagneticForceSystem(int priority) {
        super(Family.all(PositionComponent.class, PhysixBodyComponent.class, BallComponent.class, MagneticInfluenceComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        MagneticInfluenceComponent magnets = ComponentMappers.magneticInfluence.get(entity);
        
        for (int i = 0; i < magnets.magneticFields.size(); i++) {
        	System.out.println("MagnetEffekt #" + i);
			Vector2 otherPos = new Vector2();
			otherPos.x = magnets.magneticFields.get(i).getComponent(PositionComponent.class).x;
			otherPos.y = magnets.magneticFields.get(i).getComponent(PositionComponent.class).y;
			//vom Magnet weg, falls gleiche Polarität
			Vector2 magneticForce = new Vector2(otherPos.x - position.x, otherPos.y - position.y);
			if( magnets.magneticFields.get(i).getComponent(MagnetComponent.class).positiv == entity.getComponent(MagnetComponent.class).positiv)
				magneticForce.scl(-1f);
			
			magneticForce.nor();
			magneticForce.scl(50.f);
			physix.getBody().applyForceToCenter(magneticForce, true);
		}
    }

}
