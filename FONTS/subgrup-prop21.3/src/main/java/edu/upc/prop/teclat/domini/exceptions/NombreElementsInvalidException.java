package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan la llista té 0 paraules o una paraula no te una freqüència assignada.*/
public class NombreElementsInvalidException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public NombreElementsInvalidException() {
        super("La llista de paraules no és vàlida\nAquesta ha de contenir com a mínim una paraula, i tota paraula va seguida de la seva freqüència\n");
    }
}
