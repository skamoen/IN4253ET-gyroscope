package nl.tudelft.gyroscope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by Sille Kamoen on 7-3-16.
 */
@RestController
@RequestMapping(value = "/data")
@CrossOrigin(origins = "http://localhost:5000")
public class DataEntryController {

    @Autowired
    AttemptRepository attemptRepository;

    @Autowired
    ProcessingService processingService;

    @RequestMapping(method = RequestMethod.GET)
    List<Attempt> showAllData() {
        return attemptRepository.findAll();
    }

    @RequestMapping(value = "/results")
    List<AttemptResult> showAllResults() {
        return processingService.getAllResults();
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> testData(@RequestBody Attempt attempt) {

        AttemptResult result = null;
        try {
            result = processingService.processAttempt(attempt);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{androidId}")
    ResponseEntity<?> getDataForId(@PathVariable String androidId) {
        Set<Attempt> dataEntrySet = attemptRepository.findByAndroidId(androidId);

        return new ResponseEntity<>(dataEntrySet, HttpStatus.OK);
    }
}
