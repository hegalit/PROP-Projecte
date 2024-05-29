package edu.upc.prop.teclat.presentacio;

import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.Text;
import edu.upc.prop.teclat.domini.exceptions.*;

/**
 * Aquesta classe gestiona la vista de creació de textos.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public class CrearTextFrame extends GestioTextosFrame {
    /**String que anirà al botó de crear*/
    private final String doneButtonString = "Crear";

    /** Crea la interfície d'usuari de creació de textos rebent una referència de la vista
     *  principal del programa.
     *
     * @param vistaPrincipal Referència de la vista principal.
     */
    CrearTextFrame(VistaPrincipalFrame vistaPrincipal) {
        super(vistaPrincipal);
        super.doneButtonString = doneButtonString;
        super.init();
        setTitle("Crear un nou text");
        setVisible(true);
    }

    /**
     * Crea un nou text a partir dels paràmetres d'entrada.
     *
     * @param name Nom del text a crear.
     * @param content Nous símbols que es donarà al text a crear.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema un text identificat pel nom donat.
     * @throws NomMassaLlargException El nom donat té més de {@value Text#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws TextEstaBuitException {@code content} no té caràcters.
     */
    @Override
    void creaOModificaText(String name, String content) throws NomJaExisteixException, TextEstaBuitException, NomBuitException, NomMassaLlargException, NumSimbolsInvalidException {
        vistaPrincipal.crearText(name, content);
    }
}
