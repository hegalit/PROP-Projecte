package edu.upc.prop.teclat.presentacio;

import java.util.ArrayList;

import edu.upc.prop.teclat.domini.exceptions.NomNoExisteixException;
import edu.upc.prop.teclat.domini.exceptions.teclat.TeclatTemporalBuitException;

/**
 * Aquesta classe gestiona la vista d'edició de teclats.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public class EditarTeclatFrame extends GestioTeclatsFrame {
    //Constants

    /**String que anirà al botó de guardar*/
    private static final String doneButtonString = "Guardar";

    /**Títol de la finestra d'editar teclats*/
    private static final String title = "Editant el teclat ";


    
    /** 
     * Crea el frame d'edició de teclats donat una referència de la vista principal del programa, 
     * el nom del teclat a editar i dues llistes que contenen els noms dels alfabets i algorismes 
     * generadors existents al Sistema.
     *
     * @param vistaPrincipal Referència de la vista principal.
     * @param nomOriginal Nom del teclat a editar.
     * @param alfabets Llista dels noms dels alfabets existents al Sistema.
     * @param algoritmes Llista dels noms dels algorismes generadors existents al Sistema.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap teclat identificat per aquest nom.
     */
    public EditarTeclatFrame(VistaPrincipalFrame vistaPrincipal, String nomOriginal, ArrayList<String> alfabets, ArrayList<String> algoritmes) throws NomNoExisteixException {
        super(vistaPrincipal, doneButtonString);
        vistaPrincipal.carregarTeclatTemporal(nomOriginal);
        try {
            super.init(alfabets, algoritmes, nomOriginal);
        } catch (TeclatTemporalBuitException e) {
            // No hauria de passar
            e.printStackTrace();
        }
        setTitle(title);
        setVisible(true);
    }
}
