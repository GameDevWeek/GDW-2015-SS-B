package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss15.events.PullEvent;
import de.hochschuletrier.gdw.ss15.events.ShootEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.input.InputPuffer;

public class InputBallSystem extends IteratingSystem  {
	
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
	      if (input.pull != InputPuffer.pull) { // wechsel hat stattgefunden
	    	  if (InputPuffer.pull) {
	    		  PositionComponent pos = ComponentMappers.position.get(entity);
		    	  
		    	  float directionX = Gdx.input.getX() - pos.x;
		    	  float directionY = Gdx.input.getY() - pos.y;
		    	  
		    	  PullEvent.emitOn(entity, new Vector2(directionX, directionY).nor()); 
	    	  }
	    	  else {
	    		  PullEvent.emitOff(entity);
	    	  }
	    	  
	    	  input.pull = InputPuffer.pull;
	      }
	      
	      if ( false /* Spieler hat Ball */ ) {
	    	  
	    	  PositionComponent pos = ComponentMappers.position.get(entity);
	    	  
	    	  float directionX = Gdx.input.getX() - pos.x;
	    	  float directionY = Gdx.input.getY() - pos.y;
	    	  
	    	  ShootEvent.emit(entity, new Vector2(directionX, directionY).nor());
	    	  
	      }	        
	}
}
