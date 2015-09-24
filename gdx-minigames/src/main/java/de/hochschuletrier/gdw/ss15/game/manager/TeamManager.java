package de.hochschuletrier.gdw.ss15.game.manager;

import de.hochschuletrier.gdw.ss15.events.GoalEvent;
import de.hochschuletrier.gdw.ss15.game.data.Team;

public final class TeamManager implements GoalEvent.Listener {

    private final int scores[] = new int[2];

    public TeamManager() {
        resetScores();
        GoalEvent.register(this);
    }
    
    public void dispose() {
        GoalEvent.unregister(this);
    }
    
    public int getScore(Team team) {
        return scores[team.ordinal()];
    }
    
    private void addToScore(Team team, int value) {
        scores[team.ordinal()] += value;
    }
    
    private void resetScores() {
        scores[Team.BLUE.ordinal()] = 0;
        scores[Team.RED.ordinal()] = 0;
    }

    @Override
    public void onGoalEvent(Team team) {
        addToScore(team, 1);
    }
}
