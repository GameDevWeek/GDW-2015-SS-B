package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.SnapshotArray;

public class PullEvent {
	public static interface Listener {
		void pullEventOn(Entity entity, Vector2 direction);
		void pullEventOff(Entity entity);
	}

	private static final SnapshotArray<Listener> listeners = new SnapshotArray<Listener>();

	public static void emitOn(Entity entity, Vector2 direction) {
		Object[] items = listeners.begin();
	    for (int i = 0, n = listeners.size; i < n; i++) {
	    	((Listener)items[i]).pullEventOn(entity, direction);
	    }
	    listeners.end();
	}
	
	public static void emitOff(Entity entity) {
		Object[] items = listeners.begin();
	    for (int i = 0, n = listeners.size; i < n; i++) {
	    	((Listener)items[i]).pullEventOff(entity);
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