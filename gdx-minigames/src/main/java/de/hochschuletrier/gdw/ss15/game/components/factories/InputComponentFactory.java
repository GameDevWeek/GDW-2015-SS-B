package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.MovableComponent;

/**
 * Created by isik on 23.09.15.
 */
public class InputComponentFactory extends SimpleComponentFactory {

    public InputComponentFactory() {
        super("Input", InputBallComponent.class);
    }
}
