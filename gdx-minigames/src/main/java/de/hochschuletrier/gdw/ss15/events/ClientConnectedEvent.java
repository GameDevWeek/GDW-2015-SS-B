package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;

public class ClientConnectedEvent {

    public static interface Listener {
        void onClientConnectedEvent(NetConnection connection);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(NetConnection connection) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onClientConnectedEvent(connection);
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
