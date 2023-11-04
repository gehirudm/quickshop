/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import quickshop.entity.User;
import quickshop.services.UsersService;
import quickshop.services.UsersService.UserType;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gehiru
 */
public class UsersWindow extends javax.swing.JFrame {

    private Logger logger;
    private UsersService usersService;

    private ArrayList<User> users;

    /**
     * Creates new form UsersWindow
     */
    public UsersWindow(Logger logger, UsersService usersService) {
        this.logger = logger;
        this.usersService = usersService;
        try {
            users = new ArrayList<>(usersService.getAllUsers());
        } catch (SQLException e) {
            logger.severe("Failed to retrieve users");
            JOptionPane.showMessageDialog(this, "Failed to retrieve users from the database (SQLException)",
                    "Error Occured", JOptionPane.ERROR_MESSAGE);
        }
        initComponents();
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        searchTextField = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        newUserBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 10, 5));
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(searchTextField, java.awt.BorderLayout.CENTER);

        searchBtn.setText("Search");
        jPanel1.add(searchBtn, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        var userTableRows = users.stream().map(user -> new Object[] {
                user.ID(),
                user.username(),
                user.type() == UserType.ADMIN ? "Admin" : "Cashier"
        }).toArray(Object[][]::new);

        userTable.setModel(new javax.swing.table.DefaultTableModel(
                userTableRows,
                new String[] {
                        "ID", "Name", "Account Type"
                }) {
            Class<?>[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[] {
                    false, false, false
            };

            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(userTable);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        newUserBtn.setText("New User");
        jPanel2.add(newUserBtn);

        deleteBtn.setText("Delete Selected");
        jPanel2.add(deleteBtn);

        editBtn.setText("Edit Selected");
        jPanel2.add(editBtn);

        newUserBtn.addActionListener(this::addNewUser);
        deleteBtn.addActionListener(this::removeSelectedUser);
        editBtn.addActionListener(this::editSelectedUser);
        searchBtn.addActionListener(this::searchUsers);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        pack();
    }

    private void searchUsers(ActionEvent evt) {
        var searchPhrase = searchTextField.getText();

        ArrayList<User> results = new ArrayList<>();

        if (searchPhrase.equals("")) {
            results = users;
        } else {
            results = users.stream().filter(user -> user.username().contains(searchPhrase))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        userTable.setModel(getTableModel());

        results.stream().forEach(user -> {
            ((DefaultTableModel) userTable.getModel()).addRow(new Object[] {
                    user.ID(),
                    user.username(),
                    user.type() == UserType.ADMIN ? "Admin" : "Cashier"
            });
        });

    }

    private void addNewUser(ActionEvent evt) {
        new EditAddUserWindow(logger, (user, window) -> {
            window.setVisible(false);
            window.dispose();

            var password = user.password().orElse("");
            User newUser;

            try {
                newUser = usersService.createUser(user.username(), password, user.type());
            } catch (SQLException e) {
                logger.severe("Failed to create user account");
                JOptionPane.showMessageDialog(this, "Failed to create new user account (SQLException)",
                        "Error Occured", JOptionPane.ERROR_MESSAGE);
                return;
            }

            users.add(newUser);

            var tableModel = (DefaultTableModel) userTable.getModel();

            tableModel.addRow(new Object[] {
                    newUser.ID(),
                    newUser.username(),
                    newUser.type() == UserType.ADMIN ? "Admin" : "Cashier"
            });
        }).setVisible(true);
    }

    private void editSelectedUser(ActionEvent evt) {
        var selectedIndex = userTable.getSelectedRow();
        if (selectedIndex == -1)
            return;

        var selectedUser = users.get(selectedIndex);

        var selectedUserEditUser = new User(selectedUser.ID(), selectedUser.username(), selectedUser.type());

        new EditAddUserWindow(logger, selectedUserEditUser, (user, window) -> {
            window.setVisible(false);
            window.dispose();

            User updatedUser;

            // Check if password has been changed
            try {
                if (user.password().isPresent()) {
                    updatedUser = usersService.updateUser(selectedUser.ID(), selectedUser.username(),
                            user.password().get(), selectedUser.type());
                } else {
                    updatedUser = usersService.updateUser(selectedUser.ID(), user.username(), user.type());
                }
            } catch (SQLException e) {
                logger.severe("Failed to update user account");
                JOptionPane.showMessageDialog(this, "Failed to update new user account (SQLException)",
                        "Error Occured", JOptionPane.ERROR_MESSAGE);
                return;
            }

            users.set(selectedIndex, updatedUser);

            var tableModel = (DefaultTableModel) userTable.getModel();

            tableModel.removeRow(selectedIndex);
            tableModel.insertRow(selectedIndex, new Object[] {
                    updatedUser.ID(),
                    updatedUser.username(),
                    updatedUser.type() == UserType.ADMIN ? "Admin" : "Cashier"
            });
        }).setVisible(true);
    }

    private void removeSelectedUser(ActionEvent evt) {
        var selectedIndex = userTable.getSelectedRow();
        if (selectedIndex == -1)
            return;

        var choice = JOptionPane.showConfirmDialog(
                this, // Parent component (null for a default frame)
                "Are you sure you want to delete the selected User. This cannot be undone", // Message to display
                "Confirm Action", // Dialog title
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION)
            return;

        // Delete item from the database
        try {
            usersService.deleteUser(users.get(selectedIndex).ID());
        } catch (SQLException e) {
            logger.severe("Failed to delete user account");
            JOptionPane.showMessageDialog(this, "Failed to delete new user account (SQLException)",
                    "Error Occured", JOptionPane.ERROR_MESSAGE);
            return;
        }

        users.remove(selectedIndex);

        var tableModel = (DefaultTableModel) userTable.getModel();
        tableModel.removeRow(selectedIndex);
    }

    private DefaultTableModel getTableModel() {
        return new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "ID", "Name", "Account Type"
                }) {
            Class<?>[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[] {
                    false, false, false
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newUserBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
