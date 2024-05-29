package edu.upc.prop.teclat.domini.generatoralgorithms.hillclimbing.problems;

import java.util.ArrayList;

import edu.upc.prop.teclat.domini.generatoralgorithms.hillclimbing.SuccessorFunction;

public class QAPSuccessorFunction implements SuccessorFunction {

    public QAPSuccessorFunction() {

    }
    
    // Returns a list of all possible successors from Object p
    public ArrayList<Object> getSuccessors(Object obj) {
        ArrayList<Object> retval = new ArrayList<Object>();
        QAP_HC state = (QAP_HC)obj;

        for (int i = 0; i < state.getNumKeys(); ++i) {
            for (int j = i+1; j < state.getNumKeys(); ++j) {
                QAP_HC auxState = state.copy();
                auxState.swap(i, j);
                retval.add(auxState);
            }
        }
        return retval;
    }
}