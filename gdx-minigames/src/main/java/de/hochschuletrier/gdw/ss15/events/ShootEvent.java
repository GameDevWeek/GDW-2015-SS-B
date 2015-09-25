package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SnapshotArray;

public class ShootEvent {
	public static interface Listener {
		void onShootEvent(Entity entity, Vector2 direction);
	}

	private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

	public static void emit(Entity entity, Vector2 direction) {
		Object[] items = listeners.begin();
	    for (int i = 0, n = listeners.size; i < n; i++) {
	    	((Listener)items[i]).onShootEvent(entity, direction);
	    }
	    listeners.end();
	}

	public static void register(Listener listener) {
		listeners.add(listener);
		System.out.println("listener wurde hinzugefügt");
	}

	public static void unregister(Listener listener) {
		listeners.removeValue(listener, true);
	}

	public static void unregisterAll() {
		listeners.clear();
	}
}
