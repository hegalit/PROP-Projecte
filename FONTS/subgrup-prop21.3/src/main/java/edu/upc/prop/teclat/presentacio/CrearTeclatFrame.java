package edu.upc.prop.teclat.presentacio;

import java.util.ArrayList;

import edu.upc.prop.teclat.domini.exceptions.NomNoExisteixException;
import edu.upc.prop.teclat.domini.exceptions.teclat.TeclatTemporalBuitException;

/**
 * Aquesta classe s’encarrega de crear els teclats que es podran utilitzar.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public class CrearTeclatFrame extends GestioTeclatsFrame {
    //Constants

    /**String que anirà al botó de crear*/
    private static final String doneButtonString = "Crear";

    /**Títol que tindrà la pestanya*/
    private static final String title = "Crear un nou teclat";



    /** 
     * Crea el frame donat una referència de la vista principal del programa, i dues llistes
     * que contenen els noms dels alfabets i algorismes generadors existents al Sistema.
     *
     * @param vistaPrincipal Referència de la vista principal.
     * @param alfabets Llista dels noms dels alfabets existents al Sistema.
     * @param algoritmes Llista dels noms dels algorismes generadors existents al Sistema.
     */
    public CrearTeclatFrame(VistaPrincipalFrame vistaPrincipal, ArrayList<String> alfabets, ArrayList<String> algoritmes) {
        super(vistaPrincipal, doneButtonString);
        try {
            vistaPrincipal.crearTeclatTemporal(alfabets.get(0));
        } catch (NomNoExisteixException e) {
            // No hauria de passar
            e.printStackTrace();
        }
        try {
            super.init(alfabets, algoritmes);
        } catch (TeclatTemporalBuitException e) {
            // No hauria de passar
            e.printStackTrace();
        }
        setTitle(title);
        setVisible(true);
    }
}
