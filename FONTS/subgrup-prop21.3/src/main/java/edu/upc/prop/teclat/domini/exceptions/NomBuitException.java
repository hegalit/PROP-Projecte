package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan L'objecte no te nom */
public class NomBuitException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public NomBuitException() {
        super("L'objecte ha de tenir un nom");
    }
}