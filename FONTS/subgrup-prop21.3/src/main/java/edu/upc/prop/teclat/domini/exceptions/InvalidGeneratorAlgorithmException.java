package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan l'algoritme que es vol fer servir no existeix. */
public class InvalidGeneratorAlgorithmException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public InvalidGeneratorAlgorithmException() {
        super("L'algorisme generador al que s'intenta cridar no existeix");
    }
}
