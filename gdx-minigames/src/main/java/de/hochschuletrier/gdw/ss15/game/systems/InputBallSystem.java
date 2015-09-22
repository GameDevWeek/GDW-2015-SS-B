package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;

public class InputBallSystem extends IteratingSystem {
	
	public InputBallSystem() {
		this(0);
	}

	@SuppressWarnings("unchecked")
	public InputBallSystem(int priority) {
		super(Family.all(InputBallComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
	}

}
