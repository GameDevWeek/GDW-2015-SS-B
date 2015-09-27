package de.hochschuletrier.gdw.ss15.game.components.factories;

import de.hochschuletrier.gdw.ss15.game.components.LocalPlayerComponent;

/**
 * Created by isik on 23.09.15.
 */
public class LocalPlayerComponentFactory extends SimpleComponentFactory {

    public LocalPlayerComponentFactory() {
        super("LocalPlayer", LocalPlayerComponent.class);
    }
}

