package edu.upc.prop.teclat.dades;

import edu.upc.prop.teclat.dades.exceptions.InvalidFileException;
import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.ControladorCapaDomini;
import edu.upc.prop.teclat.domini.exceptions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Subclasse de {@link GestorFitxers} que emmagatzema les dades dels alfabets del Sistema.
 * 
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 */
public class GestorAlfabets extends GestorFitxers {
    /**Nom del directori creat pel programa on s'emmagatzemen les dades dels alfabets.*/
    private static final String saveFolder = "alfabets";



    /** Constructora que associa el gestor d'alfabets amb la instància del controlador 
     *  de domini indicada.
     *
     * @param controladorCapaDomini Instància del controlador de la capa de domini.
     */
    public GestorAlfabets(ControladorCapaDomini controladorCapaDomini) {
        super(controladorCapaDomini, saveFolder);
    }


    /** Importa un alfabet indicant el path del seu fitxer.
     *
     * @param path Path de l'alfabet a importar.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomJaExisteixException Ja existeix un alfabet amb el mateix nom que aquell 
     *                                que es vol importar.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws InvalidFileException El fitxer no té l'extensió correcta.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a l'alfabet que es vol importar.
     * @throws NumSimbolsInvalidException L'alfabet a importar està buit o té més de 
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a l'alfabet que es vol importar.
     * @throws NomProhibitException El nom de l'alfabet a importar és 
     *                              {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}, que està prohibit.
     */
    public void importar(Path path) throws IOException, NomJaExisteixException, NomMassaLlargException, InvalidFileException, SimbolRepetitException, NumSimbolsInvalidException, SimbolInvalidException, NomProhibitException {
        // Si no és un fitxer de text, llencem una excepció
        if (!path.getFileName().toString().endsWith(".txt")) throw new InvalidFileException();
        String contingut = Files.readString(path);
        String nom = path.getFileName().toString().replace(".txt", "");
        try {
            controladorCapaDomini.crearAlfabet(nom, contingut);
        } catch (NomBuitException e) {
            // No hauria de passar
            e.printStackTrace();
        }
    }


    /** Guarda en el fitxer indicat pel path els símbols
     *  emmagatzemats a {@code contingut}.
     *
     * @param path Path indicant el fitxer on es desen les dades de {@code contingut}.
     * @param contingut Els símbols que es volen desar al fitxer indicat pel path.
     *                  Representa els símbols d'un alfabet existent al Sistema.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     */
    public void exportar(Path path, String contingut) throws IOException {
        // Escrivim el contingut al fitxer
        Files.writeString(path, contingut);
    }


    /** Actualitza els canvis de l'alfabet identificat per {@code nom} al disc.
     *  Si existeix al domini un alfabet identificat per {@code nom}, actualitza el contingut del fitxer
     *  que l'emmagatzema. Altrament, si existeix un fitxer d'alfabet amb el mateix nom, s'esborra.
     *
     * @param nom Nom de l'alfabet del que volem desar els canvis.
     */
    public void guardarCanvis(String nom) {
        Path path = Path.of(savePath + nom + ".txt");

        // Si no existeix, cal eliminar el fitxer
        if(!controladorCapaDomini.existeixAlfabet(nom)) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // No hauria de passar
                e.printStackTrace();
            }
            return;

        // Si existeix, cal guardar el contingut
        } else {
            try {
                String contingut = controladorCapaDomini.getSimbolsAlfabet(nom);
                createAppData();
                exportar(path, contingut);
            } catch (NomNoExisteixException | IOException e) {
                // No hauria de passar
                e.printStackTrace();
            }
        }
    }


    /** Importa a domini tots els alfabets emmagatzemats dins del 
     *  directori on guarda les dades aquest gestor d'alfabets.
    .*/
    public void carregar() {
        try {
            createAppData();
            Files.list(Path.of(savePath)).forEach(path -> {
                try {
                    importar(path);
                } catch (NomJaExisteixException | NomMassaLlargException | InvalidFileException | IOException | SimbolInvalidException | SimbolRepetitException | NumSimbolsInvalidException | NomProhibitException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            // No hauria de passar
            e.printStackTrace();
        }
    }
}
