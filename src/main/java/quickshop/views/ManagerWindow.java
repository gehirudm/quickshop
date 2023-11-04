/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import quickshop.services.ItemsService;
import quickshop.services.SalesService;
import quickshop.util.Util;

import java.awt.CardLayout;
import java.sql.SQLException;
import java.util.function.Consumer;

import javax.swing.JFrame;

/**
 *
 * @author gehiru
 */
public class ManagerWindow extends javax.swing.JFrame {

    private SalesService salesService;
    private ItemsService itemsService;
    private Consumer<JFrame> onItemsClick;
    private Consumer<JFrame> onUsersClick;
    private Consumer<JFrame> onReportsClick;
    private Consumer<JFrame> onSettingsClick;
    private Consumer<JFrame> onLogOut;

    /**
     * Creates new form ManagerWindow
     * 
     * @throws SQLException
     */
    public ManagerWindow(
            SalesService salesService,
            ItemsService itemsService,
            Consumer<JFrame> onItemsClick,
            Consumer<JFrame> onUsersClick,
            Consumer<JFrame> onReportsClick,
            Consumer<JFrame> onSettingsClick,
            Consumer<JFrame> onLogOut) throws SQLException {

        this.salesService = salesService;
        this.itemsService = itemsService;
        this.onItemsClick = onItemsClick;
        this.onUsersClick = onUsersClick;
        this.onReportsClick = onReportsClick;
        this.onSettingsClick = onSettingsClick;
        this.onLogOut = onLogOut;

        initComponents();
    }

