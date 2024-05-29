package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan no es pot deixar un camp de text buit.*/
public class TextEstaBuitException extends Exception {
    /**Constructora que crea l'excepció mencionada.*/
    public TextEstaBuitException() {
        super ("No es pot deixar un camp de text buit");
    }
}
