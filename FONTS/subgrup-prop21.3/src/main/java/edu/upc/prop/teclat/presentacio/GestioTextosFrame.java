package edu.upc.prop.teclat.presentacio;

import javax.swing.JOptionPane;

import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.Text;
import edu.upc.prop.teclat.domini.exceptions.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Aquesta classe gestiona les vistes de manipulació de textos.
 * Es tracta d'una classe abstracta.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */
public abstract class GestioTextosFrame extends javax.swing.JFrame {
    //Atributs

    /**Objecte de la vista principal*/
    VistaPrincipalFrame vistaPrincipal;

    /**String del botó de crear/confirmar*/
    String doneButtonString;



    //Constructora
    /** Associa el frame de gestió de textos amb la instància del frame de la vista principal indicada.
     *
     * @param vistaPrincipal Referència de la vista principal.
     */
    GestioTextosFrame(VistaPrincipalFrame vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
    }

    /**
     * Inicialitza tots els components de la classe.
    */
    void init() {
        initComponents();
        setLocationRelativeTo(vistaPrincipal);
        createButton.setText(doneButtonString);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }

    /**
     * Canvia el nom i el contingut del frame pels indicats.
     *
     * @param nom Nou nom que se li donarà al frame
     * @param contingut Nou contingut que se li donarà al frame.
     */
    void init(String nom, String contingut) {
        init();
        nameTextField.setText(nom);
        contentTextArea.setText(contingut);
    }

    /**
     * Crea o modifica un text del Sistema amb les dades rebudes com 
     * a paràmetre d'entrada.
     *
     * @param name Nom del nou text a crear (en cas de creació) o bé 
     *             nou nom del text existent (en cas de modificació).
     * @param content Nou cos que es donarà al text.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema un text identificat pel nom donat.
     * @throws NomMassaLlargException El nom donat té més de {@value Text#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws NomNoExisteixException No existeix cap text identificat pel nom donat.
     * @throws TextEstaBuitException {@code content} no té caràcters.
     */
    abstract void creaOModificaText(String name, String content) throws NomJaExisteixException, NomNoExisteixException, NomBuitException, NomMassaLlargException, TextEstaBuitException, NumSimbolsInvalidException;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        contentLabel = new javax.swing.JLabel();
        contentScrollPane = new javax.swing.JScrollPane();
        contentTextArea = new javax.swing.JTextArea();
        cancelButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(396, 349));

        nameLabel.setText("Nom:");

        contentLabel.setText("Contingut:");

        contentTextArea.setColumns(20);
        contentTextArea.setLineWrap(true);
        contentTextArea.setRows(5);
        contentScrollPane.setViewportView(contentTextArea);

        cancelButton.setText("Cancel·lar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(createButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(contentLabel)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createButton)
                    .addComponent(cancelButton))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** Genera les accions que fa botó de crear de la interfície.
     *
     * @param evt Event de l'acció crear.
     */
    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        try {
            creaOModificaText(nameTextField.getText(), contentTextArea.getText());
            vistaPrincipal.setVisible(true);
            dispose();
        } catch (TextEstaBuitException e) {
            JOptionPane.showMessageDialog(this, "El text no pot estar buit.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NomJaExisteixException e) {
            JOptionPane.showMessageDialog(this, "Ja existeix un text amb el nom " + nameTextField.getText() + ".", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NomBuitException e) {
            JOptionPane.showMessageDialog(this, "El nom no pot estar buit.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NomNoExisteixException e) {
            // No s'ha de donar mai
            e.printStackTrace();
        } catch (NumSimbolsInvalidException e) {
            JOptionPane.showMessageDialog(this, "El text no pot tenir més de 100 símbols diferents.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch(NomMassaLlargException e) {
            JOptionPane.showMessageDialog(this, "El nom del text és massa llarg.\nEl màxim permès és 100 caràcters.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_createButtonActionPerformed

    /**Aquesta funció cancel·la les edicions.
     *
     * @param evt Event de l'acció cancel·lar.
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        close();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Tanca la finestra.
     */
    private void close() {
        int result = JOptionPane.showConfirmDialog(this, "Estàs segur que vols cancel·lar?", "Confirmació", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) return;
        vistaPrincipal.setVisible(true);
        dispose();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel contentLabel;
    private javax.swing.JScrollPane contentScrollPane;
    private javax.swing.JTextArea contentTextArea;
    private javax.swing.JButton createButton;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    // End of variables declaration//GEN-END:variables
}
