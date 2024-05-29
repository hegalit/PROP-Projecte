package edu.upc.prop.teclat.domini.exceptions.teclat;

/**Excepció que es llença quan no hi ha un millor layout per al teclat temporal*/
public class MissingBestLayoutException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public MissingBestLayoutException() {
        super("No hi ha un millor layout per al teclat temporal");
    }
}
