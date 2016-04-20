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
    DataRepository dataRepository;

    @RequestMapping(method = RequestMethod.GET)
    List<DataEntry> showAllData() {
        return dataRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addDataEntry(@RequestBody Attempt attempt) {
        String androidId = attempt.getAndroidId();
        for (GyroDTO dto : attempt.getEntries()) {
            dataRepository.save(new DataEntry(androidId, dto.getTime(), dto.getgData0(), dto.getgData1(),
                    dto.getgData2(), dto.getgData3()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{androidId}")
    ResponseEntity<?> getDataForId(@PathVariable String androidId) {
        Set<DataEntry> dataEntrySet = dataRepository.findByAndroidId(androidId);

        return new ResponseEntity<>(dataEntrySet, HttpStatus.OK);
    }
}
