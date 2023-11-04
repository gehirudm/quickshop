/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import quickshop.entity.Item;
import quickshop.services.ItemsService;
import quickshop.util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author gehiru
 */
public class EditAddItemWindow extends javax.swing.JFrame {

    private File selectedImageFile;
    private BiConsumer<Item, JFrame> onSave;
    private Logger logger;
    private boolean isEdit = false;
    private Item editItem;

    public EditAddItemWindow(Logger logger, BiConsumer<Item, JFrame> onSave) {
        this.logger = logger;
        this.onSave = onSave;
        initComponents();
    }

    public EditAddItemWindow(Logger logger, Item editItem, BiConsumer<Item, JFrame> onSave) {
        this.logger = logger;
        this.editItem = editItem;
        this.onSave = onSave;
        this.isEdit = true;

        initComponents();
        initFields(editItem);
    }

    private void initFields(Item item) {
        stockField.setText(String.valueOf(item.stock()));
        unitPriceField.setText(String.valueOf(item.price()));
        nameField.setText(item.name());
        IDField.setText(item.ID());
        selectedImageNameLabel.setText(item.imagePath().getFileName().toString());
    }

    private void initComponents() {

        // jLabel1 = new javax.swing.JLabel();
        // jTextField1 = new javax.swing.JTextField();
        // jLabel2 = new javax.swing.JLabel();
        // jTextField2 = new javax.swing.JTextField();
        // jPanel3 = new javax.swing.JPanel();
        new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        IDField = new javax.swing.JTextField();
        nameField = new javax.swing.JTextField();
        unitPriceField = new javax.swing.JTextField();
        stockField = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        selectImageBtn = new javax.swing.JButton();
        selectedImageNameLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        errorLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Add New Item");

        jPanel2.setMinimumSize(new java.awt.Dimension(360, 130));
        jPanel2.setPreferredSize(new java.awt.Dimension(360, 170));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 0, 5));
        jPanel4.setLayout(new java.awt.GridLayout(5, 0, 0, 5));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("ID");
        jPanel4.add(jLabel3);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Name");
        jPanel4.add(jLabel4);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Unit Price");
        jPanel4.add(jLabel5);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Stock");
        jPanel4.add(jLabel6);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Item Thumbnail");
        jPanel4.add(jLabel7);

        jPanel2.add(jPanel4, java.awt.BorderLayout.LINE_START);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 0, 5));
        jPanel6.setPreferredSize(new java.awt.Dimension(250, 113));
        jPanel6.setLayout(new java.awt.GridLayout(5, 0, 0, 5));

        IDField.setEditable(false);
        IDField.setEnabled(false);
        IDField.setText(UUID.randomUUID().toString());

        jPanel6.add(IDField);
        jPanel6.add(nameField);
        jPanel6.add(unitPriceField);
        jPanel6.add(stockField);

        jPanel9.setLayout(new java.awt.BorderLayout());

        selectImageBtn.setText("Select Image");
        jPanel9.add(selectImageBtn, java.awt.BorderLayout.LINE_START);
        jPanel9.add(selectedImageNameLabel, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel9);

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

        selectImageBtn.addActionListener(this::selectImage);
        saveBtn.addActionListener(this::saveItem);
        cancelBtn.addActionListener(this::cancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }

    private void selectImage(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileFilter() {
            public final static String JPEG = "jpeg";
            public final static String JPG = "jpg";
            public final static String PNG = "png";

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                String extension = getExtension(f);
                if (extension != null) {
                    return extension.equals(JPEG) || extension.equals(JPG) || extension.equals(PNG) ? true : false;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Image Only";
            }

            String getExtension(File f) {
                String ext = null;
                String s = f.getName();
                int i = s.lastIndexOf('.');

                if (i > 0 && i < s.length() - 1) {
                    ext = s.substring(i + 1).toLowerCase();
                }
                return ext;
            }
        });

        fileChooser.setAcceptAllFileFilterUsed(false);

        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            selectedImageNameLabel.setText(file.getName());
            selectedImageFile = file;
        }
    }

    private void saveItem(java.awt.event.ActionEvent evt) {
        // Validate fields
        try {
            var itemId = IDField.getText();
            var itemName = nameField.getText();
            var itemPrice = (float) Util.validateAndConvert(unitPriceField.getText(), Float.class).get();
            var itemStock = (int) Util.validateAndConvert(stockField.getText(), Integer.class).get();

            Item item;

            // Copy the image if an Image was selected
            if (selectedImageFile != null) {
                var copiedFilePath = new File("./images/" + itemId + "." + Util.getFileExtension(selectedImageFile)).toPath();
                Files.copy(selectedImageFile.toPath(), copiedFilePath);

                item = new Item(itemId, itemName, itemPrice, itemStock, copiedFilePath);
            } else {
                var imagePath = isEdit ? editItem.imagePath() : ItemsService.getItemDefaultImagePath();

                item = new Item(itemId, itemName, itemPrice, itemStock, imagePath);
            }

            // Execute callback
            onSave.accept(item, this);
        } catch (NoSuchElementException e) {
            errorLabel.setText("Invalid Inputs");
        } catch (IOException e) {
            logger.severe("Failed to copy item thumbnail file");
        }
    }

    private void cancel(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        this.dispose();
    }

    /*
     * public static void main(String args[]) {
     * try {
     * UIManager.setLookAndFeel(new FlatCyanLightIJTheme());
     * } catch (Exception ex) {
     * System.err.println("Failed to initialize LaF");
     * }
     * 
     * java.awt.EventQueue.invokeLater(new Runnable() {
     * public void run() {
     * new EditAddItemWindow().setVisible(true);
     * }
     * });
     * }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IDField;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel errorLabel;
    // private javax.swing.JLabel jLabel1;
    // private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    // private javax.swing.JTextField jTextField1;
    // private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton selectImageBtn;
    private javax.swing.JLabel selectedImageNameLabel;
    private javax.swing.JTextField stockField;
    private javax.swing.JTextField unitPriceField;
    // End of variables declaration//GEN-END:variables
}
