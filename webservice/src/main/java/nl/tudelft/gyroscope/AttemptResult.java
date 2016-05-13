package nl.tudelft.gyroscope;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Sille Kamoen on 6-5-16.
 */
@Entity
public class AttemptResult {

    @GeneratedValue
    @Id
    Long id;

    private Long attemptId;

    private String result;

    public AttemptResult(Attempt attempt, String result) {
        this.attemptId = attempt.getId();
        this.result = result;
    }

    public AttemptResult() {
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
