package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundInstance;
import de.hochschuletrier.gdw.ss15.game.data.SoundChannel;

public class SoundEmitterComponent extends Component implements Pool.Poolable {

    public final SoundEmitter emitter = new SoundEmitter();
    public final SoundInstance channels[] = new SoundInstance[SoundChannel.values().length];

    @Override
    public void reset() {
        emitter.dispose();
        
        for (int i = 0; i < channels.length; i++) {
            channels[i] = null;
        }
    }
}
