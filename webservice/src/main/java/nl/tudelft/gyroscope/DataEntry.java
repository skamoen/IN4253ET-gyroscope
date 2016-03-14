package nl.tudelft.gyroscope;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Sille Kamoen on 7-3-16.
 */
@Entity
public class DataEntry {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    Device device;

    String androidId;

    String time;
    String gData0;
    String gData1;
    String gData2;
    String gData3;

    public DataEntry(String androidId, String time, String gData0, String gData1, String gData2, String gData3) {
        this.androidId = androidId;
        this.time = time;
        this.gData0 = gData0;
        this.gData1 = gData1;
        this.gData2 = gData2;
        this.gData3 = gData3;
    }

    public DataEntry(){
        //JPA ONLY
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getgData0() {
        return gData0;
    }

    public void setgData0(String gData0) {
        this.gData0 = gData0;
    }

    public String getgData1() {
        return gData1;
    }

    public void setgData1(String gData1) {
        this.gData1 = gData1;
    }

    public String getgData2() {
        return gData2;
    }

    public void setgData2(String gData2) {
        this.gData2 = gData2;
    }

    public String getgData3() {
        return gData3;
    }

    public void setgData3(String gData3) {
        this.gData3 = gData3;
    }
}
