package Packets;

import java.io.Serializable;

public class GameStatePacket implements Serializable {
    private static final long serialVersionUID = 5L;

    public String State;


    public GameStatePacket(String state) {
        this.State = state;
    }
}
