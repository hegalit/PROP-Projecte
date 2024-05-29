package edu.upc.prop.teclat.domini;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import edu.upc.prop.teclat.domini.exceptions.InvalidFrequencyException;
import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NombreElementsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.ParaulaBuidaException;
import edu.upc.prop.teclat.domini.exceptions.ParaulaRepetidaException;
import edu.upc.prop.teclat.domini.exceptions.SimbolInvalidException;
import edu.upc.prop.teclat.util.Pair;

/**
 * Classe que representa associacions de paraules amb les seves freqüències
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 */

public class LlistaDeFrequencia {
    //Constants
    /**Llargada màxima permesa pel nom.*/
    public static final int MAX_NAME_LENGTH = 100;

    
    //Atributs
    /**El nom d'una llista de freqüències*/
    private String nom;

    /**{@link LinkedHashMap} que conté la freqüència de cada paraula*/
    private LinkedHashMap<String, Integer> llista;


    //Constructora

    /** Construeix una llista de freqüència amb el nom donat
     *  i una seqüència de parells &lt;paraula, freqüència&gt;.
     *
     * @param nom Nom de la llista de freqüència
     * @param contingut Contingut de la llista a crear
     * 
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value MAX_NAME_LENGTH} caràcters.
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
    LlistaDeFrequencia(String nom, ArrayList<Pair<String, Integer>> contingut) throws NomBuitException, NomMassaLlargException, NombreElementsInvalidException, InvalidFrequencyException, ParaulaRepetidaException, SimbolInvalidException, ParaulaBuidaException, NumSimbolsInvalidException {
        setNom(nom);
        setContingut(contingut);
    }


    //Getters

    /**
     * Retorna el nom de la llista de freqüència
     * @return El nom de la llista de freqüència
     */
    String getNom() {
        return nom;
    }

    /**
     * Retorna un {@link LinkedHashMap} amb el contingut de la llista de  
     * freqüència (les associacions de paraules amb les seves freqüències).
     * 
     * @return Un {@link LinkedHashMap} amb el contingut de la llista de freqüència.
     */
    LinkedHashMap<String, Integer> getContingut() {
        return llista;
    }

    /**
     * Retorna el contingut de la llista de freqüència (les associacions 
     * de paraules amb les seves freqüències) com una seqüència de parells.
     * 
     * @return Una seqüència de parells amb el contingut de la llista de freqüència.
     */
    ArrayList<Pair<String, Integer>> getContingutAsPairs() {
        ArrayList<Pair<String, Integer>> parelles = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : llista.entrySet()) {
            parelles.add(new Pair<String, Integer>(entry.getKey(), entry.getValue()));
        }
        return parelles;
    }

    /**
     * Retorna un {@link TreeSet} amb els caràcters de les paraules contingudes 
     * a la llista de freqüència.
     *
     * @return Un {@link TreeSet} amb els caràcters de les paraules contingudes
     *         a la llista de freqüència ordenats lexicogràficament.
     */
    TreeSet<Character> getSimbols() {
        TreeSet<Character> symbols = new TreeSet<>();
        try {
            symbols = Alfabet.obtainSymbolsFromMap(llista);
        } catch (SimbolInvalidException | NumSimbolsInvalidException e) {
            // No pot passar
            e.printStackTrace();
        }
        return symbols;
    }

    /**
     * Retorna un {@code String} amb els caràcters de les paraules contingudes 
     * a la llista de freqüència ordenats lexicogràficament i sense repeticions.
     *
     * @return Un String amb els caràcters de les paraules contingudes a la llista
     *         de freqüència ordenats lexicogràficament i sense repeticions.
     */
    String getSimbolsAsString() {
        String symbols = new String(); 
        try {
            symbols = Alfabet.obtainSymbolsFromMapToString(llista);
        } catch (SimbolInvalidException | NumSimbolsInvalidException e) {
            //No pot passar
            e.printStackTrace();
        }
        return symbols;
    }

    
    //Setters
    
    /**
     * Sobreescriu el nom de la llista de freqüència amb el nou nom especificat.
     * 
     * @param nom El nou nom que es vol donar a la llista de freqüència.
     * 
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value MAX_NAME_LENGTH} caràcters.
     */
    void setNom(String nom) throws NomBuitException, NomMassaLlargException {
        nom = nom.strip();
        if (nom.length() <= 0) throw new NomBuitException();
        if (nom.length() > MAX_NAME_LENGTH) throw new NomMassaLlargException();
        this.nom = nom;
    }

    /**
     * Sobreescriu el contingut de la llista de paraules amb l'obtingut
     * a partir d'una seqüència de parells &lt;paraula, freqüència&gt;.
     *
     * @param contingut Una seqüència de parells &lt;paraula, freqüència&gt; de
     *                  la que obtenir les associacions paraula-freqüència.
     * 
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
    void setContingut(ArrayList<Pair<String, Integer>> contingut) throws NombreElementsInvalidException, ParaulaRepetidaException, InvalidFrequencyException, SimbolInvalidException, ParaulaBuidaException, NumSimbolsInvalidException {
        //Comprovem que la seqüència d'entrada no estigui buida
        if (contingut.size() <= 0) throw new NombreElementsInvalidException();

        LinkedHashMap<String, Integer> frequencies = new LinkedHashMap<>();
        TreeSet<Character> symbols = new TreeSet<>();

        //Tractem cadascuna de les parelles de la seqüència
        for (Pair<String, Integer> pair : contingut) {
            String word = String.valueOf(pair.getFirst());
            if (word.length() <= 0) throw new ParaulaBuidaException();

            //Comprovem que la paraula no tingui caràcters invàlids i no estigui repetida
            for (int i = 0; i < word.length(); ++i) {
                char c = word.charAt(i);
                if (Alfabet.invalid_symbols.contains(c)) throw new SimbolInvalidException();
                symbols.add(c);
            }
            if (frequencies.containsKey(word)) throw new ParaulaRepetidaException();

            Integer frequency = pair.getSecond();
            if (frequency <= 0) throw new InvalidFrequencyException();
	        frequencies.put(word, frequency);
        }

        if (symbols.size() > Alfabet.MAX_NUM_SYMBOLS) 
            throw new NumSimbolsInvalidException();

        this.llista = frequencies; 
    }
}
