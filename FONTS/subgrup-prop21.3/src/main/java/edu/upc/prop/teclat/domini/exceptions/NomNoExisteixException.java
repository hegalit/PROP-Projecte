package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan el nom de l'objecte no existeix al sistema. */
public class NomNoExisteixException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public NomNoExisteixException() {
        super("No s'ha trobat cap element amb aquest nom");
    }
}
