package edu.upc.prop.teclat.presentacio;

import java.util.ArrayList;

import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.LlistaDeFrequencia;
import edu.upc.prop.teclat.domini.exceptions.*;
import edu.upc.prop.teclat.util.Pair;

/**
 * Aquesta classe gestiona la vista de creació de llistes de freqüències.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */

public class CrearLlistaFreqFrame extends GestioLlistesFreqFrame {
    //Constants

    /**String que anirà al botó de crear*/
    private static final String doneButtonString = "Crear";

    /**String que serà el titol de la finestra de creació de llistes.*/
    private static final String title = "Crear una nova llista de freqüència";


    //Constructora    
    /** 
     *  Crea el frame donades una referència de la vista principal del programa i una llista
     *  que conté els noms dels textos existents al Sistema
     *
     * @param vistaPrincipal Referència de la vista principal.
     * @param textos {@link ArrayList} que conté els noms dels textos existents al Sistema.
     */
    public CrearLlistaFreqFrame(VistaPrincipalFrame vistaPrincipal, ArrayList<String> textos) {
        super(vistaPrincipal, doneButtonString);
        super.init(textos);
        setTitle(title);
        setVisible(true);
    }


    /** Crear una llista de freqüència nova a partir dels paràmetres d'entrada.
     *
     * @param nom Nom de la llista de freqüència.
     * @param contingut Seqüència de parells (paraula, freqüència) amb què obtenir les associacions
     *                  paraula-freqüència per a generar el contingut de la llista de freqüència.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema una llista de freqüències amb el nom donat.
     * @throws NomMassaLlargException El nom donat té més de {@value LlistaDeFrequencia#MAX_NAME_LENGTH} 
     *                                caràcters.
     * @throws NumSimbolsInvalidException La seqüència donada té més de {@value Alfabet#MAX_NUM_SYMBOLS} 
     *                                    símbols diferents.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a la seqüència donada.
     * @throws NombreElementsInvalidException La seqüència donada està buida.
     * @throws InvalidFrequencyException Una o més paraules de la seqüència tenen freqüència &lt;= 0.
     * @throws ParaulaRepetidaException Hi ha una o més paraules repetides a la llista.
     * @throws ParaulaBuidaException Hi ha una o més paraules buides a la llista de freqüència.
     */
    @Override
    void crearOEditarLlistaFreq(String nom, ArrayList<Pair<String, Integer>> contingut) throws NomJaExisteixException, InvalidFrequencyException, NomBuitException, NomMassaLlargException, NombreElementsInvalidException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException {
        vistaPrincipal.crearLlistaFreq(nom, contingut);
    }
}
