package edu.upc.prop.teclat.presentacio;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;

import edu.upc.prop.teclat.domini.exceptions.InvalidGeneratorAlgorithmException;
import edu.upc.prop.teclat.domini.exceptions.NomBuitException;
import edu.upc.prop.teclat.domini.exceptions.NomJaExisteixException;
import edu.upc.prop.teclat.domini.exceptions.NomMassaLlargException;
import edu.upc.prop.teclat.domini.exceptions.NomNoExisteixException;
import edu.upc.prop.teclat.domini.exceptions.teclat.CaractersNoInclososException;
import edu.upc.prop.teclat.domini.exceptions.teclat.IndexosInvalidsException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingBestLayoutException;
import edu.upc.prop.teclat.domini.exceptions.teclat.MissingPairsFreqException;
import edu.upc.prop.teclat.domini.exceptions.teclat.TeclatTemporalBuitException;

import java.util.ArrayList;

/**
 * Aquesta classe gestiona les vistes de manipulació de teclats.
 * Es tracta d'una classe abstracta.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public abstract class GestioTeclatsFrame extends javax.swing.JFrame {
    //Constants

    /**Apareix si no hi ha textos compatibles*/
    private static final String defaultComboBoxText = "No hi ha textos compatibles";

    /**Apareix si no hi ha llistes de freqüències compatibles*/
    private static final String defaultComboBoxFreqList = "No hi ha llistes de freqüències compatibles";

    /**Apareix la primera vegada que s'inserta un text al teclat*/
    private static final String textSelectLabel = "Selecciona el text:";

    /**Apareix la primera vegada que s'inserta una llista de freqüències al teclat*/
    private static final String freqListSelectLabel = "Selecciona la llista de freqüències:";

    /**Apareix a dalt del teclat per indicar on està el cost d'aquest*/
    private static final String costDisposicioTecles = "Cost d'aquesta disposició de tecles: ";

    /**VistaPrincipalFrame que ha generat aquesta finestra*/
    private final VistaPrincipalFrame vistaPrincipal;

    /**Text a mostrar en el botó de crear o editar el teclat*/
    private final String doneButtonString;

    /**Indica si el la finestra ha acabat d'inicialitzarse*/
    private boolean inicialitzat = false;


    

    //Constructora
    /** Associa el frame de gestió de teclats amb la instància del frame de la vista principal indicada.
     * També rep el nom donat al botó de finalitzar.
     *
     * @param vistaPrincipal Referència de la vista principal.
     * @param doneButtonString Nom del botó de finalitzar.
     */
    public GestioTeclatsFrame(VistaPrincipalFrame vistaPrincipal, String doneButtonString) {
        this.vistaPrincipal = vistaPrincipal;
        this.doneButtonString = doneButtonString;
    }

    /**
     * Inicialitza tots els components de la interficie d'usuari.
     *
     * @param alfabets alfabet del teclat.
     * @param algoritmes algoritme pel teclat a crear.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    void init(ArrayList<String> alfabets, ArrayList<String> algoritmes) throws TeclatTemporalBuitException {
        
        initComponents();
        setLocationRelativeTo(vistaPrincipal);
        createButton.setText(doneButtonString);

        // Fem que només pugui haver nombres a les caixetes de text de dimensions
        NumberFormat longFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(longFormat);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0l);
        rowsFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(numberFormatter));
        colsFormatedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(numberFormatter));

        // Modifiquem el comportament de tancar la finestra amb la creu
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        // Configurem els desplegables
        alfabetSelectorComboBox.removeAllItems();
        for (String text : alfabets) {
            alfabetSelectorComboBox.addItem(text);
        }
        algorithmSelectorComboBox.removeAllItems();
        for (String algoritme : algoritmes) {
            algorithmSelectorComboBox.addItem(algoritme);
        }
        updateTextOLlistesFreqComboBox();

        // Actualitzem la vista
        updateTeclat();

        inicialitzat = true;
    }

    /**
     * Inicialitza els atributs de la classe, rebent el nom del teclat visualitzat junt amb una 
     * llista dels alfabets i algorismes generadors existents al Sistema.
     *
     * @param alfabets Alfabets del sistema.
     * @param algoritmes Algoritmes disponibles per al teclat.
     * @param nomOriginal Nom original del teclat.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     */
    void init(ArrayList<String> alfabets, ArrayList<String> algoritmes, String nomOriginal) throws TeclatTemporalBuitException {
        init(alfabets, algoritmes);
        nameTextField.setText(nomOriginal);
    }

    /**
     * Intercanvia de lloc els dos símbols indicats de la disposició del teclat.
     *
     * @param ID1 Id del símbol 1.
     * @param ID2 Id del símbol 2.
     *
     * @throws TeclatTemporalBuitException No hi ha dades al teclat temporal.
     * @throws IndexosInvalidsException S'ha intentat accedir a una posició incorrecta.
     */
    void swapKeys(int ID1, int ID2) throws TeclatTemporalBuitException, IndexosInvalidsException {
        vistaPrincipal.swapKeysTeclat(ID1, ID2);
        updateTeclat();
    }

    /**
     * Actualitza tots els atributs del teclat visualitzat.
     */
    private void updateTeclat() {
        updateDimensionsTextFields();
        personalizedKeysPane.removeAll();
        try {
            personalizedKeysPane.add(new PreviewTeclatPanel(this, vistaPrincipal.getLayoutTeclat(), vistaPrincipal.getColumnesTeclat()));
        } catch (TeclatTemporalBuitException e) {
            // No pot passar
            e.printStackTrace();
        }
        personalizedKeysPane.revalidate();
        personalizedKeysPane.repaint();

        // Actualitzem el cost d'aquesta disposició de tecles
        updateCost();

        // Actualitzem si el botó de retornar al millor layout ha d'estar actiu
        boolean canSetBestLayout = false;
        try {
            canSetBestLayout = vistaPrincipal.canSetBestLayoutTeclat();
        } catch (TeclatTemporalBuitException e) {
            // No hauria de passar
            e.printStackTrace();
        }
        if (canSetBestLayout) {
            retornarMillorLayoutButton.setEnabled(true);
        } else {
            retornarMillorLayoutButton.setEnabled(false);
        }
    }

    /**
     * Tanca el frame.
     */
    private void close() {
        int result = JOptionPane.showConfirmDialog(this, "Estàs segur que vols cancel·lar?", "Confirmació", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) return;
        vistaPrincipal.setVisible(true);
        dispose();
    }

    /**
     * Recalcula el cost de la disposició actual de símbols del teclat.
     */
    private void updateCost() {
        try {
            double cost = vistaPrincipal.getCostTeclat();
            cost = Math.round(cost * 10000.0) / 10000.0;
            costTeclatLabel.setText(costDisposicioTecles + cost);
        } catch (MissingPairsFreqException | TeclatTemporalBuitException e) {
            // Si no hi ha pairs posem un guió
            costTeclatLabel.setText(costDisposicioTecles + "-");
        }
    }

    /**
     * Actualitza les dimensions de la disposició de símbols del teclat.
     */
    void updateDimensionsTextFields() {
        try {
            rowsFormattedTextField.setValue(vistaPrincipal.getFilesTeclat());
            colsFormatedTextField.setValue(vistaPrincipal.getColumnesTeclat());
        } catch (TeclatTemporalBuitException e) {
            // No hauria de passar
            e.printStackTrace();
        }
    }

    /**
    * Actualitza el contingut del desplegable de llistes de freqüència / textos perquè tingui els noms del domini.
    */
    private void updateTextOLlistesFreqComboBox() {
        if (generateLayoutFromTextRadioButton.isSelected()) {
            ArrayList<String> textos;
            try {
                textos = vistaPrincipal.getTextosCompatibles();
            } catch (TeclatTemporalBuitException e) {
                // No pot passar
                e.printStackTrace();
                return;
            }
            textOrFreqListSelectorComboBox.removeAllItems();
            if (textos.isEmpty()) textOrFreqListSelectorComboBox.addItem(defaultComboBoxText);
            for (String text : textos) {
                textOrFreqListSelectorComboBox.addItem(text);
            }
            selectTextOrFreqListLabel.setText(textSelectLabel);
        } else {
            ArrayList<String> llistesFreq;
            try {
                llistesFreq = vistaPrincipal.getLlistesFreqCompatibles();
            } catch (TeclatTemporalBuitException e) {
                // No pot passar
                e.printStackTrace();
                return;
            }
            textOrFreqListSelectorComboBox.removeAllItems();
            if (llistesFreq.isEmpty()) textOrFreqListSelectorComboBox.addItem(defaultComboBoxFreqList);
            for (String llistaFreq : llistesFreq) {
                textOrFreqListSelectorComboBox.addItem(llistaFreq);
            }
            selectTextOrFreqListLabel.setText(freqListSelectLabel);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton3 = new javax.swing.JButton();
        keyboardPreviewLabel = new javax.swing.JLabel();
        costTeclatLabel = new javax.swing.JLabel();
        retornarMillorLayoutButton = new javax.swing.JButton();
        keyboardPreviewPane = new javax.swing.JPanel();
        centeredPane = new javax.swing.JPanel();
        personalizedKeysPane = new javax.swing.JPanel();
        teclesPerDefectePane = new edu.upc.prop.teclat.presentacio.TeclesPerDefectePanel();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        alfabetSelectorLabel = new javax.swing.JLabel();
        alfabetSelectorComboBox = new javax.swing.JComboBox<>();
        dimensionsLabel = new javax.swing.JLabel();
        rowsFormattedTextField = new javax.swing.JFormattedTextField();
        dimensionsXLabel = new javax.swing.JLabel();
        colsFormatedTextField = new javax.swing.JFormattedTextField();
        generateLayoutFromLabel = new javax.swing.JLabel();
        generateLayoutFromTextRadioButton = new javax.swing.JRadioButton();
        generateLayoutFromFreqListRadioButton = new javax.swing.JRadioButton();
        selectTextOrFreqListLabel = new javax.swing.JLabel();
        textOrFreqListSelectorComboBox = new javax.swing.JComboBox<>();
        algorithmSelectorLabel = new javax.swing.JLabel();
        algorithmSelectorComboBox = new javax.swing.JComboBox<>();
        algorithmGenerateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();

        jButton3.setText("jButton3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Crear nou teclat");
        setResizable(false);

        keyboardPreviewLabel.setText("Preview del teclat:");

        costTeclatLabel.setText("Cost: 0");

        retornarMillorLayoutButton.setText("Retornar al millor layout");
        retornarMillorLayoutButton.setEnabled(false);
        retornarMillorLayoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retornarMillorLayoutButtonActionPerformed(evt);
            }
        });

        keyboardPreviewPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        keyboardPreviewPane.setLayout(new java.awt.GridBagLayout());

        centeredPane.setLayout(new javax.swing.BoxLayout(centeredPane, javax.swing.BoxLayout.Y_AXIS));
        centeredPane.add(personalizedKeysPane);
        centeredPane.add(teclesPerDefectePane);

        keyboardPreviewPane.add(centeredPane, new java.awt.GridBagConstraints());

        nameLabel.setText("Nom:");

        alfabetSelectorLabel.setText("Alfabet:");

        alfabetSelectorComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        alfabetSelectorComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alfabetSelectorComboBoxActionPerformed(evt);
            }
        });

        dimensionsLabel.setText("Dimensions (files x columnes):");

        rowsFormattedTextField.setValue(0);
        rowsFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rowsFormattedTextFieldActionPerformed(evt);
            }
        });

        dimensionsXLabel.setText("X");

        colsFormatedTextField.setValue(0);
        colsFormatedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colsFormatedTextFieldActionPerformed(evt);
            }
        });

        generateLayoutFromLabel.setText("Generar layout a partir de:");

        buttonGroup1.add(generateLayoutFromTextRadioButton);
        generateLayoutFromTextRadioButton.setSelected(true);
        generateLayoutFromTextRadioButton.setText("Text");
        generateLayoutFromTextRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateLayoutFromTextRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(generateLayoutFromFreqListRadioButton);
        generateLayoutFromFreqListRadioButton.setText("Llista de freqüències");
        generateLayoutFromFreqListRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateLayoutFromFreqListRadioButtonActionPerformed(evt);
            }
        });

        selectTextOrFreqListLabel.setText("Selecciona el text:");

        textOrFreqListSelectorComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        textOrFreqListSelectorComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textOrFreqListSelectorComboBoxActionPerformed(evt);
            }
        });

        algorithmSelectorLabel.setText("Algorisme per a generar el layout:");

        algorithmSelectorComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        algorithmGenerateButton.setText("Generar");
        algorithmGenerateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                algorithmGenerateButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel·lar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        createButton.setText("Crear");
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
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(keyboardPreviewLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(costTeclatLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(retornarMillorLayoutButton))
                    .addComponent(keyboardPreviewPane, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(algorithmSelectorLabel)
                    .addComponent(selectTextOrFreqListLabel)
                    .addComponent(generateLayoutFromTextRadioButton)
                    .addComponent(nameLabel)
                    .addComponent(alfabetSelectorLabel)
                    .addComponent(dimensionsLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rowsFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(dimensionsXLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colsFormatedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(generateLayoutFromLabel)
                    .addComponent(generateLayoutFromFreqListRadioButton)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cancelButton)
                            .addComponent(algorithmSelectorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(createButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(algorithmGenerateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(alfabetSelectorComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(textOrFreqListSelectorComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(keyboardPreviewLabel)
                    .addComponent(costTeclatLabel)
                    .addComponent(retornarMillorLayoutButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(alfabetSelectorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(alfabetSelectorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dimensionsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rowsFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dimensionsXLabel)
                            .addComponent(colsFormatedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(generateLayoutFromLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generateLayoutFromTextRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generateLayoutFromFreqListRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectTextOrFreqListLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textOrFreqListSelectorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(algorithmSelectorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(algorithmSelectorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(algorithmGenerateButton))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(createButton)
                            .addComponent(cancelButton)))
                    .addComponent(keyboardPreviewPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Actualitza les rows del teclat temporal, repinta el teclat de preview i actualitza les caixes de rows i columns.
     *
     * @param evt Event d'actualitzar les files del teclat.
     */
    private void rowsFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rowsFormattedTextFieldActionPerformed
        try {
            vistaPrincipal.setFilesTeclat(((Long)rowsFormattedTextField.getValue()).intValue());
            updateTeclat();
        } catch (TeclatTemporalBuitException e) {
            // No hauria de passar
            e.printStackTrace();
        }
    }//GEN-LAST:event_rowsFormattedTextFieldActionPerformed

    /**
     * Actualitza les cols del teclat temporal, repinta el teclat de preview i actualitza les caixes de rows i columns
     *
     * @param evt Event d'actualitzar les columnes del teclat.
     */
    private void colsFormatedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colsFormatedTextFieldActionPerformed
        try {
            vistaPrincipal.setColsTeclat(((Long) colsFormatedTextField.getValue()).intValue());
            updateTeclat();
        } catch (TeclatTemporalBuitException e) {
            // No hauria de passar
            e.printStackTrace();
        }
    }//GEN-LAST:event_colsFormatedTextFieldActionPerformed

    /**
     * Actualitza el desplegable de textos o llistes de freqüència perquè
     * mostri les llistes de freqüència en resposta a la nova selecció del radio button.
     *
     * @param evt Event de clicar el radio button per seleccionar llistes de freqüències.
     */
    private void generateLayoutFromFreqListRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateLayoutFromFreqListRadioButtonActionPerformed
        updateTextOLlistesFreqComboBox();
        updateTeclat();
    }//GEN-LAST:event_generateLayoutFromFreqListRadioButtonActionPerformed


    /**
     * Actualitza el desplegable de textos o llistes de freqüència perquè
     * mostri els textos en resposta a la nova selecció del radio button.
     *
     * @param evt Event de clicar el radio button per seleccionar textos.
     */
    private void generateLayoutFromTextRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateLayoutFromTextRadioButtonActionPerformed
        updateTextOLlistesFreqComboBox();
        updateTeclat();
    }//GEN-LAST:event_generateLayoutFromTextRadioButtonActionPerformed

    /**
     * Crida a presentació per actualitzar el teclat temporal i torna a pintar el preview del teclat.
     *
     * @param evt Event de seleccionar l'alfabet.
     */
    private void alfabetSelectorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alfabetSelectorComboBoxActionPerformed
        if(!inicialitzat) return;
        if(alfabetSelectorComboBox.getSelectedItem() == null) return;
        try {
            vistaPrincipal.setAlfabetTeclat(alfabetSelectorComboBox.getSelectedItem().toString());
        } catch (TeclatTemporalBuitException | NomNoExisteixException e) {
            // No hauria de passar
            e.printStackTrace();
            return;
        }
        updateTextOLlistesFreqComboBox();
        // Actualitzem la vista del teclat
        updateTeclat();
    }//GEN-LAST:event_alfabetSelectorComboBoxActionPerformed

    
    /**
     * Es crida quan es clica el botó de regenerar el layout d'un teclat.
     * Genera un nou layout amb l'algoritme seleccionat.
     *
     * @param evt Event d'haver clicat el botó generar
     */
    private void algorithmGenerateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_algorithmGenerateButtonActionPerformed
        String algoritmeSeleccionat = algorithmSelectorComboBox.getSelectedItem().toString();
        try {
            vistaPrincipal.generateLayoutTeclat(algoritmeSeleccionat);
        } catch (TeclatTemporalBuitException | InvalidGeneratorAlgorithmException e) {
            // No pot passar
            e.printStackTrace();
        } catch (MissingPairsFreqException e) {
            // No s'ha seleccionat un text o llista de freqüència
            JOptionPane.showMessageDialog(this, "No s'ha seleccionat un text o llista de freqüència", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Actualitzem el teclat
        updateTeclat();
    }//GEN-LAST:event_algorithmGenerateButtonActionPerformed

    /**
     * Tanca la finestra. Es crida quan es prem el botó de cancel·lar.
     *
     * @param evt Event de cancel·lar la gestió.
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        close();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Crea un nou teclat amb els valors presents a la finestra.
     * Es crida quan es prem el botó de crear.
     *
     * @param evt Event de crear.
     */
    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        try {
            vistaPrincipal.guardarTeclat(nameTextField.getText());
        } catch (NomJaExisteixException e) {
            JOptionPane.showMessageDialog(this, "Ja existeix un teclat amb el nom " + e.getNom() + ".", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (NomNoExisteixException e) {
            JOptionPane.showMessageDialog(this, "El nom no pot estar buit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (TeclatTemporalBuitException e) {
            // No pot passar
            e.printStackTrace();
        } catch (NomBuitException e) {
            JOptionPane.showMessageDialog(this, "El nom no pot estar buit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (NomMassaLlargException e) {
            JOptionPane.showMessageDialog(this, "El nom és massa llarg.\nEl màxim permès és 100 caràcters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        vistaPrincipal.setVisible(true);
        dispose();
    }//GEN-LAST:event_createButtonActionPerformed

    /**
     * Actualitza el teclat temporal amb els parells de freqüència addients.
     * Es crida quan es prem el combo box per seleccionar el text o la llista del teclat.
     *
     * @param evt Event de triar text o llista.
     */
    private void textOrFreqListSelectorComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textOrFreqListSelectorComboBoxActionPerformed
        // actualitza la llista de parells de freqüència del teclat temporal mitjançant el text o freq list seleccionada
        if (textOrFreqListSelectorComboBox.getSelectedItem() == null || textOrFreqListSelectorComboBox.getSelectedItem().toString().equals(defaultComboBoxText) || textOrFreqListSelectorComboBox.getSelectedItem().toString().equals(defaultComboBoxFreqList)) {
            return;
        }
        if (generateLayoutFromTextRadioButton.isSelected()) {
            try {
                vistaPrincipal.setFreqPairsByTextTeclat(textOrFreqListSelectorComboBox.getSelectedItem().toString());
            } catch (NomNoExisteixException | CaractersNoInclososException | TeclatTemporalBuitException e) {
                // No pot passar
                System.out.println("Error al seleccionar el text");
                e.printStackTrace();
            }
        } else {
            try {
                vistaPrincipal.setFreqPairsByFreqListTeclat(textOrFreqListSelectorComboBox.getSelectedItem().toString());
            } catch (NomNoExisteixException | TeclatTemporalBuitException | CaractersNoInclososException e) {
                // No pot passar
                System.out.println("Error al seleccionar la llista de freqüències");
                e.printStackTrace();
            }
        }
        updateTeclat();
    }//GEN-LAST:event_textOrFreqListSelectorComboBoxActionPerformed

    /**
     * Fica el millor layout trobat fins ara.
     * Es crida quan l'usuari fa clic al botó per retornar al millor layout trobat fins ara.
     *
     * @param evt Event de fer clic al botó per retornar al millor layout trobat fins ara.
     */
    private void retornarMillorLayoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retornarMillorLayoutButtonActionPerformed
        try {
            vistaPrincipal.setBestLayoutTeclat();
        } catch (TeclatTemporalBuitException | MissingBestLayoutException e) {
            // No pot passar
            e.printStackTrace();
        }
        updateTeclat();
    }//GEN-LAST:event_retornarMillorLayoutButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> alfabetSelectorComboBox;
    private javax.swing.JLabel alfabetSelectorLabel;
    private javax.swing.JButton algorithmGenerateButton;
    private javax.swing.JComboBox<String> algorithmSelectorComboBox;
    private javax.swing.JLabel algorithmSelectorLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel centeredPane;
    private javax.swing.JFormattedTextField colsFormatedTextField;
    private javax.swing.JLabel costTeclatLabel;
    private javax.swing.JButton createButton;
    private javax.swing.JLabel dimensionsLabel;
    private javax.swing.JLabel dimensionsXLabel;
    private javax.swing.JRadioButton generateLayoutFromFreqListRadioButton;
    private javax.swing.JLabel generateLayoutFromLabel;
    private javax.swing.JRadioButton generateLayoutFromTextRadioButton;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel keyboardPreviewLabel;
    private javax.swing.JPanel keyboardPreviewPane;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPanel personalizedKeysPane;
    private javax.swing.JButton retornarMillorLayoutButton;
    private javax.swing.JFormattedTextField rowsFormattedTextField;
    private javax.swing.JLabel selectTextOrFreqListLabel;
    private edu.upc.prop.teclat.presentacio.TeclesPerDefectePanel teclesPerDefectePane;
    private javax.swing.JComboBox<String> textOrFreqListSelectorComboBox;
    // End of variables declaration//GEN-END:variables
}
