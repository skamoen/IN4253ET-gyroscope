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
    ResponseEntity<?> addDataEntry(@RequestBody AndroidDTO androidDTO) {
        String androidId = androidDTO.getAndroidId();
        for (GyroDTO dto : androidDTO.getEntries()) {
            dataRepository.save(new DataEntry(androidId, dto.time, dto.gData0, dto.gData1, dto.gData2, dto.gData3));
        }

        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{androidId}")
    ResponseEntity<?> getDataForId(@PathVariable String androidID) {
        Set<DataEntry> dataEntrySet = dataRepository.findByAndroidId(androidID);

        return new ResponseEntity<Set<DataEntry>>(dataEntrySet, HttpStatus.OK);
    }
}
