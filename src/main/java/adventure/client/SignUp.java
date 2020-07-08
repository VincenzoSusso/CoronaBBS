/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure.client;

import javax.swing.*;

/**
 *
 * @author mstel
 */
public class SignUp extends javax.swing.JFrame {

    private JButton JButtonSignUp;
    private JComboBox<String> JComboBoxDay;
    private JComboBox<String> JComboBoxMonth;
    private JComboBox<String> JComboBoxYear;
    private JLabel JLabelUsername;
    private JLabel JLabelPassword;
    private JLabel JLabelDate;
    private JLabel JLabelResidence;
    private JLabel JLabelDay;
    private JLabel JLabelMonth;
    private JLabel JLabelYear;
    private JPasswordField JPasswordField;
    private JTextField JTUsernameField;
    private JTextField JTResidenceField;
    private JDialog JDialogMain;

    public SignUp(JDialog parentDialog) {
        JDialogMain = new JDialog(parentDialog, true);
        initComponents();
    }

    public JButton getJButtonSignUp() {
        return JButtonSignUp;
    }

    public JComboBox<String> getJComboBoxDay() {
        return JComboBoxDay;
    }

    public JComboBox<String> getJComboBoxMonth() {
        return JComboBoxMonth;
    }

    public JComboBox<String> getJComboBoxYear() {
        return JComboBoxYear;
    }

    public JLabel getJLabelDate() {
        return JLabelDate;
    }

    public JLabel getJLabelDay() {
        return JLabelDay;
    }

    public JLabel getJLabelMonth() {
        return JLabelMonth;
    }

    public JLabel getJLabelPassword() {
        return JLabelPassword;
    }

    public JLabel getJLabelResidence() {
        return JLabelResidence;
    }

    public JDialog getJDialogMain() {
        return JDialogMain;
    }

    public JLabel getJLabelUsername() {
        return JLabelUsername;
    }

    public JLabel getJLabelYear() {
        return JLabelYear;
    }

    public javax.swing.JPasswordField getJPasswordField() {
        return JPasswordField;
    }

    public JTextField getJTResidenceField() {
        return JTResidenceField;
    }

    public JTextField getJTUsernameField() {
        return JTUsernameField;
    }

    public void setJButtonSignUp(String JButtonSignUp) {
        this.JButtonSignUp.setName(JButtonSignUp);
    }

    public void setJComboBoxDay(String JComboBoxDay) {
        this.JComboBoxDay.setName(JComboBoxDay);
    }

    public void setJComboBoxMonth(String JComboBoxMonth) {
        this.JComboBoxMonth.setName(JComboBoxMonth);
    }

    public void setJComboBoxYear(String JComboBoxYear) {
        this.JComboBoxYear.setName(JComboBoxYear);
    }

    public void setJLabelDate(String JLabelDate) {
        this.JLabelDate.setName(JLabelDate);
    }

    public void setJDialogMain(String JDialogMain) {
        this.JDialogMain.setName(JDialogMain);
    }

    public void setJLabelDay(String JLabelDay) {
        this.JLabelDay.setName(JLabelDay);
    }

    public void setJLabelMonth(String JLabelMonth) {
        this.JLabelMonth.setName(JLabelMonth);
    }

    public void setJLabelPassword(String JLabelPassword) {
        this.JLabelPassword.setName(JLabelPassword);
    }

    public void setJLabelResidence(String JLabelResidence) {
        this.JLabelResidence.setName(JLabelResidence);
    }

    public void setJLabelUsername(String JLabelUsername) {
        this.JLabelUsername.setName(JLabelUsername);
    }

    public void setJLabelYear(String JLabelYear) {
        this.JLabelYear.setName(JLabelYear);
    }

    public void setJPasswordField(String JPasswordField) {
        this.JPasswordField.setName(JPasswordField);
    }

    public void setJTResidenceField(String JTResidenceField) {
        this.JTResidenceField.setName(JTResidenceField);
    }

    public void setJTUsernameField(String JTUsernameField) {
        this.JTUsernameField.setName(JTUsernameField);
    }

    private void initComponents() {

        JTUsernameField = new JTextField();
        JLabelUsername = new JLabel();
        JLabelPassword = new JLabel();
        JPasswordField = new JPasswordField();
        JButtonSignUp = new JButton();
        JLabelDate = new JLabel();
        JLabelResidence = new JLabel();
        JTResidenceField = new JTextField();
        JComboBoxDay = new JComboBox<>();
        JLabelDay = new JLabel();
        JLabelMonth = new JLabel();
        JComboBoxMonth = new JComboBox<>();
        JLabelYear = new JLabel();
        JComboBoxYear = new JComboBox<>();

        JDialogMain.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JDialogMain.setTitle("Registrazione");
        JDialogMain.add(JButtonSignUp);
        JDialogMain.add(JComboBoxDay);
        JDialogMain.add(JComboBoxMonth);
        JDialogMain.add(JComboBoxYear);
        JDialogMain.add(JLabelUsername);
        JDialogMain.add(JLabelPassword);
        JDialogMain.add(JLabelDate);
        JDialogMain.add(JLabelResidence);
        JDialogMain.add(JLabelDay);
        JDialogMain.add(JLabelMonth);
        JDialogMain.add(JLabelYear);
        JDialogMain.add(JPasswordField);
        JDialogMain.add(JTUsernameField);
        JDialogMain.add(JTResidenceField);

        JButtonSignUp.setEnabled(false);
        JDialogMain.setResizable(false);

        JLabelUsername.setText("Username:");

        JLabelPassword.setText("Password:");

        JButtonSignUp.setText("Registrati");
        JButtonSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        JLabelDate.setText("Data di nascita:");

        JLabelResidence.setText("Residenza:");

        JTResidenceField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        JComboBoxDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        JComboBoxDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        JLabelDay.setText("Giorno:");

        JLabelMonth.setText("Mese:");

        JComboBoxMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre" }));
        JComboBoxMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        JLabelYear.setText("Anno:");

        JComboBoxYear.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958", "1959", "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969", "1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014" }));
        JComboBoxYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(JDialogMain.getContentPane());
        JDialogMain.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(168, 168, 168)
                                .addComponent(JButtonSignUp))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(JLabelDate)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTResidenceField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(JTUsernameField)
                            .addComponent(JPasswordField)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JLabelUsername)
                                    .addComponent(JLabelPassword)
                                    .addComponent(JLabelResidence)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(JLabelDay)
                                        .addGap(13, 13, 13)
                                        .addComponent(JComboBoxDay, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(JLabelMonth)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(JComboBoxMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(JLabelYear)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JComboBoxYear, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(JLabelUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTUsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(JLabelPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(JLabelDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(JComboBoxDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLabelDay)
                    .addComponent(JLabelMonth)
                    .addComponent(JComboBoxMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JLabelYear)
                    .addComponent(JComboBoxYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(JLabelResidence)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JTResidenceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(JButtonSignUp)
                .addContainerGap())
        );
        JDialogMain.pack();
        JDialogMain.setLocationRelativeTo(null);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

}