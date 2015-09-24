package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponInfluenceComponent;

public class WeaponSystem extends IteratingSystem{
	
	public WeaponSystem() {
        this(0);
    }

    public WeaponSystem(int priority) {
        super(Family.all(PositionComponent.class, PhysixBodyComponent.class, BallComponent.class, WeaponInfluenceComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        WeaponInfluenceComponent weapons = ComponentMappers.weaponInfluence.get(entity);
        
        for (int i = 0; i < weapons.weaponFields.size(); i++) {
			Vector2 otherPos = new Vector2();
			otherPos.x = weapons.weaponFields.get(i).getComponent(PositionComponent.class).x;
			otherPos.y = weapons.weaponFields.get(i).getComponent(PositionComponent.class).y;
			Vector2 weaponForce = new Vector2(otherPos.x - position.x, otherPos.y - position.y);
			if( weapons.weaponFields.get(i).getComponent(WeaponComponent.class).on == entity.getComponent(WeaponComponent.class).on)
				weaponForce.scl(-1f);
			
			weaponForce.nor();
			weaponForce.scl(50.f);
			physix.getBody().applyForceToCenter(weaponForce, true);
		}
    }

}

