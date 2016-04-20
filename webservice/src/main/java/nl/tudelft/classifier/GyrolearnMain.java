package nl.tudelft.classifier;

import java.util.Random;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;

public class GyrolearnMain {


    public static void main(String[] args) throws Exception {

        Gyrolearn.defineClass();
        Instances dataset = Gyrolearn.loadDataset();

        AbstractClassifier classifier = new NaiveBayes();
        classifier.buildClassifier(dataset);

        Evaluation eval = new Evaluation(dataset);
        eval.crossValidateModel(classifier, dataset, 10, new Random());
        System.out.println("Estimated Accuracy: "+Double.toString(eval.pctCorrect()));

        Instances test = Gyrolearn.loadData("data/test/pin1393.txt");
        Gyrolearn.predictPin(classifier,test);


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