/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views.components;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import quickshop.entity.Item;

import java.awt.Image;
import java.util.function.BiConsumer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author gehiru
 */
public class ItemCard extends JPanel {
    private SimpleIntegerProperty quantity = new SimpleIntegerProperty(1);
    private Item item;
    private BiConsumer<Item, Integer> onAddItem;

    private int currentStock;

    /**
     * Creates new form ItemCard
     */
    public ItemCard(Item item, BiConsumer<Item, Integer> onAddItem) {
        this.item = item;
        this.onAddItem = onAddItem;
        this.currentStock = item.stock();

        this.quantityUpdateListner = (ChangeListener<Number>) (observable, oldValue, newValue) -> updateText();
        quantity.addListener(this.quantityUpdateListner);

        initComponents();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        quantity.removeListener(quantityUpdateListner);
    }

    
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        imgLabel = new javax.swing.JLabel();
        itemNameLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        decAmountBtn = new javax.swing.JButton();
        addToCartBtn = new javax.swing.JButton();
        incAmountBtn = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(200, 200));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setPreferredSize(new java.awt.Dimension(200, 200));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        ImageIcon imageIcon = new ImageIcon(
                new ImageIcon(item.imagePath().toString()).getImage().getScaledInstance(150, 150,
                        Image.SCALE_DEFAULT));
        imgLabel.setIcon(imageIcon);

        jPanel3.add(imgLabel, java.awt.BorderLayout.CENTER);

        itemNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        itemNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        itemNameLabel.setText(item.name());
        itemNameLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        jPanel3.add(itemNameLabel, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(200, 30));
        jPanel2.setLayout(new java.awt.BorderLayout());

        decAmountBtn.setText("-");
        jPanel2.add(decAmountBtn, java.awt.BorderLayout.LINE_START);

        addToCartBtn.setText("Add " + quantity.get() + " to Cart");
        jPanel2.add(addToCartBtn, java.awt.BorderLayout.CENTER);

        incAmountBtn.setText("+");
        jPanel2.add(incAmountBtn, java.awt.BorderLayout.LINE_END);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        decAmountBtn.addActionListener(e -> {
            if (quantity.get() == 1) {
                return;
            }

            quantity.set(quantity.get() - 1);
        });
        incAmountBtn.addActionListener(e -> {
            if (quantity.get() == item.stock()) {
                return;
            }

            quantity.set(quantity.get() + 1);
        });
        addToCartBtn.addActionListener(e -> {
            if ((currentStock - quantity.get()) < 1) {
                return;
            }
            currentStock = currentStock - quantity.get();
            onAddItem.accept(item, quantity.get());
        });

        add(jPanel1);
    }

    private void updateText() {
        addToCartBtn.setText("Add " + quantity.get() + " to Cart");
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addToCartBtn;
    private javax.swing.JButton decAmountBtn;
    private javax.swing.JLabel imgLabel;
    private javax.swing.JButton incAmountBtn;
    private javax.swing.JLabel itemNameLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
    private ChangeListener<Number> quantityUpdateListner;
}
