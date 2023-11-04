/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import quickshop.dto.RecieptItem;
import quickshop.dto.ReceiptMetadata;
import quickshop.entity.Sale;
import quickshop.entity.SalesItem;
import quickshop.services.EnvironmentService;
import quickshop.util.PrintUtils;
import quickshop.util.ReceiptGenerator;
import quickshop.util.Util;

import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 *
 * @author gehiru
 */
public class OrderViewWindow extends javax.swing.JFrame {

    private Sale order;
    private EnvironmentService environmentService;
    private Logger logger;

    /**
     * Creates new form OrderViewWindow
     */
    public OrderViewWindow(Logger logger, EnvironmentService environmentService, Sale order) {
        this.logger = logger;
        this.order = order;
        this.environmentService = environmentService;
        initComponents();
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        orderIDLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        orderTotalLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        orderDateLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderItemsTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        printRecieptBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridLayout(3, 0));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Order ID: ");
        jPanel5.add(jLabel1);

        orderIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        orderIDLabel.setText(order.ID());
        jPanel5.add(orderIDLabel);

        jPanel1.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Total: ");
        jPanel6.add(jLabel3);

        orderTotalLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        orderTotalLabel.setText(Util.formatToRupees(order.total()));
        jPanel6.add(orderTotalLabel);

        jPanel1.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Date: ");
        jPanel7.add(jLabel5);

        orderDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        orderDateLabel.setText(Util.formatDate(order.date()));
        jPanel7.add(orderDateLabel);

        jPanel1.add(jPanel7);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        var count = 1;
        var orderItemsTableRows = new ArrayList<Object[]>();
        for (SalesItem orderItem : order.items()) {
            orderItemsTableRows.add(new Object[] {
                    count,
                    orderItem.ID(),
                    orderItem.name(),
                    orderItem.unitPrice(),
                    orderItem.amount(),
                    orderItem.unitPrice() * orderItem.amount()
            });
        }

        orderItemsTable.setModel(new javax.swing.table.DefaultTableModel(
                orderItemsTableRows.toArray(Object[][]::new),
                new String[] {
                        "No", "Item ID", "Item Name", "Item Unit Price", "Quantity", "Total"
                }) {
            Class<?>[] types = new Class[] {
                    java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class,
                    java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false, false
            };

            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        orderItemsTable.setFocusable(false);
        jScrollPane1.setViewportView(orderItemsTable);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        printRecieptBtn.setText("Print Reciept");
        jPanel4.add(printRecieptBtn);

        printRecieptBtn.addActionListener(this::printRecipet);

        getContentPane().add(jPanel4, java.awt.BorderLayout.PAGE_END);

        pack();
    }

    private void printRecipet(ActionEvent evt) {
        // Check if there's a already generated recipet
        File receiptFile = new File("./receipts/receipt_" + order.ID() + ".pdf");
        String pdfPath;

        if (receiptFile.exists()) {
            pdfPath = "./receipts/receipt_" + order.ID() + ".pdf";
        } else {
            List<RecieptItem> recieptItems = order.items().stream().map(item -> new RecieptItem(item.ID(),
                    item.name(), item.unitPrice(), item.amount())).toList();

            var generator = new ReceiptGenerator("./receipts",
                    new ReceiptMetadata("N/A", order.ID(), recieptItems, (float) getOrderTotal()));

            try {
                pdfPath = generator
                        .setHeaderImage(environmentService.getCompanyLogoPath().toString())
                        .generate();
            } catch (IOException e) {
                logger.severe("Failed to print reciept for sale " + order.ID() + "(IOException)");
                JOptionPane.showMessageDialog(this, "Failed to print reciept. (IOException)", "Error Occured",
                        ERROR);
                return;
            }
        }

        try {
            PrintUtils.printPdf(pdfPath);
        } catch (PrinterException | IOException e) {
            logger.severe("Failed to print reciept for sale " + order.ID() + "(PrinterException)");
            JOptionPane.showMessageDialog(this, "Failed to print reciept. (PrinterException)",
                    "Error Occured", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double getOrderTotal() {
        return order.items().stream().mapToDouble(i -> i.amount() * i.unitPrice()).sum();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable orderItemsTable;
    private javax.swing.JLabel orderDateLabel;
    private javax.swing.JLabel orderIDLabel;
    private javax.swing.JLabel orderTotalLabel;
    private javax.swing.JButton printRecieptBtn;
    // End of variables declaration//GEN-END:variables
}
