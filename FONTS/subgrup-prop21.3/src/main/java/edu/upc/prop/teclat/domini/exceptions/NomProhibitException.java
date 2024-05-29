package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan el nom donat a l'objecte està prohibit.*/
public class NomProhibitException extends Exception {
    /**Constructora que crea l'excepció mencionada.*/
    public NomProhibitException() {
        super("El nom donat a l'objecte està prohibit");
    }
}
