package edu.upc.prop.teclat.dades;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.IOException;
import java.util.ArrayList;

import edu.upc.prop.teclat.dades.exceptions.InvalidFileException;
import edu.upc.prop.teclat.dades.exceptions.InvalidFormatException;
import edu.upc.prop.teclat.domini.ControladorCapaDomini;
import edu.upc.prop.teclat.domini.LlistaDeFrequencia;
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
 * Subclasse de {@link GestorFitxers} que emmagatzema les dades de les
 * llistes de freqüències del Sistema.
 * 
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public class GestorLlistesFreq extends GestorFitxers {
    /**Nom del directori creat pel programa on s'emmagatzemen les dades de les
     * llistes de freqüències.*/
    private static final String saveFolder = "llistesFreq";


    
    /** Constructora que associa el gestor de llistes de freqüències amb la 
     *  instància del controlador de domini indicada.
     *
     * @param controladorCapaDomini Instància del controlador de la capa de domini.
     */
    public GestorLlistesFreq(ControladorCapaDomini controladorCapaDomini) {
        super(controladorCapaDomini, saveFolder);
    }


    /**
     * Importa una llista de freqüències indicant el path del seu fitxer.
     *
     * @param path Path de la llista de freqüències a importar.
     *
     * @throws IOException Si ha hagut un error de lectura.
     * @throws NomJaExisteixException Si ja hi ha una llista de freqüències amb aquest nom al domini.
     * @throws NomMassaLlargException El nom donat té més de {@value LlistaDeFrequencia#MAX_NAME_LENGTH} 
     *                                caràcters.
     * @throws InvalidFormatException El fitxer no té el format correcte.
     * @throws InvalidFileException El fitxer no té l'extensió correcta.
     */
    public void importar(Path path) throws IOException, NomJaExisteixException, NomMassaLlargException, InvalidFileException, InvalidFormatException {
        // Si no és un fitxer de text, llencem una excepció
        if (!path.getFileName().toString().endsWith(".txt")) throw new InvalidFileException();
        
        // Llegir el fitxer
        String nom = path.getFileName().toString().replace(".txt", "");
        String data = Files.readString(path);
        ArrayList<Pair<String, Integer>> contingut = new ArrayList<Pair<String, Integer>>();

        // Interpretem cada línia del fitxer de text com un parell (Paraula, freqüència)
        for (String linia : data.split("\\r?\\n")) {
            String[] paraules = linia.split(" ");
            if (paraules.length != 2) throw new InvalidFormatException();
            int freq;
            try {
                freq = Integer.parseInt(paraules[1]);
            } catch (NumberFormatException e) {
                throw new InvalidFormatException();
            }
            contingut.add(new Pair<String, Integer>(paraules[0], freq));
        }
        
        // Creem la llista al domini
        try {
            controladorCapaDomini.crearLlistaFrequencia(nom, contingut);
        } catch (NomBuitException | NombreElementsInvalidException e) {
            // No pot passar
            e.printStackTrace();
        } catch (NumSimbolsInvalidException | ParaulaBuidaException | InvalidFrequencyException | SimbolInvalidException | ParaulaRepetidaException e) {
            throw new InvalidFormatException();
        }
    }


    /**
     * Guarda en el fitxer indicat pel path les associacions paraula-freqüència
     * emmagatzemades a {@code contingut}.
     *
     * @param path Path indicant el fitxer on es desen les dades de {@code contingut}.
     * @param contingut Les associacions paraula-freqüència que es volen desar al fitxer indicat pel path.
     *                  Representa el contingut d'una llista de freqüències existent al Sistema.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa
     */
    public void exportar(Path path, ArrayList<Pair<String, Integer>> contingut) throws IOException {
        String data = "";
        // Per cada parell en contingut, l'escrivim al fitxer d'output separats per un salt de línia
        for (Pair<String, Integer> pair : contingut) {
            data += pair.getFirst() + " " + pair.getSecond() + "\n";
        }
        Files.writeString(path, data);
    }


    /** Actualitza els canvis de la llista de freqüències identificada per {@code nom} al disc.
     *  Si existeix al domini una llista de freqüències identificada per {@code nom}, actualitza el contingut del fitxer
     *  que la emmagatzema. Altrament, si existeix un fitxer de llistes de freqüències amb el mateix nom, s'esborra.
     *
     * @param nom Nom de la llista de freqüències de la que volem desar els canvis.
     */
    public void guardarCanvis(String nom) {
        Path path = Path.of(savePath + nom + ".txt");

        // Si no existeix, cal eliminar el fitxer
        if(!controladorCapaDomini.existeixLlistaDeFrequencia(nom)) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // No hauria de passar
                e.printStackTrace();
            }
        // Si existeix, cal guardar el contingut
        } else {
            try {
                ArrayList<Pair<String, Integer>> contingut = controladorCapaDomini.getContingutLlistaFreq(nom);
                createAppData();
                exportar(path, contingut);
            } catch (IOException | NomNoExisteixException e) {
                // No hauria de passar
                e.printStackTrace();
            }
        }
    }

    
    /** Importa a domini totes les llistes de freqüències emmagatzemades dins del 
     *  directori on guarda les dades aquest gestor de llistes de freqüències.
    .*/
    public void carregar() {
        try {
            createAppData();
            Files.list(Path.of(savePath)).forEach(path -> {
                try {
                    importar(path);
                } catch (IOException | NomJaExisteixException | NomMassaLlargException | InvalidFileException | InvalidFormatException e) {
                    // No hauria de passar
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            // No hauria de passar
            e.printStackTrace();
        }
    }
}
