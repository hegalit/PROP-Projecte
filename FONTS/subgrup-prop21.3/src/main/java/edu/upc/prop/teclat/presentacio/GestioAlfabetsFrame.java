package edu.upc.prop.teclat.presentacio;

import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.exceptions.*;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Aquesta classe gestiona les vistes de manipulació d'alfabets.
 * Es tracta d'una classe abstracta.
 * @author David Vilar (david.vilar.gallego@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public abstract class GestioAlfabetsFrame extends javax.swing.JFrame {
    //Constants
    
    /**String per demanar a l'usuari que seleccioni una llista de freqüències*/
    private final String seleccionaLlistaFreq = "Selecciona la llista de freqüència:";

    /**String per demanar a l'usuari que seleccioni un text*/
    private final String seleccionaText = "Selecciona el text:";

    /**String per informar a l'usuari que no hi ha textos al sistema*/
    private final String defaultComboBoxText = "No hi ha textos al sistema";

    /**String per informar a l'usuari que no hi ha llistes de freqüència al sistema*/
    private final String defaultComboBoxFreqList = "No hi ha llistes de freq. al sistema";

    /**String del botó de crear/confirmar*/
    private String doneButtonString;

    /**{@link ArrayList} que conté les llistes de freqüència*/
    private ArrayList<String> llistesFreq;

    /**{@link ArrayList} que conté els textos*/
    private ArrayList<String> textos;
    


    //Atributs

    /**Objecte de la vista principal*/
    VistaPrincipalFrame vistaPrincipal;


    
    /** Construeix un frame de gestió d'alfabets indicant una referència de la vista principal, 
     *  els noms dels textos i llistes que existeixen al Sistema, i el nom del botó de finalitzar.
     *
     * @param vistaPrincipal Referència de la vista principal.
     * @param doneButtonString Nom del botó de finalitzar.
     * @param llistesFreq {@link ArrayList} que conté els noms de les llistes de freqüència existents al Sistema.
     * @param textos {@link ArrayList} que conté els noms dels textos existents al Sistema.
     */
    GestioAlfabetsFrame(VistaPrincipalFrame vistaPrincipal, String doneButtonString, ArrayList<String> llistesFreq, ArrayList<String> textos) {
        this.vistaPrincipal = vistaPrincipal;
        this.doneButtonString = doneButtonString;
        this.llistesFreq = llistesFreq;
        this.textos = textos;
    }

    /**
     * Inicialitza tots els components del frame.
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

        // Al principi tenim les llistes de freqüències seleccionades
        selectTextOrFreqListLabel.setText(seleccionaLlistaFreq);
        updateComboBoxWithFreqLists();
    }

    /**
     * Inicialitza el frame indicant nom i contingut d'aquest.
     *
     * @param nom Nom de l'alfabet.
     * @param contingut Símbols de l'alfabet.
     */
    void init(String nom, String contingut) {
        init();
        nameTextField.setText(nom);
        contentTextArea.setText(contingut);
    }

    /**
     * Crea o modifica un alfabet del Sistema amb les dades rebudes com 
     * a paràmetre d'entrada.
     *
     * @param name Nom del nou alfabet (en cas de creació).
     *             Nou nom de l'alfabet seleccionat (en cas d'edició).
     * @param content Seqüència de símbols que indica els que tindrà l'alfabet a crear/editar.
     *
     * @throws NomBuitException El nom donat no té caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema un alfabet identificat per {@code nom}.
     * @throws NomMassaLlargException El nom donat té més de {@value Alfabet#MAX_NAME_LENGTH} caràcters.
     * @throws NumSimbolsInvalidException L'String donat està buit o té més de
     *                                    {@value Alfabet#MAX_NUM_SYMBOLS} símbols diferents.
     * @throws SimbolRepetitException Hi ha un o més símbols repetits a la seqüència donada.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a la seqüència donada.
     * @throws NomNoExisteixException No existeix cap alfabet identificat pel nom donat.
     * @throws NomProhibitException El nom indicat és {@value Alfabet#KEYBOARD_ORIGINAL_ALPHABET}, 
     *                              que està prohibit.
     */
    abstract void creaOModificaAlfabet(String name, String content) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, NomNoExisteixException, SimbolRepetitException, NumSimbolsInvalidException, SimbolInvalidException, NomProhibitException;


    // Mètodes auxiliars

    /**Actualitza la llista de noms dels textos existents al sistema.*/
    private void updateComboBoxWithTexts() {
        selectTextOrFreqListComboBox.removeAllItems();
        if (textos.isEmpty()) {
            selectTextOrFreqListComboBox.addItem(defaultComboBoxText);
            return;
        }	
        for (String text : textos) {
            selectTextOrFreqListComboBox.addItem(text);
        }
    }

    /**Actualitza la llista de noms de les llistes de freqüències disponibles al sistema.*/
    private void updateComboBoxWithFreqLists() {
        selectTextOrFreqListComboBox.removeAllItems();
        if (llistesFreq.isEmpty()) {
            selectTextOrFreqListComboBox.addItem(defaultComboBoxFreqList);
            return;
        }
        for (String llistaFreq : llistesFreq) {
            selectTextOrFreqListComboBox.addItem(llistaFreq);
        }
    }

    /** Retorna els símbols del text identificat pel nom donat.
     *
     * @param titol Nom del text del Sistema del que volem obtenir els seus símbols.
     *
     * @return Símbols del text identificat pel nom donat.
     * 
     * @throws NomNoExisteixException No existeix cap alfabet identificat pel nom donat.
     */
    public String obtenirSimbolsText(String titol) throws NomNoExisteixException {
        return vistaPrincipal.obtenirSimbolsText(titol);
    }

    /** Retorna els símbols de la llista de freqüències identificada pel nom donat.
     *
     * @param nomLlista Nom de la llista de freqüències del Sistema de la que volem 
     *                  obtenir els seus símbols.
     *
     * @return Símbols del text identificat pel nom donat.
     *
     * @throws NomNoExisteixException No existeix cap alfabet identificat pel nom donat.
     */
    public String obtenirSimbolsLlistaFrequencia(String nomLlista) throws NomNoExisteixException {
        return vistaPrincipal.obtenirSimbolsLlistaFrequencia(nomLlista);
    }

    /** Tanca la finestra.
     */
    private void close() {
        int result = JOptionPane.showConfirmDialog(this, "Estàs segur que vols cancel·lar?", "Confirmació", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) return;
        setVisible(false);
        vistaPrincipal.setVisible(true);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    /** Inicialitza els components del frame */
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        topPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        contentLabel = new javax.swing.JLabel();
        midPanel = new javax.swing.JPanel();
        contentScrollPane = new javax.swing.JScrollPane();
        contentTextArea = new javax.swing.JTextArea();
        botPanel = new javax.swing.JPanel();
        extractionPane = new javax.swing.JPanel();
        extractionTitleLabel = new javax.swing.JLabel();
        extractFromLabel = new javax.swing.JLabel();
        freqListRadioButton = new javax.swing.JRadioButton();
        textRadioButton = new javax.swing.JRadioButton();
        selectTextOrFreqListLabel = new javax.swing.JLabel();
        selectTextOrFreqListComboBox = new javax.swing.JComboBox<>();
        extractButton = new javax.swing.JButton();
        bottomButtonsPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Crear nou alfabet");
        setMinimumSize(new java.awt.Dimension(341, 479));

        topPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 0, 20));

        nameLabel.setText("Nom:");

        contentLabel.setText("Contingut de l'alfabet:");

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameLabel)
                    .addComponent(contentLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 138, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contentLabel)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        midPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));

        contentTextArea.setColumns(20);
        contentTextArea.setRows(5);
        contentScrollPane.setViewportView(contentTextArea);

        javax.swing.GroupLayout midPanelLayout = new javax.swing.GroupLayout(midPanel);
        midPanel.setLayout(midPanelLayout);
        midPanelLayout.setHorizontalGroup(
            midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 338, Short.MAX_VALUE)
            .addGroup(midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(contentScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
        );
        midPanelLayout.setVerticalGroup(
            midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 171, Short.MAX_VALUE)
            .addGroup(midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(contentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
        );

        getContentPane().add(midPanel, java.awt.BorderLayout.CENTER);

        botPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 10, 20));
        botPanel.setLayout(new java.awt.BorderLayout());

        extractionPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        extractionTitleLabel.setText("Extraure automàticament");

        extractFromLabel.setText("Extreure de:");

        buttonGroup1.add(freqListRadioButton);
        freqListRadioButton.setSelected(true);
        freqListRadioButton.setText("Llista de freqüències");
        freqListRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                freqListRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(textRadioButton);
        textRadioButton.setText("Text");
        textRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textRadioButtonActionPerformed(evt);
            }
        });

        extractButton.setText("Extraure");
        extractButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extractButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout extractionPaneLayout = new javax.swing.GroupLayout(extractionPane);
        extractionPane.setLayout(extractionPaneLayout);
        extractionPaneLayout.setHorizontalGroup(
            extractionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(extractionPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(extractionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(extractionPaneLayout.createSequentialGroup()
                        .addComponent(selectTextOrFreqListComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(extractButton))
                    .addGroup(extractionPaneLayout.createSequentialGroup()
                        .addGroup(extractionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textRadioButton)
                            .addComponent(freqListRadioButton)
                            .addComponent(extractionTitleLabel)
                            .addComponent(extractFromLabel)
                            .addComponent(selectTextOrFreqListLabel))
                        .addGap(0, 190, Short.MAX_VALUE)))
                .addContainerGap())
        );
        extractionPaneLayout.setVerticalGroup(
            extractionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(extractionPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(extractionTitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(extractFromLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(freqListRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(selectTextOrFreqListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(extractionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectTextOrFreqListComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extractButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        botPanel.add(extractionPane, java.awt.BorderLayout.NORTH);

        cancelButton.setText("Cancel·lar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        createButton.setText("Guardar");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bottomButtonsPanelLayout = new javax.swing.GroupLayout(bottomButtonsPanel);
        bottomButtonsPanel.setLayout(bottomButtonsPanelLayout);
        bottomButtonsPanelLayout.setHorizontalGroup(
            bottomButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bottomButtonsPanelLayout.createSequentialGroup()
                .addComponent(cancelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                .addComponent(createButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        bottomButtonsPanelLayout.setVerticalGroup(
            bottomButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bottomButtonsPanelLayout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addGroup(bottomButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(createButton)))
        );

        botPanel.add(bottomButtonsPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(botPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Events

    /**Genera les accions que fa el radio button amb la opció de text de la interficie.
     *
     * @param evt  Event per seleccionar el radiobutton text de la interficie.
     */
    private void textRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textRadioButtonActionPerformed
        if (textRadioButton.isSelected()) {
            selectTextOrFreqListLabel.setText(seleccionaText);
            updateComboBoxWithTexts();

        }
    }//GEN-LAST:event_textRadioButtonActionPerformed

    /** Genera les accions que fa el botó de crear de la interfície d'usuari.
     *
     * @param evt Event de l'acció crear.
     */
    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        try {
            creaOModificaAlfabet(nameTextField.getText(), contentTextArea.getText());
            vistaPrincipal.setVisible(true);
            dispose();
        } catch (NomJaExisteixException e) {
            JOptionPane.showMessageDialog(this, "El nom de l'alfabet \"" + nameTextField.getText() + "\" ja existeix.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumSimbolsInvalidException e) {
            JOptionPane.showMessageDialog(this, "L'alfabet ha de tenir entre 1 i 100 símbols diferents.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SimbolRepetitException e) {
            JOptionPane.showMessageDialog(this, "Hi ha simbols repetits.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SimbolInvalidException e) {
            JOptionPane.showMessageDialog(this, "No es poden posar espais, punts, comes, salts de línia, \ncarriage return, tabuladors ni form feeds a l'alfabet.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NomBuitException e) {
            JOptionPane.showMessageDialog(this, "El nom de l'alfabet no pot estar buit.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NomNoExisteixException e) {
            JOptionPane.showMessageDialog(this, "El nom de l'alfabet no existeix.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NomMassaLlargException e) {
            JOptionPane.showMessageDialog(this, "El nom de l'alfabet és massa llarg.\nEl màxim permès és 100 caràcters.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NomProhibitException e) {
            JOptionPane.showMessageDialog(this, "El nom donat és \"Alfabet original del teclat\", que està prohibit.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_createButtonActionPerformed

    /** Extrau els símbols del text o llista de freqüències seleccionat i els posa al 
     *  al camp de text dels símbols.
     *
     * @param evt Event de l'acció extraure.
     */
    private void extractButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extractButtonActionPerformed
        Object extractedItem = selectTextOrFreqListComboBox.getSelectedItem();
        if(extractedItem == null) {
            JOptionPane.showMessageDialog(this, "No hi ha cap text o llista de freqüència seleccionada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String titolSeleccionat = extractedItem.toString();
        if (textRadioButton.isSelected()) {
            if (titolSeleccionat != null && !titolSeleccionat.isEmpty()) {
                String simbols = null;
                try {
                    simbols = obtenirSimbolsText(titolSeleccionat);
                } catch (NomNoExisteixException e) {
                    JOptionPane.showMessageDialog(this, "Nom no existeix.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } 
                if (simbols != null && !simbols.isEmpty()) {
                    contentTextArea.setText(simbols);
                }
            }
        }
        if (freqListRadioButton.isSelected()) {
            if (titolSeleccionat != null && !titolSeleccionat.isEmpty()) {
                String contingutLlista = null;
                try {
                    contingutLlista = obtenirSimbolsLlistaFrequencia(titolSeleccionat);
                } catch (NomNoExisteixException e) {
                    JOptionPane.showMessageDialog(this, "No hi ha llista de frequencia seleccionada.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } 
                if (contingutLlista != null && !contingutLlista.isEmpty()) {
                    contentTextArea.setText(contingutLlista);
                }
            }
        }
    }//GEN-LAST:event_extractButtonActionPerformed

    /** Cancel·la les edicions.
     *
     * @param evt Event de l'acció cancel·lar.
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        close();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Genera les accions que fa el radio button amb la opció de llista de freqüència de la interficie.
     *
     * @param evt Event de l'acció del radio button de la llista.
     */
    private void freqListRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_freqListRadioButtonActionPerformed
        if (freqListRadioButton.isSelected()) {
            selectTextOrFreqListLabel.setText(seleccionaLlistaFreq);
            updateComboBoxWithFreqLists();
        }
    }//GEN-LAST:event_freqListRadioButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel botPanel;
    private javax.swing.JPanel bottomButtonsPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel contentLabel;
    private javax.swing.JScrollPane contentScrollPane;
    private javax.swing.JTextArea contentTextArea;
    private javax.swing.JButton createButton;
    private javax.swing.JButton extractButton;
    private javax.swing.JLabel extractFromLabel;
    private javax.swing.JPanel extractionPane;
    private javax.swing.JLabel extractionTitleLabel;
    private javax.swing.JRadioButton freqListRadioButton;
    private javax.swing.JPanel midPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JComboBox<String> selectTextOrFreqListComboBox;
    private javax.swing.JLabel selectTextOrFreqListLabel;
    private javax.swing.JRadioButton textRadioButton;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
