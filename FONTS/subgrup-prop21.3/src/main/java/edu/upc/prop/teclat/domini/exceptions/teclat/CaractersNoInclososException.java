package edu.upc.prop.teclat.domini.exceptions.teclat;

/**Excepció que es llença quan l'alfabet del teclat no conté tots els
 * símbols que té el text o llista seleccionat per a regenerar el teclat.*/
public class CaractersNoInclososException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public CaractersNoInclososException() {
        super("L'alfabet del teclat no conté tots els símbols que té el text o llista seleccionat per a regenerar el teclat");
    }
}
