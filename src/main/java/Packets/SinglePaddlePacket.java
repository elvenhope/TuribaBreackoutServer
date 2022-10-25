package Packets;

import java.io.Serializable;

public class SinglePaddlePacket implements Serializable {
    private static final long serialVersionUID = 10L;

    public int id;
    public int dx;
    public SinglePaddlePacket(int id, int dx) {
        this.id = id;
        this.dx = dx;
    }
}
