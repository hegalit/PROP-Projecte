package edu.upc.prop.teclat.domini;

import java.io.Serializable;
import java.util.TreeSet;
import java.lang.Math;

import edu.upc.prop.teclat.domini.exceptions.SimbolInvalidException;
import edu.upc.prop.teclat.domini.generatoralgorithms.GeneratorAlgorithm;
import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NomProhibitException;
import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.SimbolRepetitException;
import edu.upc.prop.teclat.domini.exceptions.teclat.CaractersNoInclososException;
import edu.upc.prop.teclat.domini.exceptions.teclat.IndexosInvalidsException;
import edu.upc.prop.teclat.domini.exceptions.teclat.LayoutInvalidException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingPairsFreqException;

/**
 * Classe que representa una distribució de símbols dins d'una graella
 * @author Héctor García Lirola (hector.garcia.lirola@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */

public class Teclat implements Serializable {
    //Constants

    /**Llargada màxima permesa pel nom.*/
    public static final int MAX_NAME_LENGTH = 100;

    /**Amplada mínima del teclat.*/
    private static final int MIN_WIDTH = 1;

    /**Amplada màxima del teclat.*/
    private static final int MAX_WIDTH = 18;

    /**Alçada mínima del teclat.*/
    private static final int MIN_HEIGHT = 1;

    /**Alçada màxima del teclat.*/
    private static final int MAX_HEIGHT = 10;


    //Atributs
    
    /**El nom d'un teclat.*/
    private String nom;

    /**L'alfabet que emmagatzema els símbols del teclat*/
    private Alfabet alfabet;

    /**Disposició de símbols del teclat*/
    private char[] layout;

    /**Nombre de columnes del teclat*/
    private int cols;


    //Constructora

    /** Construeix un teclat a partir d'un nom, una seqüència de símbols sense repeticions
     *  i un enter que representa el nombre de columnes que tindrà el teclat.
     *
     * @param nom Nom del teclat a crear.
     * @param layout Seqüència de símbols sense repeticions que indica els símbols presents
     *               dins del teclat a crear. Aquests es disposaran dins del teclat per files
     *               i en el mateix ordre en què es donin.
     * @param cols Nombre de columnes del teclat a crear. Si el número de columnes no és aplicable
     *             (per exemple, per ser massa gran o massa petit), es corregirà automàticament.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomMassaLlargException El nom donat té més de {@value MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'String {@code layout} donat.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a l'String {@code layout} donat.
     */
    Teclat(String nom, String layout, int cols) throws NomBuitException, NomMassaLlargException, NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException {
        setNom(nom);
        try {
            this.alfabet = new Alfabet("Keyboard's alphabet", layout);
        } catch (NomProhibitException e) {
            //No pot passar perquè estem imposant el nom
        }
        this.layout = layout.toCharArray();
        setCols(cols);
    }


    //Getters

    /**
     * Retorna el nom del teclat.
     * @return El nom del teclat.
     */
    String getNom() {
        return nom;
    }

    /**
     * Retorna el nombre de files del teclat.
     * @return El nombre de files del teclat.
     */
    int getRows() {
        return missingSideLength(cols);
    }

    /**
     * Retorna el nombre de columnes del teclat.
     * @return El nombre de columnes del teclat.
     */
    int getCols() {
        return cols;
    }

    /**
     * Retorna un {@link TreeSet} que conté tots els simbols del teclat.
     * @return Un {@link TreeSet} amb tots els simbols del teclat.
     */
    TreeSet<Character> getSimbols() {
        TreeSet<Character> simbols = new TreeSet<>();
        simbols.addAll(alfabet.getSimbols());
        return simbols;
    }

    /**
     * Retorna un String que conté els símbols del teclat en ordre lexicogràfic
     * i sense repeticions.
     * @return Un String que conté els símbols del teclat en ordre lexicogràfic
     * i sense repeticions.
     */
    String getSimbolsAsString() {
        return alfabet.getSimbolsAsString();
    }

    /**
     * Retorna un char array amb els símbols que el teclat conté, en l'ordre en 
     * què es troben dins d'aquest (recorrent el teclat per files).
     * @return Un char array amb els símbols que el teclat conté, en l'ordre en 
     * què es troben dins d'aquest (recorrent el teclat per files).
     */
    char[] getLayout() {
        return layout;
    }

