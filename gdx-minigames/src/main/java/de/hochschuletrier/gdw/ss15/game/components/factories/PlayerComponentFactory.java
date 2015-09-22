package de.hochschuletrier.gdw.ss15.game.components.factories;

import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

public class PlayerComponentFactory extends SimpleComponentFactory {

    public PlayerComponentFactory() {
        super("Player", PlayerComponent.class);
    }
}
