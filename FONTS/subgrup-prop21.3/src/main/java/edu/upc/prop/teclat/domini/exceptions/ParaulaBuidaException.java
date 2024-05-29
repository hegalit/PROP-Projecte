package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan hi ha una o més paraules buides a la llista de freqüència.*/
public class ParaulaBuidaException extends Exception {
    /**Constructora que crea l'excepció mencionada.*/
    public ParaulaBuidaException() {
        super("Hi ha una o més paraules buides a la llista de freqüència");
    }
}
