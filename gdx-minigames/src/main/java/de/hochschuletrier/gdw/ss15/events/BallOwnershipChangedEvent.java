package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.SnapshotArray;

public class BallOwnershipChangedEvent {

    public static interface Listener {
        void onBallOwnershipChangedEvent(Entity owner);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(Entity owner) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onBallOwnershipChangedEvent(owner);
        }
        listeners.end();
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
