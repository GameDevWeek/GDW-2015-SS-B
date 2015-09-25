package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.BallDropEvent;
import de.hochschuletrier.gdw.ss15.events.PullEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.NotReceptiveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;

public class PlayerContactListener extends PhysixContactAdapter {
    private final PooledEngine engine;
    private final float stunningTime = 5;

    public PlayerContactListener(PooledEngine engine) {
        this.engine = engine;
    }

    @Override
    public void beginContact(PhysixContact contact) {
        Entity myEntity = contact.getMyComponent().getEntity();
        PhysixBodyComponent comp = contact.getOtherComponent();
        Entity otherEntity = null;
        if (comp != null) {
            otherEntity = comp.getEntity();
        }

        if (otherEntity != null) {
            if (ComponentMappers.player.has(myEntity)
                    && ComponentMappers.player.has(otherEntity)) {
                System.out.println("sieler berührt sich");
                Team myteam = myEntity.getComponent(TeamComponent.class).team;
                Team otherTeam = otherEntity.getComponent(TeamComponent.class).team;
                if (myteam != otherTeam) {
                    System.out.println("gefault");
                    // (NotReceptiveComponent.class);
                    if (myEntity.getComponent(PlayerComponent.class).hasBall) {
                        stunnPlayer(myEntity);
                    } else if (otherEntity.getComponent(PlayerComponent.class).hasBall) {
                        stunnPlayer(otherEntity);
                    }

                }

            }
        }
    }

    private void stunnPlayer(Entity player) {
        System.out.println("stunned");
        Vector2 dir = new Vector2(
                -(player.getComponent(InputBallComponent.class).move.x),
                -(player.getComponent(InputBallComponent.class).move.y));
        BallDropEvent.emit(player, dir);
        NotReceptiveComponent notReceptive = engine
                .createComponent(NotReceptiveComponent.class);
        notReceptive.remainingTime = stunningTime;
        notReceptive.time = stunningTime;
        notReceptive.isStunned = true;
        // myEntity.remove(InputBallComponent.class);//nicht erwünscht besser
        // mit:
        player.getComponent(InputBallComponent.class).isStunned = true;
        player.add(notReceptive);
    }

}
