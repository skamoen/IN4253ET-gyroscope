package nl.tudelft.gyroscope;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Sille Kamoen on 25-4-16.
 */
@Repository
public interface ResultRepository extends JpaRepository<AttemptResult, Long> {

    Set<AttemptResult> findByAttemptId(String attemptId);

}
