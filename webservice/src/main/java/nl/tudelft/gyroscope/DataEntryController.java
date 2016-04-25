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

    @RequestMapping(method = RequestMethod.GET, value = "/{androidId}")
    ResponseEntity<?> getDataForId(@PathVariable String androidId) {
        Set<Attempt> dataEntrySet = attemptRepository.findByAndroidId(androidId);

        return new ResponseEntity<>(dataEntrySet, HttpStatus.OK);
    }
}
