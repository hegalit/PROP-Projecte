package edu.upc.prop.teclat.domini.exceptions.teclat;

/**Excepció que es llença quan no hi ha dades al teclat temporal.*/
public class TeclatTemporalBuitException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public TeclatTemporalBuitException() {
        super("No hi ha dades al teclat temporal");
    }
}
