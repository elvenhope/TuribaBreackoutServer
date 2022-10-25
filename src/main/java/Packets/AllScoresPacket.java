package Packets;

import java.io.Serializable;
import java.util.HashMap;

public class AllScoresPacket implements Serializable {
    private static final long serialVersionUID = 12L;
    public HashMap<Integer, Integer> scoreTracker = new HashMap<>();
}
