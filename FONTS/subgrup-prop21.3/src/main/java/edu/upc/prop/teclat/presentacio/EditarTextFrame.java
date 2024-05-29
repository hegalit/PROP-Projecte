package edu.upc.prop.teclat.presentacio;

import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.Text;
import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomJaExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NomNoExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.TextEstaBuitException;

/**
 * Aquesta classe gestiona la vista d'edició de textos.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public class EditarTextFrame extends GestioTextosFrame{
    /**String que anirà al botó de guardar*/
    private final String doneButtonString = "Guardar";
    /**String que representa el nom original de l'alfabet*/
    private String nomOriginal;

    /** Crea la interfície d'usuari d'edició de textos indicant nom i contingut del text a editar
     *  (existent al Sistema) i una referència de la vista principal del programa.
     *
     * @param vistaPrincipal Referència de la vista principal.
     * @param nomOriginal Nom que identifica al text que es vol editar.
     * @param contingutOriginal Contingut del text identificat pel nom donat.
     */
    EditarTextFrame(VistaPrincipalFrame vistaPrincipal, String nomOriginal, String contingutOriginal) {
        super(vistaPrincipal);
        this.nomOriginal = nomOriginal;
        super.doneButtonString = doneButtonString;
        super.init(nomOriginal, contingutOriginal);
        setTitle("Editant el text " + nomOriginal);
        setVisible(true);
    }


    /**
     * Modifica el text indicat en la creadora amb els paràmetres d'entrada.
     *
     * @param name Nou nom del text existent al Sistema indicat en la creadora de la classe.
     * @param content Nou cos que es donarà al text indicat en la creadora de la classe.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema un text identificat pel nom donat.
     * @throws NomMassaLlargException El nom donat té més de {@value Text#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws NomNoExisteixException No existeix cap text identificat pel nom donat en la creadora 
     *                                de la classe.
     * @throws TextEstaBuitException {@code content} no té caràcters.
     */
    @Override
    void creaOModificaText(String name, String content) throws NomJaExisteixException, TextEstaBuitException, NomBuitException, NomMassaLlargException, NomNoExisteixException, NumSimbolsInvalidException {
        vistaPrincipal.editarText(nomOriginal, name, content);
    }
}
