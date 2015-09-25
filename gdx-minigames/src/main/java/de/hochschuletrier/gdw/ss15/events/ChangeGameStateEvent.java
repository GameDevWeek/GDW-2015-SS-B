package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.gdx.utils.SnapshotArray;
import de.hochschuletrier.gdw.ss15.game.data.GameState;

public class ChangeGameStateEvent {

    public static interface Listener {

        void onChangeGameStateEvent(GameState newState);
    }

    private static final SnapshotArray<Listener> listeners = new SnapshotArray();

    public static void emit(GameState newState) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((Listener) items[i]).onChangeGameStateEvent(newState);
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

