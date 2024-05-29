package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan la freqüència és menor o igual a 0. */
public class InvalidFrequencyException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public InvalidFrequencyException() {
        super("La freqüència ha de ser un enter > 0");
    }
}
