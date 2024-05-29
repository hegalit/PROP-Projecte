package edu.upc.prop.teclat.dades;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.upc.prop.teclat.domini.ControladorCapaDomini;

/**
 * Classe que determina la ubicació on s'emmagatzemen els fitxers i directoris creats
 * pel programa.
 * 
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public abstract class GestorFitxers {
    //Constants

    /** Nom del sistema operatiu. */
    private static final String OS = (System.getProperty("os.name")).toUpperCase();

    /** Nom de la carpeta principal creada per la gestió de fitxers del programa.
     *  Dins d'aquesta es troben la resta de subdirectoris creats per l'aplicació.
    */
    private final String appFolderName = "gestorTeclatsG21.3";

    /** Path complet indicant la ubicació del directori {@code appFolderName} 
     *  dins del sistema operatiu. A Windows aques directori es trobarà a "AppData",
     *  mentre que a Linux serà a "user.dir" */
    private final String appPath;

    /** Path complet que indica la ubicació d'un subdirectori de {@code appFolderName}
     *  on es guarden els fitxers creats pel programa.*/
    final String savePath;

    /** Instància del controlador de domini. */
    final ControladorCapaDomini controladorCapaDomini;


    
    /**
     * Constructora que associa el gestor de fitxers a una instància del controlador
     * de domini i determina el path on desar els fitxers del programa indicant
     * el nom del subdirectori de la carpeta principal de l'aplicació.
     *
     * @param controladorCapaDomini Instància del controlador de domini.
     * @param saveFolder Nom del subdirectori de la carpeta principal de l'aplicació
     *                   on guardarà els fitxers. Si es rep un nom de carpeta buit, desarà 
     *                   els fitxers directament a la carpeta principal de l'aplicació.
     */
    public GestorFitxers(ControladorCapaDomini controladorCapaDomini, String saveFolder) {
        this.controladorCapaDomini = controladorCapaDomini;
        
        // Determina el directori d'AppData
        final String workingDirectory;
        if (OS.contains("WIN")) {
            // En windows guardem les dades a %AppData%
            workingDirectory = System.getenv("AppData");
        } else {
            // En linux guardem les dades a user.dir
            // Es tracta de la carpeta desde la qual s'executa java
            workingDirectory = System.getProperty("user.dir");
        }
        appPath = workingDirectory + "/" + appFolderName + "/";
        this.savePath = appPath + saveFolder + "/";
    }


    /**
     * Crea el directori principal de l'aplicació i el subdirectori on guardar els fitxers 
     * creats (només si no existeixen).
     */
    void createAppData() {
        try {
            if(Files.notExists(Path.of(appPath))) Files.createDirectory(Path.of(appPath));
            if(Files.notExists(Path.of(savePath))) Files.createDirectory(Path.of(savePath));
        } catch (IOException e) {
            // No hauria de passar
            e.printStackTrace();
        }
    }

    
    /**
     * Aquesta funció guarda el fitxer amb nom "nom" a persistència.
     *
     * @param nom nom del fitxer a guardar.
     */
    public abstract void guardarCanvis(String nom);
}
