/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import quickshop.dto.CreateOrUpdateUserDto;
import quickshop.dto.SetupDto;
import quickshop.entity.Item;
import quickshop.entity.User;
import quickshop.services.LocalizationService;
import quickshop.services.LocalizationService.Language;
import quickshop.services.UsersService.UserType;

import java.awt.CardLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gehiru
 */
public class QuickSetupWindow extends javax.swing.JFrame {

    private String[] cardNames = new String[] { "welcomeCard", "languageCard", "companyDetailsCard", "usersCard",
            "itemsCard" };
    private int currentCardPos = 0;

    private Path companyLogoImagePath;
    private CreateOrUpdateUserDto adminUserDto;
    private CreateOrUpdateUserDto cashierUserDto;
    private ArrayList<Item> items;

    private Logger logger;
    private LocalizationService localizationService;
    private BiConsumer<JFrame, SetupDto> onFinalize;

    /**
     * Creates new form QuickSetupWindow
     * 
     * @throws IOException
     */
    public QuickSetupWindow(Logger logger, LocalizationService localizationService,
            BiConsumer<JFrame, SetupDto> onFinalize) throws IOException {
        this.logger = logger;
        this.localizationService = localizationService;
        this.onFinalize = onFinalize;
        this.items = new ArrayList<>();

        initComponents();
        ((CardLayout) cardContainer.getLayout()).show(cardContainer, "welcomeCard");
    }

