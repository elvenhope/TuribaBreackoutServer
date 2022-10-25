package Packets;

import java.io.Serializable;
import java.util.HashMap;

public class PaddlesPacket implements Serializable {
    private static final long serialVersionUID = 8L;
    public HashMap<Integer, Integer> paddles = new HashMap<>();

    public PaddlesPacket(HashMap<Integer, Integer> NewData) {
        paddles.clear();
        for (HashMap.Entry<Integer, Integer> entry : NewData.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            paddles.put(key, value);
        }
    }
}
