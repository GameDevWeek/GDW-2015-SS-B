package de.hochschuletrier.gdw.ss15.game.components.factories;

import de.hochschuletrier.gdw.ss15.game.components.MovableComponent;

public class MovableComponentFactory extends SimpleComponentFactory {

    public MovableComponentFactory() {
        super("Movable", MovableComponent.class);
    }
}
