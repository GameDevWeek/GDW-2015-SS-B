/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.StateRelatedAnimationsComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;

/**
 *
 * @author rftpool11
 */
public class ChangeAnimationStateEvent {

    public static interface Listener {

        void onAnimationStateChangedEvent(EntityAnimationState newState, Entity entity);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(EntityAnimationState newState, Entity entity) {
        StateRelatedAnimationsComponent animation = ComponentMappers.stateRelatedAnimations.get(entity);
        if(animation.currentState != newState) {
            Object[] items = listeners.begin();
            for (int i = 0, n = listeners.size; i < n; i++) {
                ((Listener) items[i]).onAnimationStateChangedEvent(newState, entity);
            }
            listeners.end();
        }
    }

    public static void register(Listener listener) {
        listeners.add(listener);
    }

    public static void unregister(Listener listener) {
        listeners.removeValue(listener, true);
    }

    public static void unregisterAll() {
        listeners.clear();
    }
}
