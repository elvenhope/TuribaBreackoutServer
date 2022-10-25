package Packets;

import java.io.Serializable;

public class ScoreboardPacket implements Serializable {
    private static final long serialVersionUID = 11L;

    public int id;

    public int score;

    public ScoreboardPacket(int id, int score) {
        this.id = id;
        this.score = score;
    }
}
