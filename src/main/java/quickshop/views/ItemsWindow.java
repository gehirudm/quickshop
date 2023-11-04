/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import quickshop.entity.Item;
import quickshop.entity.User;
import quickshop.services.ItemsService;
import quickshop.services.UsersService.UserType;
import quickshop.util.Util;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author gehiru
 */
public class ItemsWindow extends javax.swing.JFrame {

    private ArrayList<Item> items;
    private Logger logger;
    private ItemsService itemsService;

    /**
     * Creates new form ItemsWindow
     */
    public ItemsWindow(Logger logger, ItemsService itemsService) {
        this.logger = logger;
        this.itemsService = itemsService;

        try {
            this.items = new ArrayList<>(itemsService.getAllItems());
        } catch (SQLException e) {
            logger.severe("Failed to retrieve items");
            JOptionPane.showMessageDialog(this, "Failed to retrieve items from the database (SQLException)",
                    "Error Occured", JOptionPane.ERROR_MESSAGE);
        }

        initComponents();
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        searchTextField = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        newItemBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 10, 5));
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(searchTextField, java.awt.BorderLayout.CENTER);

        searchBtn.setText("Search");
        jPanel1.add(searchBtn, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        var tableRows = items.stream().map(item -> new Object[] {
                item.ID(),
                item.name(),
                Util.formatToRupees(item.price()),
                item.stock()
        }).toArray(Object[][]::new);

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
                tableRows,
                new String[] {
                        "ID", "Name", "Unit Price", "Stock"
                }) {
            Class<?>[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean[] {
                    false, false, false, false
            };

            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(itemTable);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        newItemBtn.setText("Add New");
        jPanel2.add(newItemBtn);

        deleteBtn.setText("Delete Selected");
        jPanel2.add(deleteBtn);

        editBtn.setText("Edit Selected");
        jPanel2.add(editBtn);

        newItemBtn.addActionListener(this::addNewItem);
        deleteBtn.addActionListener(this::deleteSelected);
        editBtn.addActionListener(this::editSelected);
        searchBtn.addActionListener(this::searchItems);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        pack();
    }

    private void addNewItem(ActionEvent evt) {
        new EditAddItemWindow(logger, (item, window) -> {
            window.setVisible(false);
            window.dispose();

            // Add item to database
            Item newItem;

            try {
                newItem = itemsService.createItem(item.ID(), item.name(), item.price(), item.stock());
            } catch (SQLException e) {
                logger.severe("Failed to create item");
                JOptionPane.showMessageDialog(this, "Failed to create new item (SQLException)",
                        "Error Occured", JOptionPane.ERROR_MESSAGE);
                return;
            }

            items.add(newItem);
            var tableModel = (DefaultTableModel) itemTable.getModel();

            tableModel.addRow(new Object[] { newItem.ID(), newItem.name(), newItem.price(), newItem.stock() });
        }).setVisible(true);
    }

    private void deleteSelected(ActionEvent evt) {
        var selectedIndex = itemTable.getSelectedRow();
        if (selectedIndex == -1)
            return;

        var choice = JOptionPane.showConfirmDialog(
                this, // Parent component (null for a default frame)
                "Are you sure you want to delete the selected Item. This cannot be undone", // Message to display
                "Confirm Action", // Dialog title
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION)
            return;

        // Delete item from the database
        try {
            itemsService.deleteItem(items.get(selectedIndex).ID());
        } catch (SQLException e) {
            logger.severe("Failed to delete item");
            JOptionPane.showMessageDialog(this, "Failed to delete new item (SQLException)",
                    "Error Occured", JOptionPane.ERROR_MESSAGE);
            return;
        }

        items.remove(selectedIndex);

        var tableModel = (DefaultTableModel) itemTable.getModel();
        tableModel.removeRow(selectedIndex);
    }

    private void editSelected(ActionEvent evt) {
        var selectedIndex = itemTable.getSelectedRow();
        if (selectedIndex == -1)
            return;

        new EditAddItemWindow(logger, items.get(selectedIndex), (item, window) -> {
            window.setVisible(false);
            window.dispose();

            // Add item to database
            Item updatedItem = new Item(item.ID(), item.name(), item.price(), item.stock(), item.imagePath());

            try {
                itemsService.updateItem(updatedItem);
            } catch (SQLException e) {
                logger.severe("Failed to update item");
                JOptionPane.showMessageDialog(this, "Failed to update new item (SQLException)",
                        "Error Occured", JOptionPane.ERROR_MESSAGE);
                return;
            }

            items.set(selectedIndex, updatedItem);
            var tableModel = (DefaultTableModel) itemTable.getModel();

            tableModel.removeRow(selectedIndex);
            tableModel.insertRow(selectedIndex,
                    new Object[] { updatedItem.ID(), updatedItem.name(), updatedItem.price(), updatedItem.stock() });
        }).setVisible(true);
    }

    private void searchItems(ActionEvent evt) {
        var searchPhrase = searchTextField.getText();

        ArrayList<Item> results = new ArrayList<>();

        if (searchPhrase.equals("")) {
            results = items;
        } else {
            results = items.stream().filter(user -> user.name().contains(searchPhrase))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        itemTable.setModel(getTableModel());

        results.stream().forEach(user -> {
            ((DefaultTableModel) itemTable.getModel()).addRow(new Object[] { user.ID(), user.name(), user.price(), user.stock() });
        });

    }

    private DefaultTableModel getTableModel() {
        return new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "ID", "Name", "Unit Price", "Stock"
                }) {
            Class<?>[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean[] {
                    false, false, false, false
            };

            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JTable itemTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newItemBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
