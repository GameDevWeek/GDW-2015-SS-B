package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.core.NetStatistic;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.commons.netcode.simple.NetServerSimple;
import de.hochschuletrier.gdw.ss15.Main;
import java.util.HashMap;

public class NetHudRenderSystem extends EntitySystem {

    private static final int UPDATE_INTERVAL = 1;

    private final BitmapFont font;
    private String speed = "";
    private String datagrams = "";
    private final NetClientSimple netClient;
    private final NetServerSimple netServer;
    private final HashMap<NetConnection, NetSum> connections = new HashMap();
    private float nextUpdate = UPDATE_INTERVAL;
    private final NetSum total = new NetSum();

    // private PooledEngine engine;
    public NetHudRenderSystem(AssetManagerX assetManager, NetClientSimple netClient, NetServerSimple netServer, int priority) {
        super(priority);
        font = assetManager.getFont("verdana_24");
        this.netClient = netClient;
        this.netServer = netServer;
    }

    @Override
    public void update(float deltaTime) {
        nextUpdate -= deltaTime;
        if (nextUpdate <= 0) {
            float timePassed = UPDATE_INTERVAL - nextUpdate;
            nextUpdate = UPDATE_INTERVAL;
            total.reset();
            if (netClient != null) {
                calculateConnection(netClient.getConnection());
            } else if (netServer != null) {
                for (NetConnection connection : netServer.getConnections()) {
                    calculateConnection(connection);
                }
            }
            
            datagrams = "Dgrams: " + total.numDatagramsSent + " UP / " + total.numDatagramsReceived + " Down";
            speed = "Bytes: " + total.numBytesWritten + " UP / " + total.numBytesRead + " Down";
        }
        
        float y = Gdx.graphics.getHeight() - 100;
        Main.getInstance().screenCamera.bind();
        font.setColor(Color.WHITE);
        font.draw(DrawUtil.batch, datagrams, 50, y);
        font.draw(DrawUtil.batch, speed, 50, y + 50);
    }

    private void calculateConnection(NetConnection connection) {
        NetSum last = getOrAdd(connection);
        NetStatistic tcp = connection.getTcpStatistic();
        NetStatistic udp = connection.getUdpStatistic();
        
        long numBytesRead = tcp.getNumBytesRead() + udp.getNumBytesRead();
        long numBytesWritten = tcp.getNumBytesWritten()+ udp.getNumBytesWritten();
        long numDatagramsReceived = tcp.getNumDatagramsReceived()+ udp.getNumDatagramsReceived();
        long numDatagramsSent = tcp.getNumDatagramsSent()+ udp.getNumDatagramsSent();

        total.numBytesRead += numBytesRead - last.numBytesRead;
        total.numBytesWritten += numBytesWritten - last.numBytesWritten;
        total.numDatagramsReceived += numDatagramsReceived - last.numDatagramsReceived;
        total.numDatagramsSent += numDatagramsSent - last.numDatagramsSent;
        
        last.numBytesRead = numBytesRead;
        last.numBytesWritten = numBytesWritten;
        last.numDatagramsReceived = numDatagramsReceived;
        last.numDatagramsSent = numDatagramsSent;
    }

    private NetSum getOrAdd(NetConnection connection) {
        NetSum sum = connections.get(connection);
        if (sum == null) {
            sum = new NetSum();
            connections.put(connection, sum);
        }
        return sum;
    }

    private static class NetSum {

        long numBytesRead;
        long numBytesWritten;
        long numDatagramsReceived;
        long numDatagramsSent;
        
        public void reset() {
            numBytesRead = 0;
            numBytesWritten = 0;
            numDatagramsReceived = 0;
            numDatagramsSent = 0;
        }
    }

}
