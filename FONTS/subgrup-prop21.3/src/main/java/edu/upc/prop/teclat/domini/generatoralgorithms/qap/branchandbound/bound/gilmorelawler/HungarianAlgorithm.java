package edu.upc.prop.teclat.domini.generatoralgorithms.qap.branchandbound.bound.gilmorelawler;

import java.util.Stack;

/**
 * Implementa el Hungarian Algorithm per a resoldre el AP lineal.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
class HungarianAlgorithm {
    double[][] costMatrix;
    boolean[] coveredRows;
    boolean[] coveredColumns;
    
    HungarianAlgorithm(double[][] costMatrix) {
        this.costMatrix = costMatrix;
    }
    
    int solve() {
        // Reduïm les files
        for(int i=0; i<costMatrix.length; ++i) {
            double min = Double.MAX_VALUE;
            for(int j=0; j<costMatrix[i].length; ++j) {
                if(costMatrix[i][j] < min) {
                    min = costMatrix[i][j];
                }
            }
            for(int j=0; j<costMatrix[i].length; ++j) {
                costMatrix[i][j] -= min;
            }
        }
        
        // Reduïm les columnes
        for(int i=0; i<costMatrix.length; ++i) {
            double min = Integer.MAX_VALUE;
            for(int j=0; j<costMatrix[i].length; ++j) {
                if(costMatrix[j][i] < min) {
                    min = costMatrix[j][i];
                }
            }
            for(int j=0; j<costMatrix[i].length; ++j) {
                costMatrix[j][i] -= min;
            }
        }
        
        // Repetim fins acabar
        while(true) {
            // Calculem el nombre de línies mínimes per cobrir tots els 0
            cover();
            int lines = 0;
            for(int i=0; i<costMatrix.length; ++i) {
                if(coveredRows[i]) lines++;
                if(coveredColumns[i]) lines++;
            }
            // Si és N, hem acabat
            if(lines == costMatrix.length) {
                int[] assignation = greedyAssignation();
                int cost = 0;
                for(int i=0; i<costMatrix.length; i++) {
                    cost += costMatrix[i][assignation[i]];
                }
                return cost;
            } else {
                // Trobem el mínim nombre no cobert
                double min = Double.MAX_VALUE;
                for(int i=0; i<costMatrix.length; ++i) {
                    for(int j=0; j<costMatrix[i].length; ++j) {
                        if(!coveredRows[i] && !coveredColumns[j] && costMatrix[i][j] < min) {
                            min = costMatrix[i][j];
                        }
                    }
                }
                // El sumem a les files no cobertes
                for(int i=0; i<costMatrix.length; ++i) {
                    if(!coveredRows[i]) {
                        for(int j=0; j<costMatrix[i].length; ++j) {
                            costMatrix[i][j] += min;
                        }
                    }
                }
                // I a les columnes no cobertes
                for(int i=0; i<costMatrix.length; ++i) {
                    if(coveredColumns[i]) {
                        for(int j=0; j<costMatrix[i].length; ++j) {
                            costMatrix[j][i] += min;
                        }
                    }
                }
                // Trobem el mínim de la matriu
                min = Integer.MAX_VALUE;
                for(int i=0; i<costMatrix.length; ++i) {
                    for(int j=0; j<costMatrix[i].length; ++j) {
                        if(costMatrix[i][j] < min) {
                            min = costMatrix[i][j];
                        }
                    }
                }
                // I el restem a tots els elements
                for(int i=0; i<costMatrix.length; ++i) {
                    for(int j=0; j<costMatrix[i].length; ++j) {
                        costMatrix[i][j] -= min;
                    }
                }
            }
        }
    }
    
    // Cobreix tots els 0 de la matriu amb el mínim nombre de files i columnes
    private void cover() {
        int[] assignation = greedyAssignation();
        boolean[] markedRows = new boolean[costMatrix.length];
        boolean[] markedColumns = new boolean[costMatrix.length];
        
        // Marquem les files sense assignació
        for(int i=0; i<costMatrix.length; ++i) {
            if(assignation[i] == -1) {
                markedRows[i] = true;
            }
        }
        
        boolean changesMade;
        do {
            changesMade = false;
            // Marquem les columnes amb un 0 en una fila marcada
            for(int i=0; i<costMatrix.length; ++i) {
                if(markedRows[i]) {
                    for(int j=0; j<costMatrix[i].length; ++j) {
                        if(costMatrix[i][j] == 0 && !markedColumns[j]) {
                            markedColumns[j] = true;
                            changesMade = true;
                        }
                    }
                }
            }
            
            // Marquem les files amb un 0 en una columna marcada
            for(int i=0; i<costMatrix.length; ++i) {
                if(markedColumns[i]) {
                    for(int j=0; j<costMatrix[i].length; ++j) {
                        if(costMatrix[j][i] == 0 && !markedRows[j]) {
                            markedRows[j] = true;
                            changesMade = true;
                        }
                    }
                }
            }
        } while(changesMade);
        
        // Finalment les línies mínimes són les que resulten de recobrir les columnes marcades i les files no marcades
        coveredRows = new boolean[costMatrix.length];
        coveredColumns = new boolean[costMatrix.length];
        for(int i=0; i<costMatrix.length; ++i) {
            if(!markedRows[i]) {
                coveredRows[i] = true;
            }
            if(markedColumns[i]) {
                coveredColumns[i] = true;
            }
        }
    }
    
    // Retorna una assignació el més completa possible (per cada fila, quina columna o -1)
    private int[] greedyAssignation() {
        Stack<int[]> stack = new Stack<int[]>();
        int[] bestAssignation = null;
        int bestBenefit = -1;
        stack.push(new int[0]);
        while(!stack.isEmpty()) {
            int[] assignation = stack.pop();
            if(assignation.length == costMatrix.length) {
                int benefit = -1;
                for(int i=0; i<costMatrix.length; ++i) {
                    if(assignation[i] != -1) benefit += 1;
                }
                if(benefit > bestBenefit) {
                    bestBenefit = benefit;
                    bestAssignation = assignation;
                }
            } else {
                for(int i=-1; i<costMatrix.length; ++i) {
                    boolean alreadyAssigned = false;
                    for(int j=0; j<assignation.length; ++j) {
                        if(assignation[j] == i) {
                            alreadyAssigned = true;
                            break;
                        }
                    }
                    if(!alreadyAssigned) {
                        int[] newAssignation = new int[assignation.length+1];
                        System.arraycopy(assignation, 0, newAssignation, 0, assignation.length);
                        newAssignation[assignation.length] = i;
                        stack.push(newAssignation);
                    }
                }
            }
        }
        return bestAssignation;
    }
}
