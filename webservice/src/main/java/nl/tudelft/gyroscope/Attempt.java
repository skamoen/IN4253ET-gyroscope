package nl.tudelft.gyroscope;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sille Kamoen on 7-3-16.
 */
@Entity
public class Attempt {

    @Id
    @GeneratedValue
    Long id;

    private String androidId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<GyroData> entries;

    public Attempt() {
        //JPA
        this.entries = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public List<GyroData> getEntries() {
        return entries;
    }

    public void setEntries(List<GyroData> entries) {
        this.entries = entries;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }
}
