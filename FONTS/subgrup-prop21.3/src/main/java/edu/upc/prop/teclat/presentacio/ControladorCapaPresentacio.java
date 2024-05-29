package edu.upc.prop.teclat.presentacio;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Path;

import edu.upc.prop.teclat.dades.exceptions.InvalidFileException;
import edu.upc.prop.teclat.dades.exceptions.InvalidFormatException;
import edu.upc.prop.teclat.domini.*;
import edu.upc.prop.teclat.domini.exceptions.*;
import edu.upc.prop.teclat.domini.exceptions.teclat.CaractersNoInclososException;
import edu.upc.prop.teclat.domini.exceptions.teclat.IndexosInvalidsException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingBestLayoutException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingPairsFreqException;
import edu.upc.prop.teclat.domini.exceptions.teclat.TeclatTemporalBuitException;
import edu.upc.prop.teclat.util.Pair;

/**
 * Gestiona la comunicació amb la capa de domini.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public class ControladorCapaPresentacio {
    /**Instància del controlador de domini*/
    ControladorCapaDomini controladorCapaDomini;

    /**Instància del menú principal de l'aplicació*/
    VistaPrincipalFrame vistaPrincipal;

    /** Constructora per defecte
     *	Associa el controlador de la capa de presentació amb la instància del controlador 
     *  de la capa de domini indicada.
     */
    public ControladorCapaPresentacio() {
        controladorCapaDomini = new ControladorCapaDomini();
    }

    /** Inicialitza la interfície de l'aplicació */
    public void inicialitzarPresentacio() {
        vistaPrincipal = new VistaPrincipalFrame(this);
        vistaPrincipal.setVisible(true);
    }



    // Obtenir els noms dels diferents components

    /**
     * Retorna la llista dels noms (en ordre lexicogràfic) dels teclats guardats al Sistema.
     *
     * @return Una llista que conté els noms (en ordre lexicogràfic) dels teclats guardats al Sistema.
     */
    ArrayList<String> getNomsTeclats() {
        return controladorCapaDomini.getNomsTeclats();
    }

    /**
     * Retorna la llista dels noms (en ordre lexicogràfic) dels alfabets guardats al Sistema.
     *
     * @return Una llista que conté els noms (en ordre lexicogràfic) dels alfabets guardats al sistema.
     */
    ArrayList<String> getNomsAlfabets() {
        return controladorCapaDomini.getNomsAlfabets();
    }

    /**
     * Retorna la llista dels noms (en ordre lexicogràfic) dels alfabets guardats al Sistema, i 
     * afegeix com a primer element el nom clau {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}.
     *
     * @return Retorna els noms dels alfabets ordenats alfabèticament.
     */
    ArrayList<String> getNomsAlfabetsAmbOriginalTeclat() {
        return controladorCapaDomini.getNomsAlfabetsAmbOriginalTeclat();
    }

    /**
     *  Retorna la llista dels noms (en ordre lexicogràfic) de les llistes de freqüències 
     *  guardades al Sistema.
     *
     * @return Una llista que conté els noms (en ordre lexicogràfic) de les llistes de freqüències 
     *         guardades al Sistema.
     */
    ArrayList<String> getNomsLlistesFreq() {
        return controladorCapaDomini.getNomsLlistesDeFrequencia();
    }

    /**
     *  Retorna la llista de noms (en ordre lexicogràfic) dels textos guardats al Sistema.
     *
     * @return Una llista que conté els noms (en ordre lexicogràfic) dels textos guardats al Sistema.
     */
    ArrayList<String> getNomsTextos() {
        return controladorCapaDomini.getNomsTextos();
    }

    /**
     *  Retorna una llista que conté els noms dels algorismes que es poden cridar per a regenerar 
     *  la disposició d'un teclat.
     *
     * @return Una llista que conté els noms dels algorismes que es poden cridar per a regenerar 
     *         la disposició d'un teclat..
     */
    ArrayList<String> getNomsAlgoritmes() {
        return controladorCapaDomini.getNomsAlgoritmes();
    }



    // Comprovar si un element existeix

    /**
     *  Retorna si existeix un teclat amb el nom especificat.
     *
     * @param nomTeclat Nom del teclat que es vol verificar si existeix.
     *
     * @return True si existeix el teclat amb el nom donat. Altrament, false.
     */
    boolean existeixTeclat(String nomTeclat) {
        return controladorCapaDomini.existeixTeclat(nomTeclat);
    }

    /**
     *  Retorna si existeix un alfabet amb el nom especificat.
     *
     * @param nomAlfabet Nom de l'alfabet que es vol verificar si existeix.
     *
     * @return True si existeix l'alfabet amb el nom donat. Altrament, false.
     */
    boolean existeixAlfabet(String nomAlfabet) {
        return controladorCapaDomini.existeixAlfabet(nomAlfabet);
    }

    /**
     *  Retorna si existeix una llista de freqüències amb el nom especificat.
     *
     * @param nomLlistaDeFrequencia Nom de la llista de freqüències que es vol verificar si existeix.
     *
     * @return True si existeix la llista de freqüències amb el nom donat. Altrament, false.
     */
    boolean existeixLlistaDeFrequencia(String nomLlistaDeFrequencia) {
        return controladorCapaDomini.existeixLlistaDeFrequencia(nomLlistaDeFrequencia);
    }

    /**
     *  Retorna si existeix un text amb el nom especificat.
     *
     * @param nomText Nom del text que es vol verificar si existeix.
     *
     * @return True si existeix el text amb el nom donat. Altrament, false.
     */
    boolean existeixText(String nomText) {
        return controladorCapaDomini.existeixText(nomText);
    }



    // Crear nous objectes

    /**
     * Crea un nou text a partir dels paràmetres d'entrada i l'afegeix al Sistema.
     *
     * @param nom Nom del nou text a afegir.
     * @param contingut Una seqüència de caràcters que representa el cos del nou text a afegir.
     *
     * @throws NomJaExisteixException Ja existeix un alfabet identificat pel nom donat al Sistema.
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws TextEstaBuitException La seqüència donada no té caràcters.
     */
    void crearText(String nom, String contingut) throws NomJaExisteixException, TextEstaBuitException, NomBuitException, NomMassaLlargException, NumSimbolsInvalidException {
        controladorCapaDomini.crearText(nom, contingut);
    }

    /**
     *  Crea un nou alfabet a partir dels paràmetres d'entrada i l'afegeix al Sistema. 
     *
     * @param nom Nom del nou alfabet a afegir.
     * @param simbols Seqüència de símbols que indica els que tindrà el nou alfabet a crear.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix un alfabet identificat per {@code nom} al Sistema.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException La seqüència de símbols donada està buida o té més de 
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a la seqüència de símbols 
     *                                donada.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a la seqüència de símbols 
     *                                donada.
     * @throws NomProhibitException El nom donat és {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}, 
     *                              que està prohibit.
     */
    void crearAlfabet(String nom, String simbols) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException, NomProhibitException {
        controladorCapaDomini.crearAlfabet(nom, simbols);
    }

    /**
     *  Crea una nova llista de freqüències a partir dels paràmetres d'entrada i l'afegeix al Sistema.
     *
     * @param nom Nom de la nova llista de freqüències a afegir.
     * @param llista Seqüència de parells &lt;paraula, freqüència&gt; de la que
     *               obtenir les associacions paraula-freqüència.
     *
     * @throws NomBuitException El nom de la llista a afegir no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value LlistaDeFrequencia#MAX_NAME_LENGTH} 
     *                                caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema una llista de freqüències 
     *                                identificada pel nom donat.
     * @throws NombreElementsInvalidException La seqüència d’entrada està buida. Aquesta ha de contenir 
     *                                        com a mínim una paraula, i tota paraula va seguida de la 
     *                                        seva freqüència.
     * @throws InvalidFrequencyException Hi ha alguna paraula de la seqüència donada que té
     *                                   freqüència &lt;= 0.
     * @throws ParaulaRepetidaException Una o més paraules es troben repetides a la seqüència donada.
     * @throws NumSimbolsInvalidException La seqüència donada conté més de {@value Alfabet#MAX_NUM_SYMBOLS}
     *                                    símbols diferents entre totes les paraules que té.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos en alguna
     *                                de les paraules que conté la seqüència donada.
     * @throws ParaulaBuidaException Una o més paraules de la seqüència donada no tenen caràcters.
     */
    void crearLlistaDeFrequencia(String nom, ArrayList<Pair<String, Integer>> llista) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, NombreElementsInvalidException, InvalidFrequencyException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException {
        controladorCapaDomini.crearLlistaFrequencia(nom, (ArrayList<Pair<String, Integer>>) llista);
    }



    // Modificar elements

    /**
     * Sobreescriu les dades de l'alfabet identificat per {@code nomOriginal} i existent al Sistema,
     * donant-li com a nou nom {@code nouNom} i com a símbols {@code simbols}.
     *
     * @param nomOriginal Nom d'aquell alfabet existent al Sistema i que volem modificar.
     * @param nouNom Nom que se li donarà a l'alfabet identificat per {@code nomOriginal}.
     * @param simbols Els nous símbols que tindrà l'alfabet identificat per {@code nomOriginal}.
     *
     * @throws NomJaExisteixException Ja existeix al Sistema un alfabet identificat per {@code new_nom}.
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom 
     *                                {@code nomOriginal}.
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'String {@code simbols} donat.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a l'String {@code simbols} donat.
     * @throws NomProhibitException El nom donat és "Alfabet original del teclat", que està prohibit.
     */
    void editarAlfabet(String nomOriginal, String nouNom, String simbols) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, NumSimbolsInvalidException, SimbolRepetitException, NomNoExisteixException, SimbolInvalidException, NomProhibitException {
        controladorCapaDomini.setNomAlfabet(nomOriginal, nouNom);
        try {
            controladorCapaDomini.setSimbolsAlfabet(nouNom, simbols);
        } catch (NumSimbolsInvalidException | SimbolRepetitException | NomNoExisteixException | SimbolInvalidException e) {
            controladorCapaDomini.setNomAlfabet(nouNom, nomOriginal);
            throw e;
        }
    }

    /**
     *  Donat el nom d'un text existent al Sistema, retorna un String que conté tots els símbols 
     *  d'aquest text en ordre lexicogràfic i sense repeticions.
     *
     * @param nomText Nom del text, existent al Sistema, del que es vol
     *                obtenir els seus símbols.
     *
     * @return Un String que conté tots els símbols en ordre lexicogràfic del text 
     *         identificat pel nom donat (sense repeticions).
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     */
    public String obtenirSimbolsText(String nomText) throws NomNoExisteixException {
        return controladorCapaDomini.getTextSimbols(nomText);
    }

    /**
     *  Donat el nom d'una llista de freqüències existent, retorna un String que conté tots  
     *  els símbols d'aquesta llista de freqüències en ordre lexicogràfic i sense repeticions.
     *
     * @param nomLlista Nom de la llista de freqüències, existent al Sistema, de la que obtenir 
     *                  els seus símbols.
     *
     * @return Un String que conté tots els símbols en ordre lexicogràfic de la llista de freqüències
     *         identificada pel nom donat (sense repeticions).
     *
     * @throws NomNoExisteixException No existeix al Sistema cap llista de freqüències identificada
     *                                per nomLlista.
     */
    public String obtenirSimbolsLlistaFrequencia(String nomLlista) throws NomNoExisteixException {
        return controladorCapaDomini.getLlistaFreqSimbols(nomLlista);
    }

    /**
     * Donat un nom {@code nomOriginal} que identifica una llista de freqüències present
     * al Sistema, sobreescriu el nom d'aquesta amb l'indicat per {@code nouNom} i canvia el seu
     * contingut amb l'obtingut a partir d'una seqüència de parells (paraula, freqüència).
     *
     * @param nomOriginal Nom d'aquella llista de freqüències existent al Sistema i que volem modificar.
     * @param nouNom Nom que se li donarà a la llista de freqüències identificada per {@code nomOriginal}.
     * @param contingut Una seqüència de parells &lt;paraula, freqüència&gt; de
     *                  la que obtenir les associacions paraula-freqüència.
     *
     * @throws NomBuitException El nom {@code new_nom} no té caràcters.
     * @throws NomMassaLlargException El nom {@code new_nom} té més de 
     *                                {@value LlistaDeFrequencia#MAX_NAME_LENGTH} caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema una llista de
     *                                freqüència identificada per {@code new_nom}.
     * @throws NomNoExisteixException No existeix al Sistema cap llista de
     *                                freqüències identificada pel nom {@code old_nom}.
     * @throws NombreElementsInvalidException La seqüència donada està buida.
     * @throws InvalidFrequencyException Hi ha alguna paraula de la seqüència donada que té  
     *                                   freqüència &gt;= a 0.
     * @throws ParaulaRepetidaException Una o més paraules es troben repetides a la seqüència donada.
     * @throws NumSimbolsInvalidException La seqüència donada conté més de {@value Alfabet#MAX_NUM_SYMBOLS}
     *                                    símbols diferents entre totes les paraules que té.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos en alguna
     *                                de les paraules que conté la seqüència donada.
     * @throws ParaulaBuidaException Una o més paraules de la seqüència donada no tenen caràcters.
     */
    void editarLlistaFreq(String nomOriginal, String nouNom, ArrayList<Pair<String, Integer>> contingut) throws NomNoExisteixException, NomBuitException, NomMassaLlargException, NomJaExisteixException, NombreElementsInvalidException, InvalidFrequencyException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException {
        controladorCapaDomini.setNomLlistaFreq(nomOriginal, nouNom);
        try {
            controladorCapaDomini.setContingutLlistaFreq(nouNom, contingut);
        } catch (NombreElementsInvalidException | InvalidFrequencyException | SimbolInvalidException | ParaulaRepetidaException | ParaulaBuidaException | NumSimbolsInvalidException e) {
            controladorCapaDomini.setNomLlistaFreq(nouNom, nomOriginal);
            throw e;
        }
    }

    /**
     * Donat un nom {@code nomOriginal} que identifica un text present al Sistema, sobreescriu 
     * el nom d'aquest amb l'indicat per {@code nouNom} i canvia el seu cos pel contingut de
     * {@code nouContingut}.
     *
     * @param nomOriginal Nom d'aquell text existent al Sistema i que volem modificar.
     * @param nouNom Nom que se li donarà al text identificat per {@code nomOriginal}.
     * @param nouContingut Nou cos que se li donarà al text identificat per {@code nomOriginal}.
     *
     * @throws NomJaExisteixException El nom donat ja existeix.
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat per {@code old_nom}.
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value Text#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws TextEstaBuitException El cos del text no té caràcters.
     */
    void editarText(String nomOriginal, String nouNom, String nouContingut) throws NomJaExisteixException, TextEstaBuitException, NomBuitException, NomMassaLlargException, NomNoExisteixException, NumSimbolsInvalidException {
        controladorCapaDomini.setNomText(nomOriginal, nouNom);
        try {
            controladorCapaDomini.setCosText(nouNom, nouContingut);
        } catch (TextEstaBuitException e) {
            controladorCapaDomini.setNomText(nouNom, nomOriginal);
            throw e;
        }
    }


    
    // Eliminar elements

    /**
     * Elimina del Sistema el teclat identificat pel nom donat i guarda els canvis al Sistema.
     *
     * @param nomTeclat Nom del teclat a eliminar del Sistema.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap teclat identificat pel nom donat.
     */
    void eliminarTeclat(String nomTeclat) throws NomNoExisteixException {
        controladorCapaDomini.eliminarTeclat(nomTeclat);
    }

    /**
     * Elimina del Sistema l'alfabet identificat pel nom donat i guarda els canvis al Sistema.
     *
     * @param nomAlfabet Nom de l'alfabet a eliminar del Sistema.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom donat.
     */
    void eliminarAlfabet(String nomAlfabet) throws NomNoExisteixException {
        controladorCapaDomini.eliminarAlfabet(nomAlfabet);
    }

    /**
     * Elimina del Sistema la llista de freqüències identificada pel nom donat i guarda 
     * els canvis al Sistema.
     *
     * @param nomLlistaDeFrequencia Nom de la llista de freqüència a eliminar del Sistema.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap llista de freqüències 
     *                                identificada pel nom donat.
     */
    void eliminarLlistaDeFrequencia(String nomLlistaDeFrequencia) throws NomNoExisteixException {
        controladorCapaDomini.eliminarLlistaFrequencia(nomLlistaDeFrequencia);
    }

    /**
     * Elimina del Sistema el text identificat pel nom donat i guarda els canvis al Sistema.
     *
     * @param nomText Nom del text a eliminar del Sistema.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     */
    void eliminarText(String nomText) throws NomNoExisteixException {
        controladorCapaDomini.eliminarText(nomText);
    }
    


    // Consultar elements

    /**
     *  Donat el nom d'un alfabet existent al Sistema, retorna un String que conté 
     *  tots els símbols d'aquest alfabet en ordre lexicogràfic.
     *
     * @param nom Nom de l'alfabet, existent dins del Sistema, del qual obtenir els seus símbols.
     *
     * @return Un String que conté tots els símbols en ordre lexicogràfic de l'alfabet identificat
     *         pel nom donat.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom donat.
     */
    String getAlfabet(String nom) throws NomNoExisteixException {
        return controladorCapaDomini.getSimbolsAlfabet(nom);
    }

    /**
     * Retorna una seqüència de parells amb el contingut de la llista de freqüències 
     * indentificada pel nom donat i existent al Sistema.
     *
     * @param nomLlistaDeFrequencia Nom de la llista de freqüències, existent dins del 
     *                              Sistema, de la qual obtenir el seu contingut.
     *
     * @return Retorna el contingut de la llista de freqüències identificada pel nom donat
     *         i existent al Sistema.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap llista de freqüències 
     *                                identificada pel nom donat.
     */
    ArrayList<Pair<String, Integer>> getLlistaDeFrequencia(String nomLlistaDeFrequencia) throws NomNoExisteixException {
        return controladorCapaDomini.getContingutLlistaFreq(nomLlistaDeFrequencia);
    }
    
    /**
     * Retorna el cos del text identificat pel nom donat i existent al Sistema.
     *
     * @param nomText Nom del text, existent al Sistema, del que es vol obtenir el seu cos.
     *
     * @return El cos del text identificat pel nom donat i existent al Sistema.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     */
    String getCosText(String nomText) throws NomNoExisteixException {
        return controladorCapaDomini.getCosText(nomText);
    }



    // Importar elements

    /**
     * Importa un teclat indicant el path del seu fitxer.
     *
     * @param path Path del teclat a importar.
     *
     * @throws ClassNotFoundException No s'ha trobat la classe.
     * @throws InvalidFileException El fitxer donat no té el format demanat.
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomJaExisteixException Ja existeix al Sistema un teclat identificat pel nom del
     *                                teclat que es vol importar.
     */
    void importarTeclat(Path path) throws ClassNotFoundException, InvalidFileException, IOException, NomJaExisteixException {
        controladorCapaDomini.importarTeclat(path);
    }

    /**
     * Importa una llista de freqüències indicant el path del seu fitxer.
     *
     * @param path Path de la llista de freqüències a importar.
     *
     * @throws InvalidFileException El fitxer donat no té la extensió demanada.
     * @throws InvalidFormatException El fitxer donat no té el format demanat.
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomJaExisteixException Ja existeix al Sistema una llista de freqüències identificada 
     *                                pel nom de la llista de freqüències que es vol importar.
     * @throws NomMassaLlargException El nom donat té més de {@value Text#MAX_NAME_LENGTH} caràcters.
     */
    void importarLlistaDeFrequencia(Path path) throws IOException, InvalidFormatException, NomMassaLlargException, NomJaExisteixException, InvalidFileException {
        controladorCapaDomini.importarLlistaFrequencia(path);
    }

    /**
     * Importa un alfabet indicant el path del seu fitxer.
     *
     * @param path direcció per a importar l'alfabet.
     *
     * @throws InvalidFileException El fitxer donat no té la extensió demanada.
     * @throws NomJaExisteixException Ja existeix al Sistema un alfabet amb el mateix nom que  
     *                                aquell que es vol importar.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws InvalidFileException El fitxer no té l'extensió correcta.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'alfabet que es vol importar.
     * @throws NumSimbolsInvalidException L'alfabet a importar està buit o té més de 
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a l'alfabet que es vol importar.
     * @throws NomProhibitException El nom de l'alfabet a importar és "Alfabet original del teclat", 
     *                              que està prohibit.
     */
    void importarAlfabet(Path path) throws IOException, NomMassaLlargException, NomJaExisteixException, InvalidFileException, SimbolInvalidException, SimbolRepetitException, NumSimbolsInvalidException, NomProhibitException {
        controladorCapaDomini.importarAlfabet(path);
    }

    /**
     * Importa un text indicant el path del seu fitxer.
     *
     * @param path Path del text a importar.
     *
     * @throws InvalidFileException El fitxer donat no té la extensió demanada.
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomJaExisteixException Ja existeix al Sistema un text identificat pel nom donat.
     * @throws NomMassaLlargException El nom donat té més de 100 caràcters.
     * @throws TextEstaBuitException El cos del text no té caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    100 símbols diferents.
     */
    void importarText(Path path) throws IOException, NomMassaLlargException, NomJaExisteixException, TextEstaBuitException, InvalidFileException, NumSimbolsInvalidException {
        controladorCapaDomini.importarText(path);
    }


    // Exportar elements
    /**
     * Exporta un teclat existent al Sistema i identificat pel nom donat al path indicat.
     *
     * @param nom Nom del teclat, existent dins del Sistema, a exportar.
     * @param path Path a on es vol exportar el teclat identificat pel nom donat.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomNoExisteixException No existeix al Sistema cap teclat identificat pel nom donat.
     */
    void exportarTeclat(String nom, Path path) throws IOException, NomNoExisteixException {
        controladorCapaDomini.exportarTeclat(nom, path);
    }

    /**
     * Exporta una llista de freqüències existent dins del Sistema i identificada pel nom donat
     * al path indicat.
     *
     * @param nom Nom de la llista de freqüències, existent al Sistema, a exportar.
     * @param path Path a on es vol exportar la llista de freqüències identificada pel nom donat.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomNoExisteixException No existeix al Sistema cap llista de freqüències
     *                                identificada pel nom donat.
     */
    void exportarLlistaDeFrequencia(String nom, Path path) throws IOException, NomNoExisteixException {
        controladorCapaDomini.exportarLlistaFrequencia(nom, path);
    }

    /**
     * Exporta un text existent dins del Sistema i identificat pel nom donat al path indicat.
     *
     * @param nom Nom del text a exportar.
     * @param path Path a on es vol exportar el text identificat pel nom donat.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomNoExisteixException El nom donat no existeix.
     */
    void exportarText(String nom, Path path) throws IOException, NomNoExisteixException {
        controladorCapaDomini.exportarText(nom, path);
    }

    /**
     * Exporta un alfabet existent dins del Sistema i identificat pel nom donat al path indicat.
     *
     * @param nom Nom de l'alfabet, existent dins del Sistema, a exportar.
     * @param path Path a on es vol exportar la llista de freqüències identificada pel nom donat.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomNoExisteixException El nom donat no existeix.
     */
    void exportarAlfabet(String nom, Path path) throws IOException, NomNoExisteixException {
        controladorCapaDomini.exportarAlfabet(nom, path);
    }

    /**
     * Donat el nom d'un text present al Sistema, retorna una seqüència de parells &lt;paraula, 
     * freqüència&gt; on cada parella representa una paraula present al cos del text i el número 
     * d'ocurrències d'aquesta dins del text (la seva freqüència).
     *
     * @param nomText Nom del text existent dins del conjunt de textos del que extreure 
     *                les associacions paraula-freqüència.
     *
     * @return La seqüència de parells (paraula, freqüència) generada.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     */
    ArrayList<Pair<String, Integer>> getLlistaDeFrequenciaAPartirDeText(String nomText) throws NomNoExisteixException {
        return controladorCapaDomini.getFrequenciesText(nomText);
    }


    
    // Manipular teclats temporals

    /**
     * Sobreescriu el teclat temporal del conjunt de teclats amb un de nou generat a partir dels 
     * símbols que conté l'alfabet identificat per nomAlfabet, i redimensiona el teclat temporal 
     * perquè tingui ràtio amplada-alçada de 2.
     *
     * @param nomAlfabet Nom de l'alfabet, existent al Sistema, del que obtenir els símbols.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom donat.
     */
    void crearTeclat(String nomAlfabet) throws NomNoExisteixException {
        controladorCapaDomini.crearTeclat(nomAlfabet);
    }

    /**
     * Carrega al teclat temporal un teclat existent dins del Sistema.
     *
     * @param nomTeclat Nom que identifica al teclat, existent al Sistema, que es vol carregar.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap teclat identificat pel nom donat.
     */
    void carregarTeclat(String nomTeclat) throws NomNoExisteixException {
        controladorCapaDomini.carregarTeclat(nomTeclat);
    }

    /**
     * Guarda un nou teclat amb les dades del teclat temporal, i que s'identificarà pel 
     * nom nouNom, dins del conjunt de teclats.
     *
     * @param nomTeclat Nom amb què es guardarà el nou teclat.
     *
     * @throws NomNoExisteixException S'està intentant modificar un teclat que no existeix.
     * @throws NomJaExisteixException Ja existeix al Sistema un teclat identificat pel nom donat.
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value Teclat#MAX_NAME_LENGTH} caràcters.
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    void guardarTeclat(String nomTeclat) throws NomJaExisteixException, NomNoExisteixException, TeclatTemporalBuitException, NomBuitException, NomMassaLlargException {
        controladorCapaDomini.guardarTeclat(nomTeclat);
    }

    /**
     * Retorna el nombre de files del teclat temporal del Sistema.
     *
     * @return El nombre de files del teclat temporal del Sistema.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    int getFilesTeclat() throws TeclatTemporalBuitException {
        return controladorCapaDomini.getFilesTeclat();
    }

    /**
     * Retorna el nombre de columnes del teclat temporal del Sistema.
     *
     * @return El nombre de columnes del teclat temporal del Sistema.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    int getColumnesTeclat() throws TeclatTemporalBuitException {
        return controladorCapaDomini.getColsTeclat();
    }

    /**
     * Modifica les dimensions del teclat temporal del Sistema en funció del número de files. 
     * Si aquest últim no és aplicable, el nombre de files passarà a ser el valor aplicable més
     * proper al donat.
     *
     * @param files Nombre de files que es vol donar al teclat temporal del Sistema.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    void setFilesTeclat(int files) throws TeclatTemporalBuitException {
        controladorCapaDomini.setFilesTeclat(files);
    }

    /**
     * Modifica les dimensions del teclat temporal del Sistema en funció del número de columnes. 
     * Si aquest últim no és aplicable, el nombre de columnes passarà a ser el valor aplicable 
     * més proper al donat.
     *
     * @param columnes Nombre de columnes que es vol donar al teclat temporal del Sistema.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    void setColsTeclat(int columnes) throws TeclatTemporalBuitException {
        controladorCapaDomini.setColsTeclat(columnes);
    }

    /**
     * Retorna la disposició actual del teclat temporal.
     *
     * @return Una seqüència de caràcters amb la disposició actual del teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    char[] getLayoutTeclat() throws TeclatTemporalBuitException {
        return controladorCapaDomini.getLayoutTeclat();
    }

    /**
     * Intercanvia els símbols ubicats als índexos tecla1 i tecla2 dins la disposició del 
     * teclat temporal del conjunt de teclats.
     *
     * @param tecla1 Índex del primer símbol.
     * @param tecla2 Índex del segon símbol.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     * @throws IndexosInvalidsException S'ha intentat accedir a una posició incorrecta.
     */
    void swapKeysTeclat(int tecla1, int tecla2) throws IndexosInvalidsException, TeclatTemporalBuitException {
        controladorCapaDomini.swapKeysTeclat(tecla1, tecla2);
    }

    /**
     * Regenera la disposició del teclat temporal del Sistema, indicant el nom de 
     * l'algorisme generador a emprar i utilitzant els parells de freqüències guardats al Sistema.
     *
     * @param nomAlgoritme Nom de l'algoritme.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     * @throws MissingPairsFreqException No estan especificats els parells de freqüències.
     * @throws InvalidGeneratorAlgorithmException El generador de l'algoritme és invàlid.
     */
    void regenerarTeclat(String nomAlgoritme) throws MissingPairsFreqException, TeclatTemporalBuitException, InvalidGeneratorAlgorithmException {
        controladorCapaDomini.regenerarTeclat(nomAlgoritme);
    }

    /**
     * Modifica els símbols de l'alfabet del teclat temporal del Sistema. A més a més, 
     * es reemplacen els símbols de la disposició del teclat temporal del Sistema pels 
     * símbols seleccionats, i es col·loquen per files ordenats lexicogràficament. 
     * Conseqüentment s'adapten les dimensions del teclat temporal del Sistema a la nova
     * disposició tal que el ràtio amplada-alçada sigui 2.
     *
     * @param nomAlfabet El nom de l'alfabet del que extreure els símbols que tindrà el 
     *                   teclat temporal. Si aquest nom coincideix amb {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}
     *                   selecciona els símbols que el teclat seleccionat tenia abans de cap modificació. 
     *                   Altrament selecciona els símbols de l'alfabet identificat per nom_alfabet.
     *
     * @throws NomNoExisteixException No existeix cap alfabet identificat pel nom donat.
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    void setAlfabetTeclat(String nomAlfabet) throws NomNoExisteixException, TeclatTemporalBuitException {
        controladorCapaDomini.setSimbolsTeclat(nomAlfabet);
    }

    /**
     * Sobreescriu els parells de freqüències del Sistema amb uns de nous generats
     * a partir de la llista identificada pel nom donat.
     *
     * @param nomLlistaFreq Nom de la llista de freqüències de la que obtenir els nous parells 
     *                      de freqüències.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap llista de freqüències identificada 
     *                                pel nom donat.
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     * @throws CaractersNoInclososException L'alfabet del teclat temporal del Sistema no conté tots els 
     *                                      símbols de la llista de freqüències identificada pel nom donat.
     */
    void setFreqPairsByFreqListTeclat(String nomLlistaFreq) throws NomNoExisteixException, CaractersNoInclososException, TeclatTemporalBuitException {
        controladorCapaDomini.setFreqPairsByFreqListTeclat(nomLlistaFreq);
    }

    /**
     * Sobreescriu els parells de freqüències del conjunt de teclats amb uns de nous generats
     * a partir del text identificat pel nom donat i existent al Sistema.
     *
     * @param nomText Nom del text, existent al Sistema, del que obtenir els nous parells de freqüències.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     * @throws CaractersNoInclososException L'alfabet del teclat temporal del conjunt de teclats no 
     *                                      conté tots els símbols del text identificat pel nom donat.
     */
    void setFreqPairsByTextTeclat(String nomText) throws NomNoExisteixException, CaractersNoInclososException, TeclatTemporalBuitException {
        controladorCapaDomini.setFreqPairsByTextTeclat(nomText);
    }

    /**
     * Retorna el cost de la disposició de símbols que té el teclat temporal del Sistema.
     *
     * @return El cost de la disposició de símbols que té el teclat temporal del Sistema.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     * @throws MissingPairsFreqException No s’ha donat un valor als parells de freqüència 
     *                                   del Sistema.
     */
    double getCostTeclat() throws TeclatTemporalBuitException, MissingPairsFreqException {
        return controladorCapaDomini.getCostTeclat();
    }

    /**
     * Intercanvia els símbols ubicats als índexos tecla1 i tecla2 dins la disposició del teclat 
     * temporal del Sistema.
     *
     * @param tecla1 Index del primer símbol.
     * @param tecla2 Index del segon símbol.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     * @throws IndexosInvalidsException S'ha intentat accedir a una posició incorrecta.
     */
    void swapKeysTeclatTemporal(char tecla1, char tecla2) throws IndexosInvalidsException, TeclatTemporalBuitException {
        controladorCapaDomini.swapKeysTeclat(tecla1, tecla2);
    }

    /**
     * Regenera la disposició del teclat temporal del Sistema, indicant el nom de l'algorisme
     * generador a emprar i utilitzant els parells de freqüències guardats al Sistema.
     *
     * @param nomAlgoritme Nom de l'algorisme amb el que es generarà la nova disposició 
     *                     del teclat temporal del conjunt de teclats.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     * @throws MissingPairsFreqException No s’ha donat un valor als parells de freqüències del 
     *                                   conjunt de teclats.
     * @throws InvalidGeneratorAlgorithmException L'algorisme generador al que s'intenta cridar 
     *                                            no existeix.
     */
    void generateLayoutTeclat(String nomAlgoritme) throws MissingPairsFreqException, TeclatTemporalBuitException, InvalidGeneratorAlgorithmException {
        controladorCapaDomini.regenerarTeclat(nomAlgoritme);
    }

    /**
     * Retorna els noms dels textos existents al Sistema tals que tots els símbols que contenen 
     * estan presents al conjunt rebut com a paràmetre d'entrada.
     *
     * @return Una llista amb els noms dels textos tals que tots els símbols que contenen estan 
     *         presents al conjunt donat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    ArrayList<String> getTextosCompatibles() throws TeclatTemporalBuitException {
        return controladorCapaDomini.getTextosCompatibles();
    }

    /**
     * Retorna els noms de les llistes de freqüència existents al Sistema tals que tots els 
     * símbols que contenen estan presents al conjunt donat.
     *
     * @return Una llista amb els noms de les llistes de freqüència tals que tots els símbols
     *         que contenen estan presents al conjunt donat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    ArrayList<String> getLlistesFreqCompatibles() throws TeclatTemporalBuitException {
        return controladorCapaDomini.getLlistesFreqCompatibles();
    }

    /**
     * Indica si el layout del teclat temporal del conjunt de teclats es pot canviar per la millor
     * disposició trobada. Si el teclat temporal del Sistema ja es troba en una disposició òptima,
     * no es podrà canviar. Si no es té registre d'una disposició prèvia millor, tampoc.
     *
     * @return True si si el layout del teclat temporal del Sistema es pot canviar per la millor 
     *         disposició trobada. Altrament, false.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    boolean canSetBestLayoutTeclat() throws TeclatTemporalBuitException {
        return controladorCapaDomini.canSetBestLayoutTeclat();
    }

    /**
     * Sobreescriu el layout del teclat temporal del Sistema amb el millor trobat 
     * fins ara amb la resta de paràmetres del teclat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     * @throws MissingBestLayoutException No hi ha un millor layout per al teclat temporal.
     */
    void setBestLayoutTeclat() throws TeclatTemporalBuitException, MissingBestLayoutException {
        controladorCapaDomini.setBestLayoutTeclat();
    }
}