    /** Retorna el cost de la distribució de símbols actual del teclat, aplicant les
     *  freqüències entre parells de símbols obtingudes pel {@link PairsFrequency} d’entrada.
     *
     * @param pairsFreq Un {@link PairsFrequency} d'on consultar la freqüència de 
     *                  cada parell de símbols possible.
     *
     * @return El cost de la distribució de símbols actual del teclat, aplicant les freqüències
     *         entre parells de símbols obtingudes pel {@link PairsFrequency} d’entrada.
     *
     * @throws MissingPairsFreqException S’ha rebut una referència nul·la de {@code pairsFreq}
     */
    double getCost(PairsFrequency pairsFreq) throws MissingPairsFreqException {
        if (pairsFreq == null) throw new MissingPairsFreqException();

        //Tractem totes les combinacions de símbols possibles dins del teclat
        //sense repetir combinacions de símbols ja tractades
        double cost = 0;
        for (int i = 0; i < layout.length; ++i) {
            int row_i = i / cols;
            int col_i = i % cols;
            for (int j = i + 1; j < layout.length; ++j) {
                int row_j = j / cols;
                int col_j = j % cols;

                //Afegim la freqüència del parell de caràcters 
                //multiplicada per la seva distància dins del layout
                cost += pairsFreq.get(layout[i], layout[j]) * distance(row_i, col_i, row_j, col_j);
            }
        }
        return cost;
    }


    //Setters

    /**
     * Sobreescriu el nom del teclat amb el nou nom especificat.
     *
     * @param nom El nou nom que es vol donar al teclat.
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
     * Modifica les dimensions del teclat en funció del número de files. 
     * Si aquest últim no és aplicable, el nombre de files passarà a ser
     * el valor aplicable més proper al donat.
     *
     * @param rows El nombre de files que es vol donar al teclat.
     */
    void setRows(int rows) {
        //Amplada i alçada màximes que el teclat no pot excedir
        int max_length_h = Math.min(alfabet.getSimbols().size(), MAX_WIDTH);
        int max_length_v = Math.min(alfabet.getSimbols().size(), MAX_HEIGHT);

        //Noves dimensions a aplicar
        rows = Math.max(MIN_HEIGHT, Math.min(max_length_v, rows));
        this.cols = Math.max(MIN_WIDTH, Math.min(max_length_h, missingSideLength(rows)));
    }

    /**
     * Modifica les dimensions del teclat en funció del número de columnes. 
     * Si aquest últim no és aplicable, el nombre de columnes passarà a ser
     * el valor aplicable més proper al donat.
     *
     * @param cols El nombre de columnes que es vol donar al teclat.
     */
    void setCols(int cols) {
        //Amplada i alçada màximes que el teclat no pot excedir
        int max_width = Math.min(alfabet.getSimbols().size(), MAX_WIDTH);
        int max_height = Math.min(alfabet.getSimbols().size(), MAX_HEIGHT);
        
        //Potencials dimensions a aplicar
        this.cols = Math.max(MIN_WIDTH, Math.min(max_width, cols));
        int rows = missingSideLength(this.cols);

        //Si el nombre de files a aplicar supera l'alçada màxima, cal 
        //modificar les dimensions calculades prèviament
        if (rows > max_height) {
            rows = Math.max(MIN_HEIGHT, Math.min(max_height, rows));
            this.cols = Math.max(MIN_WIDTH, Math.min(max_width, missingSideLength(rows)));
        }
    }

    /**
     * Sobreescriu els símbols que l'alfabet del teclat conté amb els nous símbols especificats.
     * A més a més, es reemplacen els símbols de la disposició pels que conté l'String, i es  
     * col·loquen per files ordenats lexicogràficament. Conseqüentment s'adapten les dimensions
     * del teclat a la nova disposició tal que el ràtio amplada-alçada sigui 2.
     *
     * @param simbols Els símbols que passarà a tenir l'alfabet del teclat.
     *
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'String donat.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a l'String donat.
     */
    void setSimbols(String simbols) throws NumSimbolsInvalidException, SimbolRepetitException, SimbolInvalidException {
        alfabet.setSimbols(new String(simbols));
        layout = alfabet.getSimbolsAsString().toCharArray();

        //Modifica les dimensions del teclat en funció 
        //del nombre de símbols del nou alfabet
        double ratio = 2.0;
        int rows = (int)Math.ceil(Math.sqrt(layout.length/ratio));
        setRows(rows);
    }

