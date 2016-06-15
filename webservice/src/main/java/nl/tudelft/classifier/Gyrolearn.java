package nl.tudelft.classifier;

import nl.tudelft.gyroscope.Attempt;
import nl.tudelft.gyroscope.GyroData;
import nl.tudelft.gyroscope.ResultPin;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Gyrolearn {

    public static final int ATTR_YAW = 1;
    public static final int ATTR_PITCH = 2;
    public static final int ATTR_ROLL = 3;

    public static final int DATA_CAPACITY = 9999;

    public static final int MARGIN_FRONT = 10;
    public static final int MARGIN_BACK = 10;

    public static final int NUM_OF_TIMEFRAME = 40;

    public static ArrayList<String> classes = new ArrayList<>();

    public static AbstractClassifier theClassifier;


    public static AbstractClassifier getClassifier() throws Exception {
        if (theClassifier == null) {
            // if we already have the classifier, do not rebuild it again
            // ideally this should be serialized/saved into the db or such and load from that
            Gyrolearn.defineClass();
            Instances dataset = Gyrolearn.loadDataset();
            //theClassifier = new NaiveBayes();
            theClassifier = new SMO();
            theClassifier.buildClassifier(dataset);
        }
        return theClassifier;
    }

    public static void defineClass() {
        classes = new ArrayList();
        for (int pin = 0; pin <= 9; pin++) {
            classes.add("pin_" + pin);
        }
    }

    public static Instances createDataset() {
        ArrayList<Attribute> attr_list = new ArrayList<>();

        for (int i = 0; i < NUM_OF_TIMEFRAME*3; ++i) {
            attr_list.add(new Attribute("attr" + i));
        }

        Attribute class_attr = new Attribute("class", classes);

        attr_list.add(class_attr);

        Instances dataset = new Instances("GyroData", attr_list, DATA_CAPACITY);

        dataset.setClass(class_attr);

        return dataset;
    }

    public static Instances loadDataset() throws IOException {

        Instances dataset = createDataset();

        for (String cls : classes) {
            File dir = new File("../gyro_data/" + cls + "/");

            File[] fileList = dir.listFiles();
            for (File file : fileList) {
                if (file.isFile()) {
                    String filename = file.getAbsolutePath();
                    Instances raw = loadData(filename);
                    Instance record = preProcessTrain(raw);

                    record.setValue(dataset.classAttribute(), cls);

                    dataset.add(record);

                }
            }
        }
        return dataset;
    }

    public static Instances loadData(String inputfile) throws IOException {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(inputfile));
        Instances data = loader.getDataSet();

        return data;
    }

    public static Instance extractFeatures(Instances raw, int start_index, int end_index) {
        Instance output = new DenseInstance(3*NUM_OF_TIMEFRAME+1);

        int n = end_index - start_index;

        double yaw_max = -999;
        double yaw_min = 999;
        double pitch_max = -999;
        double pitch_min = 999;
        double roll_max = -999;
        double roll_min = 999;

        for (int time = 0; time < NUM_OF_TIMEFRAME; time++) {
            int t = start_index + n * time / (NUM_OF_TIMEFRAME+1);
            int t_1 = start_index + n * (time + 1) / (NUM_OF_TIMEFRAME+1);

            Instance data_t_ins = raw.get(t);
            String[] data_t = data_t_ins.stringValue(0).split(";");

            Instance data_t_1_ins = raw.get(t_1);
            String[] data_t_1 = data_t_1_ins.stringValue(0).split(";");
            double delta_yaw = Double.parseDouble(data_t_1[1]) - Double.parseDouble(data_t[1]);
            if (delta_yaw > yaw_max) {
                yaw_max = delta_yaw;
            }
            if (delta_yaw < yaw_min) {
                yaw_min = delta_yaw;
            }
            double delta_pitch = Double.parseDouble(data_t_1[2]) - Double.parseDouble(data_t[2]);
            if (delta_pitch > pitch_max) {
                yaw_max = delta_pitch;
            }
            if (delta_pitch < pitch_min) {
                yaw_min = delta_pitch;
            }

            double delta_roll = Double.parseDouble(data_t_1[3]) - Double.parseDouble(data_t[3]);
            if (delta_roll > roll_max) {
                roll_max = delta_roll;
            }
            if (delta_roll < roll_min) {
                yaw_min = delta_roll;
            }
            //System.out.println("yaw_ = "+Double.parseDouble(data_t_1[1])+" pitch = "+Double.parseDouble(data_t_1[2])+" roll = "+Double.parseDouble(data_t_1[3]));

            output.setValue(time, delta_yaw);
            output.setValue(time + NUM_OF_TIMEFRAME, delta_pitch);
            output.setValue(time + 2*NUM_OF_TIMEFRAME, delta_roll);

        }
        //System.out.println("yaw_max = "+yaw_max+" yaw_min = "+yaw_min);
/*        for(int time=0; time<100; time++){
            if(yaw_max > yaw_min){ output.setValue(time, (output.value(time)-yaw_min)*100/(yaw_max-yaw_min)); }
            if(pitch_max > pitch_min){ output.setValue(time+100, (output.value(time+100)-pitch_min)*100/(pitch_max-pitch_min)); }
            if(roll_max > roll_min){ output.setValue(time+200, (output.value(time+200)-roll_min)*100/(roll_max-roll_min)); }
        }
*/
        return output;
    }


    public static Instance extractFeatures(ArrayList<String> raw, int start_index, int end_index) {
        Instance output = new DenseInstance(3*NUM_OF_TIMEFRAME+1);

        int n = end_index - start_index;

        double yaw_max = -999;
        double yaw_min = 999;
        double pitch_max = -999;
        double pitch_min = 999;
        double roll_max = -999;
        double roll_min = 999;

        for (int time = 0; time < NUM_OF_TIMEFRAME; time++) {
            int t = start_index + n * time / (NUM_OF_TIMEFRAME+1);
            int t_1 = start_index + n * (time + 1) / (NUM_OF_TIMEFRAME+1);

            String data_t_ins = raw.get(t);
            String[] data_t = data_t_ins.split(";");

            String data_t_1_ins = raw.get(t_1);
            String[] data_t_1 = data_t_1_ins.split(";");
            double delta_yaw = Double.parseDouble(data_t_1[1]) - Double.parseDouble(data_t[1]);
            if (delta_yaw > yaw_max) {
                yaw_max = delta_yaw;
            }
            if (delta_yaw < yaw_min) {
                yaw_min = delta_yaw;
            }
            double delta_pitch = Double.parseDouble(data_t_1[2]) - Double.parseDouble(data_t[2]);
            if (delta_pitch > pitch_max) {
                yaw_max = delta_pitch;
            }
            if (delta_pitch < pitch_min) {
                yaw_min = delta_pitch;
            }

            double delta_roll = Double.parseDouble(data_t_1[3]) - Double.parseDouble(data_t[3]);
            if (delta_roll > roll_max) {
                roll_max = delta_roll;
            }
            if (delta_roll < roll_min) {
                yaw_min = delta_roll;
            }
            //System.out.println("yaw_ = "+Double.parseDouble(data_t_1[1])+" pitch = "+Double.parseDouble(data_t_1[2])+" roll = "+Double.parseDouble(data_t_1[3]));

            output.setValue(time, delta_yaw);
            output.setValue(time + NUM_OF_TIMEFRAME, delta_pitch);
            output.setValue(time + 2*NUM_OF_TIMEFRAME, delta_roll);

        }
        //System.out.println("yaw_max = "+yaw_max+" yaw_min = "+yaw_min);
/*        for(int time=0; time<100; time++){
            if(yaw_max > yaw_min){ output.setValue(time, (output.value(time)-yaw_min)*100/(yaw_max-yaw_min)); }
            if(pitch_max > pitch_min){ output.setValue(time+100, (output.value(time+100)-pitch_min)*100/(pitch_max-pitch_min)); }
            if(roll_max > roll_min){ output.setValue(time+200, (output.value(time+200)-roll_min)*100/(roll_max-roll_min)); }
        }
*/
        return output;
    }

    public static Instance preProcessTrain(Instances raw) {
        return extractFeatures(raw, MARGIN_FRONT, raw.size() - MARGIN_BACK);

    }

    public static ArrayList<Instance> preProcessTest(Instances testdata) {

        ArrayList<Instance> output_list = new ArrayList<>();
        ArrayList<Integer> start_index = new ArrayList<>();
        ArrayList<Integer> end_index = new ArrayList<>();

        int n_pin = 4;

        int data_size = testdata.size() - MARGIN_FRONT - MARGIN_BACK;

        //defining starting & ending time index for each pin digit
        int counter = MARGIN_FRONT + 1;
        for (int p = 0; p < n_pin; ++p) {
            start_index.add(counter);
            counter += (int) data_size / 4;
            end_index.add(counter);
        }

        //extracting the features for each digit
        for (int p = 0; p < n_pin; ++p) {
            Instance single_digit = extractFeatures(testdata, start_index.get(p), end_index.get(p));
            output_list.add(single_digit);
        }

        return output_list;
    }

    public static ArrayList<Instance> preProcessTest(ArrayList<String> testdata) {

        ArrayList<Instance> output_list = new ArrayList<>();
        ArrayList<Integer> start_index = new ArrayList<>();
        ArrayList<Integer> end_index = new ArrayList<>();

        int n_pin = 4;

        int data_size = testdata.size() - MARGIN_FRONT - MARGIN_BACK;

        //defining starting & ending time index for each pin digit
        int counter = MARGIN_FRONT + 1;
        for (int p = 0; p < n_pin; ++p) {
            start_index.add(counter);
            counter += (int) data_size / 4;
            end_index.add(counter);
        }

        //extracting the features for each digit
        for (int p = 0; p < n_pin; ++p) {
            Instance single_digit = extractFeatures(testdata, start_index.get(p), end_index.get(p));
            output_list.add(single_digit);
        }

        return output_list;
    }

    public static String predictPin(AbstractClassifier classifier, Instances rawtest)
            throws Exception {
        String pin = "";
        ArrayList<Instance> datatest = preProcessTest(rawtest);
        Instances dataset = createDataset();
        int counter = 1;
        for (Instance d : datatest) {
            d.setDataset(dataset);
            /*
            double[] p= classifier.distributionForInstance(d);
            System.out.print("digit "+counter+": "); counter++;
            for(double dist: p){
                System.out.print(dist+" ");
            }
            System.out.println("");
            */
            System.out.println("digit " + counter + ": " + classifier.classifyInstance(d));
            counter++;

        }
        return pin;
    }

    public static ArrayList<ResultPin> predictPin(AbstractClassifier classifier,
                                                  ArrayList<String> rawtest)
            throws Exception {
        ArrayList<Instance> datatest = preProcessTest(rawtest);
        Instances dataset = createDataset();
        ArrayList<ResultPin> results = new ArrayList<>();

        int counter = 1;
        for (Instance d : datatest) {
            d.setDataset(dataset);
            double[] p = classifier.distributionForInstance(d);
            System.out.print("digit " + counter + ": ");
            results.add(counter - 1, new ResultPin());

            for (int idx = 0; idx < p.length; idx++) {
                results.get(counter - 1).addResult(idx, p[idx]);

                System.out.print(p[idx] + " ");

            }
            counter++;

        }
        return results;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static String predictPinFromAttempt(AbstractClassifier classifier, Attempt attempt)
            throws Exception {
        String pin = "";
        ArrayList<Instance> datatest = preProcessAttempt(attempt);
        Instances dataset = createDataset();
        int counter = 1;
        for (Instance d : datatest) {
            d.setDataset(dataset);
            double[] p = classifier.distributionForInstance(d);
            System.out.print("digit " + counter + ": ");
            counter++;
            for (double dist : p) {
                System.out.print(dist + " ");
            }
            System.out.println("");

        }
        return pin;
    }

    private static ArrayList<Instance> preProcessAttempt(Attempt attempt) {
        ArrayList<Instance> output_list = new ArrayList<>();
        ArrayList<Integer> start_index = new ArrayList<>();
        ArrayList<Integer> end_index = new ArrayList<>();

        int n_pin = 4;

        int data_size = attempt.getEntries().size() - MARGIN_FRONT - MARGIN_BACK;

        //defining starting & ending time index for each pin digit
        int counter = MARGIN_FRONT + 1;
        for (int p = 0; p < n_pin; ++p) {
            start_index.add(counter);
            counter += (int) data_size / 4;
            end_index.add(counter);
        }

        //extracting the features for each digit
        for (int p = 0; p < n_pin; ++p) {
            Instance
                    single_digit =
                    extractFeaturesFromAttempt(attempt, start_index.get(p), end_index.get(p));
            output_list.add(single_digit);
        }

        return output_list;
    }

    private static Instance extractFeaturesFromAttempt(Attempt attempt, int start_index,
                                                       int end_index) {
        Instance output = new DenseInstance(3*NUM_OF_TIMEFRAME+1);

        ArrayList<Double> delta_yaw = new ArrayList<>();
        ArrayList<Double> delta_pitch = new ArrayList<>();
        ArrayList<Double> delta_roll = new ArrayList<>();

        GyroData data_prev_ins = attempt.getEntries().get(start_index);
//        String[] data_prev = data_prev_ins.stringValue(0).split(";");

        for (int i = start_index + 1; i <= end_index; i++) {
            GyroData data_t_ins = attempt.getEntries().get(i);
            delta_yaw.add(
                    Double.parseDouble(data_t_ins.getYaw()) - Double.parseDouble(data_prev_ins.getYaw()));
            delta_pitch.add(
                    Double.parseDouble(data_t_ins.getPitch()) - Double.parseDouble(data_prev_ins.getPitch()));
            delta_roll.add(
                    Double.parseDouble(data_t_ins.getRoll()) - Double.parseDouble(data_prev_ins.getRoll()));
            data_prev_ins = data_t_ins;
        }

        int n = delta_yaw.size();
        for (int time = 0; time < NUM_OF_TIMEFRAME; time++) {
            int t = (int) ((double) (time / NUM_OF_TIMEFRAME)) * n;
            output.setValue(time, delta_yaw.get(t));
            output.setValue(time + NUM_OF_TIMEFRAME, delta_pitch.get(t));
            output.setValue(time + 2*NUM_OF_TIMEFRAME, delta_roll.get(t));
        }

        return output;

    }

}