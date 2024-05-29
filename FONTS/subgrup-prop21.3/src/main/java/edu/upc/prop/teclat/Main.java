package edu.upc.prop.teclat;

import edu.upc.prop.teclat.presentacio.ControladorCapaPresentacio;

/**
 * Classe Main. Punt d'entrada del programa.
 */
public class Main {

    /*
     * Punt d'entrada del codi. Crida al controlador de presentació per iniciar l'aplicació.
     */
    public static void main(String[] args) throws Exception {
        ControladorCapaPresentacio c = new ControladorCapaPresentacio();
        c.inicialitzarPresentacio();
    }
}
