package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan s'ha detectat un símbol invàlid.*/
public class SimbolInvalidException extends Exception {
    /**Constructora que crea l'excepció mencionada.*/
    public SimbolInvalidException() {
        super("S'ha detectat un símbol invàlid");
    }
}