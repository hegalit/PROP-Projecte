package edu.upc.prop.teclat.domini;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;
import java.io.Serializable;

import edu.upc.prop.teclat.domini.exceptions.SimbolInvalidException;
import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NomProhibitException;
import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.SimbolRepetitException;

/**
 * Un objecte que emmagatzema un conjunt de símbols ordenats lexicogràficament.
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 */
public class Alfabet implements Serializable {
    //Constants
    /**Llargada màxima permesa pel nom.*/
    public static final int MAX_NAME_LENGTH = 100;

    /**El nombre mínim de símbols d'un alfabet.*/
    public static final int MIN_NUM_SYMBOLS = 1;

    /**El nombre màxim de símbols d'un alfabet.*/
    public static final int MAX_NUM_SYMBOLS = 100;

    /**{@link TreeSet} que conté els símbols no permesos a cap alfabet.*/
    public static final TreeSet<Character> invalid_symbols = new TreeSet<>(Arrays.asList(' ', ',', '.', '\n', '\r', '\t', '\f'));

    /**Nom que cap instància d'alfabet té permès tenir com a nom*/
    public static final String KEYBOARD_ORIGINAL_ALPHABET = "Alfabet original del teclat";


    //Atributs

    /**El nom d'un alfabet.*/
    private String nom;

    /**{@link TreeSet} que conté els símbols de l'alfabet.*/
    private TreeSet<Character> simbols;


    // Constructora

    /** Construeix un alfabet amb el nom donat i els símbols que conté l'String indicat.
     *
     * @param nom Nom de l'alfabet.
     * @param simbols Símbols de l'alfabet.
     * 
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de 
     *                                    {@value MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'String {@code simbols} donat.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a l'String {@code simbols} donat.
     * @throws NomProhibitException El nom donat és {@value KEYBOARD_ORIGINAL_ALPHABET}, que està prohibit.
     */
    Alfabet(String nom, String simbols) throws NomBuitException, NomMassaLlargException, NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException, NomProhibitException {
        setNom(nom);
        setSimbols(simbols);
    }
    

    // Getters

    /**
     * Retorna el nom de l'alfabet.
     * @return El nom de l'alfabet.
     */
    String getNom() {
        return nom;
    }

    /**
     * Retorna un {@link TreeSet} que conté tots els simbols de l'alfabet.
     * @return Un {@link TreeSet} amb tots els simbols de l'alfabet.
     */
    TreeSet<Character> getSimbols() {
        return simbols;
    }

    /**
     * Retorna un String que conté els símbols de l'alfabet en ordre lexicogràfic.
     * @return Un String que conté els símbols de l'alfabet en ordre lexicogràfic.
     */
    String getSimbolsAsString() {
        return treeSetToString(simbols);
    }


    // Setters

    /**
     * Sobreescriu el nom de l'alfabet amb el nou nom especificat.
     *
     * @param nom El nou nom que es vol donar a l'alfabet.
     * 
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value MAX_NAME_LENGTH} caràcters.
     * @throws NomProhibitException El nom donat és {@value KEYBOARD_ORIGINAL_ALPHABET}, que està prohibit.
     */
    void setNom(String nom) throws NomBuitException, NomMassaLlargException, NomProhibitException {
        nom = nom.trim();
        if (nom.length() == 0) throw new NomBuitException();
        if (nom.length() > MAX_NAME_LENGTH) throw new NomMassaLlargException();
        if (nom.equals(KEYBOARD_ORIGINAL_ALPHABET)) throw new NomProhibitException();
        this.nom = nom;
    }

    /**
     * Sobreescriu els símbols de l'alfabet amb els símbols que conté l'String donat.
     * 
     * @param input_sequence Els símbols que passarà a tenir l'alfabet.
     * 
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de 
     *                                    {@value MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'String donat.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a l'String donat.
     */
    void setSimbols(String input_sequence) throws NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException {
        this.simbols = obtainSymbols(input_sequence, false, false);
    }


    // Mètodes estàtics per a obtenir els símbols d'una entrada donada

    /**
     * Retorna un {@link TreeSet} amb els diferents símbols que conté l'String indicat
     * excloent els no permesos.
     *
     * @param input_sequence String del que s'extrauen els símbols.
     * 
     * @return El {@link TreeSet} resultant.
     * 
     * @throws NumSimbolsInvalidException L'String està buit o té més de 
     *                                    {@value MAX_NUM_SYMBOLS} símbols diferents.
     */
    static public TreeSet<Character> obtainSymbolsFromString(String input_sequence) throws NumSimbolsInvalidException {
        TreeSet<Character> input_symbols = new TreeSet<>();
        try {
            input_symbols = obtainSymbols(input_sequence, true, true);
        } catch (SimbolInvalidException | SimbolRepetitException e) {
            //No pot passar
            e.printStackTrace();
        }
        return input_symbols;
    }

