package nl.tudelft.gyroscope;

import java.util.ArrayList;

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

  private ArrayList<ResultPin> resultPins;


  public AttemptResult() {
  }

  public Long getAttemptId() {
    return attemptId;
  }

  public void setAttemptId(Long attemptId) {
    this.attemptId = attemptId;
  }

  public AttemptResult(Long attemptId, ArrayList<ResultPin> resultPins) {
    this.attemptId = attemptId;
    this.resultPins = resultPins;
  }

  public Long getId() {
    return id;
  }

  public ArrayList<ResultPin> getResultPins() {
    return resultPins;
  }

  public void setResultPins(ArrayList<ResultPin> resultPins) {
    this.resultPins = resultPins;
  }
}




