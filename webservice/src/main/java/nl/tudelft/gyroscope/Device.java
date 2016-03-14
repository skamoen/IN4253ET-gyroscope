package nl.tudelft.gyroscope;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Sille Kamoen on 7-3-16.
 */
@Entity
public class Device {
    @Id
    Long deviceId;

    String macAddress;

    public Device(String macAddress) {
        this.macAddress = macAddress;
    }
}
