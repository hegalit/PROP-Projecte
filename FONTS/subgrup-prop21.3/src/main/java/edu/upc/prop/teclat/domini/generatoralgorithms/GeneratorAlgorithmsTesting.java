package edu.upc.prop.teclat.domini.generatoralgorithms;

import edu.upc.prop.teclat.domini.PairsFrequency;
import edu.upc.prop.teclat.domini.generatoralgorithms.qap.GreedyGenerator;
import edu.upc.prop.teclat.domini.generatoralgorithms.qap.QAProblem;
import edu.upc.prop.teclat.domini.generatoralgorithms.qap.branchandbound.BranchAndBoundGenerator;

/**
 * Classe interna per al testeig dels algoritmes generadors de layouts de teclat
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
class GeneratorAlgorithmsTesting {
    /** Punt d'entrada dels testos d'algoritmes de generació de teclats*/
    public static void main(String[] args) {
        final int columnes = 3;
        final String simbols = "abcdefghi";
        PairsFrequency freq = new PairsFrequency();
        freq.put('a', 'b', 2);
        freq.put('a', 'c', 3);
        freq.put('a', 'd', 7);
        freq.put('a', 'e', 10);
        freq.put('a', 'f', 4);
        freq.put('a', 'g', 1);
        freq.put('a', 'h', 3);
        freq.put('a', 'i', 7);
        freq.put('c', 'e', 9);
        freq.put('c', 'e', 5);
        //freq.put('d', 'd', 4);
        //freq.put('e', 'i', 5);
        //freq.put('f', 'd', 1);
        //freq.put('g', 'b', 3);
        //freq.put('h', 'c', 2);
        //freq.put('i', 'e', 3);
        //freq.put('j', 'e', 4);

        final QAProblem problem = new QAProblem(columnes, simbols, freq);

        System.out.println("Resultat Greedy: ");
        final GreedyGenerator greedy = new GreedyGenerator();
        String solucio = new String(greedy.solve(simbols, columnes, freq));
        System.out.println(solucio);
        System.out.println(problem.costSolucio(solucio));

        System.out.println("Resultat BranchAndBound: ");
        final BranchAndBoundGenerator branchAndBound = new BranchAndBoundGenerator();
        solucio = new String(branchAndBound.solve(simbols, columnes, freq));
        System.out.println(solucio);
        System.out.println(problem.costSolucio(solucio));
    }
}