    private void initComponents() throws SQLException {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        userAccountsBtn = new javax.swing.JButton();
        itemsBtn = new javax.swing.JButton();
        reportsBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        settingsBtn = new javax.swing.JButton();
        logOutBtn = new javax.swing.JButton();
        sidePanel = new javax.swing.JPanel();
        beginSetupCard = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        startSetupBtn = new javax.swing.JButton();
        quickReportCard = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        totItemsStatLabel = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        totItemsStatLabel1 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        totItemsStatLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanel1.setLayout(new java.awt.GridLayout(0, 2, 20, 20));

        jPanel2.setPreferredSize(new java.awt.Dimension(280, 260));
        jPanel2.setLayout(new java.awt.GridLayout(4, 0));

        userAccountsBtn.setFont(userAccountsBtn.getFont().deriveFont((float) 15));
        userAccountsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.png"))); // NOI18N
        userAccountsBtn.setText("Manage User Accounts");
        userAccountsBtn.setFocusPainted(false);
        userAccountsBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        userAccountsBtn.setIconTextGap(10);
        userAccountsBtn.setPreferredSize(new java.awt.Dimension(320, 60));
        jPanel2.add(userAccountsBtn);

        itemsBtn.setFont(itemsBtn.getFont().deriveFont((float) 15));
        itemsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/edit.png"))); // NOI18N
        itemsBtn.setText("Manage Items");
        itemsBtn.setFocusPainted(false);
        itemsBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        itemsBtn.setIconTextGap(10);
        itemsBtn.setPreferredSize(new java.awt.Dimension(320, 60));
        jPanel2.add(itemsBtn);

        reportsBtn.setFont(reportsBtn.getFont().deriveFont((float) 15));
        reportsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/report.png"))); // NOI18N
        reportsBtn.setText("View Reports");
        reportsBtn.setFocusPainted(false);
        reportsBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reportsBtn.setIconTextGap(10);
        reportsBtn.setPreferredSize(new java.awt.Dimension(320, 60));
        jPanel2.add(reportsBtn);

        jPanel3.setLayout(new java.awt.GridLayout());

        settingsBtn.setFont(settingsBtn.getFont().deriveFont((float) 15));
        settingsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/setting.png"))); // NOI18N
        settingsBtn.setText("Settings");
        settingsBtn.setFocusPainted(false);
        settingsBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        settingsBtn.setIconTextGap(10);
        settingsBtn.setPreferredSize(new java.awt.Dimension(320, 60));

        jPanel3.add(settingsBtn);

        logOutBtn.setFont(logOutBtn.getFont().deriveFont(logOutBtn.getFont().getStyle() | java.awt.Font.BOLD, 15));
        logOutBtn.setForeground(new java.awt.Color(255, 51, 51));
        logOutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit.png"))); // NOI18N
        logOutBtn.setText("Log Out");
        logOutBtn.setFocusPainted(false);
        logOutBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        logOutBtn.setIconTextGap(10);
        logOutBtn.setPreferredSize(new java.awt.Dimension(320, 60));

        jPanel3.add(logOutBtn);

        jPanel2.add(jPanel3);

        jPanel1.add(jPanel2);

        sidePanel.setPreferredSize(new java.awt.Dimension(300, 260));
        sidePanel.setLayout(new java.awt.CardLayout());

        beginSetupCard.setPreferredSize(new java.awt.Dimension(280, 260));
        beginSetupCard.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 1, 40, 1));
        jPanel4.setPreferredSize(new java.awt.Dimension(280, 200));
        jPanel4.setLayout(new java.awt.GridLayout(3, 0));

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD,
                jLabel1.getFont().getSize() + 10));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quick Setup");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel4.add(jLabel1);

        jPanel5.setLayout(new java.awt.GridLayout(2, 0));

        jLabel2.setText("It appears that you have not gone through the setup phrase yet. ");
        jLabel2.setPreferredSize(new java.awt.Dimension(250, 16));
        jPanel5.add(jLabel2);
        jLabel2.getAccessibleContext().setAccessibleName(
                "<html>It appears that you have not gone through the setup phrase yet. Quickly go through couple of steps and complete the setup.</html>");

        jLabel3.setText("Quickly go through couple of steps and complete the setup.");
        jPanel5.add(jLabel3);

        jPanel4.add(jPanel5);

        startSetupBtn.setFont(startSetupBtn.getFont().deriveFont(
                startSetupBtn.getFont().getStyle() | java.awt.Font.BOLD, startSetupBtn.getFont().getSize() + 9));
        startSetupBtn.setText("Start Setup");

        jPanel4.add(startSetupBtn);

        beginSetupCard.add(jPanel4, java.awt.BorderLayout.CENTER);

        sidePanel.add(beginSetupCard, "card2");

        quickReportCard.setPreferredSize(new java.awt.Dimension(280, 260));
        quickReportCard.setLayout(new java.awt.BorderLayout());

        jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getSize() + 10f));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Quick Stats");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 15, 1));
        quickReportCard.add(jLabel4, java.awt.BorderLayout.PAGE_START);

        jPanel6.setLayout(new java.awt.GridLayout(3, 0, 0, 15));

        jPanel7.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(null, "Total Items",
                        javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new java.awt.Font("Segoe UI", 1, 12)),
                javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10))); // NOI18N
        jPanel7.setLayout(new java.awt.BorderLayout());

        totItemsStatLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totItemsStatLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totItemsStatLabel.setText(String.valueOf(itemsService.getItemCount()));
        totItemsStatLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(totItemsStatLabel, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel7);

        jPanel10.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(null, "Sales Today",
                        javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new java.awt.Font("Segoe UI", 1, 12)),
                javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10))); // NOI18N
        jPanel10.setLayout(new java.awt.BorderLayout());

        totItemsStatLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totItemsStatLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totItemsStatLabel1.setText(String.valueOf(salesService.getTodaySalesCount()));
        totItemsStatLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel10.add(totItemsStatLabel1, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel10);

        jPanel11.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createTitledBorder(null, "Income Today",
                        javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new java.awt.Font("Segoe UI", 1, 12)),
                javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10))); // NOI18N
        jPanel11.setLayout(new java.awt.BorderLayout());

        totItemsStatLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totItemsStatLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totItemsStatLabel2.setText(Util.formatToRupees(salesService.getTodaySalesTotal()));
        totItemsStatLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel11.add(totItemsStatLabel2, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel11);

        quickReportCard.add(jPanel6, java.awt.BorderLayout.CENTER);

        sidePanel.add(quickReportCard, "quickReportCard");

        jPanel1.add(sidePanel);

        getContentPane().add(jPanel1);

        ((CardLayout) sidePanel.getLayout()).show(sidePanel, "quickReportCard");

        startSetupBtn.addActionListener(this::startSetupBtnActionPerformed);
        settingsBtn.addActionListener(this::settingsBtnActionPerformed);
        logOutBtn.addActionListener(this::logOutBtnActionPerformed);
        itemsBtn.addActionListener(this::openItems);
        reportsBtn.addActionListener(this::openReports);
        userAccountsBtn.addActionListener(this::openUserAccounts);

        pack();
    }

    private void startSetupBtnActionPerformed(java.awt.event.ActionEvent evt) {
        return;
    }

    private void logOutBtnActionPerformed(java.awt.event.ActionEvent evt) {
        onLogOut.accept(this);
    }

    private void settingsBtnActionPerformed(java.awt.event.ActionEvent evt) {
        onSettingsClick.accept(this);
    }

    private void openUserAccounts(java.awt.event.ActionEvent evt) {
        onUsersClick.accept(this);
    }

    private void openItems(java.awt.event.ActionEvent evt) {
        onItemsClick.accept(this);
    }

    private void openReports(java.awt.event.ActionEvent evt) {
        onReportsClick.accept(this);
    }

    /*
     * public static void main(String args[]) {
     * try {
     * UIManager.setLookAndFeel(new FlatCyanLightIJTheme());
     * } catch (Exception ex) {
     * System.err.println("Failed to initialize LaF");
     * }
     * 
     * java.awt.EventQueue.invokeLater(() -> {
     * new ManagerWindow().setVisible(true);
     * });
     * }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel beginSetupCard;
    private javax.swing.JButton itemsBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton logOutBtn;
    private javax.swing.JPanel quickReportCard;
    private javax.swing.JButton reportsBtn;
    private javax.swing.JButton settingsBtn;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JButton startSetupBtn;
    private javax.swing.JLabel totItemsStatLabel;
    private javax.swing.JLabel totItemsStatLabel1;
    private javax.swing.JLabel totItemsStatLabel2;
    private javax.swing.JButton userAccountsBtn;
    // End of variables declaration//GEN-END:variables
}
