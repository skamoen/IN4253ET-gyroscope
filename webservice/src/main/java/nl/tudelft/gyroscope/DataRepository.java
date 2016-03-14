package nl.tudelft.gyroscope;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Sille Kamoen on 7-3-16.
 */
@Repository
public interface DataRepository extends JpaRepository<DataEntry, Long> {

    Set<DataEntry> findByAndroidId(String androidId);
}
