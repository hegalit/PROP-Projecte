package edu.upc.prop.teclat.presentacio;

/**
 * Aquesta classe representa un element que es mostrarà en els panells de l'aplicació.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
class VistaPrincipalElemPanel extends javax.swing.JPanel {
    /**
     * Nom de l'element del panell
     */
    private String nom;

    /**
     * Constructora de l'element del panell customitzats.
     * Requereix com a paràmetre el text o nom a mostrar en el panell.
     *
     * @param nom Nom de l'element del panell.
     */
    VistaPrincipalElemPanel(String nom) {
        initComponents();
        nameLabel.setText(nom);
        this.nom = nom;
    }

    /**
     * Retorna el nom de l'element del panell.
     *
     * @return El nom de l'element del panell.
     */
    String getNom() {
        return nom;
    }

    /**
     * Marca de color groc l’element si està seleccionat. Altrament, es pinta de blanc.
     *
     * @param selected element seleccionat
     */
    void setSelected(boolean selected) {
        if(selected) {
            setBackground(new java.awt.Color(255, 255, 153));
        } else {
            setBackground(new java.awt.Color(214, 217, 223));
        }
        validate();
        repaint();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    /** Inicialitza atributs i components bàsics de l'element del panell.*/
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setMaximumSize(new java.awt.Dimension(32767, 30));

        nameLabel.setText("ExampleName");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel nameLabel;
    // End of variables declaration//GEN-END:variables
}
