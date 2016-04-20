package gyrolearning;

import java.io.IOException;
import weka.core.Instances;

/**
 *
 * @author H2c
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        String file = "data/pin_1/gyro_-1966429616.txt";
        Instances sample = GyroLearner.loadData(file);
        System.out.println(sample.toString());
        
        
    }
    
}
