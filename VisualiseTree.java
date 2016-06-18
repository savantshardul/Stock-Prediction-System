package com.stookers.stockclass;



import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 * Hello world!
 *
 */
public class VisualiseTree 
{
 public VisualiseTree(String sSymbol) 
  {
	 try{
    	
    	 	String SourcePath="c:/eclipse projects/stookers/stockdata/" + sSymbol.toUpperCase() + "_StockCopyData.csv";
    	 	String DestinationPath="c:/eclipse projects/stookers/stockdata/" + sSymbol.toUpperCase() + "_StockCopyData.arff";
    	    // load CSV
    	    CSVLoader loader = new CSVLoader();
    	    loader.setSource(new File(SourcePath));
    	    Instances data = loader.getDataSet();
    	 
    	    // save ARFF
    	    ArffSaver saver = new ArffSaver();
    	    saver.setInstances(data);
    	    saver.setFile(new File(DestinationPath));
    	    //saver.setDestination(new File("Users\\rohan_mehta\\Documents\\workspace\\j48"));
    	    saver.writeBatch();
    	J48 cls = new J48();
    	cls.setUnpruned(true);
        Instances data1 = new Instances(new BufferedReader(new FileReader(DestinationPath)));
        data1.setClassIndex(data1.numAttributes() - 1);
        cls.buildClassifier(data1);
        Evaluation eval = new Evaluation(data1);
        eval.crossValidateModel(cls, data1, 10, new Random(1));
        Double Correct_Classifaction = eval.correct();
        Double Correct_Per_Classifaction= eval.pctCorrect();
        Double Total_Instnaces = eval.numInstances();
        Double Tree_size= cls.measureTreeSize();
        Double Num_Leaves =  cls.measureNumLeaves();
        String Tree_size1= "Size of tree: "+String.valueOf(Tree_size);
        String Num_Leaves1= "Total number of leaves: "+String.valueOf(Num_Leaves);
        String Total_Instnaces1= "Total number of instances: "+String.valueOf(Total_Instnaces);
        String Correct_Classifaction1= "Number of correct instances taken: "+String.valueOf(Correct_Classifaction);
        String Correct_Per_Classifaction1= "Percentage of correctness: "+String.valueOf(Correct_Per_Classifaction);
        String label= "<html>Cross Validation of : 10 folds &nbsp;&nbsp;&nbsp; Percentage Split of : 60.00%<br><br>"+Correct_Classifaction1+"<br><br>"+Correct_Per_Classifaction1+"<br><br>"+Total_Instnaces1+"<br><br>"+Num_Leaves1+"<br><br>"+Tree_size1+"</html>";
        // display classifier
        final javax.swing.JFrame jf = 
          new javax.swing.JFrame("Tree Visualizer: J48");
        JLabel jl = new JLabel(label,JLabel.TRAILING);
        jf.setSize(500,400);
        jf.getContentPane().setLayout(new BorderLayout());
        TreeVisualizer tv = new TreeVisualizer(null,
            cls.graph(),
            new PlaceNode2());
        jf.getContentPane().add(tv, BorderLayout.CENTER);
        jf.getContentPane().add(jl,BorderLayout.PAGE_END);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
          public void windowClosing(java.awt.event.WindowEvent e) {
            jf.dispose();
          }
        });
    
        jf.setVisible(true);
        tv.fitToScreen();
	 }
	 catch(Exception e)
	 {
		 System.out.print(e);
	 }
	 
  }
	 }
    

