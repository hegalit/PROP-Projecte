package edu.upc.prop.teclat.domini.generatoralgorithms.qap.branchandbound.bound.gilmorelawler;

import java.util.Arrays;
import java.util.Collections;

import edu.upc.prop.teclat.domini.generatoralgorithms.qap.QAProblem;
import edu.upc.prop.teclat.domini.generatoralgorithms.qap.branchandbound.bound.Bound;

/**
 * Representa el Bound de Gilmore Lawler per als problemes QAP
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public class BoundGilmoreLawler implements Bound {
    private QAProblem problem;
    String partialSol;
    String availableSymbols;
    int N;
    int m;

    public BoundGilmoreLawler(QAProblem problem) {
        this.problem = problem;
    }

    // Calcula el bound per a una solució parcial donada i els símbols disponibles restants
    public double getBound(String partialSol, String availableSymbols) {
        this.partialSol = partialSol;
        this.availableSymbols = availableSymbols;

        //Inicialitzem variables
        N = problem.getSimbols().length();
        m = partialSol.length();
        double[][] C1 = new double[N-m][N-m];
        double[][] C2 = new double[N-m][N-m];

        //C1
        Character[] partialSolArr = new Character[N];
        for(int i = 0; i < m; i++) {
            partialSolArr[i] = partialSol.charAt(i);
        }
        for(int i = 0; i < N-m; i++) {
            for(int k = 0; k < N-m; k++) {
                partialSolArr[m+k] = availableSymbols.charAt(i);
                C1[i][k] = problem.costSolucio(partialSolArr);
                partialSolArr[m+k] = null;
            }
        }

        //C2
        for(int i = 0; i < N-m; i++) {
            for(int k = 0; k < N-m; k++) {
                Integer[] T = t(i);
                Double[] D = d(k);
                Arrays.sort(T);
                Arrays.sort(D, Collections.reverseOrder());
                C2[i][k] = dotProduct(T, D);
            }
        }

        //Calculem la matriu de costos
        double[][] C = new double[N-m][N-m];
        for(int i = 0; i < N-m; i++) {
            for(int k = 0; k < N-m; k++) {
                C[i][k] = C1[i][k] + C2[i][k];
            }
        }

        //Finalment obtenim la cota amb el Hungarian Algorithm
        HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(C);
        double T0 = problem.costSolucio(partialSol);
        double T1 = hungarianAlgorithm.solve();
        return T0 + T1;
    }

    //Vector de trànsit del ièssim símbol a la resta de símbols no col·locats
    private Integer[] t(int i) {
        Integer[] res  = new Integer[problem.getSimbols().length() - partialSol.length()];
        for(int j = 0; j<N-m; j++) {
            res[j] = problem.getFreq().get(availableSymbols.charAt(i), availableSymbols.charAt(j));
        }
        return res;
    }

    //Vector de distàncies de la kèssima posició a la resta de no ocupades
    private Double[] d(int k) {
        Double[] res = new Double[N-m];
        for(int j = 0; j<N-m; j++) {
            res[j] = problem.dist(k+m, j+m);
        }
        return res;
    }

    //Producte escalar de dos vectors a i b
    private double dotProduct(Integer[] a, Double[] b) {
        double res = 0;
        for(int i = 0; i < a.length; i++) {
            res += a[i] * b[i];
        }
        return res;
    }
}