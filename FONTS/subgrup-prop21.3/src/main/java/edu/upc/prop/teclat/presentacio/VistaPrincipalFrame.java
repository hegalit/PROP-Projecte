package edu.upc.prop.teclat.presentacio;

import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.upc.prop.teclat.dades.exceptions.InvalidFileException;
import edu.upc.prop.teclat.dades.exceptions.InvalidFormatException;
import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.LlistaDeFrequencia;
import edu.upc.prop.teclat.domini.Teclat;
import edu.upc.prop.teclat.domini.Text;
import edu.upc.prop.teclat.domini.exceptions.*;
import edu.upc.prop.teclat.domini.exceptions.teclat.CaractersNoInclososException;
import edu.upc.prop.teclat.domini.exceptions.teclat.IndexosInvalidsException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingBestLayoutException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingPairsFreqException;
import edu.upc.prop.teclat.domini.exceptions.teclat.TeclatTemporalBuitException;
import edu.upc.prop.teclat.util.Pair;

/**
 * Classe que representa el menú principal del programa.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */

public class VistaPrincipalFrame extends javax.swing.JFrame {
    //Constants

    /** Velocitat a la qual es desplaçarà la barra desplaçadora quan sigui necessari*/
    private final int scrollSpeed = 16;

    /**Objecte del controlador de presentació*/
    private final ControladorCapaPresentacio controladorCapaPresentacio;

    /**String que indica el nom del teclat seleccionat.*/
    private String teclatSeleccionat = "";

    /**String que indica el nom de l'alfabet seleccionat.*/
    private String alfabetSeleccionat = "";

    /**String que indica el nom de la llista de freqüències seleccionada.*/
    private String llistaFreqSeleccionada = "";

    /**String que indica el nom del text seleccionat.*/
    private String textSeleccionat = "";



    /** Constructora del mainFrame o vista principal.
     *  Necessita com a paràmetre una instància del controlador de la capa de presentació del sistema.
     *
     * @param controladorCapaPresentacio Instància del controlador de la capa de presentació.
     *  */
    public VistaPrincipalFrame(ControladorCapaPresentacio controladorCapaPresentacio) {
        this.controladorCapaPresentacio = controladorCapaPresentacio;

        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaPrincipalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        initComponents();
        setLocationRelativeTo(null);

        // Configurar la velocitat de l'scroll
        keyboardsScrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        alfabetsScrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        freqListScrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);
        textsScrollPane.getVerticalScrollBar().setUnitIncrement(scrollSpeed);

        // Carregar els teclats i alfabets als scroll panels
        updateTeclats();
        updateAlfabets();
        updateLlistesFreq();
        updateTextos();
        
