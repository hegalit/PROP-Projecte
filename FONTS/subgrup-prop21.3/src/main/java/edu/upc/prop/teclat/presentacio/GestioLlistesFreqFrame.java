package edu.upc.prop.teclat.presentacio;

import edu.upc.prop.teclat.domini.Alfabet;
import edu.upc.prop.teclat.domini.LlistaDeFrequencia;
import edu.upc.prop.teclat.domini.exceptions.*;
import edu.upc.prop.teclat.util.Pair;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Aquesta classe gestiona les vistes de manipulació de llistes de freqüències.
 * Es tracta d'una classe abstracta.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 * @author Albert Panicello Torras (albert.panicello.torras@estudiantat.upc.edu)
 */

public abstract class GestioLlistesFreqFrame extends javax.swing.JFrame {
    /**Objecte de la vista principal*/
    VistaPrincipalFrame vistaPrincipal;
    /**String del botó de crear/confirmar*/
    private String doneButtonString;
    /**{@link ArrayList} que conté el contingut de les llistes de freqüència*/
    private ArrayList<Pair<String, Integer>> contingut;
    /**String per informar a l'usuari que no hi ha textos al sistema*/
    private final String defaultComboBoxText = "No hi ha textos al sistema";

    //Constructora
    /** Associa el frame de gestió de llistes de freqüències amb la instància del frame de 
     *  la vista principal indicada. També rep el nom donat al botó de finalitzar.
     *
     * @param vistaPrincipal Referència de la vista principal.
     * @param doneButtonString Nom del botó de finalitzar.
     */
    public GestioLlistesFreqFrame(VistaPrincipalFrame vistaPrincipal, String doneButtonString) {
        this.vistaPrincipal = vistaPrincipal;
        this.doneButtonString = doneButtonString;
    }

