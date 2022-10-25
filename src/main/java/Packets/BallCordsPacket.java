package Packets;

import java.io.Serializable;

public class BallCordsPacket implements Serializable {
    private static final long serialVersionUID = 2L;

    public int BallX;
    public int BallY;
    public int Xdir;
    public int Ydir;

    public BallCordsPacket(int x, int y, int xdir, int ydir) {
        this.BallX = x;
        this.BallY = y;
        this.Xdir = xdir;
        this.Ydir = ydir;
    }
}
