package edu.upc.prop.teclat.presentacio;
import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.exceptions.*;

import java.util.ArrayList;

/**
 * Classe que representa la interfície d'usuari mostrada en la creació d'alfabets.
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public class CrearAlfabetFrame extends GestioAlfabetsFrame{
    //Constants

    /**String que anirà al botó de crear*/
    private static final String doneButtonString = "Crear";



    /** Constructora de la classe
     *  Rep com a paràmetre una referència de la vista principal del programa, junt dues llistes
     *  que contenen cadascuna els noms dels textos i les llistes de freqüències existents al Sistema
     *  respectivament.
     *
     * @param vistaPrincipal Objecte que apunta a la vista principal.
     * @param llistesFreq {@link ArrayList} que conté els noms de les llistes de freqüències existents
     *                    al Sistema.
     * @param textos {@link ArrayList} que conté els noms dels textos existents al Sistema.
     */
    CrearAlfabetFrame(VistaPrincipalFrame vistaPrincipal, ArrayList<String> llistesFreq, ArrayList<String> textos) {
        super(vistaPrincipal, doneButtonString, llistesFreq, textos);
        super.init();
        setTitle("Crear un nou alfabet");
        setVisible(true);
    }

    /** Modifica l'alfabet indicat en la creadora amb els paràmetres d'entrada.
     *
     * @param name Nom de l'alfabet a crear.
     * @param content Seqüència de símbols que indica els que tindrà l'alfabet a editar un cop modificat.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema un alfabet identificat per {@code nom}.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a la seqüència de símbols donada.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a la seqüència de símbols donada.
     * @throws NomProhibitException El nom donat és {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET},
     *                              que està prohibit.
     */
    @Override
    public void creaOModificaAlfabet(String name, String content) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, SimbolRepetitException, NumSimbolsInvalidException, SimbolInvalidException, NomProhibitException {
        vistaPrincipal.crearAlfabet(name, content);
    }
}