    /**
     * Sobreescriu la disposició de símbols del teclat amb la del char array donat.
     *
     * @param input_layout La nova disposició de símbols del teclat.
     *
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a la seqüència de símbols donada.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a la seqüència de símbols donada.
     * @throws LayoutInvalidException Els símbols presents al char array donat no són els mateixos
     *                                que té l'alfabet del teclat.
     */
    void setLayout(char[] input_layout) throws SimbolInvalidException, SimbolRepetitException, LayoutInvalidException {
        //Obtenim els símbols del l'alfabet del teclat
        TreeSet<Character> alfa_symbols = alfabet.getSimbols();

        //Obtenim els símbols de la seqüència de caràcters d'entrada
        TreeSet<Character> input_symbols = new TreeSet<>();
        for (int i = 0; i < layout.length; ++i) {
            char c = layout[i];
            if (Alfabet.invalid_symbols.contains(c)) throw new SimbolInvalidException();
            if (input_symbols.contains(c)) throw new SimbolRepetitException();
            input_symbols.add(c);
        }

        //Si l'alfabet del teclat no té exactament els mateixos símbols que
        //la seqüència d'entrada, no podem sobreescriure el layout
        if (!alfa_symbols.equals(input_symbols)) throw new LayoutInvalidException();
        System.arraycopy(input_layout, 0, this.layout, 0, input_layout.length);
    }


    //Altres mètodes d'edició del layout

    /**
     * Modifica la disposició dels símbols del teclat a partir de l'algorisme generador donat i les
     * freqüències entre parells de caràcters obtingudes a partir del {@link PairsFrequency} donat.
     *
     * @param algorithm L'algorisme generador que s'aplicarà per a regenerar la disposició dels símbols.
     * @param pairs El {@link PairsFrequency} emprat a l'algorisme generador.
     *
     * @throws CaractersNoInclososException L'alfabet del teclat no conté tots els símbols necessaris per a regenerar el teclat.
     */
    void regenerate(GeneratorAlgorithm algorithm, PairsFrequency pairs) throws CaractersNoInclososException {
        //Comprovem si hi ha símbols conflictius no continguts a l'alfabet del teclat
        TreeSet<Character> symbols_alphabet = alfabet.getSimbols();
        TreeSet<Character> symbols_pairs = pairs.getSimbols();
        if (!symbols_alphabet.containsAll(symbols_pairs)) throw new CaractersNoInclososException();

        //Generem una nova disposició de tecles (layout) pel teclat
        layout = algorithm.solve(alfabet.getSimbolsAsString(), cols, pairs);
    }

    /**
     * Intercanvia els símbols de la disposició ubicats als índexos k1 i k2.
     *
     * @param k1 Index del primer símbol.
     * @param k2 Index del segon símbol.
     *
     * @throws IndexosInvalidsException S'ha intentat accedir a una posició incorrecta.
     */
    void swapKeys(int k1, int k2) throws IndexosInvalidsException {
        //Comprovem que els índexos son vàlids
        if (k1 >= 0 && k1 < layout.length && k2 >= 0 && k2 < layout.length && k1 != k2) {
            char c = layout[k1];
            layout[k1] = layout[k2];
            layout[k2] = c;
        }
        else throw new IndexosInvalidsException(k1, k2, layout.length);
    }

    
    //Mètodes privats

    /**
     * Calcula la mida del costat desconegut de les dimensions del teclat,
     * imposant com a mida d'un costat el valor indicat a {@code side_length}.
     *
     * @param side_length Mida del costat conegut de les dimensions.
     * 
     * @return La mida del costat desconegut de les dimensions.
     */
    private int missingSideLength(int side_length) {
        return (int)Math.ceil((double)layout.length / side_length);
    }

    /**
     * Retorna la distància Euclidiana entre dos punts.
     *
     * @param x1 coordenada x del punt 1.
     * @param y1 coordenada y del punt 1.
     * @param x2 coordenada x del punt 2.
     * @param y2 coordenada y del punt 2.
     * 
     * @return La distància entre els punts 1 i 2.
     */
    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }
}
