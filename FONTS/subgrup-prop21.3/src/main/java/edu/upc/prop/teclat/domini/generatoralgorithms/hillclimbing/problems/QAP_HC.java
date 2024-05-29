package edu.upc.prop.teclat.domini.generatoralgorithms.hillclimbing.problems;

import java.util.Arrays;
import java.util.Random;
import java.lang.Math;
import edu.upc.prop.teclat.domini.PairsFrequency;

/*
 * Representación del estado
 */
public class QAP_HC {
    static private int cols;
    static private PairsFrequency pairFreq; //Frequency of each pair of symbols 
    private char[] layout;  //Layout of symbols assigned to keys that represents the solution


    // Constructor
    public QAP_HC(char[] symbols, int cols, PairsFrequency pairFreq){
        QAP_HC.cols        = cols;
        QAP_HC.pairFreq    = pairFreq;
        this.layout     = symbols;
    }

    // Copy function by value
    public QAP_HC copy() { 
        char[] symbols = Arrays.copyOf(layout, layout.length);   
        return new QAP_HC(symbols, cols, pairFreq);
    }

    // Returns Euclidean distance between two points (2D)
    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }


    /*
     * Initial solution generators
     */

    //Shuffle symbols in layout using Fisher-Yates algorithm
    public void FYshuffle (long seed) {
        Random random = new Random(seed);
        for (int i = layout.length -1; i >= 0; --i) {
            int j = random.nextInt(i+1);
            char c = layout[i];
            layout[i] = layout[j];
            layout[j] = c;
        }
    }

    
    /*
     * Operators for Hill Climbing
     */

    // Operator to swap the symbols of positions i and j
    public void swap(int i, int j) {
        char c = layout[i];
        layout[i] = layout[j];
        layout[j] = c;
    }


    /*
     * Heuristic Functions
     */

    // Returns the quality of the current layout (the smaller the value, the better)
    public double getCost() {
        double cost = 0;
        for (int i = 0; i < layout.length; ++i) {
            int row_i = i / cols;
            int col_i = i % cols;
            for (int j = i + 1; j < layout.length; ++j) {
                int row_j = j / cols;
                int col_j = j % cols;

                //Add the frequency of the char pair multiplied by their distance on the layout
                cost += pairFreq.get(layout[i], layout[j]) * distance(row_i, col_i, row_j, col_j);;
            }
        }
        return cost;
    }
    

    /* 
     * Getters
     */

    public char[] getLayout() {
        return layout;
    }

    public int getRows() {
        return layout.length/cols;
    }

    public int getCols() {
        return cols;
    }

    public int getNumKeys() {
        return layout.length;
    }
}

