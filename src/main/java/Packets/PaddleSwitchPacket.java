package Packets;

import java.io.Serializable;
import java.util.HashMap;

public class PaddleSwitchPacket implements Serializable {
    private static final long serialVersionUID = 13L;
    public HashMap<Integer, Integer> paddles = new HashMap<>();
}
