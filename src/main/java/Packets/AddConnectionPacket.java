package Packets;

import java.io.Serializable;

public class AddConnectionPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;

    public AddConnectionPacket(int id) {
        this.id = id;
    }
}
