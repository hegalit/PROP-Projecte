package edu.upc.prop.teclat.domini;

import edu.upc.prop.teclat.domini.exceptions.*;
import edu.upc.prop.teclat.domini.exceptions.teclat.CaractersNoInclososException;
import edu.upc.prop.teclat.domini.exceptions.teclat.IndexosInvalidsException;
import edu.upc.prop.teclat.domini.exceptions.teclat.LayoutInvalidException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingBestLayoutException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingPairsFreqException;
import edu.upc.prop.teclat.domini.exceptions.teclat.TeclatTemporalBuitException;
import edu.upc.prop.teclat.domini.generatoralgorithms.GeneratorAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Classe que enregistra tots els teclats del Sistema.
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */

class CjtTeclats {
    //Atributs

    /**TreeMap on s'emmagatzemen els teclats*/
    private TreeMap<String, Teclat> teclats;

    /**Teclat separat de la resta per a gestionar la creació/edició de teclats.*/
    private Teclat temp_teclat;

    /**Nom original del teclat que s’estigui editant.*/
    private String nom_teclat_base;

    /**Seqüència de símbols que guarda la millor distribució de tecles trobada.*/
    private char[] best_layout;

    /**Millor cost del teclat generat.*/
    private double best_cost;

    /**PairsFrequency emprat (en cas d’usar-ne un).*/
    private PairsFrequency pairsFreq;



    /** Constructora per defecte
     *	Crea un nou conjunt de teclats buit.
     */
    CjtTeclats() {
        teclats = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }



    //Operacions del conjunt

    /**
     * Afegeix el teclat rebut com a paràmetre d'entrada al conjunt.
     *
     * @param teclat Teclat a inserir dins del conjunt.
     *
     * @throws NomJaExisteixException Ja existeix un teclat dins del conjunt amb el mateix 
     *                                nom que el teclat donat.
     */
    void add(Teclat teclat) throws NomJaExisteixException {
        String nom = teclat.getNom();
        if (teclats.containsKey(nom)) throw new NomJaExisteixException(nom);
        teclats.put(nom, teclat);
    }

