package edu.upc.prop.teclat.domini;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import java.nio.file.Path;

import edu.upc.prop.teclat.dades.GestorAlfabets;
import edu.upc.prop.teclat.dades.GestorLlistesFreq;
import edu.upc.prop.teclat.dades.GestorTeclats;
import edu.upc.prop.teclat.dades.GestorTextos;
import edu.upc.prop.teclat.dades.exceptions.InvalidFileException;
import edu.upc.prop.teclat.dades.exceptions.InvalidFormatException;
import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.ParaulaBuidaException;
import edu.upc.prop.teclat.domini.exceptions.ParaulaRepetidaException;
import edu.upc.prop.teclat.domini.exceptions.InvalidFrequencyException;
import edu.upc.prop.teclat.domini.exceptions.InvalidGeneratorAlgorithmException;
import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomJaExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NomNoExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomProhibitException;
import edu.upc.prop.teclat.domini.exceptions.NombreElementsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.SimbolInvalidException;
import edu.upc.prop.teclat.domini.exceptions.SimbolRepetitException;
import edu.upc.prop.teclat.domini.exceptions.TextEstaBuitException;
import edu.upc.prop.teclat.domini.exceptions.teclat.CaractersNoInclososException;
import edu.upc.prop.teclat.domini.exceptions.teclat.IndexosInvalidsException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingBestLayoutException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingPairsFreqException;
import edu.upc.prop.teclat.domini.exceptions.teclat.TeclatTemporalBuitException;
import edu.upc.prop.teclat.domini.generatoralgorithms.GeneratorAlgorithm;
import edu.upc.prop.teclat.domini.generatoralgorithms.hillclimbing.problems.QAPHillClimbing;
import edu.upc.prop.teclat.domini.generatoralgorithms.qap.GreedyGenerator;
import edu.upc.prop.teclat.domini.generatoralgorithms.qap.branchandbound.BranchAndBoundGenerator;
import edu.upc.prop.teclat.util.Pair;

/**
 * Gestiona la comunicació entre la capa de domini i les capes de persistència i presentació.
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public class ControladorCapaDomini {
    //Constants
    /** Algoritme Branch&amp;Bound per generar el teclat*/
    private final String algorisme_BnB = "Branch&Bound";

    /** Algoritme Hill Climbing per generar el teclat*/
    private final String algorisme_HC = "Hill Climbing";

    /** Algoritme Greedy per generar el teclat*/
    private final String algorisme_Greedy = "Greedy";

    /** Estructura on emmagatzemar les associacions d'algorismes generadors amb els seus noms*/
    private final LinkedHashMap<String, GeneratorAlgorithm> algorismes = new LinkedHashMap<String, GeneratorAlgorithm>() {{
        put(algorisme_HC,       new QAPHillClimbing());
        put(algorisme_Greedy,   new GreedyGenerator());
        put(algorisme_BnB,      new BranchAndBoundGenerator());
    }};

    //Atributs
    /** Instància per la persistència de teclats*/
    GestorTeclats gestorTeclats;

    /** Instància per la persistència d'alfabets*/
    GestorAlfabets gestorAlfabets;

    /** Instància per la persistència de llistes de freqüència*/
    GestorLlistesFreq gestorLlistesFreq;

    /** Instància per la persistència de textos*/
    GestorTextos gestorTextos;

    /** Instància pels conjunts d'alfabets */
    CjtAlfabets cjtAlfabets;

    /** Instància pels conjunts de teclats */
    CjtTeclats cjtTeclats;

    /** Instància pels conjunts de llistes de freqüència */
    CjtLlistesDeFrequencia cjtLlistesDeFrequencia;

    /** Instància pels conjunts de textos */
    CjtTextos cjtTextos;

    // Constructora
    /** Constructora per defecte
     *	Inicialitza totes les instàncies dels conjunts i de les classes de persistència.
     */
    public ControladorCapaDomini() {
        cjtAlfabets = new CjtAlfabets();
        cjtTeclats = new CjtTeclats();
        cjtLlistesDeFrequencia = new CjtLlistesDeFrequencia();
        cjtTextos = new CjtTextos();

        gestorTeclats = new GestorTeclats(this);
        gestorTeclats.carregar();
        gestorAlfabets = new GestorAlfabets(this);
        gestorAlfabets.carregar();
        gestorLlistesFreq = new GestorLlistesFreq(this);
        gestorLlistesFreq.carregar();
        gestorTextos = new GestorTextos(this);
        gestorTextos.carregar();
    }

    /**
     * Sobreescriu el teclat temporal del conjunt de teclats amb un de nou generat a partir dels 
     * símbols que conté l'alfabet identificat per {@code nomAlfabet}, i redimensiona el teclat 
     * temporal perquè tingui ràtio amplada-alçada de 2.
     *
     * @param nomAlfabet Nom de l'alfabet, existent al Sistema, del que obtenir els símbols.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom donat.
     */
    public void crearTeclat(String nomAlfabet) throws NomNoExisteixException { 
        String s = cjtAlfabets.getSimbolsAsString(nomAlfabet);
        try {
            cjtTeclats.make(s);
        } catch (NumSimbolsInvalidException | SimbolRepetitException | SimbolInvalidException e) {
            // No hauria de passar mai
            e.printStackTrace();
        }
    }

    /**
     * Afegeix el teclat donat al conjunt de teclats.
     *
     * @param teclat La instància de teclat que es vol afegir.
     *
     * @throws NomJaExisteixException Ja existeix un teclat amb aquest nom.
     */
    public void afegirTeclat(Teclat teclat) throws NomJaExisteixException {
        cjtTeclats.add(teclat);
        gestorTeclats.guardarCanvis(teclat.getNom());
    }

    /**
     * Obté el teclat identificat pel nom donat.
     *
     * @param nom Nom del teclat que es vol obtenir.
     *
     * @return El teclat identificat pel nom donat (en cas d'existir).
     *
     * @throws NomNoExisteixException No existeix cap teclat identificat pel nom donat.
     */
    public Teclat getTeclat(String nom) throws NomNoExisteixException {
        return cjtTeclats.get(nom);
    }

    /**
     * Carrega al teclat temporal un teclat existent dins del conjunt de teclats.
     *
     * @param nomTeclat Nom que identifica al teclat, existent al Sistema, que es vol carregar.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap teclat identificat pel nom donat.
     */
    public void carregarTeclat(String nomTeclat) throws NomNoExisteixException {
        cjtTeclats.load(nomTeclat);
    }

    /**
     * Guarda un nou teclat amb les dades del teclat temporal, i que s'identificarà pel nom nouNom, 
     * dins del conjunt de teclats.
     *
     * @param nomNou Nom amb què es guardarà el nou teclat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     * @throws NomJaExisteixException Ja existeix al Sistema un teclat identificat pel nom donat.
     * @throws NomNoExisteixException S'està intentant modificar un teclat que no existeix.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws NomBuitException El nom del teclat no pot estar buit.
     */
    public void guardarTeclat(String nomNou) throws TeclatTemporalBuitException, NomJaExisteixException, NomNoExisteixException, NomBuitException, NomMassaLlargException {
        String nomAntic = cjtTeclats.getNom();
        cjtTeclats.store(nomNou);
        gestorTeclats.guardarCanvis(nomAntic);
        gestorTeclats.guardarCanvis(nomNou);
    }



