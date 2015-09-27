/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.events;

import com.badlogic.gdx.utils.SnapshotArray;

import de.hochschuletrier.gdw.ss15.game.data.Team;

/**
 *
 * emitted when a goal is scored
 */
public class GoalShotEvent {
    
    public static interface Listener {
        void onGoalShotEvent(Team team);
    }
    private static final SnapshotArray<GoalShotEvent.Listener> listeners = new SnapshotArray<GoalShotEvent.Listener>();
    
    public static void emit(Team team) {
        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            ((GoalShotEvent.Listener)items[i]).onGoalShotEvent(team);
        }
        listeners.end();
    }

    public static void register(GoalShotEvent.Listener listener) {
        listeners.add(listener);
    }

    public static void unregister(GoalShotEvent.Listener listener) {
        listeners.removeValue(listener, true);
    }

    public static void unregisterAll() {
        listeners.clear();
    }
}
