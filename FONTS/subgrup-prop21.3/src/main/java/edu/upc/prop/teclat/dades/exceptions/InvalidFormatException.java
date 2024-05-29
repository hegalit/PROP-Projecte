package edu.upc.prop.teclat.dades.exceptions;

/**Excepció que es llença quan un fitxer no te el format correcte.*/
public class InvalidFormatException extends Exception{

    /** Constructora que crea l'excepció mencionada.*/
    public InvalidFormatException() {
        super("El fitxer no té el format correcte");
    }
}
