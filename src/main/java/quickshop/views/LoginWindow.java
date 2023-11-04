/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import quickshop.entity.User;
import quickshop.services.UsersService;

import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.function.BiConsumer;

/**
 *
 * @author gehiru
 */
public class LoginWindow extends javax.swing.JFrame {

    private UsersService usersService;
    private BiConsumer<User, javax.swing.JFrame> afterLogin;
    /**
     * Creates new form LoginWindow
     */
    public LoginWindow(UsersService usersService, BiConsumer<User, javax.swing.JFrame> afterLogin) {
        this.usersService = usersService;
        this.afterLogin = afterLogin;
        initComponents();
    }

    
    private void initComponents() {

        mainContainerPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        loginBtn = new javax.swing.JButton();
        errorLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);

        // Get the screen dimensions
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calculate the x and y coordinates for centering
        int x = (screenSize.width - 300) / 2;
        int y = (screenSize.height - 400) / 2;

        setLocation(x, y);

        getContentPane().setLayout(new javax.swing.OverlayLayout(getContentPane()));

        mainContainerPanel.setMaximumSize(new java.awt.Dimension(300, 400));
        mainContainerPanel.setMinimumSize(new java.awt.Dimension(300, 400));
        mainContainerPanel.setOpaque(false);
        mainContainerPanel.setPreferredSize(new java.awt.Dimension(300, 400));
        mainContainerPanel.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Welcome");
        mainContainerPanel.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(100, 20, 80, 20));
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new java.awt.GridLayout(2, 0, 0, 10));

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.BorderLayout(0, 10));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Username");
        jPanel1.add(jLabel3, java.awt.BorderLayout.PAGE_START);
        jPanel1.add(usernameField, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel1);

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.BorderLayout(0, 10));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Password");
        jPanel5.add(jLabel4, java.awt.BorderLayout.PAGE_START);
        jPanel5.add(passwordField, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel5);

        mainContainerPanel.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 50, 5, 50));
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout());

        loginBtn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        loginBtn.setForeground(new java.awt.Color(51, 51, 51));
        loginBtn.setText("Login");
        jPanel2.add(loginBtn, java.awt.BorderLayout.CENTER);

        loginBtn.addActionListener(this::login);

        errorLabel.setForeground(new java.awt.Color(255, 0, 0));
        errorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        jPanel2.add(errorLabel, java.awt.BorderLayout.PAGE_START);

        mainContainerPanel.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(mainContainerPanel);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/backgrounds/login.jpg"))); // NOI18N
        jLabel1.setAlignmentX(0.5F);
        getContentPane().add(jLabel1);

        pack();
    }

    private void login(java.awt.event.ActionEvent evt) {
        try {
            switch (usersService.login(usernameField.getText(), new String(passwordField.getPassword()))) {
                case INVALID_PASSWORD:
                    errorLabel.setText("Invalid Password");
                    break;
                case INVALID_USERNAME:
                    errorLabel.setText("Unknown Username");
                    break;
                case SUCCESS:
                    // Get the logged in User
                    var user = usersService.getUserByUsername(usernameField.getText()).get();
                    afterLogin.accept(user, this);
                    break;
                default:
                    break;
            }
            
        } catch (SQLException e) {
            errorLabel.setText("Error occured while trying to login");
        }
    }

    // /**
    //  * @param args the command line arguments
    //  */
    // public static void main(String args[]) {
    //     /* Set the Nimbus look and feel */
    //     try {
    //         UIManager.setLookAndFeel(new FlatCyanLightIJTheme());
    //     } catch (Exception ex) {
    //         System.err.println("Failed to initialize LaF");
    //     }

    //     /* Create and display the form */
    //     java.awt.EventQueue.invokeLater(new Runnable() {
    //         public void run() {
    //             new LoginWindow().setVisible(true);
    //         }
    //     });
    // }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField usernameField;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPanel mainContainerPanel;
    // End of variables declaration//GEN-END:variables
}
