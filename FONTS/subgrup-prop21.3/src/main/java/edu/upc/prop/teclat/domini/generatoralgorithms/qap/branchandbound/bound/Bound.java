package edu.upc.prop.teclat.domini.generatoralgorithms.qap.branchandbound.bound;

/**
 * Representa un Bound per a un algoritme tipus Branch And Bound
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public interface Bound {
    /**Agafa el Bound
     * @param partialSol Solució parcial
     * @param availableSymbols Símbols disponibles
     *
     * @return Retorna el Bound.
     */
    double getBound(String partialSol, String availableSymbols);
}