        keyboardsPanel.revalidate();
        keyboardsPanel.repaint();
    }



    // Funcions cridades per les diferents finestres subordinades a aquesta

    // Crear elements

    /**
     * 
     * 
     * @throws NomJaExisteixException Ja existeix un text amb aquest nom
     * @throws NumSimbolsInvalidException El nombre de simbols de l'alfabet a crear no es valid
     * @throws SimbolRepetitException Hi ha simbols repetits a l'alfabet a crear
     * @throws SimbolInvalidException Hi ha simbols invalids a l'alfabet a crear
     */

    /** Afegeix al conjunt d'alfabets un nou alfabet creat a partir dels paràmetres d'entrada,
     *  actualitza i mostra la vista principal en cas de no haver cap error.
     *
     * @param nom Nom del nou alfabet a crear.
     * @param simbols Seqüència de símbols que indica els que tindrà el nou alfabet a crear.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema un alfabet identificat pe nom donat.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                     {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a la seqüència de símbols donada.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a la seqüència de símbols donada.
     * @throws NomProhibitException El nom donat és {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}, 
     *                              que està prohibit.
     */
    void crearAlfabet(String nom, String simbols) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, SimbolRepetitException, NumSimbolsInvalidException, SimbolInvalidException, NomProhibitException {
        controladorCapaPresentacio.crearAlfabet(nom, simbols);
        updateAlfabets();
    }

    /** Crea una nova llista de freqüències a partir dels paràmetres d'entrada i l'afegeix al Sistema.
     *
     * @param nom Nom de la nova llista de freqüències a afegir.
     * @param freqs Seqüència de parells &lt;paraula, freqüència&gt; de la que obtenir les associacions
     *              paraula-freqüència.
     *
     * @throws NomJaExisteixException Ja existeix al Sistema una llista de freqüències identificada
     *                                pel nom donat.
     * @throws InvalidFrequencyException Hi ha alguna paraula de la seqüència donada que té 
     *                                   freqüència &lt;= 0.
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NombreElementsInvalidException La seqüència d’entrada està buida. Aquesta ha de contenir
     *                                        com a mínim una paraula, i tota paraula va seguida de la 
     *                                        seva freqüència.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos en alguna de les paraules 
     *                                que conté la seqüència donada.
     * @throws ParaulaRepetidaException Una o més paraules es troben repetides a la seqüència donada.
     * @throws NumSimbolsInvalidException La seqüència donada conté més de {@value Alfabet#MAX_NUM_SYMBOLS}
     *                                    símbols diferents entre totes les paraules que té.
     * @throws ParaulaBuidaException Una o més paraules de la seqüència donada no tenen caràcters.
     */
    void crearLlistaFreq(String nom, ArrayList<Pair<String, Integer>> freqs) throws NomJaExisteixException, InvalidFrequencyException, NomBuitException, NomMassaLlargException, NombreElementsInvalidException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException {
        controladorCapaPresentacio.crearLlistaDeFrequencia(nom, freqs);
        updateLlistesFreq();
    }

    /** Crea un nou text a partir dels paràmetres d'entrada i l'afegeix al Sistema.
     *
     * @param nom Nom del nou text a afegir.
     * @param contingut Una seqüència de caràcters que representa el cos del nou text a afegir.
     *
     * @throws NomJaExisteixException Ja existeix al Sistema una llista de freqüències identificada
     *                                pel nom donat.
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws TextEstaBuitException La seqüència donada no té caràcters.
     * @throws NumSimbolsInvalidException La seqüència donada conté més de {@value Alfabet#MAX_NUM_SYMBOLS}
     *                                    símbols diferents.
     * @throws NomMassaLlargException El nom donat té més de {@value Text#MAX_NAME_LENGTH} caràcters.
     */
    void crearText(String nom, String contingut) throws NomJaExisteixException, TextEstaBuitException, NomBuitException, NomMassaLlargException, NumSimbolsInvalidException {
        controladorCapaPresentacio.crearText(nom, contingut);
        updateTextos();
    }



    // Editar elements

    /** Sobreescriu les dades de l'alfabet identificat per {@code nomOriginal} i existent al Sistema,
     * donant-li com a nou nom {@code nouNom} i com a símbols {@code simbols}.
     *
     * @param nomOriginal Nom d'aquell alfabet existent al Sistema i que volem modificar.
     * @param nouNom Nom que se li donarà a l'alfabet identificat per {@code nomOriginal}.
     * @param simbols Els nous símbols que tindrà l'alfabet identificat per {@code nomOriginal}.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom 
     *                                {@code nomOriginal}.
     * @throws NomJaExisteixException Ja existeix al Sistema un alfabet identificat pe nom donat.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                     {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a la seqüència de símbols donada.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a la seqüència de símbols donada.
     * @throws NomProhibitException El nom donat és {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}, 
     *                              que està prohibit.
     */
    void editarAlfabet(String nomOriginal, String nouNom, String simbols) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, SimbolRepetitException, NumSimbolsInvalidException, NomNoExisteixException, SimbolInvalidException, NomProhibitException {
        controladorCapaPresentacio.editarAlfabet(nomOriginal, nouNom, simbols);
        updateAlfabets();
    }

    /** Donat el nom d'un text existent al Sistema, retorna un String que conté tots els símbols 
     *  d'aquest text en ordre lexicogràfic i sense repeticions.
     *
     * @param nomText Nom del text, existent al Sistema, del que es vol obtenir els seus símbols.
     *
     * @return Un String que conté tots els símbols en ordre lexicogràfic del text identificat 
     *         pel nom donat (sense repeticions).
     * 
     * @throws NomNoExisteixException El nom de l’element a inserir no existeix al sistema.
     */
    String obtenirSimbolsText(String nomText) throws NomNoExisteixException {
        return controladorCapaPresentacio.obtenirSimbolsText(nomText);
    }

    /** Donat el nom d'una llista de freqüències existent, retorna un String que conté tots els 
     *  símbols d'aquesta llista de freqüències en ordre lexicogràfic i sense repeticions.
     *
     * @param nomLlista Nom de la llista de freqüències, existent al Sistema, de la que 
     *                  obtenir els seus símbols.
     *
     * @return Un String que conté tots els símbols en ordre lexicogràfic de la llista de freqüències
     *         identificada pel nom donat (sense repeticions).
     *
     * @throws NomNoExisteixException El nom de l’element a inserir no existeix al sistema.
     */
    String obtenirSimbolsLlistaFrequencia(String nomLlista) throws NomNoExisteixException {
        return controladorCapaPresentacio.obtenirSimbolsLlistaFrequencia(nomLlista);
    }

    /** Donat un nom {@code nomOriginal} que identifica una llista de freqüències present
     *  al Sistema, sobreescriu el nom d'aquesta amb l'indicat per {@code nouNom} i canvia el seu
     *  contingut amb l'obtingut a partir d'una seqüència de parells (paraula, freqüència).
     * 
     * @param nomOriginal Nom d'aquella llista de freqüències existent al Sistema i que volem modificar.
     * @param nouNom Nom que se li donarà a la llista de freqüències identificada per {@code nomOriginal}.
     * @param novesFreqs Una seqüència de parells &lt;paraula, freqüència&gt; de
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
    void editarLlistaFreq(String nomOriginal, String nouNom, ArrayList<Pair<String, Integer>> novesFreqs) throws NomNoExisteixException, NomBuitException, NomMassaLlargException, NomJaExisteixException, NombreElementsInvalidException, InvalidFrequencyException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException {
        controladorCapaPresentacio.editarLlistaFreq(nomOriginal, nouNom, novesFreqs);
        updateLlistesFreq();
    }

    /** Donat un nom {@code nomOriginal} que identifica un text present al Sistema, sobreescriu 
     *  el nom d'aquest amb l'indicat per {@code nouNom} i canvia el seu cos pel contingut de
     *  {@code nouContingut}.
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
        controladorCapaPresentacio.editarText(nomOriginal, nouNom, nouContingut);
        updateTextos();
    }



    // Extraure dades

    /** Donat el nom d'un text present al Sistema, retorna una seqüència de parells &lt;paraula, 
     *  freqüència&gt; on cada parella representa una paraula present al cos del text i el número 
     *  d'ocurrències d'aquesta dins del text (la seva freqüència).
     *
     * @param nomText Nom del text existent dins del conjunt de textos del que extreure 
     *                les associacions paraula-freqüència.
     *
     * @return La seqüència de parells (paraula, freqüència) generada.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     */
    ArrayList<Pair<String, Integer>> getLlistaDeFrequenciaAPartirDeText(String nomText) throws NomNoExisteixException {
        return controladorCapaPresentacio.getLlistaDeFrequenciaAPartirDeText(nomText);
    }



    // Manipulació de teclats temporals

    /** 
     * Sobreescriu el teclat temporal del conjunt de teclats amb un de nou generat a partir 
     * dels símbols que conté l'alfabet identificat per nomAlfabet, i redimensiona el teclat 
     * temporal perquè tingui ràtio amplada-alçada de 2.
     *
     * @param nomAlfabet Nom de l'alfabet, existent al Sistema, del que obtenir els símbols.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom donat.
     */
    void crearTeclatTemporal(String nomAlfabet) throws NomNoExisteixException {
        controladorCapaPresentacio.crearTeclat(nomAlfabet);
    }

    /** Carrega al teclat temporal un teclat existent dins del Sistema.
     *
     * @param nomTeclat Nom que identifica al teclat, existent al Sistema, que es vol carregar.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap teclat identificat pel nom donat.
     */
    void carregarTeclatTemporal(String nomTeclat) throws NomNoExisteixException {
        controladorCapaPresentacio.carregarTeclat(nomTeclat);
    }

    /** Retorna la disposició actual del teclat temporal.
     *
     * @return Una seqüència de caràcters amb la disposició actual del teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    char[] getLayoutTeclat() throws TeclatTemporalBuitException {
        return controladorCapaPresentacio.getLayoutTeclat();
    }

    /** Retorna el nombre de files del teclat temporal del Sistema.
     *
     * @return El nombre de files del teclat temporal del Sistema.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    int getFilesTeclat() throws TeclatTemporalBuitException {
        return controladorCapaPresentacio.getFilesTeclat();
    }

    /** Retorna el nombre de columnes del teclat temporal del Sistema.
     *
     * @return El nombre de columnes del teclat temporal del Sistema.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    int getColumnesTeclat() throws TeclatTemporalBuitException {
        return controladorCapaPresentacio.getColumnesTeclat();
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
        controladorCapaPresentacio.setFilesTeclat(files);
    }

    /** 
     * Modifica les dimensions del teclat temporal del Sistema en funció del número de columnes. 
     * Si aquest últim no és aplicable, el nombre de columnes passarà a ser el valor aplicable més 
     * proper al donat.
     *
     * @param columnes Nombre de columnes que es vol donar al teclat temporal del Sistema.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    void setColsTeclat(int columnes) throws TeclatTemporalBuitException {
        controladorCapaPresentacio.setColsTeclat(columnes);
    }

    /** 
     * Intercanvia els símbols ubicats als índexos tecla1 i tecla2 dins la disposició del teclat 
     * temporal del conjunt de teclats.
     *
     * @param id1 Índex del primer símbol.
     * @param id2 Índex del segon símbol.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     * @throws IndexosInvalidsException S'ha intentat accedir a una posició incorrecta.
     */
    void swapKeysTeclat(int id1, int id2) throws TeclatTemporalBuitException, IndexosInvalidsException {
        controladorCapaPresentacio.swapKeysTeclat(id1, id2);
    }

    /** 
     * Modifica els símbols de l'alfabet del teclat temporal del Sistema. A més a més, 
     * es reemplacen els símbols de la disposició del teclat temporal del Sistema pels 
     * símbols seleccionats, i es col·loquen per files ordenats lexicogràficament. 
     * Conseqüentment s'adapten les dimensions del teclat temporal del Sistema a la nova
     * disposició tal que el ràtio amplada-alçada sigui 2.
     *
     * @param alfab El nom de l'alfabet del que extreure els símbols que tindrà el
     *                   teclat temporal. Si aquest nom coincideix amb {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}
     *                   selecciona els símbols que el teclat seleccionat tenia abans de cap modificació. 
     *                   Altrament selecciona els símbols de l'alfabet identificat per nom_alfabet.
     *
     * @throws NomNoExisteixException No existeix cap alfabet identificat pel nom donat.
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    void setAlfabetTeclat(String alfab) throws TeclatTemporalBuitException, NomNoExisteixException {
        controladorCapaPresentacio.setAlfabetTeclat(alfab);
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
    void generateLayoutTeclat(String nomAlgoritme) throws TeclatTemporalBuitException, MissingPairsFreqException, InvalidGeneratorAlgorithmException {
        controladorCapaPresentacio.generateLayoutTeclat(nomAlgoritme);
    }

    /**
     * Sobreescriu els parells de freqüències del conjunt de teclats amb uns de nous generats
     * a partir del text identificat pel nom donat i existent al Sistema.
     *
     * @param nomText Nom del text, existent al Sistema, del que obtenir els nous parells de freqüències.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap llista de freqüències identificada 
     *                                pel nom donat.
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     * @throws CaractersNoInclososException L'alfabet del teclat temporal del conjunt de teclats no 
     *                                      conté tots els símbols del text identificat pel nom donat.
     */
    void setFreqPairsByTextTeclat(String nomText) throws NomNoExisteixException, CaractersNoInclososException, TeclatTemporalBuitException {
        controladorCapaPresentacio.setFreqPairsByTextTeclat(nomText);
    }

    /**
     * Sobreescriu els parells de freqüència amb un de nou generat a partir de l’String d’entrada.
     *
     * @param nomLlistaFreq  Passa de parells de freqüència a string.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     * @throws CaractersNoInclososException L'alfabet del teclat temporal de cjtTeclats no conté
     * tots els símbols necessaris per a regenerar el teclat.
     * @throws TeclatTemporalBuitException No s’ha donat un valor al teclat temporal de cjtTeclats.
     */
    void setFreqPairsByFreqListTeclat(String nomLlistaFreq) throws NomNoExisteixException, CaractersNoInclososException, TeclatTemporalBuitException {
        controladorCapaPresentacio.setFreqPairsByFreqListTeclat(nomLlistaFreq);
    }

    /**
     * Retorna els noms dels textos existents al Sistema tals que tots els símbols   
     * que contenen estan presents al conjunt rebut com a paràmetre d'entrada.
     *
     * @return Una llista amb els noms dels textos tals que tots els  
     *         símbols que contenen estan presents al conjunt donat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     */
    ArrayList<String> getTextosCompatibles() throws TeclatTemporalBuitException {
        return controladorCapaPresentacio.getTextosCompatibles();
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
        return controladorCapaPresentacio.getLlistesFreqCompatibles();
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
        return controladorCapaPresentacio.getCostTeclat();
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
        return controladorCapaPresentacio.canSetBestLayoutTeclat();
    }

    /**
     * Sobreescriu el layout del teclat temporal del Sistema amb el millor trobat 
     * fins ara amb la resta de paràmetres del teclat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del Sistema.
     * @throws MissingBestLayoutException No hi ha un millor layout per al teclat temporal.
     */
    void setBestLayoutTeclat() throws TeclatTemporalBuitException, MissingBestLayoutException {
        controladorCapaPresentacio.setBestLayoutTeclat();
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
        controladorCapaPresentacio.guardarTeclat(nomTeclat);
        updateTeclats();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents

    /**
     * Inicialitza atributs i components bàsics de la interficie d'usuari del menú principal.
     */
    private void initComponents() {

        tabsTabbedPane = new javax.swing.JTabbedPane();
        keyboardsScrollPane = new javax.swing.JScrollPane();
        keyboardsPanel = new javax.swing.JPanel();
        alfabetsScrollPane = new javax.swing.JScrollPane();
        alfabetsPanel = new javax.swing.JPanel();
        freqListScrollPane = new javax.swing.JScrollPane();
        freqListPanel = new javax.swing.JPanel();
        textsScrollPane = new javax.swing.JScrollPane();
        textPanel = new javax.swing.JPanel();
        ButtonsPanel = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestor de Teclats");
        setMinimumSize(new java.awt.Dimension(598, 237));
        setPreferredSize(new java.awt.Dimension(598, 237));

        tabsTabbedPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 20, 0));
        tabsTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsTabbedPaneStateChanged(evt);
            }
        });

        keyboardsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        keyboardsPanel.setLayout(new javax.swing.BoxLayout(keyboardsPanel, javax.swing.BoxLayout.Y_AXIS));
        keyboardsScrollPane.setViewportView(keyboardsPanel);

        tabsTabbedPane.addTab("Teclats", keyboardsScrollPane);

        alfabetsPanel.setLayout(new javax.swing.BoxLayout(alfabetsPanel, javax.swing.BoxLayout.Y_AXIS));
        alfabetsScrollPane.setViewportView(alfabetsPanel);

        tabsTabbedPane.addTab("Alfabets", alfabetsScrollPane);

        freqListPanel.setLayout(new javax.swing.BoxLayout(freqListPanel, javax.swing.BoxLayout.Y_AXIS));
        freqListScrollPane.setViewportView(freqListPanel);

        tabsTabbedPane.addTab("Llistes de freqüències", freqListScrollPane);

        textPanel.setLayout(new javax.swing.BoxLayout(textPanel, javax.swing.BoxLayout.Y_AXIS));
        textsScrollPane.setViewportView(textPanel);

        tabsTabbedPane.addTab("Textos", textsScrollPane);

        getContentPane().add(tabsTabbedPane, java.awt.BorderLayout.CENTER);

        ButtonsPanel.setOpaque(false);

        createButton.setText("Crear");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        openButton.setText("Obrir");
        openButton.setEnabled(false);
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Eliminar");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        importButton.setText("Importar");
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        exportButton.setText("Exportar");
        exportButton.setEnabled(false);
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ButtonsPanelLayout = new javax.swing.GroupLayout(ButtonsPanel);
        ButtonsPanel.setLayout(ButtonsPanelLayout);
        ButtonsPanelLayout.setHorizontalGroup(
            ButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ButtonsPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(ButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(exportButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(importButton, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(openButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        ButtonsPanelLayout.setVerticalGroup(
            ButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ButtonsPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(createButton)
                .addGap(5, 5, 5)
                .addComponent(openButton)
                .addGap(5, 5, 5)
                .addComponent(deleteButton)
                .addGap(5, 5, 5)
                .addComponent(importButton)
                .addGap(5, 5, 5)
                .addComponent(exportButton)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        getContentPane().add(ButtonsPanel, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Events dels botons

    /**Funció que s’executa al clickar en el botó de crear de la interficie.
     * Farà la creació depenent de quin element s’ha seleccionat.
     *
     * @param evt Event de l'acció crear.
     */
    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        switch (tabsTabbedPane.getSelectedIndex()) {
            case 0:
                // Crear teclat
                if (controladorCapaPresentacio.getNomsAlfabets().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hi ha cap alfabet per crear un teclat.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                new CrearTeclatFrame(this, controladorCapaPresentacio.getNomsAlfabets(), controladorCapaPresentacio.getNomsAlgoritmes());
                setVisible(false);
                break;
            case 1:
                // Crear alfabet
                new CrearAlfabetFrame(this, controladorCapaPresentacio.getNomsLlistesFreq(), controladorCapaPresentacio.getNomsTextos());
                setVisible(false);
                break;
            case 2:
                // Crear llista de freqüències
                new CrearLlistaFreqFrame(this, controladorCapaPresentacio.getNomsTextos());
                setVisible(false);
                break;
            case 3:
                // Crear text
                new CrearTextFrame(this);
                setVisible(false);
                break;
        }
    }//GEN-LAST:event_createButtonActionPerformed

    /**Funció que s’executa al clickar en el botó d'obrir de la interficie.
     * Farà la edició depenent de que element s’ha seleccionat.
     *
     * @param evt Event de l'acció obrir.
     */
    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        switch (tabsTabbedPane.getSelectedIndex()) {
            case 0:
                // Obrir teclat
                try {
                    new EditarTeclatFrame(this, teclatSeleccionat, controladorCapaPresentacio.getNomsAlfabetsAmbOriginalTeclat(), controladorCapaPresentacio.getNomsAlgoritmes());
                    setVisible(false);
                } catch (NomNoExisteixException e) {
                    break;
                }
                break;
            case 1:
                // Obrir alfabet
                try {
                    new EditarAlfabetFrame(this, alfabetSeleccionat, controladorCapaPresentacio.getAlfabet(alfabetSeleccionat), controladorCapaPresentacio.getNomsLlistesFreq(), controladorCapaPresentacio.getNomsTextos());
                    setVisible(false);
                } catch (NomNoExisteixException e) {
                    break;
                }
                break;
            case 2:
                // Obrir llista de freqüències
                try {
                    new EditarLlistaFreqFrame(this, llistaFreqSeleccionada, controladorCapaPresentacio.getLlistaDeFrequencia(llistaFreqSeleccionada), controladorCapaPresentacio.getNomsTextos());
                    setVisible(false);
                } catch(NomNoExisteixException e) {
                    break;
                }
                break;
            case 3:
                // Obrir text
                try {
                    new EditarTextFrame(this, textSeleccionat, controladorCapaPresentacio.getCosText(textSeleccionat));
                    setVisible(false);
                } catch (NomNoExisteixException e) {
                    break;
                }
                break;
        }
    }//GEN-LAST:event_openButtonActionPerformed

    /**Funció que s’executa al clickar en el botó de esborrar de la interficie.
     * Esborra l’element seleccionat.
     *
     * @param evt Event de l'acció d'esborrar.
     */
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int confirmationResult;
        switch (tabsTabbedPane.getSelectedIndex()) {
            case 0:
                // Eliminar teclat
                confirmationResult = JOptionPane.showConfirmDialog(this, "Estàs segur que vols eliminar el teclat \"" + teclatSeleccionat + "\"?", "Confirmació", JOptionPane.YES_NO_OPTION);
                if (confirmationResult != JOptionPane.YES_OPTION) break;
                try {
                    controladorCapaPresentacio.eliminarTeclat(teclatSeleccionat);
                } catch (NomNoExisteixException e){
                    break;
                }
                updateTeclats();
                break;
            case 1:
                // Eliminar alfabet
                confirmationResult = JOptionPane.showConfirmDialog(this, "Estàs segur que vols eliminar l'alfabet \"" + alfabetSeleccionat + "\"?", "Confirmació", JOptionPane.YES_NO_OPTION);
                if (confirmationResult != JOptionPane.YES_OPTION) break;
                try {
                    controladorCapaPresentacio.eliminarAlfabet(alfabetSeleccionat);
                } catch (NomNoExisteixException e) {
                    break;
                }
                updateAlfabets();
                break;
            case 2:
                // Eliminar llista de freqüències
                confirmationResult = JOptionPane.showConfirmDialog(this, "Estàs segur que vols eliminar la llista de freqüències \"" + llistaFreqSeleccionada + "\"?", "Confirmació", JOptionPane.YES_NO_OPTION);
                if (confirmationResult != JOptionPane.YES_OPTION) break;
                try {
                    controladorCapaPresentacio.eliminarLlistaDeFrequencia(llistaFreqSeleccionada);
                } catch (NomNoExisteixException e) {
                    break;
                }
                updateLlistesFreq();
                break;
            case 3:
                // Eliminar text
                confirmationResult = JOptionPane.showConfirmDialog(this, "Estàs segur que vols eliminar el text \"" + textSeleccionat + "\"?", "Confirmació", JOptionPane.YES_NO_OPTION);
                if (confirmationResult != JOptionPane.YES_OPTION) break;
                try {
                    controladorCapaPresentacio.eliminarText(textSeleccionat);
                } catch (NomNoExisteixException e) {
                    break;
                }
                updateTextos();
                break;
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    /**Funció que s’executa al clickar en el botó de importar de la interficie.
     * Importa un .txt a la interficie.
     *
     * @param evt Event de l'acció d'importar.
     */
    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int result;
        Path path;
        FileNameExtensionFilter filter;
        switch (tabsTabbedPane.getSelectedIndex()) {
            case 0:
                // Importar teclat
                fileChooser.setDialogTitle("Escull el fitxer que contingui el teclat");
                filter = new FileNameExtensionFilter("Fitxers de teclat (*.tcl)", "tcl");
                fileChooser.setFileFilter(filter);
                result = fileChooser.showOpenDialog(this);
                if (result != JFileChooser.APPROVE_OPTION) break;
                path = Paths.get(fileChooser.getSelectedFile().toURI());
                try {
                    controladorCapaPresentacio.importarTeclat(path);
                    updateTeclats();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Hi ha agut un problema al llegir el fitxer.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomJaExisteixException e) {
                    JOptionPane.showMessageDialog(this, "Ja existeix un teclat amb el nom \"" + e.getNom() + "\".", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidFileException e) {
                    JOptionPane.showMessageDialog(this, "El fitxer no és un fitxer de teclat (no té l'extensió .tcl).", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(this, "El fitxer no té el format correcte.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 1:
                // Importar alfabet
                fileChooser.setDialogTitle("Escull el fitxer que contingui l'alfabet");
                filter = new FileNameExtensionFilter("Fitxers de text (*.txt)", "txt");
                fileChooser.setFileFilter(filter);
                result = fileChooser.showOpenDialog(this);
                if (result != JFileChooser.APPROVE_OPTION) break;
                path = Paths.get(fileChooser.getSelectedFile().toURI());
                try {
                    controladorCapaPresentacio.importarAlfabet(path);
                    updateAlfabets();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Hi ha hagut un problema al llegir el fitxer.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomJaExisteixException e) {
                    JOptionPane.showMessageDialog(this, "Ja existeix un alfabet amb el nom \"" + fileChooser.getSelectedFile().getName().toString().replace(".txt", "") + "\".", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidFileException e) {
                    JOptionPane.showMessageDialog(this, "El fitxer no és un fitxer de text (no té l'extensió .txt).", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SimbolInvalidException e) {
                    JOptionPane.showMessageDialog(this, "Hi ha un o més símbols invàlids al fitxer proporcionat.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SimbolRepetitException e) {
                    JOptionPane.showMessageDialog(this, "Hi ha un o més símbols repetits al fitxer proporcionat.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumSimbolsInvalidException e) {
                    JOptionPane.showMessageDialog(this, "L'alfabet ha de tenir entre 1 i 100 símbols diferents.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomMassaLlargException e) {
                    JOptionPane.showMessageDialog(this, "El nom de l'alfabet és massa llarg.\nEl màxim permès és 100 caràcters.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomProhibitException e) {
                    JOptionPane.showMessageDialog(this, "El nom donat és \"Alfabet original del teclat\", que està prohibit.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 2:
                // Importar llista de freqüències
                fileChooser.setDialogTitle("Escull el fitxer que contingui la llista de freqüències");
                filter = new FileNameExtensionFilter("Fitxers de text (*.txt)", "txt");
                fileChooser.setFileFilter(filter);
                result = fileChooser.showOpenDialog(this);
                if (result != JFileChooser.APPROVE_OPTION) break;
                path = Paths.get(fileChooser.getSelectedFile().toURI());
                try {
                    controladorCapaPresentacio.importarLlistaDeFrequencia(path);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Hi ha hagut un problema al llegir el fitxer.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomJaExisteixException e) {
                    JOptionPane.showMessageDialog(this, "Ja existeix una llista de freqüències amb el nom \"" + fileChooser.getSelectedFile().getName().toString().replace(".txt", "") + "\".", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidFormatException e) {
                    JOptionPane.showMessageDialog(this, "El fitxer no té el format correcte.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidFileException e) {
                    JOptionPane.showMessageDialog(this, "El fitxer no és un fitxer de text (no té l'extensió .txt).", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomMassaLlargException e) {
                    JOptionPane.showMessageDialog(this, "El nom de la llista de freqüència és massa llarg.\nEl màxim permès és 100 caràcters.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                updateLlistesFreq();
                break;
            case 3:
                // Importar text
                fileChooser.setDialogTitle("Escull un fitxer de text");
                filter = new FileNameExtensionFilter("Fitxers de text (*.txt)", "txt");
                fileChooser.setFileFilter(filter);
                result = fileChooser.showOpenDialog(this);
                if (result != JFileChooser.APPROVE_OPTION) break;
                path = Paths.get(fileChooser.getSelectedFile().toURI());
                try {
                    controladorCapaPresentacio.importarText(path);
                    updateTextos();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Hi ha hagut un problema al llegir el fitxer.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomJaExisteixException e) {
                    JOptionPane.showMessageDialog(this, "Ja existeix un text amb el nom \"" + fileChooser.getSelectedFile().getName().toString().replace(".txt", "") + "\".", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (TextEstaBuitException e) {
                    JOptionPane.showMessageDialog(this, "El fitxer està buit.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidFileException e) {
                    JOptionPane.showMessageDialog(this, "El fitxer no és un fitxer de text (no té l'extensió .txt).", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumSimbolsInvalidException e) {
                    JOptionPane.showMessageDialog(this, "El text no pot contenir més de 100 símbols diferents.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomMassaLlargException e) {
                    JOptionPane.showMessageDialog(this, "El nom del text és massa llarg.\nEl màxim permès és 100 caràcters.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }//GEN-LAST:event_importButtonActionPerformed

    /**Funció que s’executa al clickar en el botó de exportar de la interficie.
     * Exporta un .txt a un directori local.
     *
     * @param evt Event de l'acció d'exportar.
     */
    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int result;
        Path path;
        switch (tabsTabbedPane.getSelectedIndex()) {
            case 0:
                // Exportar teclat
                if(!controladorCapaPresentacio.existeixTeclat(teclatSeleccionat)) break;
                fileChooser.setDialogTitle("Escull on guardar el teclat");
                fileChooser.setSelectedFile(new java.io.File(teclatSeleccionat + ".tcl"));
                result = fileChooser.showSaveDialog(this);
                if (result != JFileChooser.APPROVE_OPTION) break;
                path = Paths.get(fileChooser.getSelectedFile().toURI());
                try {
                    controladorCapaPresentacio.exportarTeclat(teclatSeleccionat, path);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Hi ha hagut un problema al escriure el fitxer.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomNoExisteixException e) {
                    // No pot passar
                    e.printStackTrace();
                }
                break;
            case 1:
                // Exportar alfabet
                if(!controladorCapaPresentacio.existeixAlfabet(alfabetSeleccionat)) break;
                fileChooser.setDialogTitle("Escull on guardar l'alfabet");
                fileChooser.setSelectedFile(new java.io.File(alfabetSeleccionat + ".txt"));
                result = fileChooser.showSaveDialog(this);
                if (result != JFileChooser.APPROVE_OPTION) break;
                path = Paths.get(fileChooser.getSelectedFile().toURI());
                try {
                    controladorCapaPresentacio.exportarAlfabet(alfabetSeleccionat, path);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Hi ha hagut un problema al escriure el fitxer.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomNoExisteixException e) {
                    // No pot passar
                    e.printStackTrace();
                }
                break;
            case 2:
                // Exportar llista de freqüències
                if(!controladorCapaPresentacio.existeixLlistaDeFrequencia(llistaFreqSeleccionada)) break;
                fileChooser.setDialogTitle("Escull on guardar la llista de freqüències");
                fileChooser.setSelectedFile(new java.io.File(llistaFreqSeleccionada + ".txt"));
                result = fileChooser.showSaveDialog(this);
                if (result != JFileChooser.APPROVE_OPTION) break;
                path = Paths.get(fileChooser.getSelectedFile().toURI());
                try {
                    controladorCapaPresentacio.exportarLlistaDeFrequencia(llistaFreqSeleccionada, path);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Hi ha hagut un problema al escriure el fitxer.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomNoExisteixException e) {
                    // No pot passar
                    e.printStackTrace();
                }
                break;
            case 3:
                // Exportar text
                if(!controladorCapaPresentacio.existeixText(textSeleccionat)) break;
                fileChooser.setDialogTitle("Escull on guardar el fitxer de text");
                fileChooser.setSelectedFile(new java.io.File(textSeleccionat + ".txt"));
                result = fileChooser.showSaveDialog(this);
                if (result != JFileChooser.APPROVE_OPTION) break;
                path = Paths.get(fileChooser.getSelectedFile().toURI());
                try {
                    controladorCapaPresentacio.exportarText(textSeleccionat, path);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Hi ha hagut un problema al escriure el fitxer.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NomNoExisteixException e) {
                    // No pot passar
                    e.printStackTrace();
                }
                break;
        }
    }//GEN-LAST:event_exportButtonActionPerformed

    /**Funció que actualitza els botons d'editar
     *
     * @param evt Event de l'acció d'exportar.
     */
    private void tabsTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsTabbedPaneStateChanged
        updateEditButtons();
    }//GEN-LAST:event_tabsTabbedPaneStateChanged

    // Funcions privades

    /**Actualitza la llista de teclats a partir del domini.*/
    private void updateTeclats() {
        ArrayList<String> nomsTeclats = controladorCapaPresentacio.getNomsTeclats();
        keyboardsPanel.removeAll();
        for (String nom : nomsTeclats) {
            VistaPrincipalElemPanel elemPanel = new VistaPrincipalElemPanel(nom);
            elemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    teclatSeleccionat = nom;
                    updateTeclatSeleccionat();
                }
            });
            elemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        try {
                            new EditarTeclatFrame(VistaPrincipalFrame.this, teclatSeleccionat, controladorCapaPresentacio.getNomsAlfabetsAmbOriginalTeclat(), controladorCapaPresentacio.getNomsAlgoritmes());
                            setVisible(false);
                        } catch (NomNoExisteixException e) {
                            // No hauria de passar
                            e.printStackTrace();
                        }
                    }
                }
            });
            keyboardsPanel.add(elemPanel);
        }
        keyboardsPanel.revalidate();
        keyboardsPanel.repaint();
        updateTeclatSeleccionat();
    }

    /**Actualitza la llista d'alfabets a partir del domini.*/
    private void updateAlfabets() {
        ArrayList<String> nomsAlfabets = controladorCapaPresentacio.getNomsAlfabets();
        alfabetsPanel.removeAll();
        for (String nom : nomsAlfabets) {
            VistaPrincipalElemPanel elemPanel = new VistaPrincipalElemPanel(nom);
            elemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    alfabetSeleccionat = nom;
                    updateAlfabetSeleccionat();
                }
            });
            elemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        try {
                            new EditarAlfabetFrame(VistaPrincipalFrame.this, alfabetSeleccionat, controladorCapaPresentacio.getAlfabet(alfabetSeleccionat), controladorCapaPresentacio.getNomsLlistesFreq(), controladorCapaPresentacio.getNomsTextos());
                            setVisible(false);
                        } catch (NomNoExisteixException e) {
                            // No hauria de passar
                            e.printStackTrace();
                        }
                    }
                }
            });
            alfabetsPanel.add(elemPanel);
        }
        alfabetsPanel.revalidate();
        alfabetsPanel.repaint();
        updateAlfabetSeleccionat();
    }

    /**Actualitza la llista de llistes de freqüències a partir del domini.*/
    private void updateLlistesFreq() {
        ArrayList<String> nomsLlistesFreq = controladorCapaPresentacio.getNomsLlistesFreq();
        freqListPanel.removeAll();
        for (String nom : nomsLlistesFreq) {
            VistaPrincipalElemPanel elemPanel = new VistaPrincipalElemPanel(nom);
            elemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    llistaFreqSeleccionada = nom;
                    updateLlistaFreqSeleccionada();
                }
            });
            elemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        try {
                            new EditarLlistaFreqFrame(VistaPrincipalFrame.this, llistaFreqSeleccionada, controladorCapaPresentacio.getLlistaDeFrequencia(llistaFreqSeleccionada), controladorCapaPresentacio.getNomsTextos());
                            setVisible(false);
                        } catch (NomNoExisteixException e) {
                            // No hauria de passar
                            e.printStackTrace();
                        }
                    }
                }
            });
            freqListPanel.add(elemPanel);
        }
        freqListPanel.revalidate();
        freqListPanel.repaint();
        updateLlistaFreqSeleccionada();
    }

    /**Actualitza la llista que conté els noms dels textos existents al Sistema.*/
    private void updateTextos() {
        ArrayList<String> nomsTextos = controladorCapaPresentacio.getNomsTextos();
        textPanel.removeAll();
        for (String nom : nomsTextos) {
            VistaPrincipalElemPanel elemPanel = new VistaPrincipalElemPanel(nom);
            elemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    textSeleccionat = nom;
                    updateTextSeleccionat();
                }
            });
            elemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        try {
                            new EditarTextFrame(VistaPrincipalFrame.this, textSeleccionat, controladorCapaPresentacio.getCosText(textSeleccionat));
                            setVisible(false);
                        } catch (NomNoExisteixException e) {
                            // No hauria de passar
                            e.printStackTrace();
                        }
                    }
                }
            });
            textPanel.add(elemPanel);
        }
        textPanel.revalidate();
        textPanel.repaint();
        updateTextSeleccionat();
    }

    /**Actualitza els atributs d’un teclat seleccionat.*/
    private void updateTeclatSeleccionat() {
        if(!controladorCapaPresentacio.existeixTeclat(teclatSeleccionat)) {
            teclatSeleccionat = "";
            updateEditButtons();
            return;
        }
        for (int i = 0; i < keyboardsPanel.getComponentCount(); ++i) {
            VistaPrincipalElemPanel elemPanel = (VistaPrincipalElemPanel) keyboardsPanel.getComponent(i);
            if (elemPanel.getNom().equals(teclatSeleccionat)) {
                elemPanel.setSelected(true);
            } else {
                elemPanel.setSelected(false);
            }
        }
        updateEditButtons();
    }

    /**Actualitza els atributs d’un alfabet seleccionat.*/
    private void updateAlfabetSeleccionat() {
        if(!controladorCapaPresentacio.existeixAlfabet(alfabetSeleccionat)) {
            alfabetSeleccionat = "";
            updateEditButtons();
            return;
        }
        for (int i = 0; i < alfabetsPanel.getComponentCount(); ++i) {
            VistaPrincipalElemPanel elemPanel = (VistaPrincipalElemPanel) alfabetsPanel.getComponent(i);
            if (elemPanel.getNom().equals(alfabetSeleccionat)) {
                elemPanel.setSelected(true);
            } else {
                elemPanel.setSelected(false);
            }
        }
        alfabetsPanel.revalidate();
        alfabetsPanel.repaint();
        updateEditButtons();
    }

    /**Actualitza els atributs d’una llista de freqüència seleccionada.*/
    private void updateLlistaFreqSeleccionada() {
        if(!controladorCapaPresentacio.existeixLlistaDeFrequencia(llistaFreqSeleccionada)) {
            llistaFreqSeleccionada = "";
            updateEditButtons();
            return;
        }
        for (int i = 0; i < freqListPanel.getComponentCount(); ++i) {
            VistaPrincipalElemPanel elemPanel = (VistaPrincipalElemPanel) freqListPanel.getComponent(i);
            if (elemPanel.getNom().equals(llistaFreqSeleccionada)) {
                elemPanel.setSelected(true);
            } else {
                elemPanel.setSelected(false);
            }
        }
        freqListPanel.revalidate();
        freqListPanel.repaint();
        updateEditButtons();
    }

    /**Actualitza els atributs d’un text seleccionat.*/
    private void updateTextSeleccionat() {
        if(!controladorCapaPresentacio.existeixText(textSeleccionat)) {
            textSeleccionat = "";
            updateEditButtons();
            return;
        }
        for (int i = 0; i < textPanel.getComponentCount(); ++i) {
            VistaPrincipalElemPanel elemPanel = (VistaPrincipalElemPanel) textPanel.getComponent(i);
            if (elemPanel.getNom().equals(textSeleccionat)) {
                elemPanel.setSelected(true);
            } else {
                elemPanel.setSelected(false);
            }
        }
        textPanel.revalidate();
        textPanel.repaint();
        updateEditButtons();
    }

    /**Activa els botons d'obrir, esborrar i exportar quan es selecciona un element.*/
    private void enableEditButtons() {
        openButton.setEnabled(true);
        deleteButton.setEnabled(true);
        exportButton.setEnabled(true);
    }

    /**Desactiva els botons d'obrir, esborrar i exportar quan no hi ha un element seleccionat.*/
    private void disableEditButtons() {
        openButton.setEnabled(false);
        deleteButton.setEnabled(false);
        exportButton.setEnabled(false);
    }

    /**Actualitzar si un element està o no seleccionat per activar o descativar
     * els botons d'obrir, esborrar i exportar.*/
    private void updateEditButtons() {
        switch (tabsTabbedPane.getSelectedIndex()) {
            case 0:
                if(teclatSeleccionat.equals("")) {
                    disableEditButtons();
                } else {
                    enableEditButtons();
                }
                break;
            case 1:
                if(alfabetSeleccionat.equals("")) {
                    disableEditButtons();
                } else {
                    enableEditButtons();
                }
                break;
            case 2:
                if(llistaFreqSeleccionada.equals("")) {
                    disableEditButtons();
                } else {
                    enableEditButtons();
                }
                break;
            case 3:
                if(textSeleccionat.equals("")) {
                    disableEditButtons();
                } else {
                    enableEditButtons();
                }
                break;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonsPanel;
    private javax.swing.JPanel alfabetsPanel;
    private javax.swing.JScrollPane alfabetsScrollPane;
    private javax.swing.JButton createButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JPanel freqListPanel;
    private javax.swing.JScrollPane freqListScrollPane;
    private javax.swing.JButton importButton;
    private javax.swing.JPanel keyboardsPanel;
    private javax.swing.JScrollPane keyboardsScrollPane;
    private javax.swing.JButton openButton;
    private javax.swing.JTabbedPane tabsTabbedPane;
    private javax.swing.JPanel textPanel;
    private javax.swing.JScrollPane textsScrollPane;
    // End of variables declaration//GEN-END:variables
}
