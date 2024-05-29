package edu.upc.prop.teclat.domini;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.upc.prop.teclat.domini.exceptions.InvalidFrequencyException;
import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomJaExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NomNoExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NombreElementsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.ParaulaBuidaException;
import edu.upc.prop.teclat.domini.exceptions.ParaulaRepetidaException;
import edu.upc.prop.teclat.domini.exceptions.SimbolInvalidException;
import edu.upc.prop.teclat.util.Pair;

/**
 * Classe que enregistra totes les llistes de freqüències del Sistema.
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 */

class CjtLlistesDeFrequencia {
    //Atributs
    /**TreeMap on s'emmagatzemen les llistes de freqüència*/
    private TreeMap<String, LlistaDeFrequencia> llistesDeFrequencies;


    /** Constructora per defecte
     *	Crea un nou conjunt de llistes de freqüència buit.
     */
    CjtLlistesDeFrequencia() {
        llistesDeFrequencies = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }


    //Operacions del conjunt

    /**
     * Crea una nova llista de freqüència a partir dels valors indicats i l'afegeix al conjunt
     *
     * @param nom Nom de la llista de freqüència a afegir.
     * @param contingut Seqüència de parells &lt;paraula, freqüència&gt; de la
     *                  que obtenir les associacions paraula-freqüència.
     * 
     * @throws NomJaExisteixException Ja existeix dins del conjunt una llista de freqüències 
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
    void add(String nom, ArrayList<Pair<String, Integer>> contingut) throws NomJaExisteixException, NomBuitException, NomMassaLlargException, NombreElementsInvalidException, InvalidFrequencyException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException {
        nom = nom.strip();
        if (llistesDeFrequencies.containsKey(nom)) throw new NomJaExisteixException(nom);
        LlistaDeFrequencia llista = new LlistaDeFrequencia(nom, contingut);
        llistesDeFrequencies.put(nom, llista);
    }

    /**
     * Elimina del conjunt la llista de freqüència identificada pel nom donat.
     *
     * @param nom Nom de la llista de freqüència a esborrar del conjunt.
     * 
     * @throws NomNoExisteixException No existeix dins del conjunt cap llista de
     *                                freqüència identificada pel nom donat.
     */
    void remove(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!llistesDeFrequencies.containsKey(nom)) throw new NomNoExisteixException();
        llistesDeFrequencies.remove(nom);
    }

    /**
     * Indica si dins d'aquest conjunt existeix una llista de freqüència identificada pel nom donat.
     *
     * @param nom Nom de la llista de freqüència que es vol verificar si es troba al conjunt.
     * 
     * @return True si dins del conjunt existeix una llista de freqüència
     *         identificada pel nom donat. Altrament, false.
     */
    boolean exists(String nom) {
        nom = nom.strip();
        return llistesDeFrequencies.containsKey(nom);
    }

    /**
     * Retorna una llista amb els noms de totes les llistes de freqüència
     * existents dins del conjunt, en ordre lexicogràfic.
     *
     * @return Una llista amb els noms de totes les llistes de freqüències
     *         existents dins del conjunt, en ordre lexicogràfic.
     */
    ArrayList<String> getNoms() {
        ArrayList<String> nomsLlistes = new ArrayList<>();
        nomsLlistes.addAll(llistesDeFrequencies.keySet());
        return nomsLlistes;
    }

    /**
     * Retorna els noms de les llistes de freqüència tals que tots
     * els símbols que contenen estan presents al conjunt donat.
     *
     * @param simbols El conjunt de símbols la presència dels quals es vol
     *                comprovar a cada llista de freqüència del conjunt.
     * 
     * @return Una llista amb els noms de les llistes de freqüència tals que 
     *         tots els símbols que contenen estan presents al conjunt donat.
     */
    ArrayList<String> getCompatibles(TreeSet<Character> simbols) {
        ArrayList<String> compatibles = new ArrayList<>();

        //Per cada llista de freqüència, obtenim el seu alfabet 
        //i comprovem si aquest és subconjunt de "simbols"
        for (Map.Entry<String, LlistaDeFrequencia> entry : llistesDeFrequencies.entrySet()) {
            TreeSet<Character> simbols_llista = entry.getValue().getSimbols();
            if (simbols.containsAll(simbols_llista)) 
                compatibles.add(entry.getKey());
        }
        return compatibles;
    }
    

    //Getters d'instància de llista de freqüències

    /**
     * Retorna un {@link Map} amb el contingut de la llista de freqüència
     * indentificada pel nom donat.
     *
     * @param nom Nom de la llista de freqüència de la qual obtenir el seu contingut.
     * 
     * @return Un {@link Map} amb el contingut de la llista de freqüència identificada pel nom donat.
     *
     * @throws NomNoExisteixException No existeix dins del conjunt cap llista de
     *                                freqüència identificada pel nom donat.
     */
    Map<String, Integer> getContingut(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!llistesDeFrequencies.containsKey(nom)) throw new NomNoExisteixException();
        return llistesDeFrequencies.get(nom).getContingut();
    }

    /**
     * Retorna el contingut de la llista de freqüència indentificada 
     * pel nom donat com una seqüència de parells.
     *
     * @param nom Nom de la llista de freqüència de la qual obtenir el seu contingut.
     * 
     * @return Una seqüència de parells amb el contingut de la llista 
     *         de freqüència identificada pel nom donat.
     *
     * @throws NomNoExisteixException No existeix dins del conjunt cap llista de
     *                                freqüència identificada pel nom donat.
     */
    ArrayList<Pair<String, Integer>> getContingutAsPairs(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!llistesDeFrequencies.containsKey(nom)) throw new NomNoExisteixException();
        return llistesDeFrequencies.get(nom).getContingutAsPairs();
    }

    /**
     * Retorna un {@link TreeSet} amb els símbols de les paraules contingudes 
     * a la llista de freqüència identificada pel nom donat.
     *
     * @param nom Nom de la llista de freqüència de la qual obtenir els seus símbols.
     * 
     * @return Un {@link TreeSet} amb tots els simbols de la llista de freqüència identificada pel nom donat.
     *
     * @throws NomNoExisteixException No existeix dins del conjunt cap llista de
     *                                freqüència identificada pel nom donat.
     */
    TreeSet<Character> getSimbols(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!llistesDeFrequencies.containsKey(nom)) throw new NomNoExisteixException();
        return llistesDeFrequencies.get(nom).getSimbols();
    }

    /**
     * Donat el nom d'una llista de freqüència existent, retorna un String que conté tots els
     * símbols d'aquesta llista de freqüència en ordre lexicogràfic i sense repeticions.
     *
     * @param nom Nom de la llista de freqüència de la que obtenir els seus símbols.
     * 
     * @return Un String que conté tots els símbols en ordre lexicogràfic de la  
     *         llista de freqüència identificada pel nom donat (sense repeticions).
     *
     * @throws NomNoExisteixException No existeix dins del conjunt cap llista de
     *                                freqüència identificada pel nom donat.
     */
    String getSimbolsAsString(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!llistesDeFrequencies.containsKey(nom)) throw new NomNoExisteixException();
        return llistesDeFrequencies.get(nom).getSimbolsAsString();
    }


    // Setters d'instància de llista de freqüències

    /**
     * Donat un nom {@code old_nom} que identifica una llista de freqüència present 
     * al conjunt, sobreescriu el nom d'aquesta amb l'indicat per {@code new_nom}.
     *
     * @param old_nom Nom original de la llista de freqüència a la que canviar el nom.
     * @param new_nom Nou nom per a la mateixa llista de freqüència.
     * 
     * @throws NomBuitException El nom {@code new_nom} no té caràcters.
     * @throws NomMassaLlargException El nom {@code new_nom} té més de 
     *                                {@value LlistaDeFrequencia#MAX_NAME_LENGTH} caràcters.
     * @throws NomJaExisteixException Ja existeix dins del conjunt una llista de   
     *                                freqüència identificada per {@code new_nom}.
     * @throws NomNoExisteixException No existeix dins del conjunt cap llista de
     *                                freqüència identificada pel nom {@code old_nom}.
     */
    void setNom(String old_nom, String new_nom) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, NomNoExisteixException {
        old_nom = old_nom.strip();
        new_nom = new_nom.strip();
        
        if (!llistesDeFrequencies.containsKey(old_nom)) throw new NomNoExisteixException();
        if (!old_nom.equals(new_nom) && llistesDeFrequencies.containsKey(new_nom)) throw new NomJaExisteixException(new_nom);
        LlistaDeFrequencia llista_freq = llistesDeFrequencies.get(old_nom);
        llista_freq.setNom(new_nom);
        llistesDeFrequencies.remove(old_nom);
        llistesDeFrequencies.put(new_nom, llista_freq);
    }

    /**
     * Sobreescriu el contingut de la llista de freqüència identificada per {@code nom} 
     * amb l'obtingut a partir d'una seqüència de parells &lt;paraula, freqüència&gt;.
     *
     * @param nom Nom de la llista de freqüències a la que modificar el contingut.
     * @param contingut Una seqüència de parells &lt;paraula, freqüència&gt; de
     *                  la que obtenir les associacions paraula-freqüència.
     * 
     * @throws NomNoExisteixException No existeix dins del conjunt cap llista de
     *                                freqüència identificada pel nom donat.
     * @throws NombreElementsInvalidException La seqüència donada està buida.
     * @throws InvalidFrequencyException Hi ha alguna paraula de la seqüència 
     *                                   donada que té freqüència &lt;= 0.
     * @throws ParaulaRepetidaException Una o més paraules es troben repetides a la seqüència donada.
     * @throws NumSimbolsInvalidException La seqüència donada conté més de {@value Alfabet#MAX_NUM_SYMBOLS}
     *                                    símbols diferents entre totes les paraules que té.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos en alguna
     *                                de les paraules que conté la seqüència donada.
     * @throws ParaulaBuidaException Una o més paraules de la seqüència donada no tenen caràcters.
     */
    void setContingut(String nom, ArrayList<Pair<String, Integer>> contingut) throws NomNoExisteixException, NombreElementsInvalidException, InvalidFrequencyException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException {
        nom = nom.strip();
        if (!llistesDeFrequencies.containsKey(nom)) throw new NomNoExisteixException();
        llistesDeFrequencies.get(nom).setContingut(contingut);
    }
}
