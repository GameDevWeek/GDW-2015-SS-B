package de.hochschuletrier.gdw.ss15.game.manager;

import java.awt.font.ImageGraphicAttribute;
import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;
import de.hochschuletrier.gdw.ss15.events.ChangeGameStateEvent;
import de.hochschuletrier.gdw.ss15.events.GoalEvent;
import de.hochschuletrier.gdw.ss15.events.ShootEvent;
import de.hochschuletrier.gdw.ss15.events.BallDropEvent;
import de.hochschuletrier.gdw.ss15.events.ChangeBallOwnershipEvent;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;
import de.hochschuletrier.gdw.ss15.game.components.BallSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.NotReceptiveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;
import de.hochschuletrier.gdw.ss15.game.data.GameState;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import de.hochschuletrier.gdw.ss15.game.utils.MapLoader;
import java.util.Random;


public final class BallManager implements ChangeGameStateEvent.Listener,
        GoalEvent.Listener, ShootEvent.Listener,BallDropEvent.Listener {

    ArrayList<Entity> start_spawns = new ArrayList<Entity>();
    ArrayList<Entity> team_0_spawns = new ArrayList<Entity>();
    ArrayList<Entity> team_1_spawns = new ArrayList<Entity>();

    private final PooledEngine engine;
    private final ImmutableArray<Entity> balls;
    private GameState gameState;
    private final Random random = new Random();

    public BallManager(PooledEngine engine) {
        this.engine = engine;

        distributeSpawns();
        balls = engine.getEntitiesFor(Family.all(BallComponent.class).get());

        ChangeGameStateEvent.register(this);
        GoalEvent.register(this);
        ShootEvent.register(this);
        BallDropEvent.register(this);

    }

    public void dispose() {
        ChangeGameStateEvent.unregister(this);
        GoalEvent.unregister(this);
        ShootEvent.unregister(this);
        BallDropEvent.unregister(this);
    }

    public void distributeSpawns() {

        ImmutableArray<Entity> spawns = engine.getEntitiesFor(Family.all(BallSpawnComponent.class, TeamComponent.class).get());

        for (Entity entity : spawns) {
            TeamComponent team = entity.getComponent(TeamComponent.class);

            if (team.team == null) {
                start_spawns.add(entity);
            } else if (team.team == Team.BLUE) {
                team_0_spawns.add(entity);
            } else if (team.team == Team.RED) {
                team_1_spawns.add(entity);
            }
        }
    }
    
    // Ouch!
    private void removeBalls() {
        for(Entity ball: balls) 
            engine.removeEntity(ball);
        
        ChangeBallOwnershipEvent.emit(null);
    }
    
    private PositionComponent getRandomSpawn(ArrayList<Entity> spawns) {
        Entity spawn = spawns.get(random.nextInt(spawns.size()));
        return spawn.getComponent(PositionComponent.class);
    }
    
    private void resetBall(Team team) {
        removeBalls();
        PositionComponent pos;
        if(team == Team.BLUE)
            pos = getRandomSpawn(team_0_spawns);
        else if(team == Team.RED)
            pos = getRandomSpawn(team_1_spawns);
        else
            pos = getRandomSpawn(start_spawns);
        
        Entity ball = MapLoader.createEntity(engine, "ball", pos.x, pos.y, team);
        setBallTeam(team, ball);
    }

    private void setBallTeam(Team team, Entity ball) {
        if(team == Team.BLUE)
            ChangeAnimationStateEvent.emit(EntityAnimationState.BALL_MINUS, ball);
        else if(team == Team.RED)
            ChangeAnimationStateEvent.emit(EntityAnimationState.BALL_PLUS, ball);
    }

    @Override
    public void onChangeGameStateEvent(GameState newState, float gameTime) {
        this.gameState = newState;
        switch(newState) {
            case WARMUP:
            case GAME:
                resetBall(null);
                break;
            case THREE:
            case GAME_OVER:
                removeBalls();
                break;
        }
    }

    @Override
    public void onGoalEvent(Team team) {
        if(gameState != GameState.GAME_OVER)
            resetBall(team == Team.BLUE ? Team.BLUE : Team.RED);
    }

    @Override
    public void onShootEvent(Entity entityFrom, Vector2 direction) {
        PlayerComponent player = ComponentMappers.player.get(entityFrom);
        if(player != null && player.hasBall) {
            SoundEvent.emit("ball_shot", entityFrom);
            ChangeBallOwnershipEvent.emit(null);
            //füge "stunning" hinzu/kann den Ball nicht mehr aufnehmen 
            final float stunningTime= 0.4f;
            NotReceptiveComponent notReceptive = engine
                    .createComponent(NotReceptiveComponent.class);
            notReceptive.remainingTime = stunningTime;
            notReceptive.time = stunningTime;
            notReceptive.isStunned = false;
            entityFrom.add(notReceptive);
            PhysixBodyComponent pBody = ComponentMappers.physixBody.get(entityFrom);
            for (Fixture fixture : pBody.getFixtureList()) {
                Filter filter = fixture.getFilterData();
                filter.groupIndex = -1;
                fixture.setFilterData(filter);
            }
            
            PositionComponent pos = ComponentMappers.position.get(entityFrom);
            TeamComponent team = ComponentMappers.team.get(entityFrom);
            InputBallComponent input = ComponentMappers.input.get(entityFrom);
            Vector2 dir = input.view.cpy().scl(120);
            Entity ball = MapLoader.createEntity(engine, "ball", pos.x, pos.y, team.team);
            PhysixModifierComponent modify = ComponentMappers.physixModifier.get(ball);
            modify.schedule(() -> {
                PhysixBodyComponent body = ComponentMappers.physixBody.get(ball);
                body.setLinearVelocity(dir.nor().scl(1000));
                body.setAwake(true);
            });
            ball.add(modify);
            setBallTeam(team.team, ball);
        }
    }

    @Override
    public void onDropEvent(Entity entityFrom, Vector2 direction) {
        PlayerComponent player = ComponentMappers.player.get(entityFrom);
        if(player != null && player.hasBall) {
            ChangeBallOwnershipEvent.emit(null);
            PositionComponent pos = ComponentMappers.position.get(entityFrom);
            TeamComponent team = ComponentMappers.team.get(entityFrom);
            InputBallComponent input = ComponentMappers.input.get(entityFrom);
            Vector2 dir = input.view.cpy().scl(120);
            Entity ball = MapLoader.createEntity(engine, "ball", pos.x + dir.x, pos.y + dir.y, team.team);
            PhysixModifierComponent modify = ComponentMappers.physixModifier.get(ball);
            modify.schedule(() -> {
                PhysixBodyComponent body = ComponentMappers.physixBody.get(ball);
                body.setLinearVelocity(dir.nor().scl(100));
                body.setAwake(true);
            });
            ball.add(modify);
            setBallTeam(team.team, ball);
        }

    }
}
