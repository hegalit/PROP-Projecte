package edu.upc.prop.teclat.domini;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.TextEstaBuitException;
import edu.upc.prop.teclat.util.Pair;

/**
 * Un objecte que emmagatzema cadenes de caràcters.
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */

public class Text {
    //Constants
    /**Llargada màxima permesa pel nom.*/
    public static final int MAX_NAME_LENGTH = 100;

    
    //Atributs
    /**El nom d'un text.*/
    private String nom;
    
    /**Emmagatzema el contingut del text.*/
    private String cos;

    
    //Constructora
    /** Construeix un text amb el nom donat i el cos d'aquest amb una serie de caràcters.
     *
     * @param nom Nom del text.
     * @param cos cos del text.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value MAX_NAME_LENGTH} símbols diferents.
     * @throws TextEstaBuitException El cos del text no té caràcters.
     */
    Text(String nom, String cos) throws NomBuitException, NomMassaLlargException, TextEstaBuitException, NumSimbolsInvalidException {
        setNom(nom);
        setCos(cos);
    }


    //Getters
    /**
     * Retorna el nom del text.
     * @return El nom del text.
     */
    String getNom() {
        return nom;
    }

    /**
     * Retorna el cos del text.
     * @return El cos del text.
     */
    String getCos() {
        return cos;
    }

    /**
     * Retorna un {@link TreeSet} que retorna un conjunt ordenat de caràcters sense repeticions
     * que indica tots els símbols presents al text.
     * 
     * @return Un {@link TreeSet} amb tots els simbols presents al text.
     */
    TreeSet<Character> getSimbols() {
        TreeSet<Character> symbols = new TreeSet<>(); 
        try {
            symbols = Alfabet.obtainSymbolsFromString(cos);
        } catch (NumSimbolsInvalidException e) {
            // No pot passar
            e.printStackTrace();
        }
        return symbols;
    }

    /**
     * Retorna un String amb els símbols que conté el text 
     * ordenats lexicogràficament i sense repeticions.
     * 
     * @return Un String amb els símbols que conté el text 
     *         ordenats lexicogràficament i sense repeticions.
     */
    String getSimbolsAsString() {
        String symbols = new String();
        try {
            symbols = Alfabet.obtainSymbolsFromStringToString(cos);
        } catch(NumSimbolsInvalidException e) {
            //No pot passar
            e.printStackTrace();
        }
        return symbols;
    }

    /**
     * Retorna un {@link Map} d’associacions paraula-freqüència que conté totes les paraules 
     * presents al text junt amb el número de vegades que apareixen en aquest (la freqüència).
     * 
     * @return El {@link Map} generat.
     */
    Map<String, Integer> getFrequencies() {
        TreeMap<String, Integer> llista = new TreeMap<>();

        //Separem la seqüència en paraules mitjançant els caràcters invàlids
        //(aquells que un alfabet no pot tenir)
        String[] word_list = cos.split("[ ,.\\n\\r\\t\\f]");

        //Processem totes les paraules 
        for (String s : word_list) {
            String word = String.valueOf(s);
            word = word.toLowerCase();
            //Eliminem els caràcters invàlids que puguin quedar
            word = word.replaceAll("[ ,.\\n\\r\\t\\f]", "");

            //Si la paraula no era un caràcter invàlid, s'afegeix
            if (word.length() > 0) {
                llista.put(word, llista.getOrDefault(word, 0) + 1);
            }
        }
        return llista;
    }

    /**
     * Retorna una seqüència de parells &lt;paraula, freqüència&gt; on cada parella equival a una
     * paraula present al cos del text i el número d'ocurrències d'aquesta dins del text (la
     * seva freqüència).
     * 
     * @return La seqüència de parells &lt;paraula, freqüència&gt; generada.
     */

    ArrayList<Pair<String, Integer>> getFrequenciesAsPairs() {
        Map<String, Integer> llista = getFrequencies();

        //Convertim el mapa en una seqüència de parells
        ArrayList<Pair<String, Integer>> parelles = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : llista.entrySet()) {
            parelles.add(new Pair<String, Integer>(entry.getKey(), entry.getValue()));
        }
        return parelles;
    }


    // Setters

    /**
     * Sobreescriu el nom del text amb el nou nom especificat.
     *
     * @param nom El nou nom que es vol donar al text.
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
     * Sobreescriu el contingut del text amb la seqüència de caràcters de l’entrada.
     *
     * @param cos El nou cos del text que es vol donar.
     *
     * @throws TextEstaBuitException La seqüència d’entrada està buida.
     * @throws NumSimbolsInvalidException La seqüència d’entrada conté més de 100 caràcters diferents.
     */
    void setCos(String cos) throws TextEstaBuitException, NumSimbolsInvalidException {
        if (cos.length() <= 0) throw new TextEstaBuitException();

        //Comprovem que el text no té massa caràcters vàlids
        //obtenint els símbols de l'alfabet d'aquest
        Alfabet.obtainSymbolsFromString(cos);

        this.cos = cos;
    }
}