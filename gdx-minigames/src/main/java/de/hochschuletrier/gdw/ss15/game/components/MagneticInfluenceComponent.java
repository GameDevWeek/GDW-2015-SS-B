package de.hochschuletrier.gdw.ss15.game.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
// Nur wenn sich mehrere 
public class MagneticInfluenceComponent extends Component implements Pool.Poolable{
	
	public ArrayList<Entity> magneticFields = new ArrayList<Entity>();

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		magneticFields = new ArrayList<Entity>();
	}

}
