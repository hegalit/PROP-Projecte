package edu.upc.prop.teclat.presentacio;

/**
 * Classe que representa una tecla dins de la interfície d'usuari que mostra la disposició de símbols
 * d'un teclat.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
class PreviewTeclaPanel extends javax.swing.JPanel {
    /**Id de la tecla*/
    private final int ID;


    /** Construeix una tecla a partir d'un id i el símbol que portarà la tecla.
     *
     * @param ID id de la tecla.
     * @param c símbol de la tecla.
     */
    PreviewTeclaPanel(int ID, char c) {
        this.ID = ID;
        initComponents();
        jLabel1.setText(String.valueOf(c));
    }

    /** Commuta la selecció de la tecla.
     *
     * @param b Si està en true, indica que la tecla ha d'estar seleccionada.
     *          Altrament, la tecla estarà desseleccionada.
     */
    void setSelected(boolean b) {
        if (b) {
            setBackground(new java.awt.Color(130, 148, 196));
        } else {
            setBackground(new java.awt.Color(214, 217, 223));
        }
    }

    /** Retorna l'ID de la tecla.
     *
     * @return ID de la tecla.
     */
    int getID() {
        return ID;
    }

    /**Funció que inicialitza els components del panel.*/
    //GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setPreferredSize(new java.awt.Dimension(30, 30));

        jLabel1.setText("a");
        add(jLabel1);
    }//GEN-END:initComponents


    //GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    //GEN-END:variables
}
