package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.utils.Timer;

public class ImpactSoundComponent extends Component implements Pool.Poolable {

    public final Timer lastPlayed = new Timer();
    public String sound;
    public float minImpulseStrength;
    public float minSpeed;
    public long minDelay;

    @Override
    public void reset() {
        sound = null;
        minImpulseStrength = 0;
        minSpeed = 0;
        minDelay = 0;
        lastPlayed.stop();
    }
}
