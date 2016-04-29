package nl.tudelft.gyroscope;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Sille Kamoen on 7-3-16.
 */
@Entity
public class GyroData {
    @Id
    @JsonIgnore
    @GeneratedValue
    private long id;

    private String time;
    private String yaw;
    private String pitch;
    private String roll;

    GyroData(String time, String yaw, String pitch, String roll) {
        this.time = time;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public GyroData() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getYaw() {
        return yaw;
    }

    public void setYaw(String yaw) {
        this.yaw = yaw;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    @Override
    public String toString() {
        // return the csv representation of this entry
        return time + ";" + yaw + ";" + pitch + ";" + roll + ";";
    }

}
