package edu.upc.prop.teclat.domini;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.SimbolInvalidException;
import edu.upc.prop.teclat.domini.exceptions.SimbolRepetitException;
import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomJaExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NomNoExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomProhibitException;

/**
 * Classe que enregistra tots els alfabets del Sistema.
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 */

class CjtAlfabets {
    //Atributs
    /**TreeMap on s'emmagatzemen els alfabets*/
    private TreeMap<String, Alfabet> alfabets;


    /** Constructora per defecte
     *	Crea un nou conjunt d'alfabets buit.
     */
    CjtAlfabets() {
        alfabets = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }


    //Operacions del conjunt

    /**
     * Crea un nou alfabet a partir dels valors indicats i l'afegeix al conjunt
     *
     * @param nom Nom del nou alfabet a crear.
     * @param contingut Seqüència de símbols que indica els que tindrà l'alfabet a crear.
     * 
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix dins del conjunt un alfabet identificat pel nom donat.
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
    void add(String nom, String contingut) throws NomBuitException, NomJaExisteixException, NomMassaLlargException, NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException, NomProhibitException {
        nom = nom.strip();
        if (alfabets.containsKey(nom)) throw new NomJaExisteixException(nom);
        Alfabet alfabet = new Alfabet(nom, contingut);
        alfabets.put(nom, alfabet);
    }

    /**
     * Elimina del conjunt l'alfabet identificat pel nom donat.
     *
     * @param nom Nom de l'alfabet a esborrar del conjunt.
     * 
     * @throws NomNoExisteixException No existeix dins del conjunt cap alfabet identificat pel nom donat.
     */
    void remove(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!alfabets.containsKey(nom)) throw new NomNoExisteixException();
        alfabets.remove(nom);
    }

    /**
     * Indica si dins d'aquest conjunt existeix un alfabet identificat pel nom donat.
     *
     * @param nom Nom de l'alfabet que es vol verificar si es troba al conjunt.
     * 
     * @return True si dins del conjunt existeix un alfabet
     *         identificat pel nom donat. Altrament, false.
     */
    boolean exists(String nom) {
        nom = nom.strip();
        return alfabets.containsKey(nom);
    }

    /**
     * Retorna una llista amb els noms de tots els alfabets existents 
     * dins del conjunt, en ordre lexicogràfic.
     *
     * @return Una llista amb els noms de tots els alfabets existents 
     *         dins del conjunt, en ordre lexicogràfic.
     */
    ArrayList<String> getNoms() {
        ArrayList<String> nomsAlfabets = new ArrayList<>();
        nomsAlfabets.addAll(alfabets.keySet());
        return nomsAlfabets;
    }


    //Getters d'instància d'alfabet

    /**
     * Retorna un {@link TreeSet} que conté tots els simbols de l'alfabet identificat pel nom donat.
     *
     * @param nom Nom de l'alfabet del qual obtenir els seus símbols.
     * 
     * @return Un {@link TreeSet} amb tots els simbols de l'alfabet identificat pel nom donat.
     *
     * @throws NomNoExisteixException No existeix cap alfabet identificat pel nom donat.
     */
    TreeSet<Character> getSimbols(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!alfabets.containsKey(nom)) throw new NomNoExisteixException();
        return alfabets.get(nom).getSimbols();
    }

    /**
     * Donat el nom d'un alfabet existent dins del conjunt, retorna un String que conté tots els
     * símbols d'aquest alfabet en ordre lexicogràfic.
     *
     * @param nom Nom de l'alfabet existent dins del conjunt del qual obtenir els seus símbols.
     * 
     * @return Un String que conté tots els símbols en ordre lexicogràfic de l'alfabet identificat 
     *         pel nom donat.
     *
     * @throws NomNoExisteixException No existeix dins del conjunt cap alfabet identificat pel nom donat.
     */
    String getSimbolsAsString(String nom) throws NomNoExisteixException {
        nom = nom.strip();
        if (!alfabets.containsKey(nom)) throw new NomNoExisteixException();
        return alfabets.get(nom).getSimbolsAsString();
    }   


    //Setters d'instància d'alfabet

    /**
     * Donada un alfabet identificat pel nom {@code old_nom},
     * sobreescriu el seu nom amb l'indicat per {@code new_nom}.
     *
     * @param old_nom Nom original de l'alfabet.
     * @param new_nom Nou nom de l'alfabet.
     * 
     * @throws NomBuitException El nom {@code new_nom} no té caràcters.
     * @throws NomMassaLlargException El nom {@code new_nom} té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws NomJaExisteixException Ja existeix un alfabet identificat per {@code new_nom}.
     * @throws NomNoExisteixException No existeix dins del conjunt cap alfabet identificat per 
     *                                {@code old_nom}.
     * @throws NomProhibitException El nom {@code new_nom} és {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET},
     *                              que està prohibit.
     */
    void setNom(String old_nom, String new_nom) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, NomNoExisteixException, NomProhibitException {
        old_nom = old_nom.trim();
        new_nom = new_nom.trim();

        if (old_nom.equals(Alfabet.KEYBOARD_ORIGINAL_ALPHABET) || 
            new_nom.equals(Alfabet.KEYBOARD_ORIGINAL_ALPHABET))
            throw new NomProhibitException();
        if (!alfabets.containsKey(old_nom)) throw new NomNoExisteixException();
        if (!old_nom.equals(new_nom) && alfabets.containsKey(new_nom)) throw new NomJaExisteixException(new_nom);
        Alfabet alpha = alfabets.get(old_nom);
        alpha.setNom(new_nom);
        alfabets.remove(old_nom);
        alfabets.put(new_nom, alpha);
    }

    /**
     * Sobreescriu els símbols de l'alfabet identificat per {@code nom} amb els nous símbols indicats a {@code contingut}.
     *
     * @param nom Nom de l'alfabet al que modificar els símbols.
     * @param contingut Els nous símbols per l'alfabet indicat.
     * 
     * @throws NomNoExisteixException No existeix dins del conjunt cap alfabet identificat pel nom donat.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de 
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'String {@code contingut} donat.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a l'String {@code contingut} donat.
     */
    void setSimbols(String nom, String contingut) throws NomNoExisteixException, NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException {
        nom = nom.strip();
        if (!alfabets.containsKey(nom)) throw new NomNoExisteixException();
        alfabets.get(nom).setSimbols(contingut);
    }
}