package edu.upc.prop.teclat.domini.exceptions;

/**Excepció que es llença quan un element ja creat al sistema té el nom d'un element amb el mateix
 * nom que es vo crear. */
public class NomJaExisteixException extends Exception {

    /** Nom de l'element. */
    private final String nom;

    /** Constructora que crea l'excepció mencionada.

     * @param nom nom de l'element.
     */
    public NomJaExisteixException(String nom) {
        super("Ja existeix un element amb aquest nom");
        this.nom = nom;
    }

    /** Retorna el nom de l'element.

     * @return retorna el nom de l'element.
     */
    public String getNom() {
        return nom;
    }
}
