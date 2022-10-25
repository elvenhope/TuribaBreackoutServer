package Packets;

import java.io.Serializable;
import java.util.HashMap;

public class brickTrackerPacket implements Serializable {
    private static final long serialVersionUID = 3L;

    public HashMap<Integer, Boolean> brickTracker = new HashMap<>();

    public brickTrackerPacket(HashMap<Integer, Boolean> NewData) {
        brickTracker.clear();
        for (HashMap.Entry<Integer, Boolean> entry : NewData.entrySet()) {
            Integer key = entry.getKey();
            Boolean value = entry.getValue();
            brickTracker.put(key, value);
        }
    }
}
