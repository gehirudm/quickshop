/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import quickshop.dto.CartItemDto;
import quickshop.dto.CreateSaleDto;
import quickshop.dto.CreateSalesItemDto;
import quickshop.dto.RecieptItem;
import quickshop.dto.ReceiptMetadata;
import quickshop.entity.StashedOrder;
import quickshop.entity.User;
import quickshop.services.EnvironmentService;
import quickshop.services.ItemsService;
import quickshop.services.LocalizationService;
import quickshop.services.SalesService;
import quickshop.util.PrintUtils;
import quickshop.util.ReceiptGenerator;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gehiru
 */
public class CashierWindow extends javax.swing.JFrame {
    private Timer updateDateTimer;
    private ArrayList<StashedOrder> stashedOrders = new ArrayList<>();
    private SimpleListProperty<CartItemDto> cartItems;
    private ChangeListener<ObservableList<CartItemDto>> cartItemChangeListner;

    private CustomerWindow customerWindow;
    private ItemsService itemsService;
    private SalesService salesService;
    private User currentUser;
    private Logger logger;
    private EnvironmentService environmentService;
    private LocalizationService localizationService;
    private Runnable onExit;

    /**
     * Creates new form CashierWindow
     */
    public CashierWindow(LocalizationService localizationService, Logger logger, User currentUser,
            ItemsService itemsService, SalesService salesService,
            EnvironmentService environmentService,
            Runnable onExit) {
        this.localizationService = localizationService;
        this.logger = logger;
        this.currentUser = currentUser;
        this.itemsService = itemsService;
        this.salesService = salesService;
        this.environmentService = environmentService;
        this.onExit = onExit;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                updateDateTimer.cancel();
                cartItems.removeListener(cartItemChangeListner);
                onExit.run();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                beginTimer();
                super.windowOpened(e);
            }
        });

        ObservableList<CartItemDto> itemList = FXCollections.observableArrayList();

        cartItems = new SimpleListProperty<>(itemList);

        cartItemChangeListner = (ChangeListener<ObservableList<CartItemDto>>) (observable, oldValue,
                newValue) -> {
            updateOrderItemsTable(oldValue, newValue);
            updateOrderTotal(newValue);
        };

        cartItems.addListener(cartItemChangeListner);

        initComponents();
    }

    public void beginTimer() {
        updateDateTimer = new Timer();
        updateDateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentTimeLabel.setText(getCurrentTimeFormatted());
            }
        }, 0, 1000);
    }

    public static String getCurrentTimeFormatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        exitBtn = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        currentTimeLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        openCustomerWindowBtn = new javax.swing.JButton();
        cancelCurrentOrderBtn = new javax.swing.JButton();
        stashCurrentOrderBtn = new javax.swing.JButton();
        loadOrderBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderItemsTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        orderTotalLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(localizationService.getValue("cashier.window.title", "Quick Shop Customer"));
        setSize(500, 400);
        // Get the screen dimensions
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calculate the x and y coordinates for centering
        int x = (screenSize.width - 500) / 2;
        int y = (screenSize.height - 400) / 2;

        setLocation(x, y);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jPanel4.setLayout(new java.awt.BorderLayout());

        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit.png"))); // NOI18N
        exitBtn.setFocusPainted(false);
        exitBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exitBtn.setMaximumSize(new java.awt.Dimension(50, 50));
        exitBtn.setMinimumSize(new java.awt.Dimension(50, 50));
        exitBtn.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel4.add(exitBtn, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 15));
        jPanel6.setLayout(new java.awt.GridLayout(0, 2, 15, 0));

        currentTimeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        currentTimeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clock.png"))); // NOI18N
        jPanel6.add(currentTimeLabel);

        usernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        usernameLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user.png"))); // NOI18N
        usernameLabel.setText(currentUser.username());
        jPanel6.add(usernameLabel);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 5, 20, 5));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel7.setMaximumSize(new java.awt.Dimension(160, 137));
        jPanel7.setPreferredSize(new java.awt.Dimension(220, 200));
        jPanel7.setLayout(new java.awt.GridLayout(4, 0, 0, 15));

        openCustomerWindowBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        openCustomerWindowBtn
                .setText(localizationService.getValue("cashier.window.open.customer.window", "Open Customer Window"));
        jPanel7.add(openCustomerWindowBtn);

        cancelCurrentOrderBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cancelCurrentOrderBtn
                .setText(localizationService.getValue("cashier.window.cancel.current.order", "Cancel Current Order"));
        jPanel7.add(cancelCurrentOrderBtn);

        stashCurrentOrderBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        stashCurrentOrderBtn
                .setText(localizationService.getValue("cashier.window.stash.current.order", "Stash Current Order"));
        jPanel7.add(stashCurrentOrderBtn);

        loadOrderBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        loadOrderBtn.setText(localizationService.getValue("cashier.window.load.order", "Load Order"));
        jPanel7.add(loadOrderBtn);

        jPanel2.add(jPanel7, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        orderItemsTable.setModel(createTableModel(new Object[][] {}));
        jScrollPane1.setViewportView(orderItemsTable);

        jPanel3.add(jScrollPane1);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 5));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText(localizationService.getValue("cashier.window.current.order.total", "Current Order Total"));
        jPanel5.add(jLabel1);

        orderTotalLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        orderTotalLabel.setText("Rs. 0.00");
        jPanel5.add(orderTotalLabel);

        openCustomerWindowBtn.addActionListener(this::openCustomerWindow);
        cancelCurrentOrderBtn.addActionListener(this::cancelCurrentOrder);
        stashCurrentOrderBtn.addActionListener(this::stashCurrentOrder);
        loadOrderBtn.addActionListener(this::openStashedOrdersWindow);
        exitBtn.addActionListener(e -> {
            if (customerWindow != null) {
                customerWindow.setVisible(false);
                customerWindow.dispose();
            }

            updateDateTimer.cancel();
            cartItems.removeListener(cartItemChangeListner);
            onExit.run();

            setVisible(false);
            dispose();
        });

        getContentPane().add(jPanel5, java.awt.BorderLayout.PAGE_END);

        pack();
    }

    private void updateOrderItemsTable(ObservableList<CartItemDto> oldList, ObservableList<CartItemDto> newList) {
        var tableRows = IntStream
                .range(0, newList.size())
                .mapToObj(i -> new Object[] {
                        i + 1,
                        newList.get(i).item().name(),
                        newList.get(i).item().price(),
                        newList.get(i).quantity(),
                        newList.get(i).quantity() * newList.get(i).item().price()
                })
                .toArray(Object[][]::new);

        orderItemsTable.setModel(createTableModel(tableRows));

        // // Check if a new Item was added

        // for (int i = 0; i < oldList.size(); i++) {
        // var item = oldList.get(i);

        // if (!newList.contains(item)) {
        // // This Item was removed
        // tableModel.removeRow(i);
        // }
        // }

        // newList.removeAll(oldList);

        // if (newList.size() != 0) {
        // // This item was added
        // var item = newList.get(0);
        // tableModel.addRow(new Object[] { // "No", "Name", "Unit Price", "Quantity",
        // "Total"
        // oldList.size() + 1,
        // item.item().name(),
        // item.item().price(),
        // item.quantity(),
        // item.quantity() * item.item().price()
        // });
        // }
    }

    private void updateOrderTotal(ObservableList<CartItemDto> newList) {
        orderTotalLabel.setText(quickshop.util.Util
                .formatToRupees((float) newList.stream().mapToDouble(i -> i.item().price() * i.quantity()).sum()));
    }

    private void openCustomerWindow(ActionEvent evt) {
        if (customerWindow != null)
            return;

        try {
            customerWindow = new CustomerWindow(localizationService, itemsService.getItemsInStock(), cartItems,
                    () -> openCheckoutWindow());
            customerWindow.setVisible(true);
        } catch (SQLException e) {
            logger.severe("Failed to fetch Items from the database");
            JOptionPane.showMessageDialog(this,
                    localizationService.getValue("customer.window.open.customer.window.error",
                            "Error Occured while trying to load Items from the database"),
                    localizationService.getValue("errors.titles.fatal", "Fatal Error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelCurrentOrder(ActionEvent e) {
        if (customerWindow == null)
            return;

        cartItems.clear();
        try {
            customerWindow.reset(itemsService.getItemsInStock());
        } catch (SQLException ex) {
            logger.severe("Failed to fetch Items from the database");
            JOptionPane.showMessageDialog(this,
                    localizationService.getValue("customer.window.open.customer.window.error",
                            "Error Occured while trying to load Items from the database"),
                    localizationService.getValue("errors.titles.fatal", "Fatal Error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stashCurrentOrder(ActionEvent e) {
        if (cartItems.size() == 0)
            return;

        System.out.println(cartItems.get());

        stashedOrders.add(new StashedOrder(new ArrayList<>(cartItems.get()), new Date(System.currentTimeMillis())));
        cancelCurrentOrder(e);
    }

    private void openStashedOrdersWindow(ActionEvent e) {
        new OrderStashWindow(stashedOrders, (order, index) -> {
            cartItems.set(FXCollections.observableArrayList(order.items()));
            stashedOrders.remove(index.intValue());
        }).setVisible(true);
    }

    private void openCheckoutWindow() {
        new CheckoutWindow(localizationService, (float) getCartTotal(), (window, printRecipet, cash) -> {
            var saleID = "";

            try {
                saleID = salesService.createSale(new CreateSaleDto(
                        new Date(System.currentTimeMillis()),
                        (float) getCartTotal(),
                        cartItems.get().stream().map(x -> new CreateSalesItemDto(
                                x.item().ID(),
                                x.item().price(),
                                x.quantity())).toList()));
            } catch (SQLException e) {
                e.printStackTrace();
                logger.severe("Failed to create new sale");
                JOptionPane.showMessageDialog(window,
                        localizationService.getValue("cashier.window.create.sale.error", "Failed to create new sale."),
                        localizationService.getValue("errors.titles.occured", "Error Occured"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (printRecipet) {
                List<RecieptItem> recieptItems = cartItems.get().stream().map(item -> new RecieptItem(item.item().ID(),
                        item.item().name(), item.item().price(), item.quantity())).toList();

                var generator = new ReceiptGenerator("./receipts",
                        new ReceiptMetadata(currentUser.username(), saleID, recieptItems, cash));

                try {
                    var outPath = generator
                            .setHeaderImage(environmentService.getCompanyLogoPath().toString())
                            .generate();

                    PrintUtils.printPdf(outPath);
                } catch (IOException e) {
                    logger.severe("Failed to print receipt for sale " + saleID + "(IOException)");
                    JOptionPane.showMessageDialog(window,
                            localizationService.getValue("cashier.window.failed.print.receipt",
                                    "Failed to print receipt.") + " (IOException)",
                            "Error Occured",
                            ERROR);
                } catch (PrinterException e) {
                    logger.severe("Failed to print receipt for sale " + saleID + "(PrinterException)");
                    JOptionPane.showMessageDialog(window,
                            localizationService.getValue("cashier.window.failed.print.receipt",
                                    "Failed to print receipt.") + " (PrinterException)",
                            localizationService.getValue("errors.titles.occured", "Error Occured"),
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            window.dispose();
            cancelCurrentOrder(null); // Clearing out the current state
        }, window -> { // OnCancel Function
            window.dispose();
            cancelCurrentOrder(null);
        }).setVisible(true);
    }

    private double getCartTotal() {
        return cartItems.get().stream().mapToDouble(i -> i.quantity() * i.item().price()).sum();
    }

    private DefaultTableModel createTableModel(Object[][] rows) {
        return new DefaultTableModel(
                rows,
                new String[] {
                        localizationService.getValue("cashier.window.table.no", "No"),
                        localizationService.getValue("cashier.window.table.name", "Name"),
                        localizationService.getValue("cashier.window.table.unit.price", "Unit Price"),
                        localizationService.getValue("cashier.window.table.quantity", "Quantity"),
                        localizationService.getValue("cashier.window.table.total", "Total")
                }) {
            Class<?>[] types = new Class[] {
                    java.lang.Integer.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class,
                    java.lang.Float.class
            };
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false
            };

            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
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
     * new CashierWindow().setVisible(true);
     * }
     * });
     * }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelCurrentOrderBtn;
    private javax.swing.JLabel currentTimeLabel;
    private javax.swing.JButton exitBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable orderItemsTable;
    private javax.swing.JButton loadOrderBtn;
    private javax.swing.JButton openCustomerWindowBtn;
    private javax.swing.JLabel orderTotalLabel;
    private javax.swing.JButton stashCurrentOrderBtn;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
