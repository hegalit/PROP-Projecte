package edu.upc.prop.teclat.dades;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.upc.prop.teclat.dades.exceptions.InvalidFileException;
import edu.upc.prop.teclat.domini.ControladorCapaDomini;
import edu.upc.prop.teclat.domini.Teclat;
import edu.upc.prop.teclat.domini.exceptions.*;

/**
 * Subclasse de {@link GestorFitxers} que emmagatzema les dades dels teclats del Sistema.
 * 
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 */
public class GestorTeclats extends GestorFitxers{
    /**Nom del directori creat pel programa on s'emmagatzemen les dades dels teclats.*/
    private static final String saveFolder = "teclats";


    
    /** Constructora que associa el gestor de teclats amb la instància del controlador 
     *  de domini indicada.
     *
     * @param controladorCapaDomini Instància del controlador de la capa de domini.
     */
    public GestorTeclats(ControladorCapaDomini controladorCapaDomini) {
        super(controladorCapaDomini, saveFolder);
    }


    /** Importa un teclat indicant el path del seu fitxer.
     *
     * @param path Path del teclat a importar.
     *
     * @throws ClassNotFoundException No s'ha trobat la classe.
     * @throws InvalidFileException El fitxer donat no té el format demanat.
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     * @throws NomJaExisteixException El nom del teclat a importar té més de {@value Teclat#MAX_NAME_LENGTH} 
     *                                caràcters.
     */
    public void importar(Path path) throws InvalidFileException, IOException, ClassNotFoundException, NomJaExisteixException {
        // Si no és un fitxer de teclat (.tcl), llencem una excepció
        if (!path.getFileName().toString().endsWith(".tcl")) throw new InvalidFileException();
        
        FileInputStream fis = new FileInputStream(path.toString());
        ObjectInputStream ois = new ObjectInputStream(fis);
        Teclat teclat = (Teclat) ois.readObject();
        ois.close();
        fis.close();
        controladorCapaDomini.afegirTeclat(teclat);
    }


    /** Guarda en el fitxer indicat pel path les dades del teclat donat.
     *
     * @param path Path indicant el fitxer on es desen les dades del teclat donat.
     *
     * @throws IOException S'ha produit un error en l'entrada/sortida del programa.
     */
    public void exportar(Path path, Teclat teclat) throws IOException {
        FileOutputStream fos = new FileOutputStream(path.toString());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(teclat);
        oos.close();
        fos.close();
    }


    /** Actualitza els canvis del teclat identificat per {@code nom} al disc.
     *  Si existeix al domini un teclat identificat per {@code nom}, actualitza el contingut del fitxer
     *  que l'emmagatzema. Altrament, si existeix un fitxer de teclat amb el mateix nom, s'esborra.
     *
     * @param nom Nom del teclat.
     */
    public void guardarCanvis(String nom) {
        Path path = Path.of(savePath + nom + ".tcl");

        // Si no existeix, cal eliminar el fitxer
        if(!controladorCapaDomini.existeixTeclat(nom)) {
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
                Teclat teclat = controladorCapaDomini.getTeclat(nom);
                createAppData();
                exportar(path, teclat);
            } catch (FileNotFoundException e) {
                // No hauria de passar
                e.printStackTrace();
            } catch (NomNoExisteixException | IOException e) {
                // No hauria de passar
                e.printStackTrace();
            }
        }
    }

    
    /** Importa a domini tots els teclats emmagatzemats dins del 
     *  directori on guarda les dades aquest gestor de teclats.
    .*/
    public void carregar() {
        try {
            createAppData();
            Files.list(Path.of(savePath)).forEach(path -> {
                try {
                    importar(path);
                } catch (InvalidFileException | IOException | ClassNotFoundException | NomJaExisteixException e) {
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