    /** Inicialitza tots els components de la interfície, rebent com a paràmetre 
     *  els noms dels textos existents al Sistema.
     *
     * @param textos Llista que conté els noms dels textos existents al Sistema.
     */
    void init(ArrayList<String> textos) {
        initComponents();
        setLocationRelativeTo(vistaPrincipal);
        createButton.setText(doneButtonString);

        // Fem que només pugui haver nombres a la caixeta de text de freqüències
        NumberFormat longFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(longFormat);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0l);
        freqInputFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(numberFormatter));

        // Modifiquem el comportament de tancar la finestra amb la creu
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        textSelectionComboBox.removeAllItems();
        if (textos.isEmpty()) textSelectionComboBox.addItem(defaultComboBoxText);
        for (String text : textos) {
            textSelectionComboBox.addItem(text);
        }
    }

    /** Inicialitza el frame, rebent com a paràmetre el nom i contingut de la llista de freqüències 
     * a visualitzar, junt amb una llista dels noms dels textos existents al Sistema.
     *
     * @param nom nom que tindrà la llista
     * @param contingut contingut de la llista
     * @param textos afegeix els textos disponibles per generar la llista
     */
    void init(String nom, ArrayList<Pair<String, Integer>> contingut, ArrayList<String> textos) {
        init(textos);
        nameTextField.setText(nom);
        this.contingut = contingut;
        loadList();
    }

    /** Tanca la finestra.
     */
    private void close() {
        int result = JOptionPane.showConfirmDialog(this, "Estàs segur que vols cancel·lar?", "Confirmació", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) return;
        vistaPrincipal.setVisible(true);
        dispose();
    }

    /**
     * Elimina l’element indicat com a paràmetre de la llista i actualitza els valors d’aquesta.
     *
     * @param elem element de la llista que serà esborrat.
     */
    void removeElem(GestioLlistesFreqElemPanel elem) {
        previewLlistaFreqPanel.remove(elem);
        previewLlistaFreqPanel.revalidate();
        previewLlistaFreqPanel.repaint();
    }

    /**
     * Carrega la llista de freqüències desitjada a la llista d’elements de la interfície.
     */
    private void loadList() {
        previewLlistaFreqPanel.removeAll();
        for (Pair<String, Integer> entry : contingut) {
            GestioLlistesFreqElemPanel elem = new GestioLlistesFreqElemPanel(this, entry.getFirst(), entry.getSecond());
            previewLlistaFreqPanel.add(elem);
        }
        previewLlistaFreqPanel.revalidate();
        previewLlistaFreqPanel.repaint();
    }

    /**
     * Guarda la llista de freqüències desitjada.
     */
    private void saveList() {
        contingut = new ArrayList<Pair<String, Integer>>();
        for (java.awt.Component c : previewLlistaFreqPanel.getComponents()) {
            if (c instanceof GestioLlistesFreqElemPanel) {
                GestioLlistesFreqElemPanel elem = (GestioLlistesFreqElemPanel) c;
                contingut.add(new Pair<String, Integer>(elem.getWord(), elem.getFreq()));
            }
        }
    }

    /**Crea o modifica una llista de freqüències del Sistema amb les dades rebudes com 
     * a paràmetre d'entrada.
     *
     * @param nom Nom de la nova llista a crear (en cas de creació).
     *            Nou nom de la llista a editar (en cas d'edició).
     * @param contingut Seqüència de parells paraula, freqüència que indica el nou contingut
     *                  de la llista de freqüències a crear/editar.
     *
     * @throws NomBuitException El nom no té caràcters.
     * @throws NomJaExisteixException Ja existeix al Sistema una llista de freqüències amb el nom donat.
     * @throws NomMassaLlargException El nom donat té més de {@value LlistaDeFrequencia#MAX_NAME_LENGTH} 
     *                                caràcters.
     * @throws NumSimbolsInvalidException La seqüència donada té més de {@value Alfabet#MAX_NUM_SYMBOLS}
     *                                     símbols diferents entre totes les paraules.
     * @throws SimbolInvalidException Hi ha un o més símbols no permesos a la seqüència donada.
     * @throws NombreElementsInvalidException La seqüència de parells donada està buida. 
     * @throws InvalidFrequencyException  La freqüència d'alguna paraula és &lt;=0.
     * @throws ParaulaRepetidaException Hi ha una o més paraules repetides a la seqüència donada.
     * @throws ParaulaBuidaException Hi ha una o més paraules buides a la seqüència donada.
     */
    abstract void crearOEditarLlistaFreq(String nom, ArrayList<Pair<String, Integer>> contingut) throws NomBuitException, NomMassaLlargException, NomJaExisteixException, NombreElementsInvalidException, InvalidFrequencyException, SimbolInvalidException, ParaulaRepetidaException, ParaulaBuidaException, NumSimbolsInvalidException;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    /**
     * Inicialitza els atributs del frame.
     * */
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        generatePane = new javax.swing.JPanel();
        generateLabel = new javax.swing.JLabel();
        textSelectionLabel = new javax.swing.JLabel();
        textSelectionComboBox = new javax.swing.JComboBox<>();
        generateButton = new java.awt.Button();
        addFreqPanel = new javax.swing.JPanel();
        addFreqLabel = new javax.swing.JLabel();
        wordInputLabel = new javax.swing.JLabel();
        wordInputTextField = new javax.swing.JTextField();
        freqInputLabel = new javax.swing.JLabel();
        freqInputFormattedTextField = new javax.swing.JFormattedTextField();
        addFreqButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        previewLlistaFreqScrollPane = new javax.swing.JScrollPane();
        previewLlistaFreqPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Crear nova llista de freqüències");
        setResizable(false);

        nameLabel.setText("Nom:");

        generatePane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        generateLabel.setText("Autogenerar a partir d'un text");

        textSelectionLabel.setText("Text:");

        textSelectionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        generateButton.setLabel("Generar");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout generatePaneLayout = new javax.swing.GroupLayout(generatePane);
        generatePane.setLayout(generatePaneLayout);
        generatePaneLayout.setHorizontalGroup(
            generatePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generatePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generatePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generatePaneLayout.createSequentialGroup()
                        .addComponent(textSelectionComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(generatePaneLayout.createSequentialGroup()
                        .addGroup(generatePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(generateLabel)
                            .addComponent(textSelectionLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        generatePaneLayout.setVerticalGroup(
            generatePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generatePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(generateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generatePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(generatePaneLayout.createSequentialGroup()
                        .addComponent(textSelectionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(generateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        addFreqPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        addFreqLabel.setText("Afegir paraula");

        wordInputLabel.setText("Paraula:");

        wordInputTextField.setText("NovaParaula");

        freqInputLabel.setText("Freqüència:");

        freqInputFormattedTextField.setColumns(5);
        freqInputFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        freqInputFormattedTextField.setValue(1);

        addFreqButton.setText("Afegir");
        addFreqButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFreqButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addFreqPanelLayout = new javax.swing.GroupLayout(addFreqPanel);
        addFreqPanel.setLayout(addFreqPanelLayout);
        addFreqPanelLayout.setHorizontalGroup(
            addFreqPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFreqPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addFreqPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addFreqPanelLayout.createSequentialGroup()
                        .addGroup(addFreqPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(freqInputFormattedTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(freqInputLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                        .addComponent(addFreqButton))
                    .addGroup(addFreqPanelLayout.createSequentialGroup()
                        .addGroup(addFreqPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addFreqLabel)
                            .addComponent(wordInputLabel)
                            .addComponent(wordInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        addFreqPanelLayout.setVerticalGroup(
            addFreqPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFreqPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addFreqLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wordInputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wordInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(freqInputLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addFreqPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(freqInputFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addFreqButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        createButton.setText("Crear");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel·lar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        previewLlistaFreqScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        previewLlistaFreqPanel.setLayout(new javax.swing.BoxLayout(previewLlistaFreqPanel, javax.swing.BoxLayout.Y_AXIS));
        previewLlistaFreqScrollPane.setViewportView(previewLlistaFreqPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(previewLlistaFreqScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                        .addComponent(createButton))
                    .addComponent(addFreqPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(generatePane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(34, 34, 34))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(generatePane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addFreqPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(previewLlistaFreqScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(createButton))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** Genera les accions que fa el botó de crear de la interfície.
     *
     * @param evt Event de l'acció crear.
     */
    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        try {
            saveList();
            crearOEditarLlistaFreq(nameTextField.getText(), contingut);
            vistaPrincipal.setVisible(true);
            dispose();
        } catch (NomBuitException e) {
            JOptionPane.showMessageDialog(this, "El nom no pot estar buit", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NomJaExisteixException e) {
            JOptionPane.showMessageDialog(this, "Ja existeix una llista de freqüències amb aquest nom", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NombreElementsInvalidException e) {
            JOptionPane.showMessageDialog(this, "El nombre d'elements de la llista de freqüències ha de ser major que 0", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidFrequencyException e) {
            JOptionPane.showMessageDialog(this, "La freqüència d'un element de la llista de freqüències ha de ser major que 0", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SimbolInvalidException e) {
            JOptionPane.showMessageDialog(this, "El símbol d'un element de la llista de freqüències ha de ser un caràcter vàlid (no comes, punts, etc.)", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParaulaRepetidaException e) {
            JOptionPane.showMessageDialog(this, "No es poden repetir paraules a la llista de freqüències", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParaulaBuidaException e) {
            JOptionPane.showMessageDialog(this, "Hi ha un o més camps de paraula buits a la llista de freqüències", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumSimbolsInvalidException e) {
            JOptionPane.showMessageDialog(this, "El nombre de símbols diferents a la llista ha de ser com a molt 100", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NomMassaLlargException e) {
            JOptionPane.showMessageDialog(this, "El nom de la llista de freqüència és massa llarg.\nEl màxim permès és 100 caràcters.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_createButtonActionPerformed

    /** Tanca la finestra
     *
     * @param evt Event de l'acció cancel·lar.
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        close();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Genera el contingut d'una llista a partir d'un text.
     *
     * @param evt Event de l'acció de generar el contingut d'una llista a partir d'un text.
     */
    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        if(textSelectionComboBox.getSelectedItem().toString() == defaultComboBoxText) return;
        try {
            contingut = vistaPrincipal.getLlistaDeFrequenciaAPartirDeText(textSelectionComboBox.getSelectedItem().toString());
        } catch (NomNoExisteixException e) {
            // No hauria de passar
            e.printStackTrace();
        }
        loadList();
    }//GEN-LAST:event_generateButtonActionPerformed

    /** Afegeix d’una nova paraula amb la seva freqüència a la llista.
     *
     * @param evt Event de l'acció d'afegir una nova paraula amb la freqüència corresponent.
     */
    private void addFreqButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFreqButtonActionPerformed
        // Obtenim el valor emmagatzemat al text field.
        Object value = freqInputFormattedTextField.getValue();
        int freq;
        // Cal fer el cast correcte segons el tipus d'instància (pot variar segons la mida del valor que hi hagi a la caixeta)
        if (value instanceof Integer) freq = (Integer)value;
        else freq = ((Long)value).intValue();
        GestioLlistesFreqElemPanel elem = new GestioLlistesFreqElemPanel(this, wordInputTextField.getText(), freq);
        previewLlistaFreqPanel.add(elem);
        previewLlistaFreqPanel.revalidate();
        previewLlistaFreqPanel.repaint();
    }//GEN-LAST:event_addFreqButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFreqButton;
    private javax.swing.JLabel addFreqLabel;
    private javax.swing.JPanel addFreqPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton createButton;
    private javax.swing.JFormattedTextField freqInputFormattedTextField;
    private javax.swing.JLabel freqInputLabel;
    private java.awt.Button generateButton;
    private javax.swing.JLabel generateLabel;
    private javax.swing.JPanel generatePane;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPanel previewLlistaFreqPanel;
    private javax.swing.JScrollPane previewLlistaFreqScrollPane;
    private javax.swing.JComboBox<String> textSelectionComboBox;
    private javax.swing.JLabel textSelectionLabel;
    private javax.swing.JLabel wordInputLabel;
    private javax.swing.JTextField wordInputTextField;
    // End of variables declaration//GEN-END:variables
}
