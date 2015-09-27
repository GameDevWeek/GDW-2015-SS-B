package de.hochschuletrier.gdw.ss15.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;

/**
 * send from server only
 */
public class ScoreChangedDatagram extends NetDatagram {

    private int scoreBlue;
    private int scoreRed;

    public static ScoreChangedDatagram create(int scoreBlue, int scoreRed) {
        ScoreChangedDatagram datagram = DatagramFactory.create(ScoreChangedDatagram.class);
        datagram.scoreBlue = scoreBlue;
        datagram.scoreRed = scoreRed;
        return datagram;
    }

    public int getScoreBlue() {
        return scoreBlue;
    }

    public int getScoreRed() {
        return scoreRed;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putInt(scoreBlue);
        message.putInt(scoreRed);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        scoreBlue = message.getInt();
        scoreRed = message.getInt();
    }
}
