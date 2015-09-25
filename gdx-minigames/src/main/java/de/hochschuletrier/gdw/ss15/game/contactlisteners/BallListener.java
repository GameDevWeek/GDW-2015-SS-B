package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

public class BallListener extends PhysixContactAdapter {
    private final PooledEngine engine;

    public BallListener(PooledEngine engine) {
        this.engine = engine;
    }
    @Override
    public void beginContact(PhysixContact contact) {
        Entity myEntity = contact.getMyComponent().getEntity();
        PhysixBodyComponent comp = contact.getOtherComponent();
        Entity otherEntity = null;
        if (comp != null) {
            otherEntity = comp.getEntity();
        }
        if (otherEntity != null) {
            if (ComponentMappers.magneticField.has(otherEntity)) {
                ComponentMappers.magneticInfluence.get(myEntity).magneticFields.add(otherEntity);
            } else if (!myEntity.isScheduledForRemoval() && !ComponentMappers.goalShot.has(myEntity)) {
                PlayerComponent player = ComponentMappers.player.get(otherEntity);
                if (player != null) {
                    player.hasBall = true;
                    engine.removeEntity(myEntity);
                }
            }
        }
    }

    @Override
    public void endContact(PhysixContact contact) {
        Entity myEntity = contact.getMyComponent().getEntity();
        PhysixBodyComponent comp = contact.getOtherComponent();
        Entity otherEntity = null;
        if (comp != null) {
            otherEntity = comp.getEntity();
        }
        if (otherEntity != null && ComponentMappers.magneticField.has(otherEntity)) {
            ComponentMappers.magneticInfluence.get(myEntity).magneticFields.remove(otherEntity);
        }
    }
}