    private void initComponents() throws IOException {

        cardContainer = new javax.swing.JPanel();
        welcomeCard = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        languageCard = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        languageSelectionComboBox = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        langExLabel1 = new javax.swing.JLabel();
        langExLabel2 = new javax.swing.JLabel();
        langExBtn1 = new javax.swing.JButton();
        langExBtn2 = new javax.swing.JButton();
        companyDetailsCard = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel6 = new javax.swing.JPanel();
        changeLogoBtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        logoLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        companyNameField = new javax.swing.JTextField();
        usersCard = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        createAdminUserBtn = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        createCashierUserBtn = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        adminUserEditBtn = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        adminUserDetailsUsernameLabel = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        adminUserDetailsPasswordLabel = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        cashierUserEditBtn = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        cashierUserDetailsUsernameLabel = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        cashierUserDetailsPasswordLabel = new javax.swing.JLabel();
        itemsCard = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        itemNewItemBtn = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        itemRemoveSelectedBtn = new javax.swing.JButton();
        itemEditSelectedBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        prevBtn = new javax.swing.JButton();
        nextBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cardContainer.setLayout(new java.awt.CardLayout());

        welcomeCard.setLayout(new java.awt.GridLayout(2, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Let's get started.");
        welcomeCard.add(jLabel1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Click \"Next\" to begin the setup process. This won't take long.");
        welcomeCard.add(jLabel2);

        cardContainer.add(welcomeCard, "welcomeCard");

        languageCard.setLayout(new java.awt.BorderLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Select Language");
        jPanel3.add(jLabel6);

        languageSelectionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
                (String[]) Arrays.stream(Language.values()).map(lang -> lang.lang).toArray(String[]::new)));
        languageSelectionComboBox.setSelectedIndex(-1);
        jPanel3.add(languageSelectionComboBox);

        languageCard.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel9.setBorder(javax.swing.BorderFactory
                .createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        javax.swing.BorderFactory.createTitledBorder(null, "Examples",
                                javax.swing.border.TitledBorder.CENTER,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION)));
        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 50));

        langExLabel1.setText(localizationService.getLanguageProperty().getProperty("lang.example.label1"));
        jPanel9.add(langExLabel1);

        langExLabel2.setText(localizationService.getLanguageProperty().getProperty("lang.example.label2"));
        jPanel9.add(langExLabel2);

        langExBtn1.setText(localizationService.getLanguageProperty().getProperty("lang.example.btn1"));
        jPanel9.add(langExBtn1);

        langExBtn2.setText(localizationService.getLanguageProperty().getProperty("lang.example.btn2"));
        jPanel9.add(langExBtn2);

        languageCard.add(jPanel9, java.awt.BorderLayout.SOUTH);

        cardContainer.add(languageCard, "languageCard");

        companyDetailsCard.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Your company/ store details");
        companyDetailsCard.add(jLabel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 1, 1, 1));
        jPanel4.setLayout(new java.awt.GridLayout(2, 0));

        jLayeredPane1.setLayout(new javax.swing.OverlayLayout(jLayeredPane1));

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(80, 50, 0, 0));
        jPanel6.setOpaque(false);

        changeLogoBtn.setText("Change");
        jPanel6.add(changeLogoBtn);

        jLayeredPane1.add(jPanel6);

        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(400, 120));

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo/logo.png"))); // NOI18N
        // logoLabel.setText("jLabel1");
        logoLabel.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanel5.add(logoLabel);

        jLayeredPane1.add(jPanel5);

        jPanel4.add(jLayeredPane1);

        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 80, 10));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel4.setText("Company Name");
        jPanel7.add(jLabel4, java.awt.BorderLayout.PAGE_START);
        jPanel7.add(companyNameField, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel7);

        companyDetailsCard.add(jPanel4, java.awt.BorderLayout.CENTER);

        cardContainer.add(companyDetailsCard, "companyDetailsCard");

        usersCard.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Create your first users");
        usersCard.add(jLabel5, java.awt.BorderLayout.PAGE_START);

        jPanel10.setPreferredSize(new java.awt.Dimension(180, 50));

        jLabel7.setText("Create Admin User");
        jPanel10.add(jLabel7);

        createAdminUserBtn.setText("Create");
        jPanel10.add(createAdminUserBtn);

        jPanel8.add(jPanel10);

        jPanel11.setPreferredSize(new java.awt.Dimension(180, 50));

        jLabel8.setText("Create Cashier User");
        jPanel11.add(jLabel8);

        createCashierUserBtn.setText("Create");
        jPanel11.add(createCashierUserBtn);

        jPanel8.add(jPanel11);

        usersCard.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel12.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10),
                new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true)));
        jPanel12.setLayout(new java.awt.GridLayout(2, 0));

        jPanel13.setLayout(new java.awt.BorderLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Admin User Details");
        jPanel15.add(jLabel9);

        adminUserEditBtn.setText("Edit");
        jPanel15.add(adminUserEditBtn);

        jPanel13.add(jPanel15, java.awt.BorderLayout.NORTH);

        jPanel16.setLayout(new java.awt.GridLayout(2, 0));

        jLabel10.setText("Username : ");
        jPanel17.add(jLabel10);

        jPanel17.add(adminUserDetailsUsernameLabel);

        jPanel16.add(jPanel17);

        jLabel12.setText("Password : ");
        jPanel18.add(jLabel12);

        jPanel18.add(adminUserDetailsPasswordLabel);

        jPanel16.add(jPanel18);

        jPanel13.add(jPanel16, java.awt.BorderLayout.CENTER);

        jPanel12.add(jPanel13);

        jPanel14.setLayout(new java.awt.BorderLayout());

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Cashier User Details");
        jPanel19.add(jLabel14);

        cashierUserEditBtn.setText("Edit");
        jPanel19.add(cashierUserEditBtn);

        jPanel14.add(jPanel19, java.awt.BorderLayout.NORTH);

        jPanel20.setLayout(new java.awt.GridLayout(2, 0));

        jLabel15.setText("Username : ");
        jPanel21.add(jLabel15);

        jPanel21.add(cashierUserDetailsUsernameLabel);

        jPanel20.add(jPanel21);

        jLabel17.setText("Password : ");
        jPanel22.add(jLabel17);

        jPanel22.add(cashierUserDetailsPasswordLabel);

        jPanel20.add(jPanel22);

        jPanel14.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel12.add(jPanel14);

        usersCard.add(jPanel12, java.awt.BorderLayout.PAGE_END);

        cardContainer.add(usersCard, "usersCard");

        itemsCard.setLayout(new java.awt.BorderLayout());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Create new Items");
        itemsCard.add(jLabel11, java.awt.BorderLayout.PAGE_START);

        jPanel25.setPreferredSize(new java.awt.Dimension(180, 65));

        jLabel13.setText("Click \"New +\" to create new Item");
        jPanel25.add(jLabel13);

        itemNewItemBtn.setText("New +");
        jPanel25.add(itemNewItemBtn);

        jPanel24.add(jPanel25);

        itemsCard.add(jPanel24, java.awt.BorderLayout.CENTER);

        jPanel27.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10),
                new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true)));
        jPanel27.setPreferredSize(new java.awt.Dimension(474, 200));
        jPanel27.setLayout(new java.awt.BorderLayout());

        itemRemoveSelectedBtn.setText("Remove Selected");
        jPanel26.add(itemRemoveSelectedBtn);

        itemEditSelectedBtn.setText("Edit Selected");
        jPanel26.add(itemEditSelectedBtn);

        jPanel27.add(jPanel26, java.awt.BorderLayout.NORTH);

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        "ID", "Name", "Unit Price", "Stock"
                }) {
            Class<?>[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class
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
        itemTable.setPreferredSize(new java.awt.Dimension(300, 300));
        jScrollPane1.setViewportView(itemTable);

        jPanel27.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        itemsCard.add(jPanel27, java.awt.BorderLayout.PAGE_END);

        cardContainer.add(itemsCard, "itemsCard");

        getContentPane().add(cardContainer, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        prevBtn.setText("Previous");
        prevBtn.addActionListener(this::prevBtnActionPerformed);
        jPanel1.add(prevBtn);

        nextBtn.setText("Next");
        nextBtn.addActionListener(this::nextBtnActionPerformed);
        jPanel1.add(nextBtn);

        changeLogoBtn.addActionListener(this::changeLogo);
        createAdminUserBtn.addActionListener(this::createAdminUser);
        createCashierUserBtn.addActionListener(this::createCashierUser);
        itemNewItemBtn.addActionListener(this::createNewItem);
        itemEditSelectedBtn.addActionListener(this::editSelectedItem);
        itemRemoveSelectedBtn.addActionListener(this::removeSelectedItem);
        adminUserEditBtn.addActionListener(this::editAdminUser);
        cashierUserEditBtn.addActionListener(this::editCashierUser);
        languageSelectionComboBox.addActionListener(this::languageChange);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }

    private void prevBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentCardPos == 0)
            return;

        CardLayout cardLayout = (CardLayout) cardContainer.getLayout();
        cardLayout.show(cardContainer, cardNames[--currentCardPos]);

        if (nextBtn.getText().equals("Finish"))
            nextBtn.setText("Next");
    }

    private void nextBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentCardPos == cardNames.length - 1) {
            // Finalize
            finalizeSetup();
            return;
        }

        CardLayout cardLayout = (CardLayout) cardContainer.getLayout();
        cardLayout.show(cardContainer, cardNames[++currentCardPos]);

        if (currentCardPos == cardNames.length - 1) {
            nextBtn.setText("Finish");
        }
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

    private void createAdminUser(java.awt.event.ActionEvent evt) {
        new EditAddUserWindow(logger, (user, window) -> {
            window.setVisible(false);
            window.dispose();

            adminUserDto = user;

            var password = user.password().orElse("");

            adminUserDetailsUsernameLabel.setText(user.username());
            adminUserDetailsPasswordLabel
                    .setText(password.length() > 4 ? "XXXXX" + password.substring(password.length() - 4) : password);

            // adminUserDto = new CreateOrUpdateUserDto(user.ID(), user.username(), user.password(), user.type());
        }).setVisible(true);
    }

    private void createCashierUser(java.awt.event.ActionEvent evt) {
        new EditAddUserWindow(logger, (user, window) -> {
            window.setVisible(false);
            window.dispose();

            cashierUserDto = user;

            var password = user.password().orElse("");

            cashierUserDetailsUsernameLabel.setText(user.username());
            cashierUserDetailsPasswordLabel
                    .setText(password.length() > 4 ? "XXXXX" + password.substring(password.length() - 4) : password);

            // cashierUserDto = new CreateOrUpdateUserDto(user.ID(), user.username(), user.password(), user.type());
        }).setVisible(true);
    }

    private void editAdminUser(java.awt.event.ActionEvent evt) {
        var adminUser = new User(adminUserDto.ID(), adminUserDto.username(), UserType.ADMIN);

        new EditAddUserWindow(logger, adminUser, (user, window) -> {
            window.setVisible(false);
            window.dispose();

            adminUserDto = new CreateOrUpdateUserDto(
                    user.ID(),
                    user.username(),
                    Optional.of(user.password().orElse(adminUserDto.password().get())),
                    user.type());

            var password = user.password().orElse(adminUserDto.password().orElse(""));

            adminUserDetailsUsernameLabel.setText(user.username());
            adminUserDetailsPasswordLabel
                    .setText(password.length() > 4 ? "XXXXX" + password.substring(password.length() - 4) : password);
        }).setVisible(true);
    }

    private void editCashierUser(java.awt.event.ActionEvent evt) {
        var cashierUser = new User(cashierUserDto.ID(), cashierUserDto.username(), UserType.ADMIN);

        new EditAddUserWindow(logger, cashierUser, (user, window) -> {
            window.setVisible(false);
            window.dispose();

            cashierUserDto = new CreateOrUpdateUserDto(
                    user.ID(),
                    user.username(),
                    Optional.of(user.password().orElse(cashierUserDto.password().get())),
                    user.type());

            var password = user.password().orElse(cashierUserDto.password().orElse(""));

            cashierUserDetailsUsernameLabel.setText(user.username());
            cashierUserDetailsPasswordLabel
                    .setText(password.length() > 4 ? "XXXXX" + password.substring(password.length() - 4) : password);
        }).setVisible(true);
    }

    private void createNewItem(java.awt.event.ActionEvent evt) {
        new EditAddItemWindow(logger, (item, window) -> {
            window.setVisible(false);
            window.dispose();

            items.add(item);
            ((DefaultTableModel) itemTable.getModel())
                    .addRow(new Object[] { item.ID(), item.name(), item.price(), item.stock() }); // "ID", "Name", "Unit
                                                                                                  // Price", "Stock"
        }).setVisible(true);
    }

    private void editSelectedItem(java.awt.event.ActionEvent evt) {
        var selectedItemIndex = itemTable.getSelectedRow();

        if (selectedItemIndex == -1)
            return;

        new EditAddItemWindow(logger, items.get(selectedItemIndex), (item, window) -> {
            window.setVisible(false);
            window.dispose();

            items.set(selectedItemIndex, item);
            var tableModel = (DefaultTableModel) itemTable.getModel();

            tableModel.removeRow(selectedItemIndex);
            tableModel.insertRow(selectedItemIndex,
                    new Object[] { item.ID(), item.name(), item.price(), item.stock() });
        }).setVisible(true);
    }

    private void removeSelectedItem(java.awt.event.ActionEvent evt) {
        var selectedItemIndex = itemTable.getSelectedRow();

        if (selectedItemIndex == -1)
            return;

        items.remove(selectedItemIndex);
        ((DefaultTableModel) itemTable.getModel()).removeRow(selectedItemIndex);
    }

    private void languageChange(java.awt.event.ActionEvent evt) {
        var selectedLang = switch ((String) languageSelectionComboBox.getSelectedItem()) {
            case "English" -> Language.ENGLISH;
            case "German" -> Language.GERMAN;
            case "Sinhala" -> Language.SINHALA;
            default -> Language.ENGLISH;
        };

        localizationService.setLanguage(selectedLang);

        try {
            var langProperties = localizationService.getLanguageProperty();

            langExLabel1.setText(langProperties.getProperty("lang.example.label1"));
            langExLabel2.setText(langProperties.getProperty("lang.example.label2"));
            langExBtn1.setText(langProperties.getProperty("lang.example.btn1"));
            langExBtn2.setText(langProperties.getProperty("lang.example.btn2"));

        } catch (IOException e) {
            logger.severe("Failed to get language file for language " + selectedLang.lang);
        }
    }

    private void finalizeSetup() {
        try {
            onFinalize.accept(this, new SetupDto(
                    companyNameField.getText(),
                    companyLogoImagePath,
                    switch ((String) languageSelectionComboBox.getSelectedItem()) {
                        case "English" -> Language.ENGLISH;
                        case "German" -> Language.GERMAN;
                        case "Sinhala" -> Language.SINHALA;
                        default -> Language.ENGLISH;
                    },
                    adminUserDto.username(),
                    adminUserDto.password().get(),
                    cashierUserDto.username(),
                    cashierUserDto.password().get(),
                    items));
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "Some steps seem to be incomplete", "Cannot complete setup",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * public static void main(String args[]) {
     * try {
     * UIManager.setLookAndFeel(new FlatCyanLightIJTheme());
     * } catch (Exception ex) {
     * System.err.println("Failed to initialize LaF");
     * }
     * 
     * java.awt.EventQueue.invokeLater(new Runnable() {
     * public void run() {
     * new QuickSetupWindow().setVisible(true);
     * }
     * });
     * }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminUserDetailsPasswordLabel;
    private javax.swing.JLabel adminUserDetailsUsernameLabel;
    private javax.swing.JPanel cardContainer;
    private javax.swing.JLabel cashierUserDetailsPasswordLabel;
    private javax.swing.JLabel cashierUserDetailsUsernameLabel;
    private javax.swing.JButton changeLogoBtn;
    private javax.swing.JPanel companyDetailsCard;
    private javax.swing.JTextField companyNameField;
    private javax.swing.JButton createAdminUserBtn;
    private javax.swing.JButton createCashierUserBtn;
    private javax.swing.JButton itemEditSelectedBtn;
    private javax.swing.JButton itemNewItemBtn;
    private javax.swing.JButton itemRemoveSelectedBtn;
    private javax.swing.JTable itemTable;
    private javax.swing.JPanel itemsCard;
    private javax.swing.JButton adminUserEditBtn;
    private javax.swing.JButton cashierUserEditBtn;
    private javax.swing.JComboBox<String> languageSelectionComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton langExBtn1;
    private javax.swing.JButton langExBtn2;
    private javax.swing.JLabel langExLabel1;
    private javax.swing.JLabel langExLabel2;
    private javax.swing.JPanel languageCard;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JButton nextBtn;
    private javax.swing.JButton prevBtn;
    private javax.swing.JPanel usersCard;
    private javax.swing.JPanel welcomeCard;
    // End of variables declaration//GEN-END:variables
}
