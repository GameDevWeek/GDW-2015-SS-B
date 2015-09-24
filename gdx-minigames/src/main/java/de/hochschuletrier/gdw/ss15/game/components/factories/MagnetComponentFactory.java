package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.MagnetComponent;
import de.hochschuletrier.gdw.ss15.game.components.MagneticFieldComponent;

public class MagnetComponentFactory extends SimpleComponentFactory{

	public MagnetComponentFactory() {
		super("Magnet",MagneticFieldComponent.class);
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public String getType() {
//		// TODO Auto-generated method stub
//		return "magnet";
//	}
//
//	@Override
//	public void run(Entity entity, SafeProperties meta,
//			SafeProperties properties, EntityFactoryParam param) {
//		// TODO Auto-generated method stub
//		
//	}

}
