package edu.upc.prop.teclat.domini.generatoralgorithms.qap.branchandbound;

import edu.upc.prop.teclat.domini.PairsFrequency;
import edu.upc.prop.teclat.domini.generatoralgorithms.GeneratorAlgorithm;
import edu.upc.prop.teclat.domini.generatoralgorithms.qap.GreedyGenerator;
import edu.upc.prop.teclat.domini.generatoralgorithms.qap.QAProblem;
import edu.upc.prop.teclat.domini.generatoralgorithms.qap.branchandbound.bound.Bound;
import edu.upc.prop.teclat.domini.generatoralgorithms.qap.branchandbound.bound.gilmorelawler.BoundGilmoreLawler;

/**
 * Solver de QAPProblem que utilitza un Branch And Bound
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public class BranchAndBoundGenerator implements GeneratorAlgorithm {
    private QAProblem problem;
    private Bound bound;
    private String bestSol;
    private double costBestSol = Double.MAX_VALUE;
    
    public BranchAndBoundGenerator() {
    }

    // Resol el problema QAP amb una bona cota inicial
    public char[] solve(String simbols, int cols, PairsFrequency pairsFreq) {
        this.problem = new QAProblem(cols, simbols, pairsFreq);
        this.bound = new BoundGilmoreLawler(problem);
        
        // Obtenim un bon bound fent un greedy
        GreedyGenerator greedy = new GreedyGenerator();
        bestSol = new String (greedy.solve(simbols, cols, pairsFreq));
        costBestSol = problem.costSolucio(bestSol);

        // Fem el branch and bound
        branch_and_bound("", problem.getSimbols());

        return bestSol.toCharArray();
    }

    // Aplica l'algoritme branch and bound fins trobar el mínim global del cost
    private void branch_and_bound(String partialSol, String availableSymbols) {
        if(partialSol.length() == problem.getSimbols().length()) {
            // Si la solució parcial és completa, comprovem si és millor que la millor solució trobada fins ara
            double cost = problem.costSolucio(partialSol);
            if(cost < costBestSol) {
                bestSol = partialSol;
                costBestSol = cost;
            }
        } else {
            // Si la solució parcial no és completa, generem les possibles extensions
            for(Character symbol : availableSymbols.toCharArray()) {
                String candidateSol = partialSol + symbol;
                String candidateSym = availableSymbols.replace(symbol.toString(), "");
                if(bound.getBound(candidateSol, availableSymbols) < costBestSol) {
                    branch_and_bound(candidateSol, candidateSym);
                }
            }
        }
    }
}
