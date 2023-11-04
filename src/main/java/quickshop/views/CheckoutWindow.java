/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import quickshop.services.LocalizationService;
import quickshop.util.Util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.util.function.Consumer;

import javax.swing.JFrame;

/**
 *
 * @author gehiru
 */
public class CheckoutWindow extends javax.swing.JFrame {
    @FunctionalInterface
    public interface OnConfirmFunction {
        void accept(JFrame window, boolean printReceipt, float cash);
    }

    private SimpleStringProperty cashAmountText;
    private float orderTotal;
    private OnConfirmFunction onConfirm;
    private Consumer<JFrame> onCancel;

    private float cashValue = 0.0f;
    private LocalizationService localizationService;

    /**
     * Creates new form CheckoutWindow
     */
    public CheckoutWindow(LocalizationService localizationService, float orderTotal, OnConfirmFunction onConfirm, Consumer<JFrame> onCancel) {
        this.localizationService = localizationService;
        this.orderTotal = orderTotal;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;
        cashAmountText = new SimpleStringProperty("");

        var cashAmountTextChangeListner = (ChangeListener<String>) (observable, oldValue,
                newValue) -> {
            if (validateInput(newValue)) {
                setConfirmEnabled(true);
                updateBalance(newValue);
            } else setConfirmEnabled(false);
        };

        cashAmountText.addListener(cashAmountTextChangeListner);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cashAmountText.removeListener(cashAmountTextChangeListner);
            }
        });

        initComponents();
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cashAmountField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cashLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        balanceLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        confirmAndPrintBtn = new javax.swing.JButton();
        confirmBtn = new javax.swing.JButton();
        cancelOrderBtn = new javax.swing.JButton();
        editOrderBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(new java.awt.Point(400, 250));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 5, 0, 5));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(localizationService.getValue("checkout.window.order.finalization", "Order Finalization"));
        jPanel1.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 0, 10));
        jPanel3.setLayout(new java.awt.BorderLayout(5, 15));

        jPanel5.setPreferredSize(new java.awt.Dimension(390, 30));
        jPanel5.setLayout(new java.awt.BorderLayout(15, 0));

        jLabel5.setText(localizationService.getValue("checkout.window.cash.amount", "Cash Amount (RS. )"));
        jPanel5.add(jLabel5, java.awt.BorderLayout.LINE_START);

        jPanel5.add(cashAmountField, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 15, 0));
        jPanel4.setLayout(new java.awt.GridLayout(3, 3, 15, 0));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText(localizationService.getValue("checkout.window.total", "TOTAL: "));
        jPanel4.add(jLabel2);

        totalLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        totalLabel.setText(Util.formatToRupees(orderTotal));
        jPanel4.add(totalLabel);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText(localizationService.getValue("checkout.window.cash", "CASH: "));
        jPanel4.add(jLabel3);

        cashLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cashLabel.setText("Rs. 00.00");
        jPanel4.add(cashLabel);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText(localizationService.getValue("checkout.window.balance", "BALANCE: "));
        jPanel4.add(jLabel4);

        balanceLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        balanceLabel.setText("Rs. 00.00");
        jPanel4.add(balanceLabel);

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridLayout(0, 4));

        confirmBtn.setText(localizationService.getValue("checkout.window.confirm", "Confirm"));
        confirmBtn.setEnabled(false);
        jPanel2.add(confirmBtn);

        confirmAndPrintBtn.setText(localizationService.getValue("checkout.window.confirm.print", "Confirm & Print Reciept"));
        confirmAndPrintBtn.setEnabled(false);
        jPanel2.add(confirmAndPrintBtn);

        cancelOrderBtn.setText(localizationService.getValue("checkout.window.cancel", "Cancel"));
        jPanel2.add(cancelOrderBtn);

        editOrderBtn.setText(localizationService.getValue("checkout.window.edit.order", "Edit Order"));
        jPanel2.add(editOrderBtn);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        confirmBtn.addActionListener(e -> onConfirm.accept(this, false, cashValue));
        confirmAndPrintBtn.addActionListener(e -> onConfirm.accept(this, true, cashValue));
        cancelOrderBtn.addActionListener(e -> onCancel.accept(this));
        editOrderBtn.addActionListener(e -> dispose());

        cashAmountField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                cashAmountText.set(cashAmountField.getText());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                cashAmountText.set(cashAmountField.getText());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                cashAmountText.set(cashAmountField.getText());
            }
        });

        getContentPane().add(jPanel1);

        pack();
    }

    private boolean validateInput(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setConfirmEnabled(boolean enabled) {
        confirmAndPrintBtn.setEnabled(enabled);
        confirmBtn.setEnabled(enabled);
    }

    private void updateBalance(String value) {
        var floatValue = Float.parseFloat(value);
        cashValue = floatValue;

        cashLabel.setText(Util.formatToRupees(floatValue));
        balanceLabel.setText(Util.formatToRupees(floatValue - orderTotal));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel balanceLabel;
    private javax.swing.JLabel cashLabel;
    private javax.swing.JButton confirmAndPrintBtn;
    private javax.swing.JButton confirmBtn;
    private javax.swing.JButton cancelOrderBtn;
    private javax.swing.JButton editOrderBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField cashAmountField;
    private javax.swing.JLabel totalLabel;
    // End of variables declaration//GEN-END:variables
}
