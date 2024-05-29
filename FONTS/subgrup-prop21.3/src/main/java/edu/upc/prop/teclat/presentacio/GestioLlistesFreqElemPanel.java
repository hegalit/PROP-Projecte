package edu.upc.prop.teclat.presentacio;

import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

/**
 * Aquesta classe representa un element (paraula, freqüència) en la vista d'edició o creació de
 * llistes de freqüència.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */

class GestioLlistesFreqElemPanel extends javax.swing.JPanel {
    GestioLlistesFreqFrame gestioLlistesFreqFrame;
    /**
     * Crea un nou element del panell passant-li com a paràmetre una instància de
     * la gestió de llistes de freqüència, una paraula i una freqüència.
     *
     * @param gestioLlistesFreqFrame Referència de la gestió de llistes de freqüència
     * @param word Paraula que formarà part de la llista
     * @param freq Freqüència corresponent de {@code word}
     */
    GestioLlistesFreqElemPanel(GestioLlistesFreqFrame gestioLlistesFreqFrame, String word, int freq) {
        initComponents();
        this.gestioLlistesFreqFrame = gestioLlistesFreqFrame;
        wordTextField.setText(word);

        NumberFormat longFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(longFormat);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0l);
        freqFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(numberFormatter));
        freqFormattedTextField.setValue(freq);
    }

    /**
     * Retorna la paraula que s’ha introduït.
     *
     * @return La paraula introduïda.
     */
    String getWord() {
        return wordTextField.getText();
    }

    /**
     * Retorna la freqüència de la paraula introduïda.
     *
     * @return La freqüència de la paraula introduïda.
     */
    int getFreq() {
        Object value = freqFormattedTextField.getValue();
        if (value instanceof Integer) return (Integer)value;
        return ((Long)value).intValue();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    /**
     * Inicialitza tots els elements de la interfície.
     */
    private void initComponents() {

        wordTextField = new javax.swing.JTextField();
        freqFormattedTextField = new javax.swing.JFormattedTextField();
        deleteButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        wordTextField.setText("paraula");

        freqFormattedTextField.setText("2");

        deleteButton.setText("X");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(wordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(freqFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                .addComponent(deleteButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(freqFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Esborra aquest element.
     *
     * @param evt Event de l'acció esborrar.
     */

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        gestioLlistesFreqFrame.removeElem(this);
    }//GEN-LAST:event_deleteButtonActionPerformed


    //GEN-BEGIN:variables
    private javax.swing.JButton deleteButton;
    private javax.swing.JFormattedTextField freqFormattedTextField;
    private javax.swing.JTextField wordTextField;
    //GEN-END:variables
}
