package nl.tudelft.superevilhackinglab;

import java.util.ArrayList;

/**
 * Created by divhax on 13/05/2016.
 */
public class Attempt {
    public static class Entries {
        public Entries(String time, String yaw, String pitch, String roll) {
            this.time = time;
            this.yaw = yaw;
            this.pitch = pitch;
            this.roll = roll;
        }

        public String time;
        public String yaw;
        public String pitch;
        public String roll;
    }

    public String androidId;
    public ArrayList<Entries> entries = new ArrayList<>();
}
