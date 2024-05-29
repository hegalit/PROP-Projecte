package edu.upc.prop.teclat.domini.exceptions.teclat;

/**Excepció que es llença quan s'ha intentat accedir a un index invàlid.*/
public class IndexosInvalidsException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public IndexosInvalidsException(int k1, int k2, int layoutLength) {
        super("S'ha intentat accedir a un index invàlid.\nk1: " + k1 + ", k2: " + k2 + ", mida del teclat: " + layoutLength + ".");
    }
}
