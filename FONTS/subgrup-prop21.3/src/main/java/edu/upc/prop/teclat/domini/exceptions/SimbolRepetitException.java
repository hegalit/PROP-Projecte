package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan hi ha un o més símbols repetits a l'alfabet.*/
public class SimbolRepetitException extends Exception {
    /**Constructora que crea l'excepció mencionada.*/
    public SimbolRepetitException() {
        super("Hi ha un o més símbols repetits a l'alfabet");
    }
}
