/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyrolearn;

import java.io.IOException;
import java.util.Random;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.supportVector.Kernel;
import weka.core.Instance;
import weka.core.Instances;

import weka.classifiers.functions.supportVector.Kernel;


/**
 *
 * @author USER
 */
public class Main {
    
    
    public static void main(String[] args) throws Exception {
        
        Instances dataset = Gyrolearn.loadDataset();
        
        AbstractClassifier classifier = new NaiveBayes();
        classifier.buildClassifier(dataset);
        
        Evaluation eval = new Evaluation(dataset);
        eval.crossValidateModel(classifier, dataset, 10, new Random());
        System.out.println("Estimated Accuracy: "+Double.toString(eval.pctCorrect()));
        
        
    }
}
