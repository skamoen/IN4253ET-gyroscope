package nl.tudelft.gyroscope;

import nl.tudelft.classifier.Gyrolearn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weka.classifiers.AbstractClassifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sille Kamoen on 20-4-16.
 */
@Service
public class ProcessingService {

  @Autowired
  ResultRepository resultRepository;

  public AttemptResult processAttempt(Attempt attempt) throws Exception {
    // Insert learning stuff here
    // victim post his/her gyroscope data through this entrypoint
    // application try to determine the pin
    // save the result to database, will need another entrypoint to see the result

    List<GyroData> entries = attempt.getEntries();
    ArrayList<String> rawCSVData = new ArrayList<String>();

    for (GyroData entry : entries) {
      rawCSVData.add(entry.toString());
    }

    AbstractClassifier classifier = Gyrolearn.getClassifier();
    ArrayList<ResultPin> resultPin = Gyrolearn.predictPin(classifier, rawCSVData);
    AttemptResult attemptResult = new AttemptResult(attempt.getId(), resultPin);

    // save result somewhere
    return resultRepository.save(attemptResult);
  }

  public List<AttemptResult> getAllResults() {
    return resultRepository.findAll();
  }
}
