package edu.upc.prop.teclat.domini;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomJaExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NomNoExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.SimbolInvalidException;
import edu.upc.prop.teclat.domini.exceptions.SimbolRepetitException;
import edu.upc.prop.teclat.domini.exceptions.TextEstaBuitException;
import edu.upc.prop.teclat.util.Pair;

/**
 * Classe que enregistra tots els textos del Sistema.
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public class CjtTextos {
    //Atributs
    /**TreeMap on s'emmagatzemen els textos*/
    private TreeMap<String, Text> textos;
    

    /** Constructora per defecte
     *	Crea un nou conjunt de textos buit.
     */
    CjtTextos() {
        textos = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }
    

    //Operacions del conjunt

    /**
     * Afegeix al conjunt un nou Text creat a partir de dos Strings
     * que representen el nom i cos del text a crear respectivament.
     *
     * @param nom Nom del text a crear.
     * @param cos Cos del text a crear.
     *
     * @throws NomBuitException El nom està buit.
     * @throws NomJaExisteixException Ja existeix dins del conjunt un text identificat pel nom donat.
     * @throws NomMassaLlargException El nom donat té més de {@value Text#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws TextEstaBuitException El cos del text està buit.
     */
    public void add(String nom, String cos) throws NomJaExisteixException, NomBuitException, NomMassaLlargException, TextEstaBuitException, NumSimbolsInvalidException  {
        nom = nom.strip();
        if(textos.containsKey(nom)) throw new NomJaExisteixException(nom);
        Text text = new Text(nom, cos);
        textos.put(nom, text);
    }

    /**
     * Elimina el text identificat pel nom donat.
     *
     * @param nom Nom del text a esborrar.
     *
     * @throws NomNoExisteixException No existeix cap text identificat pel nom donat.
     */
    void remove(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!textos.containsKey(nom)) throw new NomNoExisteixException();
        textos.remove(nom);
    }

    /**
     * Indica si dins d'aquest conjunt existeix un text identificat pel nom donat.
     *
     * @param nom Nom de text que es vol verificar si es troba al conjunt.
     *
     * @return True si dins del conjunt existeix un text
     *         identificat pel nom donat. Altrament, false.
     */
    boolean exists(String nom) {
        nom = nom.strip();
        return textos.containsKey(nom);
    }

    /**
     * Retorna una llista amb els noms de tots els textos existents
     * dins del conjunt, en ordre lexicogràfic.
     *
     * @return Una llista amb els noms de tots els textos existents
     *         dins del conjunt, en ordre lexicogràfic.
     */
    ArrayList<String> getNoms() {
        ArrayList<String> nomsTextos = new ArrayList<>();
        nomsTextos.addAll(textos.keySet());
        return nomsTextos;
    }

    /**
     * Retorna els noms dels textos tals que tots els símbols que contenen
     * estan presents al conjunt rebut com a paràmetre d'entrada
     *
     * @param simbols El conjunt de símbols la presència dels quals
     *                es vol comprovar a cada text del conjunt.
     *
     * @return Una llista amb els noms dels textos tals que tots els  
     *         símbols que contenen estan presents al conjunt donat.
     */
    ArrayList<String> getCompatibles(TreeSet<Character> simbols) {
        ArrayList<String> compatibles = new ArrayList<>();

        //Per cada text, obtenim el seu alfabet i comprovem si és
        //subconjunt de "simbols"
        for (Map.Entry<String, Text> entry : textos.entrySet()) {
            TreeSet<Character> simbols_text = entry.getValue().getSimbols();
            if (simbols.containsAll(simbols_text)) 
                compatibles.add(entry.getKey());
        }
        return compatibles;
    }


    //Getters d'instància de text

    /**
     * Donat el nom d'un text existent, retorna un String que conté tots els
     * símbols d'aquest text en ordre lexicogràfic.
     *
     * @param nom Nom del text del que s'obtenen els seus símbols.
     *
     * @return Un String que conté tots els símbols en ordre
     *         lexicogràfic del text identificat pel nom donat.
     *
     * @throws NomNoExisteixException No existeix cap text identificat pel nom donat.
     */
    String getCos(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!textos.containsKey(nom)) throw new NomNoExisteixException();
        return textos.get(nom).getCos();
    }

    /**
     * Retorna els símbols que conté el text identificat pel valor del paràmetre d'entrada.
     *
     * @param nom Nom del text del que s'obtenen els seus símbols.
     *
     * @return Un {@link TreeSet} que conté tots els símbols en ordre
     *         lexicogràfic del text identificat pel nom donat.
     *
     * @throws NomNoExisteixException No existeix cap text identificat pel nom donat.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a l'String donat.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'String donat.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     */
    TreeSet<Character> getSimbols(String nom) throws NomNoExisteixException, SimbolInvalidException, SimbolRepetitException, NumSimbolsInvalidException {
        nom = nom.strip();
        if (!textos.containsKey(nom)) throw new NomNoExisteixException();
        return textos.get(nom).getSimbols();
    }


    /**
     * Donat el nom d'una text existent, retorna un String que conté tots els
     * símbols d'aquest text en ordre lexicogràfic i sense repeticions.
     *
     * @param nom Nom del text del que s'obtenen els seus símbols.
     *
     * @return Un String que conté tots els símbols en ordre lexicogràfic   
     *         del text identificat pel nom donat (sense repeticions).
     *
     * @throws NomNoExisteixException No existeix cap text identificat pel nom donat.
     */
    String getSimbolsAsString(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!textos.containsKey(nom)) throw new NomNoExisteixException();
        return textos.get(nom).getSimbolsAsString();
    }

    /**
     * Retorna un {@link Map} d’associacions paraula-freqüència que conté totes les paraules 
     * presents al text junt amb el número de vegades que apareixen en aquest (la freqüència).
     *
     * @param nom Nom del text del que extreure les associacions &lt;paraula, freqüència&gt;.
     *
     * @return El {@link Map} generat.
     *
     * @throws NomNoExisteixException El nom de la llista no existeix.
     */
    Map<String, Integer> getFrequencies(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!textos.containsKey(nom)) throw new NomNoExisteixException();
        return textos.get(nom).getFrequencies();
    }

    /**
     * Donat el nom d'un text present al conjunt, retorna una seqüència de parells 
     * &lt;paraula, freqüència&gt; on cada parella representa una paraula present al cos
     * del text i el número d'ocurrències d'aquesta dins del text (la seva freqüència).
     * 
     * @param nom Nom del text del que extreure les associacions &lt;paraula, freqüència&gt;.
     *
     * @return La seqüència de parells &lt;paraula, freqüència&gt; generada.
     *
     * @throws NomNoExisteixException El nom de l'element no existeix.
     */
    ArrayList<Pair<String, Integer>> getFrequenciesAsPairs(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!textos.containsKey(nom)) throw new NomNoExisteixException();
        return textos.get(nom).getFrequenciesAsPairs();
    }


    //Setters d'instància de text
    /**
     * Donat un nom {@code old_nom} que identifica un text present al conjunt,
     * sobreescriu el nom d'aquest text amb l'indicat per {@code new_nom}.
     *
     * @param old_nom Nom original del text al que canviar el nom.
     * @param new_nom Nou nom pel mateix text.
     *
     * @throws NomBuitException El nom {@code new_nom} no té caràcters.
     * @throws NomMassaLlargException El nom {@code new_nom} té més de {@value Text#MAX_NAME_LENGTH} caràcters.
     * @throws NomJaExisteixException Ja existeix un text dins del conjunt 
     *                                identificat per {@code new_nom}.
     * @throws NomNoExisteixException No existeix dins del conjunt cap
     *                                text identificat per {@code old_nom}.
     */
    void setNom(String old_nom, String new_nom) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, NomNoExisteixException {
        old_nom = old_nom.trim();
        new_nom = new_nom.trim();
        
        if (!textos.containsKey(old_nom)) throw new NomNoExisteixException();
        if(!old_nom.equals(new_nom) && textos.containsKey(new_nom)) throw new NomJaExisteixException(new_nom);
        Text text = textos.get(old_nom);
        text.setNom(new_nom);
        textos.remove(old_nom);
        textos.put(new_nom, text);
    }

    /**
     * Sobreescriu el contingut del text amb la seqüència de caràcters de l’entrada.
     *
     * @param nom nom del text del qual se'n vol sobreescriure el contingut.
     * @param nouCos Nou contingut del text.
     *
     * @throws NomNoExisteixException El nom del text no existeix.
     * @throws TextEstaBuitException La seqüència d’entrada està buida.
     * @throws NumSimbolsInvalidException La seqüència d’entrada conté més de 100 caràcters diferents
     */
    void setCos(String nom, String nouCos) throws NomNoExisteixException, TextEstaBuitException, NumSimbolsInvalidException {
        nom = nom.strip();
        if (!textos.containsKey(nom)) throw new NomNoExisteixException();
        textos.get(nom).setCos(nouCos);
    }
}
