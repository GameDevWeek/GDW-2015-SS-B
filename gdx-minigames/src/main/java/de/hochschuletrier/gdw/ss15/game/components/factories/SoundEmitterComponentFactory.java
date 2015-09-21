package de.hochschuletrier.gdw.ss15.game.components.factories;

import de.hochschuletrier.gdw.ss15.game.components.SoundEmitterComponent;

public class SoundEmitterComponentFactory extends SimpleComponentFactory {

    public SoundEmitterComponentFactory() {
        super("SoundEmitter", SoundEmitterComponent.class);
    }
}
