package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.gdx.utils.SnapshotArray;

public class ScoreChangedEvent {

    public static interface Listener {

        void onScoreChangedEvent(int scoreBlue, int scoreRed);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(int scoreBlue, int scoreRed) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onScoreChangedEvent(scoreBlue, scoreRed);
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