    /**
     * Retorna un {@code String} amb els diferents símbols que conté  
     * l'String indicat (excloent símbols no permesos) ordenats 
     * lexicogràficament i sense repeticions.
     *
     * @param input_sequence String del que s'extrauen els símbols.
     * 
     * @return L'String resultant.
     * 
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de 
     *                                    {@value MAX_NUM_SYMBOLS} símbols diferents.
     */
    static public String obtainSymbolsFromStringToString(String input_sequence) throws NumSimbolsInvalidException {
        TreeSet<Character> input_symbols = new TreeSet<>();
        input_symbols = obtainSymbolsFromString(input_sequence);
        return treeSetToString(input_symbols);
    }

    /**
     * Retorna un {@link TreeSet} amb els símbols obtinguts a partir d'un  
     * mapa que conté associacions de paraules amb les seves freqüències.
     *
     * @param input_map Mapa del que s'extrauen els símbols.
     * 
     * @return El {@link TreeSet} amb els símbols obtinguts 
     *         a partir de les paraules del mapa indicat.
     * 
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de 
     *                                    {@value MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos en  
     *                                alguna de les paraules del mapa donat.
     */
    static public TreeSet<Character> obtainSymbolsFromMap(Map<String, Integer> input_map) throws SimbolInvalidException, NumSimbolsInvalidException {
        StringBuilder sequence = new StringBuilder();
        for (Map.Entry<String, Integer> entry : input_map.entrySet()) {
            sequence.append(entry.getKey());
        }
        TreeSet<Character> input_symbols = new TreeSet<>();
        try {
            input_symbols = obtainSymbols(sequence.toString(), false, true);
        } catch (SimbolRepetitException e) {
            //No pot passar
            e.printStackTrace();
        }
        return input_symbols;
    }

    /**
     * Donat un mapa que conté associacions de paraules amb les seves freqüències,  
     * retorna un {@code String} amb els símbols que es troben en les paraules 
     * d'aquest mapa ordenats lexicogràficament i sense repeticions.
     *
     * @param input_map Mapa del que s'extrauen els símbols.
     * 
     * @return L'String amb els símbols obtinguts a partir de les paraules del mapa indicat.
     * 
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de  
     *                                    {@value MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos en  
     *                                alguna de les paraules del mapa donat.
     */
    static public String obtainSymbolsFromMapToString(Map<String, Integer> input_map) throws SimbolInvalidException, NumSimbolsInvalidException {
        TreeSet<Character> input_symbols = new TreeSet<>();
        input_symbols = obtainSymbolsFromMap(input_map);
        return treeSetToString(input_symbols);
    }


    //Mètodes privats

    /**
     * Retorna {@link TreeSet} amb tots els símbols presents a la seqüència d'entrada.
     *
     * @param input_sequence String del que s'extrau els símbols en format String.
     * @param auto_solve_invalids Si aquest flag està activat, eliminarà caràcters invàlids
     *                            dins l'String donat. Altrament llençarà excepció.
     * @param auto_solve_repeated Si aquest flag està activat, ignorarà caràcters repetits
     *                            dins l'String donat. Altrament llençarà excepció.
     *
     * @return Un {@link TreeSet} amb els símbols obtinguts a partir de l'String donat.
     *
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de 
     *                                    {@value MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'String donat.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a l'String donat.
     */
    static private TreeSet<Character> obtainSymbols(String input_sequence, boolean auto_solve_invalids, boolean auto_solve_repeated) throws SimbolInvalidException, SimbolRepetitException, NumSimbolsInvalidException {
        String lc_input_seq = input_sequence.toLowerCase();
        TreeSet<Character> input_symbols = new TreeSet<>();

        //Recorrem tots els símbols de l'entrada
        for (int i = 0; i < lc_input_seq.length(); ++i) {
		    char c = lc_input_seq.charAt(i);
            //Si c conté un caràcter invàlid, aquest mai s'afegirà
		    if (invalid_symbols.contains(c)) {
                if (!auto_solve_invalids) throw new SimbolInvalidException();
            }
            //Si c conté un caràcter repetit, tampoc s'afegirà
            else if (!auto_solve_repeated && input_symbols.contains(c)) throw new SimbolRepetitException();
		    else input_symbols.add(c);
		}

        //Comprovem si la seqüència està buida o sobrepassa el màxim de símbols
        if (input_symbols.size() < MIN_NUM_SYMBOLS || input_symbols.size() > MAX_NUM_SYMBOLS) 
            throw new NumSimbolsInvalidException();
        return input_symbols;
    }

    /**
     * Retorna un {@code String} amb el contingut del {@link TreeSet} donat en el mateix ordre.
     * 
     * @param input_set L'String resultant de la conversió.
     * 
     * @return Un String amb el contingut del {@link TreeSet} donat en el mateix ordre.
     */
    static private String treeSetToString(TreeSet<Character> input_set) {
        StringBuilder resultat = new StringBuilder();
        for (Character sym : input_set) {
            resultat.append(sym);
        }
        return resultat.toString();
    }
}
