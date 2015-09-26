package de.hochschuletrier.gdw.ss15.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;
import de.hochschuletrier.gdw.ss15.game.data.Team;

/**
 * send from client only
 */
public class GoalShotDatagram extends NetDatagram {

    private Team team;

    public static GoalShotDatagram create(Team team) {
        GoalShotDatagram datagram = DatagramFactory.create(GoalShotDatagram.class);
        datagram.team = team;
        return datagram;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putEnum(team);
    }

    public @Override
    void readFromMessage(NetMessageIn message) {
        team = message.getEnum(Team.class);
    }
}
