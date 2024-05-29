package edu.upc.prop.teclat.domini.generatoralgorithms.qap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.upc.prop.teclat.domini.PairsFrequency;
import edu.upc.prop.teclat.domini.generatoralgorithms.GeneratorAlgorithm;

/**
 * Solver greedy per a un problema QAP
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public class GreedyGenerator implements GeneratorAlgorithm{

    // Resol el problema QAP amb un algorisme greedy
    @Override
    public char[] solve(String simbols, int cols, PairsFrequency pairsFreq) {
        QAProblem problem = new QAProblem(cols, simbols, pairsFreq);
        String solucio = "";

        // Posem tots els símbols en un set
        Set<Character> simbolsSet = new HashSet<Character>();
        for (int i=0; i<simbols.length(); ++i) {
            simbolsSet.add(simbols.charAt(i));
        }

        // Anem afegint el símbol que minimitza el cost
        while (!simbolsSet.isEmpty()) {
            Iterator<Character> it = simbolsSet.iterator();
            char proposta = it.next();
            double cost = problem.costSolucio(solucio + proposta);
            while (it.hasNext()) {
                char c = it.next();
                double cost2 = problem.costSolucio(solucio + c);
                if (cost2 < cost) {
                    proposta = c;
                    cost = cost2;
                }
            }
            solucio += proposta;
            simbolsSet.remove(proposta);
        }
        return solucio.toCharArray();
    }
}
