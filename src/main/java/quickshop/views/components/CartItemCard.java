/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views.components;

import quickshop.dto.CartItemDto;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.Image;

/**
 *
 * @author gehiru
 */
public class CartItemCard extends JPanel {

    private CartItemDto cartItem;
    private int index;
    private OnRemoveClickFunction onRemoveClick;

    @FunctionalInterface
    public interface OnRemoveClickFunction {
        void accept(CartItemCard component, CartItemDto cartItemDto, int index);
    }

    /**
     * Creates new form CartItemCard
     */
    public CartItemCard(CartItemDto cartItem, int index, OnRemoveClickFunction onRemoveClick) {
        this.cartItem = cartItem;
        this.index = index;
        this.onRemoveClick = onRemoveClick;
        initComponents();
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        itemImgLabel = new javax.swing.JLabel();
        itemNo = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        itemDescriptionLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        removeItemBtn = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setPreferredSize(new java.awt.Dimension(250, 60));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new javax.swing.OverlayLayout(jPanel3));

        ImageIcon imageIcon = new ImageIcon(
                new ImageIcon(cartItem.item().imagePath().toString()).getImage().getScaledInstance(60, 60,
                        Image.SCALE_DEFAULT));
        itemImgLabel.setIcon(imageIcon);

        itemImgLabel.setAlignmentX(0.5F);
        itemImgLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        itemImgLabel.setMaximumSize(new java.awt.Dimension(60, 60));
        itemImgLabel.setMinimumSize(new java.awt.Dimension(60, 60));
        itemImgLabel.setPreferredSize(new java.awt.Dimension(60, 60));
        jPanel3.add(itemImgLabel);

        itemNo.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        itemNo.setText("#" + index + 1);
        itemNo.setAlignmentX(0.5F);
        jPanel3.add(itemNo);

        jPanel1.add(jPanel3, java.awt.BorderLayout.LINE_START);

        jPanel4.setLayout(new java.awt.BorderLayout());

        itemDescriptionLabel.setText(cartItem.quantity() + " x " + cartItem.item().name());
        itemDescriptionLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        jPanel4.add(itemDescriptionLabel, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.setLayout(new java.awt.BorderLayout());

        removeItemBtn.setText("X");
        removeItemBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel2.add(removeItemBtn, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel2, java.awt.BorderLayout.LINE_END);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        removeItemBtn.addActionListener(e -> onRemoveClick.accept(this, cartItem, index));

        add(jPanel1);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel itemDescriptionLabel;
    private javax.swing.JLabel itemImgLabel;
    private javax.swing.JLabel itemNo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton removeItemBtn;
    // End of variables declaration//GEN-END:variables
}
