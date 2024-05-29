package edu.upc.prop.teclat.domini.generatoralgorithms.qap;

import edu.upc.prop.teclat.domini.PairsFrequency;

/**
 * Representa un problema de Quadratic Assignment.
 * Aquest tipus de problema pot ser resolt per diversos solvers, com el
 * GreedyGenerator i el BranchAndBoundGenerator.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public class QAProblem {
    private final PairsFrequency freq;
    private final int columnes;
    private final String simbols;

    public QAProblem(int columnes, String simbols, PairsFrequency freq) {
        this.columnes = columnes;
        this.simbols = simbols;
        this.freq = freq;
    }

    public String getSimbols() {
        return simbols;
    }
    public PairsFrequency getFreq() {
        return freq;
    }

    // Calcula el cost d'una solució, sigui parcial o no
    public double costSolucio(String solucio) {
        double cost = 0;
        for(int i=0; i < solucio.length(); i++) {
            for(int j=i+1; j<solucio.length(); j++) {
                Integer f = freq.get(solucio.charAt(i), solucio.charAt(j));
                cost += dist(i,j) * f;
            }
        }
        return cost;
    }

    // El cost de la solució es calcula sumant
    // freqüència lletres (i,j) * distància entre tecles (i,j) per tots els parells (i,j)
    public double costSolucio(Character[] solucio) {
        double cost = 0;
        for(int i=0; i < solucio.length; i++) {
            if(solucio[i] == null) continue;
            for(int j=i+1; j<solucio.length; j++) {
                if(solucio[j] == null) continue;
                Integer f = freq.get(solucio[i], solucio[j]);
                cost += dist(i,j) * f;
            }
        }
        return cost;
    }
    
    // Caldula la distància euclidianta entre dues posicions del teclat i j
    public double dist(int i, int j) {
        int fila_i = i / columnes;
        int columna_i = i % columnes;
        int fila_j = j / columnes;
        int columna_j = j % columnes;

        // Distància euclidiana = sqrt((x1-x2)^2 + (y1-y2)^2)
        return Math.sqrt(Math.pow(fila_i - fila_j, 2) + Math.pow(columna_i - columna_j, 2));
    }
}
