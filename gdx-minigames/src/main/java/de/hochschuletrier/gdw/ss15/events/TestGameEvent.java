package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.gdx.utils.SnapshotArray;

public class TestGameEvent {

    public static interface Listener {
        void onTestGameEvent(String mapName);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(String mapName) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener)items[i]).onTestGameEvent(mapName);
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
