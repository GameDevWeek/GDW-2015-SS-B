package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.gdx.utils.SnapshotArray;

public class JoinServerEvent {

    public static interface Listener {
        void onJoinServerEvent(String server, int port, String userName);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(String server, int port, String userName) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onJoinServerEvent(server, port, userName);
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
