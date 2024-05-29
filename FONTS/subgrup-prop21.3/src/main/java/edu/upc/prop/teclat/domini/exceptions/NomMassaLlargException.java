package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan el nom de l'objecte supera el màxim permès. */
public class NomMassaLlargException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public NomMassaLlargException() {
        super("El nom donat a l'objecte supera el límit de caràcters permès");
    }
}
