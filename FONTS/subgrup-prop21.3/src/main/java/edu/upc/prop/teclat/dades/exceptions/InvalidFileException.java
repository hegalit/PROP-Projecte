package edu.upc.prop.teclat.dades.exceptions;

/**Excepció que es llença quan un fitxer no te l'extensió correcta.*/
public class InvalidFileException extends Exception{

    /** Constructora que crea l'excepció mencionada.*/
    public InvalidFileException() {
        super("El fitxer no té l'extensió correcta");
    }
}
