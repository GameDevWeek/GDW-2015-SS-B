package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.input.InputPuffer;

public class InputBallSystem extends IteratingSystem {
	
	public InputBallSystem() {
		this(0);
	}

	@SuppressWarnings("unchecked")
	public InputBallSystem(int priority) {
		super(Family.all(LocalPlayerComponent.class, InputBallComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		 InputBallComponent input = ComponentMappers.input.get(entity);
	        
	        // transfer from puffer
	        input.vertical = InputPuffer.vertical;
	        input.horizontal = InputPuffer.horizontal;
	        
	        input.pull = InputPuffer.pull;
	        
	        if ( InputPuffer.push == true) {
	        	input.push = InputPuffer.push;
	        }
	    	
//	    	PhysixBodyComponent body = ComponentMappers.physixBody.get(entity);
//	        float strengthX = 10000;
//	        float strengthY = 20000;
//	        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
//	            body.applyImpulse(0, -strengthY);
//	        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
//	            body.applyImpulse(0, strengthY);
//	        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
//	            body.applyImpulse(-strengthX, 0);
//	        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
//	            body.applyImpulse(strengthX, 0);
//	        
	        
	}

}
