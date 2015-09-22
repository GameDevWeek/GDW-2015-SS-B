package de.hochschuletrier.gdw.ss15.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagramPool;

public final class DatagramFactory {

    public static final NetDatagramPool POOL = new NetDatagramPool(
            ConnectDatagram.class
    );

    static <T extends NetDatagram> T create(Class<T> clazz) {
        return POOL.obtain(clazz);
    }
}
