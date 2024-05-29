package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan l'alfabet no té símbols.*/
public class NumSimbolsInvalidException extends Exception {
    /**Constructora que crea l'excepció mencionada.*/
    public NumSimbolsInvalidException() {
        super("L'alfabet no té símbols");
    }
} 
