/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import quickshop.dto.CreateOrUpdateUserDto;
import quickshop.entity.User;
import quickshop.services.UsersService.UserType;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import javax.swing.JFrame;

/**
 *
 * @author gehiru
 */
public class EditAddUserWindow extends javax.swing.JFrame {

    private Logger logger;
    private BiConsumer<CreateOrUpdateUserDto, JFrame> onSave;
    private boolean isEdit = false;
    /**
     * Creates new form EditAddUserWindow
     */
    public EditAddUserWindow(Logger logger, BiConsumer<CreateOrUpdateUserDto, JFrame> onSave) {
        this.logger = logger;
        this.onSave = onSave;
        initComponents();
    }

    public EditAddUserWindow(Logger logger, User editUser, BiConsumer<CreateOrUpdateUserDto, JFrame> onSave) {
        this.logger = logger;
        this.onSave = onSave;
        this.isEdit = true;
        initComponents();
        initFields(editUser);
    }

    private void initFields(User user) {
        IDField.setText(user.ID());
        usernameField.setText(String.valueOf(user.username()));
        userTypeComboBox.setSelectedIndex(user.type() == UserType.ADMIN ? 0 : 1);
    }

    
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        IDField = new javax.swing.JTextField();
        usernameField = new javax.swing.JTextField();
        userTypeComboBox = new javax.swing.JComboBox<>();
        passwordField = new javax.swing.JPasswordField();
        passwordRepeatField = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        errorLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add New User");

        passwordField.setToolTipText("Leave this field blank to keep the password unchanged when editing a User account");

        jPanel2.setMinimumSize(new java.awt.Dimension(360, 130));
        jPanel2.setPreferredSize(new java.awt.Dimension(360, 170));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 0, 5));
        jPanel4.setLayout(new java.awt.GridLayout(5, 0, 0, 5));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("ID");
        jPanel4.add(jLabel3);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Username");
        jPanel4.add(jLabel4);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("User Type");
        jPanel4.add(jLabel1);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Password");
        jPanel4.add(jLabel5);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Repeat Password");
        jPanel4.add(jLabel6);

        jPanel2.add(jPanel4, java.awt.BorderLayout.LINE_START);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 0, 5));
        jPanel6.setPreferredSize(new java.awt.Dimension(250, 113));
        jPanel6.setLayout(new java.awt.GridLayout(5, 0, 0, 5));

        IDField.setEditable(false);
        IDField.setEnabled(false);
        IDField.setText(UUID.randomUUID().toString());

        jPanel6.add(IDField);
        jPanel6.add(usernameField);

        userTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Cashier" }));
        userTypeComboBox.setSelectedIndex(-1);
        jPanel6.add(userTypeComboBox);
        jPanel6.add(passwordField);
        jPanel6.add(passwordRepeatField);

        jPanel2.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        jPanel8.setLayout(new java.awt.BorderLayout());

        errorLabel.setForeground(new java.awt.Color(255, 51, 51));
        jPanel8.add(errorLabel, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel8, java.awt.BorderLayout.CENTER);

        saveBtn.setText("Save");
        jPanel7.add(saveBtn);

        cancelBtn.setText("Cancel");
        jPanel7.add(cancelBtn);

        jPanel1.add(jPanel7, java.awt.BorderLayout.LINE_END);

        // Add event listeners
        cancelBtn.addActionListener(this::cancel);
        saveBtn.addActionListener(this::saveUser);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }

    private void saveUser(java.awt.event.ActionEvent evt) {
        // Validate fields
        try {
            var userId = IDField.getText();
            var username = usernameField.getText();
            var password = new String(passwordField.getPassword());
            var passwordRepeat = new String(passwordRepeatField.getPassword());
            var userType = userTypeComboBox.getSelectedIndex() == 0 ? UserType.ADMIN : UserType.CASHIER;

            if (!password.equals(passwordRepeat)) {
                errorLabel.setText("Passwords fields doesn't match");
                return;
            }

            var user = new CreateOrUpdateUserDto(
                userId,
                username,
                password.equals("") ? Optional.empty() : Optional.of(password),
                userType
            );

            // Execute callback
            onSave.accept(user, this);
        } catch (NoSuchElementException e) {
            errorLabel.setText("Invalid Inputs");
        }
    }

    private void cancel(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        this.dispose();
    }

    /*
    public static void main(String args[]) {
        
        try {
            UIManager.setLookAndFeel(new FlatCyanLightIJTheme());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditAddUserWindow().setVisible(true);
            }
        });
    }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IDField;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JPasswordField passwordRepeatField;
    private javax.swing.JButton saveBtn;
    private javax.swing.JComboBox<String> userTypeComboBox;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables
}
