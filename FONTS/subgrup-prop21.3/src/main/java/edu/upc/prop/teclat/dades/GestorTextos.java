package edu.upc.prop.teclat.dades;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import edu.upc.prop.teclat.dades.exceptions.InvalidFileException;
import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.ControladorCapaDomini;
import edu.upc.prop.teclat.domini.Text;
import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomJaExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NomNoExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NumSimbolsInvalidException;
import edu.upc.prop.teclat.domini.exceptions.TextEstaBuitException;

/**
 * Subclasse de {@link GestorFitxers} que emmagatzema les dades dels textos del Sistema.
 * 
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public class GestorTextos extends GestorFitxers{
    /**Nom del directori creat pel programa on s'emmagatzemen les dades dels textos.*/
    private static final String saveFolder = "textos";



    /** Constructora que associa el gestor de textos amb la instància del controlador 
     *  de domini indicada.
     *
     * @param controladorCapaDomini Instància del controlador de la capa de domini.
     */
    public GestorTextos(ControladorCapaDomini controladorCapaDomini) {
        super(controladorCapaDomini, saveFolder);
    }


    /**
     * Importa un text indicant el path del seu fitxer.
     *
     * @param path Path del text a importar.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomJaExisteixException Ja existeix al Sistema un text identificat pel nom del
     *                                text a importar.
     * @throws NomMassaLlargException El nom del text a importar té més de {@value Text#MAX_NAME_LENGTH} 
     *                                caràcters.
     * @throws TextEstaBuitException La seqüència d’entrada està buida.
     * @throws InvalidFileException El fitxer no té l'extensió correcta.
     * @throws NumSimbolsInvalidException La seqüència d’entrada conté més de 
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} caràcters diferents.
     */
    public void importar(Path path) throws IOException, NomJaExisteixException, NomMassaLlargException, TextEstaBuitException, InvalidFileException, NumSimbolsInvalidException {
        // Si no és un fitxer de text, llencem una excepció
        if (!path.getFileName().toString().endsWith(".txt")) throw new InvalidFileException();
        String contingut = Files.readString(path);
        String nom = path.getFileName().toString().replace(".txt", "");
        try {
            controladorCapaDomini.crearText(nom, contingut);
        } catch (NomBuitException e) {
            // No pot passar
            e.printStackTrace();
        }
    }


    /**
     *  Guarda en el fitxer indicat pel path la seqüència de 
     *  caràcters emmagatzemada a {@code contingut}.
     *
     * @param path Path indicant el fitxer on es desen les dades de {@code contingut}.
     * @param contingut Seqüència de caràcters que volem guardar. Representa el cos d'un
     *                  text existent al Sistema.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa
     */
    public void exportar(Path path, String contingut) throws IOException {
        Files.writeString(path, contingut);
    }



    /**
     * Actualitza els canvis del text identificat per {@code nom} al disc.
     * Si existeix al domini un text identificat per {@code nom}, actualitza el contingut del fitxer
     * que l'emmagatzema. Altrament, si existeix un fitxer de text amb el mateix nom, s'esborra.
     *
     * @param nom Nom del text del que volem desar els canvis.
     */
    public void guardarCanvis(String nom) {
        Path path = Path.of(savePath + nom + ".txt");

        // Si no existeix, cal eliminar el fitxer
        if(!controladorCapaDomini.existeixText(nom)) {
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
                String contingut = controladorCapaDomini.getCosText(nom);
                createAppData();
                exportar(path, contingut);
            } catch (NomNoExisteixException | IOException e) {
                // No hauria de passar
                e.printStackTrace();
            }
        }
    }


    /** Importa a domini tots els textos emmagatzemats dins del 
     *  directori on guarda les dades aquest gestor de textos.
    .*/
    public void carregar() {
        try {
            createAppData();
            Files.list(Path.of(savePath)).forEach(path -> {
                try {
                    importar(path);
                } catch (IOException | NomJaExisteixException | NomMassaLlargException | TextEstaBuitException | InvalidFileException | NumSimbolsInvalidException e) {
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
