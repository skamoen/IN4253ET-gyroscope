package nl.tudelft.classifier;

import java.util.Random;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

public class GyrolearnMain {


    public static void main(String[] args) throws Exception {

        Gyrolearn.defineClass();
        Instances dataset = Gyrolearn.loadDataset();

        AbstractClassifier classifier = Gyrolearn.getClassifier();
        System.out.println("content "+dataset.size());

        Evaluation eval = new Evaluation(dataset);
        eval.crossValidateModel(classifier, dataset, 10, new Random());
        System.out.println("Estimated Accuracy: "+Double.toString(eval.pctCorrect()));

        for(int tes=1; tes<=7; tes++){
            Instances test = Gyrolearn.loadData("D:/Data/gyro_data/test/pin1234_"+tes+".txt");
            Gyrolearn.predictPin(classifier,test);


        }



        for(int i=0; i< dataset.size(); ++i){
            Instance trn = dataset.get(i);
            double[] p= classifier.distributionForInstance(trn);
            System.out.print("correct class "+trn.stringValue(dataset.classAttribute())+": ");
            for(double dist: p){
                System.out.print((dist*100)+" ");
            }
            System.out.println("");
        }

    }
}