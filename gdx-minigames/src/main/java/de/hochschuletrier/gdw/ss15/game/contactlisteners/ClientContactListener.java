package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ClientContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        contact.setEnabled(false);
    }

    @Override
    public void endContact(Contact contact) {
        contact.setEnabled(false);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        contact.setEnabled(false);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        contact.setEnabled(false);
    }

}
