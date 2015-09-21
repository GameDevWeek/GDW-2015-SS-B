package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.gdx.physics.box2d.ContactImpulse;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;

public class ImpactSoundListener extends PhysixContactAdapter {

    @Override
    public void postSolve(PhysixContact contact, ContactImpulse impulse) {
        String ignore = play(contact.getMyComponent(), impulse, null);
        play(contact.getOtherComponent(), impulse, ignore);
    }

    private String play(PhysixBodyComponent component, ContactImpulse impulse, String ignore) {
        if (component == null)
            return null;
        ImpactSoundComponent isc = ComponentMappers.impactSound.get(component.getEntity());
        if (isc == null || isc.sound.equals(ignore))
            return null;
        
        if(isc.lastPlayed.isRunning() && isc.lastPlayed.get() <= isc.minDelay) 
            return null;
        
        float impulseStrength = Math.abs(impulse.getNormalImpulses()[0]);
        if (impulseStrength <= isc.minImpulseStrength)
            return null;
        
        float speed = component.getLinearVelocity().len();
        if (speed <= isc.minSpeed)
            return null;
        
        isc.lastPlayed.reset();
        SoundEvent.emit(isc.sound, component.getEntity());
        return isc.sound;
    }
}
