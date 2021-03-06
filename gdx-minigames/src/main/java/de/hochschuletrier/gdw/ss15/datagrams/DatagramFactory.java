package de.hochschuletrier.gdw.ss15.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagramPool;

public final class DatagramFactory {

    public static final NetDatagramPool POOL = new NetDatagramPool(
            ConnectDatagram.class,
            WorldSetupDatagram.class,
            CreateEntityDatagram.class,
            RemoveEntityDatagram.class,
            GameStateDatagram.class,
            PlayerIdDatagram.class,
            MoveDatagram.class,
            BallPlayerInputDatagram.class,
            ShootDatagram.class,
            GoalShotDatagram.class,
            PullChangeDatagram.class,
            SoundDatagram.class,
            ScoreChangedDatagram.class,
            BallOwnershipChangedDatagram.class,
            AnimationStateChangeDatagram.class
    );

    static <T extends NetDatagram> T create(Class<T> clazz) {
        return POOL.obtain(clazz);
    }
}
