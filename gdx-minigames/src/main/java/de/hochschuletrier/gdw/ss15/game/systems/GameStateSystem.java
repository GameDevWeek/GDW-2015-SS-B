package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.hochschuletrier.gdw.ss15.events.ChangeGameStateEvent;
import de.hochschuletrier.gdw.ss15.events.GoalEvent;
import de.hochschuletrier.gdw.ss15.events.ScoreChangedEvent;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.data.GameState;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameStateSystem extends EntitySystem implements ChangeGameStateEvent.Listener,
        GoalEvent.Listener {
    private static final Logger logger = LoggerFactory.getLogger(GameStateSystem.class);

    private final int scores[] = new int[2];
    private GameState gameState = GameState.WARMUP;
    private float countdown = 0;

    public GameStateSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        resetScores();
        GoalEvent.register(this);
        ChangeGameStateEvent.register(this);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        GoalEvent.unregister(this);
        ChangeGameStateEvent.unregister(this);
    }

    @Override
    public void update(float deltaTime) {
        switch (gameState) {
            case WARMUP:
                updateWarmup(deltaTime);
                break;
            case THREE:
            case TWO:
            case ONE:
                updateCountdown(deltaTime);
                break;
            case GAME:
                updateGame(deltaTime);
                break;
            case GAME_OVER:
                updateGameOver(deltaTime);
                break;
            default:
                throw new AssertionError(gameState.name());
        }
    }

    @Override
    public void onChangeGameStateEvent(GameState newState) {
        this.gameState = newState;
        logger.info("Changed Gamestate to {}", gameState.toString());

        switch (gameState) {
            case WARMUP:
                countdown = 0;
                break;
            case THREE:
                countdown = 3;
                break;
            case TWO:
                countdown = 2;
                break;
            case ONE:
                countdown = 1;
                break;
            case GAME:
                countdown = 60*5;
                break;
            case GAME_OVER:
                countdown = 30;
                break;
        }
    }

    private void updateWarmup(float deltaTime) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            ChangeGameStateEvent.emit(GameState.THREE);
        }
    }

    private void updateCountdown(float deltaTime) {
        countdown -= deltaTime;
        if(countdown <= 0)
            ChangeGameStateEvent.emit(GameState.values()[gameState.ordinal()+1]);
    }

    private void updateGame(float deltaTime) {
        countdown -= deltaTime;
        if(countdown <= 0)
            ChangeGameStateEvent.emit(GameState.GAME_OVER);
    }

    private void updateGameOver(float deltaTime) {
        countdown -= deltaTime;
        if(countdown <= 0)
            ChangeGameStateEvent.emit(GameState.WARMUP);
    }

    public int getScore(Team team) {
        return scores[team.ordinal()];
    }
    
    private void addToScore(Team team, int value) {
        scores[team.ordinal()] += value;
        
        if(scores[team.ordinal()] >= GameConstants.SCORE_TO_WIN)
            ChangeGameStateEvent.emit(GameState.GAME_OVER);
        
        ScoreChangedEvent.emit(scores[0], scores[1]);
    }
    
    private void resetScores() {
        scores[Team.BLUE.ordinal()] = 0;
        scores[Team.RED.ordinal()] = 0;
        ScoreChangedEvent.emit(0, 0);
    }

    @Override
    public void onGoalEvent(Team team) {
        addToScore(team, 1);
    }
}
