package de.hochschuletrier.gdw.ss15.game.components.factories;

import de.hochschuletrier.gdw.ss15.game.components.PlayerSpawnComponent;

public class PlayerSpawnComponentFactory extends SimpleComponentFactory {

    public PlayerSpawnComponentFactory() {
        super("PlayerSpawn", PlayerSpawnComponent.class);
    }
}