// Obtenir noms

    /**
     * Retorna una llista que conté els noms (en ordre lexicogràfic) dels teclats guardats al Sistema.
     *
     * @return Una llista que conté els noms (en ordre lexicogràfic) dels teclats guardats al Sistema.
     */
    public ArrayList<String> getNomsTeclats() {
        return cjtTeclats.getNoms();
    }

    /**
     * Retorna una llista que conté els noms (en ordre lexicogràfic) dels alfabets guardats al Sistema.
     *
     * @return Una llista que conté els noms (en ordre lexicogràfic) dels alfabets guardats al Sistema.
     */
    public ArrayList<String> getNomsAlfabets() {
        return cjtAlfabets.getNoms();
    }

    /**
     * Retorna una llista que conté els noms (en ordre lexicogràfic) dels alfabets guardats al Sistema,
     * i afegeix com a primer element el nom clau {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}.
     *
     * @return Una llista que conté els noms (en ordre lexicogràfic) dels alfabets guardats al Sistema,
     *         i afegint com a primer element el nom clau {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}.
     */
    public ArrayList<String> getNomsAlfabetsAmbOriginalTeclat() {
        ArrayList<String> noms = new ArrayList<>();
        noms.add(Alfabet.KEYBOARD_ORIGINAL_ALPHABET);
        noms.addAll(cjtAlfabets.getNoms());
        return noms;
    }

    /**
     * Retorna una llista que conté els noms (en ordre lexicogràfic) de les llistes de freqüències 
     * guardades al Sistema.
     *
     * @return Una llista que conté els noms (en ordre lexicogràfic) de les llistes de freqüències
     *         guardades al Sistema.
     */
    public ArrayList<String> getNomsLlistesDeFrequencia() {
        return cjtLlistesDeFrequencia.getNoms();
    }

    /**
     * Retorna una llista que conté els noms (en ordre lexicogràfic) dels textos guardats al Sistema.
     *
     * @return Una llista que conté els noms (en ordre lexicogràfic) dels textos guardats al Sistema.
     */
    public ArrayList<String> getNomsTextos() {
        return cjtTextos.getNoms();
    }

    /**
     * Retorna una llista que conté els noms dels algorismes que es poden cridar per a
     * regenerar la disposició d'un teclat.
     *
     * @return Una llista que conté els noms dels algorismes que es poden cridar per a
     *         regenerar la disposició d'un teclat.
     */
    public final ArrayList<String> getNomsAlgoritmes() {
        ArrayList<String> nomsAlgorismes = new ArrayList<String>(algorismes.keySet());
        return nomsAlgorismes;
    }



    // Comprovar si un element existeix

    /**
     * Retorna si existeix un teclat amb el nom especificat.
     *
     * @param nomTeclat Nom del teclat que es vol verificar si existeix.
     *
     * @return True si existeix el teclat amb el nom donat.
     *         Altrament, false.
     */
    public boolean existeixTeclat(String nomTeclat) {
        return cjtTeclats.exists(nomTeclat);
    }

    /**
     * Retorna si existeix un alfabet amb el nom especificat.
     *
     * @param nomAlfabet Nom de l'alfabet que es vol verificar si existeix.
     *
     * @return True si existeix l'alfabet amb el nom donat.
     *         Altrament, false.
     */
    public boolean existeixAlfabet(String nomAlfabet) {
        return cjtAlfabets.exists(nomAlfabet);
    }

    /**
     * Retorna si existeix una llista de freqüències amb el nom especificat.
     *
     * @param nomLlistaDeFrequencia Nom de la llista de freqüències que es vol verificar si existeix.
     *
     * @return True si existeix la llista de freqüències amb el nom donat.
     *         Altrament, false.
     */
    public boolean existeixLlistaDeFrequencia(String nomLlistaDeFrequencia) {
        return cjtLlistesDeFrequencia.exists(nomLlistaDeFrequencia);
    }

    /**
     * Retorna si existeix un text amb el nom especificat.
     *
     * @param nomText Nom del text que es vol verificar si existeix.
     *
     * @return True si existeix el text amb el nom donat.
     *         Altrament, false.
     */
    public boolean existeixText(String nomText) {
        return cjtTextos.exists(nomText);
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
     * @throws NomJaExisteixException Ja existeix al Sistema un teclat identificat   
     *                                pel nom de l'alfabet que es vol importar.
     */
    public void importarTeclat(Path path) throws ClassNotFoundException, InvalidFileException, IOException, NomJaExisteixException {
        gestorTeclats.importar(path);
    }

    /**
     * Importa un alfabet indicant el path del seu fitxer.
     *
     * @param path Path de l'alfabet a importar.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomJaExisteixException Ja existeix al Sistema un alfabet identificat pel nom de l'alfabet
     *                                que es vol importar.
     * @throws NomMassaLlargException El de l'alfabet importat té més de {@value Alfabet#MAX_NAME_LENGTH}
     *                                caràcters.
     * @throws InvalidFileException El fitxer no té l'extensió correcta.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'alfabet que es vol importar.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos l'alfabet que es vol importar.
     * @throws NumSimbolsInvalidException L'alfabet que es vol importar conté més de 
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} caràcters diferents.
     * @throws NomProhibitException El nom de l'alfabet que es vol importar és 
     *                              {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}, que està prohibit.
     */
    public void importarAlfabet(Path path) throws IOException, NomJaExisteixException, NomMassaLlargException, InvalidFileException, SimbolInvalidException, SimbolRepetitException, NumSimbolsInvalidException, NomProhibitException {
        gestorAlfabets.importar(path);
    }

    /**
     * Importa una llista de freqüències indicant el path del seu fitxer.
     *
     * @param path Path de la llista de freqüències a importar.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomJaExisteixException Ja existeix una llista de freqüències amb aquest nom.
     * @throws NomMassaLlargException El nom donat té més de {@value LlistaDeFrequencia#MAX_NAME_LENGTH} 
     *                                caràcters.
     * @throws InvalidFormatException El fitxer no té el format correcte.
     * @throws InvalidFileException El fitxer no té l'extensió correcta.
     */
    public void importarLlistaFrequencia(Path path) throws IOException, InvalidFormatException, NomMassaLlargException, NomJaExisteixException, InvalidFileException {
        gestorLlistesFreq.importar(path);
    }

    /**
     * Importa un text indicant el path del seu fitxer.
     *
     * @param path Path del text a importar.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomJaExisteixException Ja existeix al Sistema un text identificat pel nom del
     *                                text a importar.
     * @throws NomMassaLlargException El nom del text a importar té més de {@value Text#MAX_NAME_LENGTH} 
     *                                caràcters.
     * @throws TextEstaBuitException El text a importar està buit.
     * @throws InvalidFileException El fitxer no té l'extensió correcta.
     * @throws NumSimbolsInvalidException El text a importar conté més de 
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} caràcters diferents.
     */
    public void importarText(Path path) throws IOException, NomMassaLlargException, NomJaExisteixException, TextEstaBuitException, InvalidFileException, NumSimbolsInvalidException {
        gestorTextos.importar(path);
    }


    // Exportar elements

    /**
     * Exporta un teclat identificat pel nom donat al path indicat.
     *
     * @param nom Nom del teclat, existent dins del Sistema, a exportar.
     * @param path Path a on es vol exportar el teclat identificat pel nom donat.
     * 
     * @throws IOException S'ha produït un error en l'entrada/sortida del programa.
     * @throws NomNoExisteixException No existeix al Sistema cap teclat identificat pel nom donat.
     */
    public void exportarTeclat(String nom, Path path) throws IOException, NomNoExisteixException {
        Teclat teclat = cjtTeclats.get(nom);
        gestorTeclats.exportar(path, teclat);
    }

    /**
     * Exporta un alfabet existent dins del Sistema i identificat pel nom donat al path indicat.
     *
     * @param nom Nom de l'alfabet, existent dins del Sistema, a exportar.
     * @param path Path a on es vol exportar l'alfabet identificat pel nom donat.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom donat.
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     */
    public void exportarAlfabet(String nom, Path path) throws NomNoExisteixException, IOException {
        String contingut = cjtAlfabets.getSimbolsAsString(nom);
        gestorAlfabets.exportar(path, contingut);
    }

    /**
     * Exporta una llista de freqüències existent dins del Sistema i identificada pel nom donat
     * al path indicat.
     *
     * @param nom Nom de la llista de freqüències, existent al Sistema, a exportar.
     * @param path Path a on es vol exportar la llista de freqüències identificada pel nom donat.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap llista de freqüències identificada 
     *                                pel nom donat.
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     */
    public void exportarLlistaFrequencia(String nom, Path path) throws NomNoExisteixException, IOException {
        ArrayList<Pair<String, Integer>> contingut = cjtLlistesDeFrequencia.getContingutAsPairs(nom);
        gestorLlistesFreq.exportar(path, contingut);
    }

    /**
     * Exporta un text existent dins del Sistema i identificat pel nom donat al path indicat.
     *
     * @param nom Nom del text a exportar.
     * @param path Path a on es vol exportar el text identificat pel nom donat.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     */
    public void exportarText(String nom, Path path) throws NomNoExisteixException, IOException {
        String contingut = cjtTextos.getCos(nom);
        gestorTextos.exportar(path, contingut);
    }

    

// Afegir elements

    /**
     * Crea un nou text a partir dels paràmetres d'entrada i l'afegeix al conjunt de textos.
     *
     * @param nom Nom del nou text a afegir.
     * @param contingut Una seqüència de caràcters que representa el cos del nou text a afegir.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema un alfabet identificat pel nom donat.
     * @throws NomMassaLlargException El nom donat té més de {@value Text#MAX_NAME_LENGTH} caràcters.
     * @throws TextEstaBuitException La seqüència d’entrada està buida.
     * @throws NumSimbolsInvalidException La seqüència d’entrada conté més de 
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} caràcters diferents.
     */
    public void crearText(String nom, String contingut) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, TextEstaBuitException, NumSimbolsInvalidException {
        cjtTextos.add(nom, contingut);
        gestorTextos.guardarCanvis(nom);
    }


    /**
     * Crea un nou alfabet a partir dels paràmetres d'entrada i l'afegeix al conjunt d'alfabets.
     *
     * @param nom Nom del nou alfabet a crear.
     * @param simbols Seqüència de símbols que indica els que tindrà el nou alfabet a crear.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema un alfabet identificat pel nom donat.
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
    public void crearAlfabet(String nom, String simbols) throws NomBuitException, NomJaExisteixException, NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException, NomMassaLlargException, NomProhibitException {
        cjtAlfabets.add(nom, simbols);
        gestorAlfabets.guardarCanvis(nom);
    }


    /**
     * Crea una nova llista de freqüències a partir dels paràmetres d'entrada i l'afegeix al conjunt
     * de llistes de freqüències.
     *
     * @param nom Nom de la nova llista de freqüències a afegir.
     * @param contingut Seqüència de parells &lt;paraula, freqüència&gt; de la que
     *                  obtenir les associacions paraula-freqüència.
     *
     * @throws NomJaExisteixException Ja existeix al Sistema una llista de freqüències 
     *                                identificada per {@code new_nom}.
     * @throws NomBuitException El nom de la llista a afegir no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value LlistaDeFrequencia#MAX_NAME_LENGTH} 
     *                                caràcters.
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
    public void crearLlistaFrequencia(String nom, ArrayList<Pair<String, Integer>> contingut) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, NombreElementsInvalidException, InvalidFrequencyException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException {
        cjtLlistesDeFrequencia.add(nom, contingut);
        gestorLlistesFreq.guardarCanvis(nom);
    }


    // Esborrar elements

    /**
     * Elimina del conjunt de textos aquell identificat pel nom donat i guarda els canvis al Sistema.
     *
     * @param nom Nom del text a eliminar del conjunt de textos.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     */
    public void eliminarText(String nom) throws NomNoExisteixException {
        cjtTextos.remove(nom);
        gestorTextos.guardarCanvis(nom);
    }

    /**
     * Elimina del conjunt d'alfabets aquell identificat pel nom donat i guarda els canvis al Sistema.
     *
     * @param nom Nom de l'alfabet a eliminar del conjunt d'alfabets.
     * 
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom donat.
     */
    public void eliminarAlfabet(String nom) throws NomNoExisteixException {
        cjtAlfabets.remove(nom);
        gestorAlfabets.guardarCanvis(nom);
    }

    /**
     * Elimina del conjunt de llistes de freqüències aquella identificada 
     * pel nom donat i guarda els canvis al sistema.
     *
     * @param nom Nom de la llista de freqüència a eliminar del conjunt de llistes de freqüències.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap llista de freqüències identificada 
     *                                pel nom donat.
     */
    public void eliminarLlistaFrequencia(String nom) throws NomNoExisteixException {
        cjtLlistesDeFrequencia.remove(nom);
        gestorLlistesFreq.guardarCanvis(nom);
    }

    /**
     * Elimina del conjunt de teclats aquell identificat pel nom donat i guarda els canvis al Sistema.
     *
     * @param nom Nom del teclat a eliminar del conjunt de teclats.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap teclat identificat pel nom donat.
     */
    public void eliminarTeclat(String nom) throws NomNoExisteixException {
        cjtTeclats.remove(nom);
        gestorTeclats.guardarCanvis(nom);
    }


    // Consultar textos

    /**
     * Retorna el cos del text identificat pel nom donat i existent al Sistema.
     *
     * @param nom Nom del text, existent al Sistema, del que es vol obtenir el seu cos.
     *
     * @return El cos del text identificat pel nom donat i existent al Sistema.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     */
    public String getCosText(String nom) throws NomNoExisteixException{
        return cjtTextos.getCos(nom);
    }

    /**
     * Donat el nom d'un text existent dins del conjunt de textos, retorna un String que 
     * conté tots els símbols d'aquest text en ordre lexicogràfic i sense repeticions.
     *
     * @param nom Nom del text, existent dins del conjunt de textos, del que es 
     *            vol obtenir els seus símbols.
     *
     * @return Un String que conté tots els símbols en ordre lexicogràfic
     *         del text identificat pel nom donat (sense repeticions).
     *
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     */
    public String getTextSimbols(String nom) throws NomNoExisteixException {
        return cjtTextos.getSimbolsAsString(nom);
    }

    /**
     * Donat el nom d'un text present al conjunt, retorna una seqüència de parells
     * (paraula, freqüència) on cada parella representa una paraula present al cos
     * del text i el número d'ocurrències d'aquesta dins del text (la seva freqüència).
     *
     * @param nom Nom del text existent dins del conjunt de textos del que extreure 
     *            les associacions paraula-freqüència.
     *
     * @return La seqüència de parells (paraula, freqüència) generada.
     *
     @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     */
    public ArrayList<Pair<String, Integer>> getFrequenciesText(String nom) throws NomNoExisteixException {
        return cjtTextos.getFrequenciesAsPairs(nom);
    }


    //Consultar alfabets

    /**
     * Donat el nom d'un alfabet existent al Sistema, retorna un String que conté tots els
     * símbols d'aquest alfabet en ordre lexicogràfic.
     *
     * @param nom Nom de l'alfabet existent dins del conjunt d'alfabets del 
     *            qual obtenir els seus símbols.
     *
     * @return Un String que conté tots els símbols en ordre
     *         lexicogràfic de l'alfabet identificat pel nom donat.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom donat.
     */
    public String getSimbolsAlfabet(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        return cjtAlfabets.getSimbolsAsString(nom);
    }


    //Consultar llistes de freqüències

    /**
     * Retorna una seqüència de parells amb el contingut de la llista de freqüències 
     * indentificada pel nom donat.
     *
     * @param nom Nom de la llista de freqüències existent dins del conjunt de llistes 
     *            de freqüències de la qual obtenir el seu contingut.
     *
     * @return Una seqüència de parells (paraula, freqüència) amb el contingut de la 
     *         llista de freqüències identificada pel nom donat.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap llista de
     *                                freqüències identificada pel nom donat.
     */
    public ArrayList<Pair<String, Integer>> getContingutLlistaFreq(String nom) throws NomNoExisteixException {
        return cjtLlistesDeFrequencia.getContingutAsPairs(nom);
    }

    /**
     * Donat el nom d'una llista de freqüències existent al conjunt de llistes de freqüències, 
     * retorna un String que conté tots els símbols d'aquesta llista de freqüències en ordre 
     * lexicogràfic i sense repeticions.
     *
     * @param nom Nom de la llista de freqüències existent al conjunt de llistes de freqüències
     *            de la que obtenir els seus símbols.
     *
     * @return Un String que conté tots els símbols en ordre lexicogràfic de la
     *         llista de freqüències identificada pel nom donat (sense repeticions).
     *
     * @throws NomNoExisteixException No existeix al Sistema cap llista de freqüències 
     *                                identificada pel nom donat.
     */
    public String getLlistaFreqSimbols(String nom) throws NomNoExisteixException {
        return cjtLlistesDeFrequencia.getSimbolsAsString(nom);
    }


    //Consultar teclats

    /**
     * Retorna el nom del teclat temporal del conjunt de teclats.
     *
     * @return El nom del teclat temporal del conjunt de teclats.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public String getNomTeclat() throws TeclatTemporalBuitException {
        return cjtTeclats.getNom();
    }

    /**
     * Retorna el nombre de files del teclat temporal del conjunt de teclats.
     *
     * @return El nombre de files del teclat temporal del conjunt de teclats.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public int getFilesTeclat() throws TeclatTemporalBuitException {
        return cjtTeclats.getRows();
    }


    /**
     * Retorna el nombre de columnes del teclat temporal del conjunt de teclats.
     *
     * @return El nombre de columnes del teclat temporal del conjunt de teclats.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public int getColsTeclat() throws TeclatTemporalBuitException {
        return cjtTeclats.getCols();
    }

    /**
     * Retorna la disposició actual del teclat temporal.
     *
     * @return Una seqüència de caràcters amb la disposició actual del teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public char[] getLayoutTeclat() throws TeclatTemporalBuitException {
        return cjtTeclats.getLayout();
    }

    /**
     * Retorna el cost de la disposició de símbols que té el teclat temporal del conjunt de teclats.
     *
     * @return El cost de la disposició de símbols que té el teclat temporal del conjunt de teclats.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     * @throws MissingPairsFreqException No s’ha donat un valor als parells de freqüència del 
     *                                   conjunt de teclats.
     */
    public double getCostTeclat() throws TeclatTemporalBuitException, MissingPairsFreqException {
        return cjtTeclats.getCost();
    }

    /**
     * Indica si el layout del teclat temporal del conjunt de teclats es pot canviar per la millor 
     * disposició trobada. Si el teclat temporal del Sistema ja es troba en una disposició òptima, 
     * no es podrà canviar. Si no es té registre d'una disposició prèvia millor, tampoc.
     *
     * @return True si si el layout del teclat temporal del conjunt de teclats es pot canviar per
     *         la millor disposició trobada. Altrament, false.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public boolean canSetBestLayoutTeclat() throws TeclatTemporalBuitException {
        return cjtTeclats.canSetBestLayout();
    }


    //Obtenir noms d'elements compatibles

    /**
     * Retorna els noms dels textos tals que tots els símbols que contenen
     * estan presents al conjunt rebut com a paràmetre d'entrada.
     *
     * @return Una llista amb els noms dels textos tals que tots els
     *         símbols que contenen estan presents al conjunt donat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public ArrayList<String> getTextosCompatibles() throws TeclatTemporalBuitException {
        TreeSet<Character> symbols = new TreeSet<>(cjtTeclats.getSimbols());
        return cjtTextos.getCompatibles(symbols);
    }

    /**
     * Retorna els noms de les llistes de freqüència tals que tots
     * els símbols que contenen estan presents al conjunt donat.
     *
     * @return Una llista amb els noms de les llistes de freqüència tals que
     *         tots els símbols que contenen estan presents al conjunt donat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public ArrayList<String> getLlistesFreqCompatibles() throws TeclatTemporalBuitException {
        TreeSet<Character> symbols = new TreeSet<>(cjtTeclats.getSimbols());
        return cjtLlistesDeFrequencia.getCompatibles(symbols);
    }


    // Editar texts

    /**
     * Canvia el nom del text existent al conjunt de textos identificat per {@code old_nom} pel 
     * nou nom {@code new_nom}.
     *
     * @param old_nom Nom del text al que canviar el nom.
     * @param new_nom Nom nou que es vol donar al text identificat per {@code old_nom}.
     *
     * @throws NomBuitException El nom {@code new_nom} no té caràcters.
     * @throws NomJaExisteixException Ja existeix un text identificat per {@code new_nom}.
     * @throws NomMassaLlargException El nom {@code new_nom} té més de {@value Text#MAX_NAME_LENGTH}
     *                                caràcters.
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat per {@code old_nom}.
     */
    public void setNomText(String old_nom, String new_nom) throws NomBuitException, NomMassaLlargException, NomNoExisteixException, NomJaExisteixException {
        cjtTextos.setNom(old_nom, new_nom);
        gestorTextos.guardarCanvis(old_nom);
        gestorTextos.guardarCanvis(new_nom);
    }

    /**
     * Sobreescriu el cos del text identificat per {@code nomText} amb el contingut de {@code nouCos}.
     *
     * @param nomText Nom del text al que canviar el cos.
     * @param nouCos Nou cos que se li donarà al text identificat per {@code nomText}.
     *
     * @throws TextEstaBuitException El nom {@code nouCos} no té caràcters.
     * @throws NumSimbolsInvalidException El nom {@code nouCos} conté més de 100 caràcters diferents
     * @throws NomNoExisteixException No existeix al Sistema cap text identificat pel nom donat.
     */
    public void setCosText(String nomText, String nouCos) throws NomNoExisteixException, TextEstaBuitException, NumSimbolsInvalidException {
        cjtTextos.setCos(nomText, nouCos);
        gestorTextos.guardarCanvis(nomText);
    }


    // Editar alfabets

    /**
     * Canvia el nom de l'alfabet identificat per {@code old_nom} pel nou nom {@code new_nom}.
     *
     * @param old_nom Nom de l'alfabet al que es vol canviar el nom.
     * @param new_nom Nom nou que es vol donar a l'alfabet identificat per {@code old_nom}.
     *
     * @throws NomBuitException El nom {@code new_nom} no té caràcters.
     * @throws NomJaExisteixException Ja existeix un alfabet identificat per {@code new_nom}.
     * @throws NomMassaLlargException El nom {@code new_nom} té més de {@value Alfabet#MAX_NAME_LENGTH} 
     *                                caràcters.
     * @throws NomNoExisteixException No existeix cap alfabet identificat pel nom {@code old_nom}.
     * @throws NomProhibitException El nom {@code new_nom} és {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET},
     *                              que està prohibit.
     */
    public void setNomAlfabet(String old_nom, String new_nom) throws NomBuitException, NomNoExisteixException, NomJaExisteixException, NomMassaLlargException, NomProhibitException {
        cjtAlfabets.setNom(old_nom, new_nom);
        gestorAlfabets.guardarCanvis(old_nom);
        gestorAlfabets.guardarCanvis(new_nom);
    }

    /**
     * Sobreescriu els símbols de l'alfabet identificat per {@code nom} amb els nous símbols.
     *
     * @param nom Nom de l'alfabet al que modificar els símbols.
     * @param simbols Els nous símbols per l'alfabet indicat.
     *
     * @throws NomNoExisteixException No existeix al Sistema cap alfabet identificat pel nom donat.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a {@code simbols}.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a {@code simbols}.
     */
    public void setSimbolsAlfabet(String nom, String simbols) throws NomNoExisteixException, NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException {
        cjtAlfabets.setSimbols(nom, simbols);
        gestorAlfabets.guardarCanvis(nom);
    }


    // Editar llistes de freqüències

    /**
     * Donat un nom {@code old_nom} que identifica una llista de freqüències present
     * al sistema, sobreescriu el nom d'aquesta amb l'indicat per {@code new_nom}.
     *
     * @param old_nom Nom de la llista de freqüència a la que canviar el nom.
     * @param new_nom Nou nom per a la mateixa llista de freqüència.
     *
     * @throws NomBuitException El nom {@code new_nom} no té caràcters.
     * @throws NomMassaLlargException El nom {@code new_nom} té més de 
     *                                {@value LlistaDeFrequencia#MAX_NAME_LENGTH} caràcters.
     * @throws NomJaExisteixException Ja existeix dins del conjunt una llista de
     *                                freqüència identificada per {@code new_nom}.
     * @throws NomNoExisteixException No existeix dins del conjunt cap llista de
     *                                freqüències identificada pel nom {@code old_nom}.
     */
    public void setNomLlistaFreq(String old_nom, String new_nom) throws NomNoExisteixException, NomBuitException, NomMassaLlargException, NomJaExisteixException {
        cjtLlistesDeFrequencia.setNom(old_nom, new_nom);
        gestorLlistesFreq.guardarCanvis(old_nom);
        gestorLlistesFreq.guardarCanvis(new_nom);
    }

    /**
     * Sobreescriu el contingut de la llista de freqüències identificada per {@code nom}
     * amb l'obtingut a partir d'una seqüència de parells (paraula, freqüència).
     *
     * @param nom Nom de la llista de freqüències a la que modificar el contingut.
     * @param contingut Una seqüència de parells &lt;paraula, freqüència&gt; de
     *                  la que obtenir les associacions paraula-freqüència.
     *
     * @throws NomNoExisteixException No existeix dins del conjunt cap llista de
     *                                freqüència identificada pel nom donat.
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
    public void setContingutLlistaFreq(String nom, ArrayList<Pair<String, Integer>> contingut) throws NomNoExisteixException, NombreElementsInvalidException, InvalidFrequencyException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException  {
        cjtLlistesDeFrequencia.setContingut(nom, contingut);
        gestorLlistesFreq.guardarCanvis(nom);
    }


    // Editar teclat

    /**
     * Sobreescriu el nom del teclat temporal del conjunt de tecltas amb l'indicat per {@code new_nom}.
     *
     * @param nom Nou nom pel teclat temporal del conjunt de teclats.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value Teclat#MAX_NAME_LENGTH} caràcters.
     */
    public void setNomTeclat(String nom) throws TeclatTemporalBuitException, NomBuitException, NomMassaLlargException {
        cjtTeclats.setNom(nom);
    }

    /**
     * Modifica les dimensions del teclat temporal del Sistema en funció del número de files. 
     * Si aquest últim no és aplicable, el nombre de files passarà a ser el valor 
     * aplicable més proper al donat.
     *
     * @param rows Nombre de files que es vol donar al teclat temporal del conjunt de teclats.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public void setFilesTeclat(int rows) throws TeclatTemporalBuitException {
        cjtTeclats.setRows(rows);
    }

    /**
     * Modifica les dimensions del teclat temporal del Sistema en funció del número de columnes.
     * Si aquest últim no és aplicable, el nombre de columnes passarà a ser el valor 
     * aplicable més proper al donat.
     *
     * @param cols Nombre de columnes que es vol donar al teclat temporal del conjunt de teclats.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public void setColsTeclat(int cols) throws TeclatTemporalBuitException {
        cjtTeclats.setCols(cols);
    }


    /**
     * Modifica els símbols de l'alfabet del teclat temporal del conjunt de teclats. A més a més, 
     * es reemplacen els símbols de la disposició del teclat temporal del conjunt de teclats pels 
     * símbols seleccionats, i es col·loquen per files ordenats lexicogràficament. Conseqüentment 
     * s'adapten les dimensions del teclat a la nova disposició tal que el ràtio amplada-alçada 
     * sigui 2.
     *
     * @param nom_alfabet El nom de l'alfabet del que extreure els símbols que tindrà el teclat temporal.
     *                    Si aquest nom coincideix amb {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET} 
     *                    selecciona els símbols que el teclat seleccionat tenia abans de cap modificació. 
     *                    Altrament selecciona els símbols de l'alfabet identificat per {@code nom_alfabet}.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     * @throws NomNoExisteixException No existeix cap alfabet identificat pel nom donat.
     */
    public void setSimbolsTeclat(String nom_alfabet) throws NomNoExisteixException, TeclatTemporalBuitException {
        if(nom_alfabet.equals(Alfabet.KEYBOARD_ORIGINAL_ALPHABET)) {
            cjtTeclats.setSimbolsBase();
        } else {
            String simbols = cjtAlfabets.getSimbolsAsString(nom_alfabet);
            try {
                cjtTeclats.setSimbols(simbols);
            } catch (NumSimbolsInvalidException | SimbolRepetitException | SimbolInvalidException e) {
                // No pot passar
                e.printStackTrace();
            }
        }
    }

    /**
     * Sobreescriu el layout del teclat temporal del conjunt de teclats amb el millor trobat 
     * fins ara amb la resta de paràmetres del teclat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     * @throws MissingBestLayoutException No hi ha un millor layout per al teclat temporal.
     */
    public void setBestLayoutTeclat() throws TeclatTemporalBuitException, MissingBestLayoutException {
        cjtTeclats.setBestLayout();
    }

    /**
     * Sobreescriu els parells de freqüències del conjunt de teclats amb uns de nous generats
     * a partir de la llista identificada pel nom donat i existent al Sistema.
     *
     * @param nom_llista_freq Nom de la llista de freqüències, existent al Sistema, de la que 
     *                        obtenir els nous parells de freqüències.
     *
     * @throws NomNoExisteixException No existeix cap llista de freqüències identificada pel nom donat. 
     * @throws CaractersNoInclososException L'alfabet del teclat temporal del conjunt de teclats no
     *                                      conté tots els símbols de la llista de freqüències 
     *                                      identificada pel nom donat.
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public void setFreqPairsByFreqListTeclat(String nom_llista_freq) throws NomNoExisteixException, CaractersNoInclososException, TeclatTemporalBuitException {
        Map<String, Integer> map = cjtLlistesDeFrequencia.getContingut(nom_llista_freq);
        try {
            cjtTeclats.setFreqPairsByFreqList(map);
        } catch (SimbolInvalidException | NumSimbolsInvalidException e) {
            // No pot passar
            e.printStackTrace();
        }
    }

    /**
     * Sobreescriu els parells de freqüències del conjunt de teclats amb uns de nous generats
     * a partir del text identificat pel nom donat i existent al Sistema.
     *
     * @param nomText Nom del text del que obtenir els nous parells de freqüències.
     *
     * @throws NomNoExisteixException No existeix cap text identificat pel nom donat.
     * @throws CaractersNoInclososException L'alfabet del teclat temporal del conjunt de teclats no 
     *                                      conté tots els símbols del text identificat pel nom donat.
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     */
    public void setFreqPairsByTextTeclat(String nomText) throws NomNoExisteixException, CaractersNoInclososException, TeclatTemporalBuitException {
        String contingut = cjtTextos.getCos(nomText);
        try {
            cjtTeclats.setFreqPairsByText(contingut);
        } catch (NumSimbolsInvalidException e) {
            // No pot passar
            e.printStackTrace();
        }
    }

    /**
     * Regenera la disposició del teclat temporal del conjunt de teclats, indicant el nom de 
     * l'algorisme generador a emprar i utilitzant els parells de freqüències guardats al conjunt
     * de teclats.
     *
     * @param algorisme_seleccionat Nom de l'algorisme amb el que es generarà la nova disposició
     *                              del teclat temporal del conjunt de teclats.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     * @throws InvalidGeneratorAlgorithmException L'algorisme generador al que s'intenta cridar 
     *                                            no existeix.
     * @throws MissingPairsFreqException No s’ha donat un valor als parells de freqüències del 
     *                                   conjunt de teclats.
     */
    public void regenerarTeclat(String algorisme_seleccionat) throws MissingPairsFreqException, TeclatTemporalBuitException, InvalidGeneratorAlgorithmException {
        GeneratorAlgorithm algorithm = algorismes.get(algorisme_seleccionat);
        if (algorithm == null) throw new InvalidGeneratorAlgorithmException();
        try {
            cjtTeclats.regenerate(algorithm);
        } catch (CaractersNoInclososException e) {
            //No pot passar
            e.printStackTrace();
        }
    }

    /**
     * Intercanvia els símbols ubicats als índexos tecla1 i tecla2 dins la disposició del teclat 
     * temporal del conjunt de teclats.
     *
     * @param tecla1 Index del primer símbol.
     * @param tecla2 Index del segon símbol.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal del conjunt de teclats.
     * @throws IndexosInvalidsException S'ha intentat accedir a una posició incorrecta.
     */
    public void swapKeysTeclat(int tecla1, int tecla2) throws IndexosInvalidsException, TeclatTemporalBuitException {
        cjtTeclats.swapKeys(tecla1, tecla2);
    }
}
