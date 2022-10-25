package Packets;

import java.io.Serializable;
import java.util.Vector;

public class LobbyConnectionListPacket implements Serializable {
    private static final long serialVersionUID = 7L;
    public Vector<Integer> CurrentUserIDs = new Vector<>();

    public void addUsers(int id) {
        CurrentUserIDs.add(id);
    }
}
