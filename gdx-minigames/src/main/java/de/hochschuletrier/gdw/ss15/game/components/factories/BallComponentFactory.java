package de.hochschuletrier.gdw.ss15.game.components.factories;


import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;

public class BallComponentFactory extends SimpleComponentFactory {

    public BallComponentFactory() {
        super("Ball", BallComponent.class);
    }
}

