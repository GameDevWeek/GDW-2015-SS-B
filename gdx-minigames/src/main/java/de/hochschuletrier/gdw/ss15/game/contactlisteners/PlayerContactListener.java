package de.hochschuletrier.gdw.ss15.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.BallDropComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;

public class PlayerContactListener extends PhysixContactAdapter {
    private final PooledEngine engine;


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
                Team myteam = myEntity.getComponent(TeamComponent.class).team;
                Team otherTeam = otherEntity.getComponent(TeamComponent.class).team;
                if (myteam != otherTeam) {
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
            BallDropComponent balldrop =engine.createComponent(BallDropComponent.class);
            player.add(balldrop);
            
    }

}
