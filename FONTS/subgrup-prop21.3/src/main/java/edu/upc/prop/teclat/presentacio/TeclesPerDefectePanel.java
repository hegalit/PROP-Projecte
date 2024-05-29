/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package edu.upc.prop.teclat.presentacio;

/**
 * Aquesta classe representa una tecla de la visualització d'un teclat.
 * @author Pau Marín Roig (pau.marin.roig@estudiantat.upc.edu)
 */
public class TeclesPerDefectePanel extends javax.swing.JPanel {

    /**
     * Constructora per defecte
     */
    public TeclesPerDefectePanel() {
        initComponents();
    }
    
    /**
     * Inicialitza els atributs de la classe i els elements que contindrà la tecla
     * (tecla per defecte en la creació de qualsevol teclat).
     */
    //GEN-BEGIN:initComponents
    private void initComponents() {

        enterPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        escPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        majPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        spacePanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        accentObertPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        accentTancatPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        delPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        tabPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();

        setLayout(new java.awt.GridLayout(1, 5, 5, 5));

        enterPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Enter");
        enterPanel.add(jLabel1);

        add(enterPanel);

        escPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("Esc");
        escPanel.add(jLabel2);

        add(escPanel);

        majPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("MAJ");
        majPanel.add(jLabel3);

        add(majPanel);

        spacePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setText("SPACE");
        spacePanel.add(jLabel4);

        add(spacePanel);

        accentObertPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setText("`");
        accentObertPanel.add(jLabel5);

        add(accentObertPanel);

        accentTancatPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setText("´");
        accentTancatPanel.add(jLabel6);

        add(accentTancatPanel);

        delPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setText("<--");
        delPanel.add(jLabel7);

        add(delPanel);

        tabPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setText("TAB");
        tabPanel.add(jLabel8);

        add(tabPanel);
    }//GEN-END:initComponents


    //GEN-BEGIN:variables
    private javax.swing.JPanel accentObertPanel;
    private javax.swing.JPanel accentTancatPanel;
    private javax.swing.JPanel delPanel;
    private javax.swing.JPanel enterPanel;
    private javax.swing.JPanel escPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel majPanel;
    private javax.swing.JPanel spacePanel;
    private javax.swing.JPanel tabPanel;
    //GEN-END:variables
}
