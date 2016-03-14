package nl.tudelft.gyroscope;

import java.util.Collection;

/**
 * Created by Sille Kamoen on 7-3-16.
 */
public class AndroidDTO {

    String androidId;

    Collection<GyroDTO> entries;

    public Collection<GyroDTO> getEntries() {
        return entries;
    }

    public void setEntries(Collection<GyroDTO> entries) {
        this.entries = entries;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }
}
