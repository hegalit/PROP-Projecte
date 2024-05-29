package edu.upc.prop.teclat.domini;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import edu.upc.prop.teclat.domini.exceptions.*;


/**
 * Classe que representa un conjunt d'associacions de parells de caràcters amb les seves freqüències.
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 */
public class PairsFrequency implements Serializable {
    /**Emmagatzema les associacions de parells de caràcters amb les seves freqüències*/
    private HashMap<String, Integer> freq = new HashMap<String, Integer>();
    
    //Constructores
    /**
     * Construeix un PairsFrequency buit.
     */
    public PairsFrequency() {
    }

    /** Construeix un PairsFrequency a partir d'una seqüència de caràcters donada.
     *
     * @param text Seqüència de caràcters del que extreu les associacions de parells 
     *             de caràcters amb les seves freqüències.
     *
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de 100 símbols diferents.
     */
    public PairsFrequency(String text) throws NumSimbolsInvalidException {
        //Si no té símbols vàlids o bé en té massa, no permetem crear el PairsFrequency
        TreeSet<Character> symbols = obtainSymbolsFromString(text);
        if (symbols.size() > Alfabet.MAX_NUM_SYMBOLS) throw new NumSimbolsInvalidException();

        //Obtenim els mots que la seqüència d'entrada pugui contenir 
        //partint en 2 allà on hi hagi caràcters invàlids
        String[] words = text.split("[ ,.\\r\\n\\t\\f]");
        for (int i = 0; i < words.length; ++i) {
            try {
                processString(words[i], 1);
            } catch (SimbolInvalidException e) {
                // No pot passar
                e.printStackTrace();
            }
        }
    }

    /** Construeix un PairsFrequency a partir del mapa donat.
     *
     * @param map Mapa del que extreu les associacions de parells de caràcters amb les seves freqüències.
     *
     * @throws NumSimbolsInvalidException El mapa donat està buit o bé té més de 100 símbols diferents
     *                                    entre totes les paraules que conté.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos al mapa donat.
     */
    public PairsFrequency(Map<String, Integer> map) throws NumSimbolsInvalidException, SimbolInvalidException {
        if (map.size() <= 0) throw new NumSimbolsInvalidException();

        //Comprovem que els parells de freqüència no tenen massa símbols

        //Juntem totes les paraules en un sol String per obtenir després
        //tots els símbols amb una sola crida a "obtainSymbolsFromString"
        StringBuilder sequence = new StringBuilder();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sequence.append(entry.getKey());
        }
        //Si té massa símbols diferents no permetem crear el PairsFrequency.
        TreeSet<Character> symbols = obtainSymbolsFromString(sequence.toString());
        if (symbols.size() > Alfabet.MAX_NUM_SYMBOLS) throw new NumSimbolsInvalidException();

        //Obtenim cadascuna de les parelles de caràcters consecutius 
        //dins de cada paraula del mapa donat
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            processString(entry.getKey(), entry.getValue());
        }
    }

    
    /** Retorna els símbols ordenats lexicogràficament que conté tenint en compte tots els 
     *  parells de caràcters que té.
     *
     * @return Els símbols ordenats lexicogràficament que conté tenint en compte tots els 
     *         parells de caràcters que té.
     */
    public TreeSet<Character> getSimbols() {
        StringBuilder sequence = new StringBuilder();
        for (Map.Entry<String, Integer> entry : freq.entrySet()) {
            sequence.append(entry.getKey());
        }
        return obtainSymbolsFromString(sequence.toString());
    }
    

    /** Funció que retorna la freqüència associada al parell de caràcters ab.
     *
     * @return La freqüència associada al parell de caràcters ab.
     */
    public int get(char a, char b) {
        Integer f = freq.get(getPair(a, b));
        if (f != null) return f;
        else return 0;
    }

    /** Sobreescriu la freqüència associada al parell de caràcters ab per {@code val}.
     *
     * @param a Símbol 1.
     * @param b Símbol 2.
     * @param val Nou valor que es vol donar a la freqüència associada al parell ab.
     */
    public void put(char a, char b, int val) {
        freq.put(getPair(a, b), val);
    }

    /** Suma {@code val} a la freqüència associada al parell de caràcters ab.
     *
     * @param a Símbol 1.
     * @param b Símbol 2.
     * @param val Valor que es vol sumar a la freqüència associada al parell ab.
     */
    public void add(char a, char b, int val) {
        String s = getPair(a, b);
        if (freq.containsKey(s)) freq.put(s, freq.get(s) + val);
        else freq.put(s, val);
    }

    /** Processa un String per tal d'obtenir tots els parells de caràcters que conté. 
     *
     * @param str String a processar.
     * @param freq La freqüència de str. Ha de ser major que 0.
     *
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a {@code str}.
     */
    private void processString(String str, int freq) throws SimbolInvalidException {
        //Si la paraula no té com a mínim 2 lletres, no podem
        //fer un parell de caràcters
        if (str.length() < 2) return;

        str = str.toLowerCase(); //passem l'String a minúscules
        char prev = str.charAt(0);
        for (int i = 1; i < str.length(); ++i) {
            char curr = str.charAt(i);

            //Comprovem que el símbol estigui permès
            if(Alfabet.invalid_symbols.contains(curr)) throw new SimbolInvalidException();
            add(curr, prev, freq);
            prev = curr;
        }
    }

    /** Funció que, donats dos símbols, retorna el parell que formen, 
     *  ordenant-los lexicogràficament.
     *
     * @param a Símbol 1.
     * @param b Símbol 2.
     *
     * @return El parell que formen els símbols donats, ordenant-los lexicogràficament.
     */
    private String getPair(char a, char b) {
        if(a > b) {
            return String.valueOf(a) + String.valueOf(b);
        } else {
            return String.valueOf(b) + String.valueOf(a);
        }
    }

    /** Retorna els símbols que conté l'String donat.
     *
     * @param input_sequence Un String qualsevol.
     *
     * @return Retorna els símbols que conté l'String donat.
     */
    private TreeSet<Character> obtainSymbolsFromString(String input_sequence) {
        TreeSet<Character> symbols = new TreeSet<>();
        for (int i = 0; i < input_sequence.length(); ++i) {
            char c = input_sequence.charAt(i);
            //Si el símbol és vàlid, l'afegim al set
            if (!Alfabet.invalid_symbols.contains(c)) symbols.add(c);
        }
        return symbols;
    }
}
