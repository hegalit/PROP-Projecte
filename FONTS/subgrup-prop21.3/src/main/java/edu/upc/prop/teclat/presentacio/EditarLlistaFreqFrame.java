package edu.upc.prop.teclat.presentacio;

import java.util.ArrayList;

import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.LlistaDeFrequencia;
import edu.upc.prop.teclat.domini.exceptions.*;
import edu.upc.prop.teclat.util.Pair;

/**
 * Aquesta classe gestiona la vista d'edició de llistes de freqüències.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public class EditarLlistaFreqFrame extends GestioLlistesFreqFrame {
    //Constants

    /**String que anirà al botó de guardar*/
    private static final String doneButtonString = "Guardar";

    /**String que conté el nom actual de la llista*/
    private final String nomOriginal;



    /** 
     * Crea el frame de l'edició de llistes de freqüències indicant el nom i contingut de la llista a editar,
     * una referència a la vista principal i els noms dels textos existents al Sistema.
     *
     * @param vistaPrincipal Objecte que apunta a la vista principal.
     * @param nomOriginal Nom que havia abans d'editar la llista.
     * @param contingutOriginal Contingut que havia abans d'editar la llista.
     * @param textos {@link ArrayList} que conté els textos per generar les llistes.
     */
    EditarLlistaFreqFrame(VistaPrincipalFrame vistaPrincipal, String nomOriginal, ArrayList<Pair<String, Integer>> contingutOriginal, ArrayList<String> textos) {
        super(vistaPrincipal, doneButtonString);
        this.nomOriginal = nomOriginal;
        super.init(nomOriginal, contingutOriginal, textos);
        setTitle("Editant la llista de freqüències " + nomOriginal);
        setVisible(true);
    }


    /** Edita la llista de freqüència indicada al crear el frame.
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
    void crearOEditarLlistaFreq(String nom, ArrayList<Pair<String, Integer>> contingut) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, NombreElementsInvalidException, InvalidFrequencyException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException {
        try {
            vistaPrincipal.editarLlistaFreq(nomOriginal, nom, contingut);
        } catch (NomNoExisteixException e) {
            // No hauria de passar
            e.printStackTrace();
        }
    }
}
