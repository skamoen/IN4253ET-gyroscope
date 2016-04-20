/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyrolearn;

import weka.core.converters.CSVLoader;
import weka.core.Instances;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;

/**
 *
 * @author USER
 */
public class Gyrolearn {

    /**
     * @param args the command line arguments
     */
    
    public static int attrib_yaw = 1;
    public static int attrib_pitch = 2;
    public static int attrib_roll = 3;
    
    public static int data_capacity = 9999;
    
    
    
    public static Instances loadDataset() throws IOException{
        
        ArrayList<String> classes = new ArrayList<>();
        classes.add("pin_"+1);
        classes.add("pin_"+3);
        classes.add("pin_"+9);
        
        ArrayList<Attribute> attr_list = new ArrayList<>();
        
        for(int i=0; i<300; ++i){
            attr_list.add(new Attribute("attr"+i));
        }
        
        Attribute Class_attr = new Attribute("Class", classes);
        
        attr_list.add(Class_attr);
        
        
        Instances dataset = new Instances("GyroData", attr_list, data_capacity);
        
        dataset.setClass(Class_attr);
        
        for(String cls: classes){
            File dir = new File("data/"+cls+"/");
            File[] fileList = dir.listFiles();
            
            for (File file : fileList) {
                if (file.isFile()) {
                    String filename = file.getAbsolutePath();
                    Instances raw = loadData(filename);
                    Instance record = preProcess(raw,dataset);
                    
                    record.setValue(dataset.classAttribute(), cls);
                    
                    dataset.add(record);
                    
                }
            }    
        }
        return dataset;
    }
    
    public static Instances loadData(String inputfile) throws IOException{
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(inputfile));
        Instances data = loader.getDataSet();
        
        return data;
    }
    
    public static Instance preProcess(Instances raw, Instances dataset){
        
        Instance output = new DenseInstance(301);
        output.setDataset(dataset);
        
        ArrayList<Double> delta_yaw = new ArrayList<>();
        ArrayList<Double> delta_pitch = new ArrayList<>();
        ArrayList<Double> delta_roll = new ArrayList<>();
        
        Instance data_prev_ins = raw.get(10);
        String[] data_prev = data_prev_ins.stringValue(0).split(";");
        
        for (int i=11; i< (raw.size() - 10); i++){
            Instance data_t_ins = raw.get(i);
            String[] data_t = data_t_ins.stringValue(0).split(";");
            delta_yaw.add(Double.parseDouble(data_t[1]) - Double.parseDouble(data_prev[1]));
            delta_pitch.add(Double.parseDouble(data_t[2]) - Double.parseDouble(data_prev[2]));
            delta_roll.add(Double.parseDouble(data_t[3]) - Double.parseDouble(data_prev[3]));
            data_prev = data_t;
        }
        
       
        int n = delta_yaw.size();
        for(int time=0; time<100; time++){
            int t = (int)((double)(time/100))*n;
            output.setValue(time, delta_yaw.get(t));
            output.setValue(time+100, delta_pitch.get(t));
            output.setValue(time+200, delta_roll.get(t));
        }
        
        return output;
    }
    
}
