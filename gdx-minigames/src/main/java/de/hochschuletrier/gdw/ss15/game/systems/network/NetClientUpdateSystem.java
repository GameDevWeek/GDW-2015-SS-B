package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.commons.netcode.simple.NetDatagramHandler;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import java.util.HashMap;

public class NetClientUpdateSystem extends EntitySystem implements NetDatagramHandler, NetClientSimple.Listener {

    private final NetClientSimple netClient;
    private PooledEngine engine;
    private final HashMap<Long, Entity> netEntityMap = new HashMap();
    private PhysixSystem physixSystem;

    public NetClientUpdateSystem(NetClientSimple netClient) {
        super(0);
        this.netClient = netClient;
    }

//        netClient.setHandler(this);
//        netClient.setListener(this);

    @Override
    public void update(float deltaTime) {
        netClient.update();
    }

    @Override
    public void onDisconnect() {
//        Main.getInstance().disconnect();
    }
}
