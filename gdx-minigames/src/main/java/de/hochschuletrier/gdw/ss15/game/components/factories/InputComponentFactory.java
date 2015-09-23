package de.hochschuletrier.gdw.ss15.game.components.factories;

import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;

/**
 * Created by isik on 23.09.15.
 */
public class InputComponentFactory extends SimpleComponentFactory {

    public InputComponentFactory() {
        super("Input", InputBallComponent.class);
    }
}
