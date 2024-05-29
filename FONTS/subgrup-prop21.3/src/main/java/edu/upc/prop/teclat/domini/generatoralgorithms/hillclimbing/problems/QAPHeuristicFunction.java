package edu.upc.prop.teclat.domini.generatoralgorithms.hillclimbing.problems;

import edu.upc.prop.teclat.domini.generatoralgorithms.hillclimbing.HeuristicFunction;

public class QAPHeuristicFunction implements HeuristicFunction {
    public QAPHeuristicFunction() {
        
    }

    public double getHeuristicValue(Object obj){
        return ((QAP_HC)obj).getCost();
    }
}