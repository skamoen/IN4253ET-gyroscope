package nl.tudelft.gyroscope;

import java.io.Serializable;

/**
 * Created by Sille Kamoen on 6-5-16.
 */
public class ResultPin implements Serializable {

  private int primaryDigit;
  private double primaryProbability;
  private int secondaryDigit;
  private double secondaryProbability;

  public void addResult(int digit, double probability) {
    if (this.primaryProbability < probability) {
      this.secondaryDigit = primaryDigit;
      this.secondaryProbability = primaryProbability;
      this.primaryDigit = digit;
      this.primaryProbability = probability;
    } else if (this.secondaryProbability < probability) {
      this.secondaryDigit = digit;
      this.secondaryProbability = probability;
    }
  }

  public ResultPin() {
  }


  public int getPrimaryDigit() {
    return primaryDigit;
  }

  public void setPrimaryDigit(int primaryDigit) {
    this.primaryDigit = primaryDigit;
  }

  public double getPrimaryProbability() {
    return primaryProbability;
  }

  public void setPrimaryProbability(double primaryProbability) {
    this.primaryProbability = primaryProbability;
  }

  public int getSecondaryDigit() {
    return secondaryDigit;
  }

  public void setSecondaryDigit(int secondaryDigit) {
    this.secondaryDigit = secondaryDigit;
  }

  public double getSecondaryProbability() {
    return secondaryProbability;
  }

  public void setSecondaryProbability(double secondaryProbability) {
    this.secondaryProbability = secondaryProbability;
  }
}
