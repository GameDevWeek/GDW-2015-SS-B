package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;

public class WeaponSystem extends IteratingSystem{

	WeaponSystem(){
		this(0);
	}
	
	WeaponSystem(int priority){
		super(Family.all(PositionComponent.class,WeaponComponent.class).get(), priority);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		// TODO Auto-generated method stub
		
	}

}
