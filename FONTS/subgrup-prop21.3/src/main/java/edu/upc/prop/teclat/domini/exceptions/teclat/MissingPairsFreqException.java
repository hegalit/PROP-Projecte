package edu.upc.prop.teclat.domini.exceptions.teclat;

/**Excepció que es llença quan s'ha rebut una referència de PairsFrequency nul·la.*/
public class MissingPairsFreqException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public MissingPairsFreqException() {
        super("S'ha rebut una referència de PairsFrequency nul·la");
    }
}
