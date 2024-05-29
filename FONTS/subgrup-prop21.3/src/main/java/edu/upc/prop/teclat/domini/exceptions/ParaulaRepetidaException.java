package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan hi ha una paraula repetida a la llista de paraules.*/
public class ParaulaRepetidaException extends Exception {
    /**Constructora que crea l'excepció mencionada.*/
    public ParaulaRepetidaException () {
        super("Hi ha una paraula repetida a la llista de paraules");
    }
}
