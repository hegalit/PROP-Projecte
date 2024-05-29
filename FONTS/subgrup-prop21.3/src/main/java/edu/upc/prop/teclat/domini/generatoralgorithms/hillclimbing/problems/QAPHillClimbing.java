package edu.upc.prop.teclat.domini.generatoralgorithms.hillclimbing.problems;

import java.util.Random;

import edu.upc.prop.teclat.domini.PairsFrequency;
import edu.upc.prop.teclat.domini.generatoralgorithms.GeneratorAlgorithm;
import edu.upc.prop.teclat.domini.generatoralgorithms.hillclimbing.HillClimbing;

public class QAPHillClimbing implements GeneratorAlgorithm {

    //Constructor
    public QAPHillClimbing() {

    }

    //Solves the instance defined at constructor
    //Input parameters define an instance of QAP
    public char[] solve(String symbols, int cols, PairsFrequency pairsFreq) {
        // Generate seeds so as to make multiple executions 
        // of the same problem with different initial states
        long[] seeds = new long[10];
        Random rnd = new Random();        
        for (int i = 0; i < seeds.length; ++i)
            seeds[i] = rnd.nextLong();

        QAP_HC solution = null; //We haven't found any solution yet
        double minCost = Double.MAX_VALUE; //Worst cost possible

        for (int i = 0; i < seeds.length; ++i) {
            QAP_HC candidate = new QAP_HC(symbols.toCharArray(), cols, pairsFreq);

            //Generate initial solution applying Fisher-Yates shuffle
            candidate.FYshuffle(seeds[i]);

            //Execute Hill Climbing in order to find a better solution
            HillClimbing hc_solver = new HillClimbing(candidate, new QAPSuccessorFunction(), new QAPHeuristicFunction());
            candidate = (QAP_HC)hc_solver.solve();
            double cost = candidate.getCost();

            if (solution == null || minCost > cost) {
                solution = candidate;
                minCost = cost;
            }
        }

        //Returns the layout of the best solution found
        return solution.getLayout();
    }
}
