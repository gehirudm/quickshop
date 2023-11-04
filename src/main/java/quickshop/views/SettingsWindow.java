/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Image;
import javax.swing.filechooser.FileFilter;

import quickshop.services.LocalizationService;
import quickshop.services.LocalizationService.Language;

/**
 *
 * @author gehiru
 */
public class SettingsWindow extends javax.swing.JFrame {

    @FunctionalInterface
    public interface OnSaveFunction {
        void accept(Optional<Path> companyLogoPath, Optional<String> companyName, Optional<Language> language, JFrame window);
    }

    private Path companyLogoImagePath;
    private OnSaveFunction onSave;
    private Path currentLogoPath;
    private String currentCompanyName;
    private Logger logger;
    private LocalizationService localizationService;

    /**
     * Creates new form SettingsWindow
     */
    public SettingsWindow(LocalizationService localizationService, Logger logger, Path currentLogoPath, String currentCompanyName, OnSaveFunction onSave) {
        this.localizationService = localizationService;
        this.logger = logger;
        this.currentLogoPath = currentLogoPath;
        this.currentCompanyName = currentCompanyName;
        this.onSave = onSave;
        initComponents();
    }

    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel6 = new javax.swing.JPanel();
        changeLogoBtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        logoLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        companyNameField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        languageSelectorPanel = new JPanel();
        languageSelectionComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(360, 350));

        jPanel4.setLayout(new java.awt.GridLayout(2, 0));

        jLayeredPane1.setPreferredSize(new java.awt.Dimension(400, 120));
        jLayeredPane1.setLayout(new javax.swing.OverlayLayout(jLayeredPane1));

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(80, 50, 0, 0));
        jPanel6.setOpaque(false);

        changeLogoBtn.setText("Change");
        jPanel6.add(changeLogoBtn);

        jLayeredPane1.add(jPanel6);

        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(400, 120));

        logoLabel.setIcon(new ImageIcon(
                new ImageIcon(currentLogoPath.toString()).getImage().getScaledInstance(100, 100,
                        Image.SCALE_DEFAULT)));
        logoLabel.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanel5.add(logoLabel);

        jLayeredPane1.add(jPanel5);

        jPanel4.add(jLayeredPane1);

        var fieldContainer = new JPanel();
        fieldContainer.setLayout(new java.awt.GridLayout(2, 0));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel2.setText("Company Name");
        jPanel1.add(jLabel2, java.awt.BorderLayout.PAGE_START);
        
        companyNameField.setText(currentCompanyName);
        
        jPanel1.add(companyNameField, java.awt.BorderLayout.CENTER);

        languageSelectorPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        languageSelectorPanel.setLayout(new java.awt.BorderLayout());

        languageSelectorPanel.add(new JLabel("UI Language"), java.awt.BorderLayout.PAGE_START);
        
        languageSelectionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
                (String[]) Arrays.stream(Language.values()).map(lang -> lang.lang).toArray(String[]::new)));
        languageSelectionComboBox.setSelectedIndex(-1);
        
        languageSelectorPanel.add(languageSelectionComboBox, java.awt.BorderLayout.CENTER);

        fieldContainer.add(jPanel1);
        fieldContainer.add(languageSelectorPanel);

        jPanel4.add(fieldContainer);
        

        // jPanel4.add(jPanel1);

        

        // jPanel4.add(languageSelectorPanel);

        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        saveBtn.setText("Save");
        jPanel3.add(saveBtn);

        cancelBtn.setText("Cancel");
        jPanel3.add(cancelBtn);

        saveBtn.addActionListener(this::save);
        cancelBtn.addActionListener(e -> dispose());
        changeLogoBtn.addActionListener(this::changeLogo);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }

    private void changeLogo(java.awt.event.ActionEvent evt) {
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
            ImageIcon imageIcon = new ImageIcon(
                    new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            logoLabel.setIcon(imageIcon);
            companyLogoImagePath = file.toPath();
        }
    }

    private void save(java.awt.event.ActionEvent evt) {
        if (companyNameField.getText().isEmpty()) 
            return;

        Optional<Path> logoPath = companyLogoImagePath == null ? Optional.empty() : Optional.of(companyLogoImagePath);
        Optional<String> name = companyNameField.getText().equals(currentCompanyName) ? Optional.empty() : Optional.of(companyNameField.getText());

        var selectedLang = switch (languageSelectionComboBox.getSelectedIndex()) {
            case 0 -> Language.ENGLISH;
            case 1 -> Language.GERMAN;
            case 2 -> Language.SINHALA;
            default -> Language.ENGLISH;
        };

        Optional<Language> language = localizationService.getCurrentLanguage() == selectedLang ? Optional.empty() : Optional.of(selectedLang);

        // if (companyLogoImagePath != null) {
        //     // Copy the image if an Image was selected
        //     var copiedFilePath = new File("./images/logo" + Util.getFileExtension(companyLogoImagePath.toFile())).toPath();
        //     try {
        //         Files.copy(companyLogoImagePath, copiedFilePath);
        //     } catch (IOException e) {
        //         logger.severe("Failed to copy logo image file (IOException)");
        //         JOptionPane.showMessageDialog(this, "Failed to save changes", "Error Occured", ERROR);
        //         dispose();
        //         return;
        //     }
            
        //     logoPath = Optional.of(copiedFilePath);
        // }
        // else logoPath = Optional.empty();

        onSave.accept(logoPath, name, language, this);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton changeLogoBtn;
    private javax.swing.JTextField companyNameField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JButton saveBtn;
    private JPanel languageSelectorPanel;
    private javax.swing.JComboBox<String> languageSelectionComboBox;
    // End of variables declaration//GEN-END:variables
}
