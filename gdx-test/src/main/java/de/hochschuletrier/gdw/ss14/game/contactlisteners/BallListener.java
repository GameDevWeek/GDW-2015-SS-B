package de.hochschuletrier.gdw.ss14.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss14.game.components.MagneticFieldComponent;
import de.hochschuletrier.gdw.ss14.game.components.MagneticInfluenceComponent;

public class BallListener implements PhysixContactListener {

	@Override
	public void beginContact(PhysixContact contact) {
		PhysixBodyComponent comp = contact.getOtherComponent();
		Entity entity = null;
		if(comp != null)
			entity = comp.getEntity();
		if(entity != null && entity.getComponent(MagneticFieldComponent.class) != null)
		{
			System.out.println("MAGNETFELD REIN");
			contact.getMyComponent().getEntity().getComponent(MagneticInfluenceComponent.class).magneticFields.add(entity);
		}
	}

	@Override
	public void endContact(PhysixContact contact) {
		PhysixBodyComponent comp = contact.getOtherComponent();
		Entity entity = null;
		if(comp != null)
			entity = comp.getEntity();
		if(entity != null && entity.getComponent(MagneticFieldComponent.class) != null)
		{
			contact.getMyComponent().getEntity().getComponent(MagneticInfluenceComponent.class).magneticFields.remove(entity);
		}
		System.out.println("MAGNETFELD RAUS");
	}

	@Override
	public void preSolve(PhysixContact contact, Manifold oldManifold) {
//		// TODO Auto-generated method stub
//		MagneticFieldComponent field = contact.getOtherComponent().getEntity().getComponent(MagneticFieldComponent.class);
//		if(field != null)
//		{
//			contact.setEnabled(false);
//			Vector2 myPos = contact.getMyComponent().getPosition();
//			Vector2 otherPos = contact.getOtherComponent().getPosition();
//			//vom Magnet weg, falls gleiche Polarität
//			Vector2 magneticForce = new Vector2(myPos.x - otherPos.x, myPos.y - otherPos.y);
//			if(contact.getOtherComponent().getEntity().getComponent(MagnetComponent.class).positiv == contact.getMyComponent().getEntity().getComponent(MagnetComponent.class).positiv)
//				magneticForce.scl(-1f);
//			magneticForce.nor();
//			contact.getMyComponent().getBody().applyForceToCenter(magneticForce, true);
//		}
	}

	@Override
	public void postSolve(PhysixContact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

	
}