    /**
     * Retorna el teclat identificat pel paràmetre d'entrada i existent dins del conjunt.
     *
     * @param nom Nom del teclat a identificar.
     *
     * @return Retorna el teclat identificat pel paràmetre d'entrada (en cas d'existir).
     *
     * @throws NomNoExisteixException No existeix dins del conjunt cap teclat identificat pel nom donat.
     */
    Teclat get(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!teclats.containsKey(nom)) throw new NomNoExisteixException();
        return teclats.get(nom);
    }

    /**
     * Sobreescriu el teclat temporal amb un de nou generat a partir 
     * d'una seqüència de símbols amb ràtio amplada-alçada de 2.
     *
     * @param layout Nova disposició que rebrà el teclat temporal.
     *
     * @throws NumSimbolsInvalidException El número de símbols diferents dins la seqüència d’entrada
     *                                     és 0 o bé major que {@value Alfabet#MAX_NUM_SYMBOLS}.
     * @throws SimbolInvalidException Hi ha algun símbol no permès a la seqüència d’entrada.
     * @throws SimbolRepetitException Hi ha algun símbol repetit a la seqüència d’entrada.
     */
    void make(String layout) throws NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException {
        //Esborrem les dades de millors layouts per si de cas
        eraseBestLayout();

        //Calculem les dimensions del teclat a crear
        double ratio = 2.0;
        int rows = (int)Math.ceil(Math.sqrt(layout.length()/ratio));
        int cols = (int)Math.ceil((double)layout.length()/rows);
        try {
            //Sobreescrivim les dades del teclat temporal
            temp_teclat = new Teclat("Temporal keyboard", layout, cols);
        } catch (NomBuitException | NomMassaLlargException e) {
            // No hauria de passar mai
            e.printStackTrace();
        }
        nom_teclat_base = null;
        pairsFreq = null;
    }

    /**
     * Sobreescriu el teclat temporal amb les dades d'un teclat identificat
     * pel paràmetre d'entrada i existent dins el conjunt.
     *
     * @param nom Nom del nou teclat a carregar.
     *
     * @throws NomNoExisteixException No existeix dins del conjunt cap teclat identificat pel nom donat.
     */
    void load(String nom) throws NomNoExisteixException {
        //Esborrem les dades de millors layouts per si de cas
        eraseBestLayout();
        nom = nom.strip();

        //Obtenim el teclat identificat per "nom"
        Teclat teclat = teclats.get(nom);
        if (teclat == null) throw new NomNoExisteixException();
        try {
            //Sobreescrivim les dades del teclat temporal amb les del seleccionat prèviament
            temp_teclat = new Teclat(teclat.getNom(), new String(teclat.getLayout()), teclat.getCols());
        } catch (NomBuitException | NomMassaLlargException | NumSimbolsInvalidException | SimbolRepetitException | SimbolInvalidException e) {
            // No hauria de passar
            e.printStackTrace();
        }
        nom_teclat_base = nom;  //Guardem el nom del teclat seleccionat
        pairsFreq = null;
    }

    /**
     * Guarda el teclat temporal dins del conjunt amb nom nouNom.
     * Si s'estava editant un teclat, es fan les modificacions corresponents així com el canvi de nom.
     * Si s'estava creant un teclat, el nom del nou teclat és nouNom.
     * 
     * @param nouNom El nou nom del teclat a crear/editar.
     *
     * @throws NomNoExisteixException S'està intentant modificar un teclat que no existeix.
     * @throws NomMassaLlargException El nom donat té més de {@value Teclat#MAX_NAME_LENGTH} caràcters.
     * @throws NomBuitException El nom no té caràcters.
     * @throws NomJaExisteixException Ja existeix al conjunt un teclat identificat pel nom donat.
     */
    void store(String nouNom) throws NomJaExisteixException, NomNoExisteixException, NomBuitException, NomMassaLlargException {
        nouNom = nouNom.strip();

        // Canviem el nom del teclat temporal
        temp_teclat.setNom(nouNom);

        //Si estem modificant un teclat que ja existia, esborrem l'antic
        if (nom_teclat_base != null) {
            //Comprovem que aquest existeix
            if (!teclats.containsKey(nom_teclat_base)) throw new NomNoExisteixException();
            teclats.remove(nom_teclat_base);
        }

        //Guardem les dades del teclat temporal
        if (teclats.containsKey(nouNom)) throw new NomJaExisteixException(nouNom);
        teclats.put(temp_teclat.getNom(), temp_teclat);

        //Esborrem les dades emprades al crear/editar
        temp_teclat = null;
        nom_teclat_base = null;
        pairsFreq = null;
    }

    /**
     * Elimina el teclat identificat pel nom donat.
     *
     * @param nom Nom del teclat a esborrar.
     *
     * @throws NomNoExisteixException No existeix cap teclat identificat pel nom donat.
     */
    void remove(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!teclats.containsKey(nom)) throw new NomNoExisteixException();
        teclats.remove(nom);
    }

    /**
     * Indica si dins d'aquest conjunt existeix un teclat identificat pel nom donat.
     *
     * @param nom Nom de teclat que es vol verificar si es troba al conjunt.
     *
     * @return True si dins del conjunt existeix un teclat
     *         identificat pel nom donat. Altrament, false.
     */
    boolean exists(String nom) {
        nom = nom.strip();
        return teclats.containsKey(nom);
    }

    /**
     * Retorna els noms dels teclats que el conjunt conté (excloent el temporal).
     *
     * @return Una llista amb els noms de tots els teclats existents
     *         dins del conjunt, en ordre lexicogràfic.
     */
    ArrayList<String> getNoms() {
        ArrayList<String> nomsTeclats = new ArrayList<>();
        nomsTeclats.addAll(teclats.keySet());
        return nomsTeclats;
    }


    // Getters del teclat temporal

    /**
     * Retorna el nom del teclat temporal.
     *
     * @return Un String amb el nom del teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    String getNom() throws TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        return temp_teclat.getNom();
    }

    /**
     * Retorna el nombre de files del teclat temporal.
     *
     * @return Un enter amb el nombre de files del teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    int getRows() throws TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        return temp_teclat.getRows();
    }

    /**
     * Retorna el nombre de columnes del teclat temporal.
     *
     * @return Un enter amb el nombre de columnes del teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    int getCols() throws TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        return temp_teclat.getCols();
    }

    /**
     * Retorna els símbols que conté el text identificat pel valor del paràmetre d'entrada.
     *
     * @return Un {@link TreeSet} que conté tots els símbols en ordre
     *         lexicogràfic del teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    TreeSet<Character> getSimbols() throws TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        return temp_teclat.getSimbols();
    }

    /**
     * Retorna la nova disposició del teclat temporal.
     *
     * @return Una seqüència de caràcters amb la nova disposició del teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    char[] getLayout() throws TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        return temp_teclat.getLayout();
    }

    /**
     * Retorna el cost de la disposició actual del teclat temporal.
     *
     * @return El cost de la disposició actual del teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     * @throws MissingPairsFreqException No s’ha donat un valor als parells de freqüència de cjtTeclats.
     */
    double getCost() throws TeclatTemporalBuitException, MissingPairsFreqException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        return temp_teclat.getCost(pairsFreq);
    }

    /**
     * Retorna els símbols que tenia el teclat seleccionat prèviament amb load()
     * abans de cap modificació (si no es va seleccionar cap llençarà excepció).
     *
     * @throws NomNoExisteixException No existeix cap teclat identificat pel nom donat.
     */
    void setSimbolsBase() throws NomNoExisteixException {
        if (!teclats.containsKey(nom_teclat_base)) throw new NomNoExisteixException();
        String simbols = teclats.get(nom_teclat_base).getSimbolsAsString();
        try {
            temp_teclat.setSimbols(simbols);
        } catch (NumSimbolsInvalidException | SimbolRepetitException | SimbolInvalidException e) {
            // No hauria de passar
            e.printStackTrace();
        }
    }

    /**
     * Indica si el layout del teclat temporal es pot canviar per la millor disposició trobada.
     * Si el teclat temporal ja es troba en una disposició òptima, no es podrà canviar.
     * Si no es té registre d'una disposició prèvia millor, tampoc.
     *
     * @return True si si el layout del teclat temporal es pot canviar per la millor disposició trobada.
     *         Altrament, false.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    boolean canSetBestLayout() throws TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        return (best_layout != null) && (!Arrays.equals(best_layout, temp_teclat.getLayout()));
    }


    // Setters

    /**
     * Sobreescriu el nom del teclat temporal amb l'indicat per {@code new_nom}.
     *
     * @param new_nom Nou nom pel teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value Teclat#MAX_NAME_LENGTH} caràcters.
     */
    void setNom(String new_nom) throws TeclatTemporalBuitException, NomBuitException, NomMassaLlargException {
        new_nom = new_nom.strip();
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        temp_teclat.setNom(new_nom);
    }

    /**
     * Modifica les dimensions del teclat temporal en funció del número de files.
     * Si aquest últim no és aplicable, el nombre de files passarà a ser
     * el valor aplicable més proper al donat.
     *
     * @param rows Nou nombre de files pel teclat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    void setRows(int rows) throws TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        temp_teclat.setRows(rows);
        eraseBestLayout();
        updateBestLayout();
    }

    /**
     * Modifica les dimensions del teclat temporal en funció del número de columnes.
     * Si aquest últim no és aplicable, el nombre de columnes passarà a ser
     * el valor aplicable més proper al donat.
     *
     * @param cols Nou nombre de columnes3 pel teclat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    void setCols(int cols) throws TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        temp_teclat.setCols(cols);
        eraseBestLayout();
        updateBestLayout();
    }

    /**
     * Sobreescriu els símbols que l'alfabet del teclat temporal conté amb els nous símbols especificats.
     * A més a més, es reemplacen els símbols de la disposició del mateix teclat pels que conté l'String,
     * i es col·loquen per files ordenats lexicogràficament. Conseqüentment s'adapten les dimensions del 
     * teclat a la nova disposició tal que el ràtio amplada-alçada sigui 2.
     *
     * @param simbols Nova seria de símbols que tindrà el teclat temporal.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     * @throws NumSimbolsInvalidException El número de símbols diferents dins la seqüència 
     *                                    d’entrada és 0 o bé {@value Alfabet#MAX_NAME_LENGTH}.
     * @throws SimbolInvalidException Hi ha algun símbol no permès a la seqüència d’entrada.
     * @throws SimbolRepetitException Hi ha algun símbol repetit a la seqüència d’entrada.
     */
    void setSimbols(String simbols) throws TeclatTemporalBuitException, NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        temp_teclat.setSimbols(simbols);
        eraseBestLayout();
        pairsFreq = null;
    }

    /**
     * Sobreescriu el layout del teclat temporal amb el del millor trobat fins ara
     * amb la resta de paràmetres del teclat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     * @throws MissingBestLayoutException No hi ha un millor layout per al teclat temporal.
     */
    void setBestLayout() throws TeclatTemporalBuitException, MissingBestLayoutException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        if (best_layout == null) throw new MissingBestLayoutException();
        try {
            temp_teclat.setLayout(best_layout);
        } catch (SimbolInvalidException | SimbolRepetitException | LayoutInvalidException e) {
            //No hauria de passar
            e.printStackTrace();
        }
    }

    /**
     * Sobreescriu els parells de freqüència amb uns de nous generats a partir de l’String d’entrada.
     *
     * @param text Un String qualsevol.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     * @throws CaractersNoInclososException L'alfabet del teclat temporal no conté tots els 
     *                                      símbols necessaris per a regenerar el teclat.
     * @throws NumSimbolsInvalidException El número de símbols diferents dins la seqüència
     *                                    d’entrada és 0 o bé > {@value Alfabet#MAX_NAME_LENGTH}.
     */
    void setFreqPairsByText(String text) throws CaractersNoInclososException, TeclatTemporalBuitException, NumSimbolsInvalidException {
        PairsFrequency parells = new PairsFrequency(text);
        if (!alphabetContainsAll(parells.getSimbols())) throw new CaractersNoInclososException();
        pairsFreq = parells;
        eraseBestLayout();
        updateBestLayout();
    }

    /**
     * Sobreescriu els parells de freqüència amb uns de nous generats a partir
     * del mapa d’associacions paraula-freqüència donat.
     *
     * @param wordsFreq Un mapa que conté associacions paraula-freqüència.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     * @throws CaractersNoInclososException L'alfabet del teclat temporal no conté tots els  
     *                                      símbols necessaris per a regenerar el teclat.
     * @throws SimbolInvalidException Hi ha algun símbol no permès a la seqüència d’entrada.
     * @throws NumSimbolsInvalidException El número de símbols diferents dins la seqüència
     *                                    d’entrada és 0 o bé > {@value Alfabet#MAX_NAME_LENGTH}.
     */
    void setFreqPairsByFreqList(Map<String, Integer> wordsFreq) throws CaractersNoInclososException, TeclatTemporalBuitException, SimbolInvalidException, NumSimbolsInvalidException {
        PairsFrequency parells = new PairsFrequency(wordsFreq);
        if (!alphabetContainsAll(parells.getSimbols())) throw new CaractersNoInclososException();
        pairsFreq = parells;
        eraseBestLayout();
        updateBestLayout();
    }

    /**
     * Regenera la disposició d’un teclat a partir d’un algorisme generador
     * rebut per l’entrada i els PairsFrequency guardats a cjtTeclats
     *
     * @param algorithm algorisme que generarà la disposició del teclat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     * @throws CaractersNoInclososException L'alfabet del teclat temporal no conté tots els 
     *                                      símbols necessaris per a regenerar el teclat.
     * @throws MissingPairsFreqException No s’ha donat un valor als parells de freqüència de cjtTeclats.
     */
    void regenerate(GeneratorAlgorithm algorithm) throws CaractersNoInclososException, MissingPairsFreqException, TeclatTemporalBuitException {
        if (pairsFreq == null) throw new MissingPairsFreqException();
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        temp_teclat.regenerate(algorithm, pairsFreq);
        updateBestLayout();
    }

    /**
     * Intercanvia els símbols de la disposició ubicats als índexos tecla1 i tecla2 del teclat temporal.
     *
     * @param tecla1 Index del primer símbol.
     * @param tecla2 Index del segon símbol.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     * @throws IndexosInvalidsException S'ha intentat accedir a una posició incorrecta.
     */
    void swapKeys(int tecla1, int tecla2) throws IndexosInvalidsException, TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        temp_teclat.swapKeys(tecla1, tecla2);
        updateBestLayout();
    }


    //Mètodes privats

    /**
     * Comprova si l'alfabet del teclat temporal conté el conjunt de símbols donat.
     *
     * @param symbols Símbols que es vol comprovar si coincideixen amb els de l'alfabet del teclat.
     *
     * @return True si l'alfabet del teclat coincideix amb el conjunt de símbols donat.
     *         Altrament, false.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    private boolean alphabetContainsAll(TreeSet<Character> symbols) throws TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        TreeSet<Character> symbols_alphabet = temp_teclat.getSimbols();
        if (symbols_alphabet.containsAll(symbols)) return true;
        return false;
    }

    /**
     * Actualitza la millor disposició trobada pel teclat temporal actual. Si no hi 
     * havia dades prèvies, la millor disposició serà aquella que tingui el teclat
     * temporal actualment. Altrament, si la disposició actual del teclat temporal té 
     * cost inferior al del millor trobat abans, el layout millor passa a ser l'actual.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    private void updateBestLayout() throws TeclatTemporalBuitException {
        if (temp_teclat == null) throw new TeclatTemporalBuitException();
        if (pairsFreq == null) return; //Si no tenim parells de freqüència, no fa res
        double curr_cost = 0;
        try {
            curr_cost = temp_teclat.getCost(pairsFreq);
        } catch (MissingPairsFreqException e) {
            //No pot passar, es comprova prèviament
        }
        //O bé no tenim dades de layouts previs o hem trobat un millor que els obtinguts fins ara
        if ((best_layout == null) || (curr_cost <= best_cost)) {
            //Posem com a millor layout el layout actual i actualitzem el cost
            char[] curr_layout = temp_teclat.getLayout();
            best_layout = new char[curr_layout.length];
            System.arraycopy(curr_layout, 0, best_layout, 0, curr_layout.length);

            best_cost = curr_cost;
        }
    }

    /** S'esborra el layout assignat previament.*/
    private void eraseBestLayout() {
        best_layout = null;
        best_cost = Double.MAX_VALUE;
    }
}
