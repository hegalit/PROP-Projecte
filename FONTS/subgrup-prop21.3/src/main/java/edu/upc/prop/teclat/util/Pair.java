package edu.upc.prop.teclat.util;

/**
 * Representa un parell genèric immutable
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 *
 * @param <T> Primer element del pair
 * @param <Q> Primer element del pair
 */
public final class Pair<T,Q> {
    /**Primer element*/
    private final T first;
    /**Segon element*/
    private final Q second;

    //Constructora
    /** Construeix un Pair amb els elements fst i snd proporcionats.
     * @param fst Primer element del Pair
     * @param snd Segon element del Pair
     */
    public Pair(T fst, Q snd) {
        this.first = fst;
        this.second = snd;
    }

    //Getters

    /**
     * Retorna el primer element del Pair
     * @return El primer element del Pair
     */
    public T getFirst() {
        return first;
    }

    /**
     * Retorna el segon element del Pair
     * @return El segon element del Pair
     */
    public Q getSecond() {
        return second;
    }
}
