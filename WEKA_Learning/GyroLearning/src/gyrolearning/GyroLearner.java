package gyrolearning;

import weka.core.converters.CSVLoader;
import weka.core.Instances;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author USER
 */
public class GyroLearner {
    
    public static Instances loadData(String inputfile) throws IOException{
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(inputfile));
        Instances data = loader.getDataSet();
        
        return data;
    }
    
    
}
