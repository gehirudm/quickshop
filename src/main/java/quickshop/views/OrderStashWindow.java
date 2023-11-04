/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import quickshop.entity.StashedOrder;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gehiru
 */
public class OrderStashWindow extends javax.swing.JFrame {

    private List<StashedOrder> stashedOrders;
    private BiConsumer<StashedOrder, Integer> onLoad;
    /**
     * Creates new form OrderStashWindow
     */
    public OrderStashWindow(List<StashedOrder> stashedOrders, BiConsumer<StashedOrder, Integer> onLoad) {
        this.stashedOrders = stashedOrders;
        this.onLoad = onLoad;
        initComponents();
    }

    
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        stashedOrdersTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        loadBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        stashedOrdersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Created At", "Total Items", "Total Price"
            }
        ) {
            Class<?>[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class<?> getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(stashedOrdersTable);

        addItemsToTable();

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        jPanel1.setLayout(new java.awt.GridLayout(1, 3, 10, 0));

        loadBtn.setText("Load");
        jPanel1.add(loadBtn);

        deleteBtn.setText("Delete");
        jPanel1.add(deleteBtn);

        cancelBtn.setText("Cancel");
        jPanel1.add(cancelBtn);

        loadBtn.addActionListener(this::loadSelected);
        deleteBtn.addActionListener(this::deleteSelected);
        cancelBtn.addActionListener(e -> dispose());

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }

    private void addItemsToTable() {
        var tableModel = (DefaultTableModel) stashedOrdersTable.getModel();

        for (int i = 0; i < stashedOrders.size(); i++) {
            var item = stashedOrders.get(i);
            tableModel.addRow(new Object[]{
                i + 1,
                item.createdAt(),
                item.items().size(),
                (float) item.items().stream().mapToDouble(x -> x.quantity() * x.item().price()).sum()
            }); // "No", "Created At", "Total Items", "Total Price"
        }
    }

    private void loadSelected(ActionEvent e) {
        var selectedIndex = stashedOrdersTable.getSelectedRow();
        if (selectedIndex == -1) return;

        var selected = stashedOrders.get(selectedIndex);
        onLoad.accept(selected, selectedIndex);
        dispose();
    }

    private void deleteSelected(ActionEvent e) {
        var selectedIndex = stashedOrdersTable.getSelectedRow();
        if (selectedIndex == -1) return;
            
        
        var tableModel = (DefaultTableModel) stashedOrdersTable.getModel();
        tableModel.removeRow(selectedIndex);
        stashedOrders.remove(selectedIndex);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable stashedOrdersTable;
    private javax.swing.JButton loadBtn;
    // End of variables declaration//GEN-END:variables
}
