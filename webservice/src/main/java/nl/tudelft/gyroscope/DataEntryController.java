package nl.tudelft.gyroscope;

import nl.tudelft.classifier.Gyrolearn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weka.classifiers.AbstractClassifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Sille Kamoen on 7-3-16.
 */
@RestController
@RequestMapping(value = "/data")
public class DataEntryController {

    @Autowired
    AttemptRepository attemptRepository;

    @RequestMapping(method = RequestMethod.GET)
    List<Attempt> showAllData() {
        return attemptRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addDataEntry(@RequestBody Attempt attempt) {
//        Attempt attempt = new Attempt();
//        attempt.setAndroidId(attempt.getAndroidId());
//
//        for (GyroData dto : attempt.getEntries()) {
//            attempt.getEntries().add(new GyroData(dto.getTime(), dto.getYaw(), dto.getPitch(), dto.getRoll()));
//        }

        attemptRepository.save(attempt);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> testData(@RequestBody Attempt attempt) {
        // victim post his/her gyroscope data through this entrypoint
        // application try to determine the pin
        // save the result to database, will need another entrypoint to see the result

        List<GyroData> entries = attempt.getEntries();
        ArrayList<String> rawCSVData = new ArrayList<String>();

        for(GyroData entry : entries) {
            rawCSVData.add(entry.toString());
        }

        String result = "";
        try {
            AbstractClassifier classifier = Gyrolearn.getClassifier();
            result = Gyrolearn.predictPin(classifier, rawCSVData);
        }
        catch (Exception ex) {

        }

        // save result somewhere

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{androidId}")
    ResponseEntity<?> getDataForId(@PathVariable String androidId) {
        Set<Attempt> dataEntrySet = attemptRepository.findByAndroidId(androidId);

        return new ResponseEntity<>(dataEntrySet, HttpStatus.OK);
    }
}
