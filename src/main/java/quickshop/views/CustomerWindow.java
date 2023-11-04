/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import quickshop.dto.CartItemDto;
import quickshop.entity.Item;
import quickshop.services.LocalizationService;
import quickshop.util.Util;
import quickshop.views.components.CartItemCard;
import quickshop.views.components.ItemCard;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.util.List;

/**
 *
 * @author gehiru
 */
public class CustomerWindow extends javax.swing.JFrame {

    private List<Item> items;
    private SimpleListProperty<CartItemDto> cartItems;
    private Runnable onCheckout;
    private LocalizationService localizationService;

    /**
     * Creates new form CustomerWindow
     */
    public CustomerWindow(LocalizationService localizationService, List<Item> items, SimpleListProperty<CartItemDto> cartItems, Runnable onCheckout) {
        this.localizationService = localizationService;
        this.items = items;
        this.cartItems = cartItems;
        this.onCheckout = onCheckout;
        initComponents();

        cartItemChangeListner = (ChangeListener<ObservableList<CartItemDto>>) (observable, oldValue,
                newValue) -> {
            redrawCartItems();
            updateTotal();
        };
        cartItems.addListener(cartItemChangeListner);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cartItems.removeListener(cartItemChangeListner);
            }
        });
    }

    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        itemContainer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cartItemContainer = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        orderTotalLabel = new javax.swing.JLabel();
        checkoutBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quick Shop");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, localizationService.getValue("customer.window.all.items", "All Items"),
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Segoe UI", 0, 18))); // NOI18N
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        itemContainer.setPreferredSize(new java.awt.Dimension(640, 2740));
        itemContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 10, 10));

        // Add Items to the container
        for (Item item : items) {
            var itemCard = new ItemCard(item, this::addItemToCart);
            itemContainer.add(itemCard);
        }

        jScrollPane2.setViewportView(itemContainer);

        jPanel2.add(jScrollPane2);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setBorder(
                javax.swing.BorderFactory.createTitledBorder(null, localizationService.getValue("customer.window.your.cart", "Your Cart"), javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        cartItemContainer.setPreferredSize(new java.awt.Dimension(270, 1600));
        jScrollPane1.setViewportView(cartItemContainer);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.LINE_END);

        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true),
                javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        jPanel3.setLayout(new java.awt.GridLayout(2, 0));

        orderTotalLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        jPanel3.add(orderTotalLabel);

        checkoutBtn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        checkoutBtn.setForeground(new java.awt.Color(255, 0, 0));
        checkoutBtn.setText(localizationService.getValue("customer.window.checkout", "CHECKOUT"));
        jPanel3.add(checkoutBtn);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        // Add Event Listners
        checkoutBtn.addActionListener(e -> onCheckout.run());

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_END);

        pack();
    }

    public void reset(List<Item> items) {
        Component[] components = itemContainer.getComponents();
        for (Component component : components) {
            itemContainer.remove(component);
        }

        for (Item item : items) {
            var itemCard = new ItemCard(item, this::addItemToCart);
            itemContainer.add(itemCard);
        }

        revalidate();
        repaint();
    }

    private void redrawCartItems() {
        Component[] components = cartItemContainer.getComponents();
        for (Component component : components) {
            cartItemContainer.remove(component);
        }

        for (int i = 0; i < cartItems.getSize(); i++) {
            var cartItemCard = new CartItemCard(cartItems.get(i), i, this::removeItemFromCart);
            cartItemContainer.add(cartItemCard);
        }

        revalidate();
        repaint();
    }

    private void updateTotal() {
        var cartTotal = cartItems.get().stream().mapToDouble(item -> item.item().price() * item.quantity()).sum();
        orderTotalLabel.setText(Util.formatToRupees((float) cartTotal));
    }

    private void addItemToCart(Item item, Integer quantity) {
        cartItems.add(new CartItemDto(item, quantity));
    }

    private void removeItemFromCart(CartItemCard component, CartItemDto cartItemDto, int index) {
        cartItems.remove(index);
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
     * new CustomerWindow().setVisible(true);
     * }
     * });
     * }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cartItemContainer;
    private javax.swing.JButton checkoutBtn;
    private javax.swing.JPanel itemContainer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel orderTotalLabel;
    // End of variables declaration//GEN-END:variables
    private ChangeListener<ObservableList<CartItemDto>> cartItemChangeListner;
}
