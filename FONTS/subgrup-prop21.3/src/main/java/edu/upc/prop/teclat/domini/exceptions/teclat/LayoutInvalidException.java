package edu.upc.prop.teclat.domini.exceptions.teclat;

/**Excepció que es llença quan el layout i l'alfabet del teclat no tenen els mateixos símbols*/
public class LayoutInvalidException extends Exception {

    /** Constructora que crea l'excepció mencionada.*/
    public LayoutInvalidException() {
        super("El layout i l'alfabet del teclat no tenen els mateixos símbols");
    }
}
