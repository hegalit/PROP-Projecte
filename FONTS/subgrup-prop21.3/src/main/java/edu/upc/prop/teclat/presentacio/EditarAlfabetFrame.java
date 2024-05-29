package edu.upc.prop.teclat.presentacio;

import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomJaExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NomNoExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomProhibitException;
import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.SimbolInvalidException;
import edu.upc.prop.teclat.domini.exceptions.SimbolRepetitException;

import java.util.ArrayList;

/**
 * Aquesta classe s’encarrega de editar els alfabets dels teclats.
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public class EditarAlfabetFrame extends GestioAlfabetsFrame{
    //Constants

    /**String que anirà al botó de guardar*/
    private static final String doneButtonString = "Guardar";

    /**String que representa el nom original de l'alfabet*/
    private String nomOriginal;


    
    /** Constructora de la classe
     *  Rep com a paràmetre una referència de la vista principal del programa, junt dues llistes
     *  que contenen cadascuna els noms dels textos i les llistes de freqüències existents al Sistema
     *  respectivament. També cal indicar el nom de l'alfabet a editar (existent al Sistema).
     *
     * @param vistaPrincipal Referència de la vista principal.
     * @param nomOriginal Nom de l'alfabet del Sistema que es vol editar.
     * @param contingutOriginal Símbols que té l'alfabet identificat pel nom {@code nomOriginal}.
     * @param llistesFreq {@link ArrayList} que conté els noms de les llistes de freqüències existents al Sistema.
     * @param textos {@link ArrayList} que conté els noms dels textos existents al Sistema.
     */
    EditarAlfabetFrame(VistaPrincipalFrame vistaPrincipal, String nomOriginal, String contingutOriginal, ArrayList<String> llistesFreq, ArrayList<String> textos) {
        super(vistaPrincipal, doneButtonString, llistesFreq, textos);
        this.nomOriginal = nomOriginal;
        super.init(nomOriginal, contingutOriginal);
        setTitle("Editant l'alfabet " + nomOriginal);
        setVisible(true);
    }

    /**Crida a la instancia crearAlfabet per editar un alfabet
     *
     * @param name Nou nom de l'alfabet existent al Sistema indicat en la creadora de la classe.
     * @param content Seqüència de símbols que conté els que tindrà l'alfabet indicat en la 
     *                creadora de la classe després de ser modificat.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix un alfabet identificat pel nom donat.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a la seqüència de símbols donada. 
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a la seqüència de símbols donada.
     * @throws NomProhibitException El nom donat és {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}, 
     *                              que està prohibit.
     */
    @Override
    public void creaOModificaAlfabet(String name, String content) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, SimbolRepetitException, NumSimbolsInvalidException, NomNoExisteixException, SimbolInvalidException, NomProhibitException {
        vistaPrincipal.editarAlfabet(nomOriginal, name, content);
    }
}
