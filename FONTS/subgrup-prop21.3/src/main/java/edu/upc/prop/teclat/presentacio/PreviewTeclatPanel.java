package edu.upc.prop.teclat.presentacio;

import edu.upc.prop.teclat.domini.exceptions.teclat.IndexosInvalidsException;
import edu.upc.prop.teclat.domini.exceptions.teclat.TeclatTemporalBuitException;

/**
 * JPanel que mostra un teclat visualment.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
class PreviewTeclatPanel extends javax.swing.JPanel {
    //Constants

    /**Determina la tecla seleccionada*/
    private int teclaSeleccionada = -1;

    /**Instancia de la gestió de tecltas*/
    private GestioTeclatsFrame gestioTeclatsFrame;



    /** Constructora de PreviewTeclatPanel.
     * Requereix d'una instancia de {@link GestioTeclatsFrame}, junt amb la disposició de símbols i 
     * nombre de columnes d'un teclat.
     *
     * @param gestioTeclatsFrame Instància de gestió de teclats.
     * @param layout Disposició de símbols dins d'un teclat.
     * @param columnes Nombre de columnes del teclat.
     */
    PreviewTeclatPanel(GestioTeclatsFrame gestioTeclatsFrame, char[] layout, int columnes) {;
        this.gestioTeclatsFrame = gestioTeclatsFrame;
        initComponents();
        update(layout, columnes);
    }

    /**Actualitza la interficie que mostra la disposició de símbols del teclat.
     *
     * @param layout Disposició de símbols del teclat.
     * @param columnes Nombre de columnes del teclat.
     */
    void update(char[] layout, int columnes) {
        teclaSeleccionada = -1;
        removeAll();
        setLayout(new java.awt.GridLayout(0, columnes, 5, 5));
        for (int i = 0; i < layout.length; i++) {
            PreviewTeclaPanel teclaPane = new PreviewTeclaPanel(i, layout[i]);
            teclaPane.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    pitjarTecla(((PreviewTeclaPanel) evt.getSource()).getID());
                }
            });
            add(teclaPane);
        }
        revalidate();
        repaint();
    }

    /** Selecciona la tecla indicada per l'ID.
     *
     * @param ID identificador de la tecla.
     */
    void pitjarTecla(int ID) {
        if (teclaSeleccionada == ID) {
            ((PreviewTeclaPanel) getComponent(teclaSeleccionada)).setSelected(false);
            teclaSeleccionada = -1;
        } else if(teclaSeleccionada == -1) {
            ((PreviewTeclaPanel) getComponent(ID)).setSelected(true);
            teclaSeleccionada = ID;
        } else {
            try {
                gestioTeclatsFrame.swapKeys(ID, teclaSeleccionada);
            } catch (TeclatTemporalBuitException | IndexosInvalidsException e) {
                // No hauria de passar
                e.printStackTrace();
            }
        }
    }

    /** Inicialitza els components de la interfície.*/
    //GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridLayout(0, 3, 5, 5));
    }//GEN-END:initComponents


    //GEN-BEGIN:variables
    //GEN-END:variables
}
