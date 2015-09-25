package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.gdx.utils.SnapshotArray;

public class CreateServerEvent {

    public static interface Listener {
        void onCreateServerEvent(int port, int maxPlayers, String mapName, String userName);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(int port, int maxPlayers, String mapName, String userName) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onCreateServerEvent(port, maxPlayers, mapName, userName);
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
