package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss15.events.PullEvent;
import de.hochschuletrier.gdw.ss15.events.ShootEvent;

import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;

public class WeaponSystem extends IteratingSystem implements  ShootEvent.Listener,PullEvent.Listener{

	public WeaponSystem(){
		this(0);
	}
	
	public WeaponSystem(int priority){
		super(Family.all(PositionComponent.class,WeaponComponent.class).get(), priority);
		ShootEvent.register(this);
		PullEvent.register(this);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shootEvent(Entity entity, Vector2 direction) {
		// TODO Auto-generated method stub
		System.out.println("es soll geschossen werden");
	}

	@Override
	public void pullEventOn(Entity entity, Vector2 direction) {
		// TODO Auto-generated method stub
		System.out.println("pullOn");
	}

	@Override
	public void pullEventOff(Entity entity) {
		// TODO Auto-generated method stub
		System.out.println("pollOff off");
		
	}

}
