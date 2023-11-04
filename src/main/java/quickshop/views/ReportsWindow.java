/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quickshop.views;

import quickshop.dto.ReportMetadata;
import quickshop.entity.Sale;
import quickshop.services.EnvironmentService;
import quickshop.services.SalesService;
import quickshop.util.PrintUtils;
import quickshop.util.ReportGenerator;
import quickshop.util.Util;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gehiru
 */
public class ReportsWindow extends javax.swing.JFrame {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<?> fetchTask;

    private Logger logger;
    private EnvironmentService environmentService;
    private SalesService salesService;
    private PropertyChangeListener datePickerPropertyChangeListner;

    private List<Sale> currentData;

    /**
     * Creates new form ReportsWindow
     */
    public ReportsWindow(Logger logger, EnvironmentService environmentService, SalesService salesService) {
        this.logger = logger;
        this.environmentService = environmentService;
        this.salesService = salesService;
        initComponents();

        fetchData();

        datePickerPropertyChangeListner = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                System.out.println("Date changed");
                if ("date".equals(e.getPropertyName())) {
                    fetchData();
                }
            }
        };

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                startDatePicker.getDateEditor().removePropertyChangeListener(datePickerPropertyChangeListner);
                endDatePicker.getDateEditor().removePropertyChangeListener(datePickerPropertyChangeListner);
            }
        });
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        startDatePicker = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        endDatePicker = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        resetBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        viewSelectedOrderBtn = new javax.swing.JButton();
        printReportBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 15, 15, 0));
        jPanel1.setLayout(new java.awt.GridLayout(0, 3, 10, 0));

        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Start Date");
        jPanel3.add(jLabel1, java.awt.BorderLayout.PAGE_START);
        jPanel3.add(startDatePicker, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("End Date");
        jPanel4.add(jLabel2, java.awt.BorderLayout.PAGE_START);
        jPanel4.add(endDatePicker, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 10));

        resetBtn.setText("Reset");
        jPanel5.add(resetBtn);

        jPanel1.add(jPanel5);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        salesTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "ID", "No. of Items", "Total", "Date"
                }) {
            Class<?>[] types = new Class[] {
                    java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
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
        salesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(salesTable);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        viewSelectedOrderBtn.setText("View Selected Order");
        jPanel2.add(viewSelectedOrderBtn);

        printReportBtn.setText("Print Report");
        jPanel2.add(printReportBtn);

        viewSelectedOrderBtn.addActionListener(this::viewSelectedOrder);
        printReportBtn.addActionListener(this::printReport);
        resetBtn.addActionListener(this::resetDates);

        startDatePicker.setDate(getMidnightTodayDate());
        endDatePicker.setDate(getTomorrowMidnightDate());

        startDatePicker.getDateEditor().addPropertyChangeListener(datePickerPropertyChangeListner);
        endDatePicker.getDateEditor().addPropertyChangeListener(datePickerPropertyChangeListner);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        pack();
    }

    private void viewSelectedOrder(ActionEvent evt) {
        var selectedIndex = salesTable.getSelectedRow();
        if (selectedIndex == -1)
            return;

        new OrderViewWindow(logger, environmentService, currentData.get(selectedIndex)).setVisible(true);
    }

    private void printReport(ActionEvent evt) {
        if (salesTable.getRowCount() == 0 || currentData.size() == 0)
            return;

        var generator = new ReportGenerator("./reports",
                new ReportMetadata(currentData));

        try {
            var outPath = generator
                    .setHeaderImage(environmentService.getCompanyLogoPath().toString())
                    .generate();

            PrintUtils.printPdf(outPath);
        } catch (IOException e) {
            logger.severe("Failed to print Report (IOException)");
            JOptionPane.showMessageDialog(this, "Failed to print report. (IOException)", "Error Occured",
                    ERROR);
        } catch (PrinterException e) {
            logger.severe("Failed to print Report (PrinterException)");
            JOptionPane.showMessageDialog(this, "Failed to print report. (PrinterException)",
                    "Error Occured", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetDates(ActionEvent evt) {
        startDatePicker.setCalendar(null);
        endDatePicker.setCalendar(null);
    }

    private void fetchData() {
        var tableModel = (DefaultTableModel) salesTable.getModel();
        var startDate = startDatePicker.getDate() == null ? getMidnightTodayDate() : startDatePicker.getDate();
        var endDate = endDatePicker.getDate() == null ? getTomorrowMidnightDate() : endDatePicker.getDate();

        if (fetchTask != null)
            fetchTask.cancel(true);

        fetchTask = executorService.submit(() -> {
            // Fetch data
            List<Sale> sales;
            try {
                sales = salesService.getSalesByDateRange(startDate, endDate);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.severe("Failed to retrieve sales");
                JOptionPane.showMessageDialog(this, "Failed to retrieve sales from the database", "Error Occured",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentData = sales;

            // Remove all row
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                tableModel.removeRow(i);
            }

            // Add row to table
            for (Sale saleItem : sales) {
                tableModel.addRow(new Object[] { // "ID", "No. of Items", "Total", "Date"
                        saleItem.ID(),
                        saleItem.items().size(),
                        Util.formatToRupees(saleItem.total()),
                        Util.formatDate(saleItem.date())
                });
            }
        });
    }

    private Date getMidnightTodayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getTomorrowMidnightDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
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
     * new ReportsWindow().setVisible(true);
     * }
     * });
     * }
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser endDatePicker;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable salesTable;
    private javax.swing.JButton printReportBtn;
    private javax.swing.JButton resetBtn;
    private com.toedter.calendar.JDateChooser startDatePicker;
    private javax.swing.JButton viewSelectedOrderBtn;
    // End of variables declaration//GEN-END:variables
}
