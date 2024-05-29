package edu.upc.prop.teclat.domini.generatoralgorithms;

import edu.upc.prop.teclat.domini.PairsFrequency;

/**
 * Representa un algoritme de generació de disposició de lletres en teclats.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public interface GeneratorAlgorithm {

    // Retorna una disposició de lletres generada a partir dels simbols, nombre de columnes i pares de freqüència proporcionats.
    char[] solve(String simbols, int cols, PairsFrequency pairsFreq);
}
