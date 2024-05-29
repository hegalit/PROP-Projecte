package edu.upc.prop.teclat.domini.generatoralgorithms.hillclimbing;

import java.util.ArrayList;

public class HillClimbing {    
    private Object solution;
    private SuccessorFunction sf;
    private HeuristicFunction hf;

    public HillClimbing(Object problem, SuccessorFunction sf, HeuristicFunction hf) {
        this.solution = problem;
        this.sf = sf;
        this.hf = hf;
    }

    public Object solve() {
        boolean solved = false;
        double minCost = hf.getHeuristicValue(solution);

        while (!solved) {
            ArrayList<Object> successors = sf.getSuccessors(solution);
            int posBest = -1;

            for (int i = 0; i < successors.size(); ++i) {
                double cost = hf.getHeuristicValue(successors.get(i));
                if (cost < minCost) {
                    minCost = cost;
                    posBest = i;
                }
            }
            if (posBest < 0) solved = true;
            else solution = successors.get(posBest);
        }
        return solution;
    }
}
