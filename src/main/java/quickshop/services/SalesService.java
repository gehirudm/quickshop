package quickshop.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import quickshop.dto.CreateSaleDto;
import quickshop.dto.CreateSalesItemDto;
import quickshop.entity.Sale;
import quickshop.entity.SalesItem;
import quickshop.util.Util;

public class SalesService {
    private java.sql.Connection connection;
    private ItemsService itemsService;
    private Logger logger;

    public SalesService(java.sql.Connection connection, Logger logger, ItemsService itemsService) {
        this.connection = connection;
        this.logger = logger;
        this.itemsService = itemsService;
    }

    public List<Sale> getSalesByDateRange(Date startDate, Date endDate) throws SQLException {
        List<Sale> sales = new ArrayList<>();

        String sql = "SELECT * FROM SALES WHERE CREATED_AT BETWEEN ? AND ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setObject(1, new Timestamp(startDate.getTime()));
        statement.setObject(2, new Timestamp(endDate.getTime()));

        ResultSet salesResultSet = statement.executeQuery();

        while (salesResultSet.next()) {
            String saleID = salesResultSet.getString("ID");
            Date date = Date.from(salesResultSet.getObject("CREATED_AT", Timestamp.class).toInstant());
            float total = salesResultSet.getFloat("TOTAL");

            // Retrieve sales items data from the "sales_items" table
            String subSql = "SELECT a.QUANTITY AS QUANTITY, b.ID AS ID, b.NAME AS NAME, b.UNIT_PRICE AS UNIT_PRICE " +
                    "FROM SALES_ITEMS a " +
                    "JOIN ITEMS b ON a.ITEM_ID = b.ID " +
                    "WHERE SALES_ID = ?";

            PreparedStatement subStatement = connection.prepareStatement(subSql);

            subStatement.setString(1, saleID);

            ResultSet itemsResultSet = subStatement.executeQuery();
            List<SalesItem> items = new ArrayList<>();

            while (itemsResultSet.next()) {
                String itemID = itemsResultSet.getString("ID");
                String name = itemsResultSet.getString("NAME");
                float unitPrice = itemsResultSet.getFloat("UNIT_PRICE");
                int quantity = itemsResultSet.getInt("QUANTITY");

                items.add(new SalesItem(itemID, name, unitPrice, quantity));
            }

            sales.add(new Sale(saleID, date, total, items));
        }

        return sales;
    }

    public String createSale(CreateSaleDto data) throws SQLException {
        String saleID = UUID.randomUUID().toString();
        float totalAmount = Util.calculateSaleTotal(data);

        // Insert the sale into the "sales" table
        String saleInsertSQL = "INSERT INTO SALES (ID, CREATED_AT, TOTAL) VALUES (?, ?, ?)";
        PreparedStatement saleStatement = connection.prepareStatement(saleInsertSQL);

        saleStatement.setString(1, saleID);
        saleStatement.setObject(2, new Timestamp(System.currentTimeMillis()));
        saleStatement.setFloat(3, totalAmount);

        saleStatement.executeUpdate();

        // Insert sales items into the "sales_items" table
        String salesItemInsertSQL = "INSERT INTO SALES_ITEMS (ID, SALES_ID, ITEM_ID, QUANTITY) VALUES (?, ?, ?, ?)";
        PreparedStatement salesItemStatement = connection.prepareStatement(salesItemInsertSQL);

        for (CreateSalesItemDto item : data.items()) {
            salesItemStatement.setString(1, UUID.randomUUID().toString());
            salesItemStatement.setString(2, saleID);
            salesItemStatement.setString(3, item.ID());
            salesItemStatement.setInt(4, item.quantity());

            salesItemStatement.executeUpdate();

            // Update item stock
            itemsService.updateItemStockByDiff(item.ID(), -item.quantity());
        }

        logger.info("Created 'Sale' with ID " + saleID);

        return saleID;
    }

    public int getTodaySalesCount() throws SQLException {
        String midnight = getMidnightTime();

        String countQuery = "SELECT COUNT(*) AS sales_count FROM SALES WHERE CREATED_AT > ?";
        PreparedStatement statement = connection.prepareStatement(countQuery);
        statement.setString(1, midnight);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("sales_count");
        }

        return 0;
    }

    public float getTodaySalesTotal() throws SQLException {
        String midnight = getMidnightTime();
        String totalQuery = "SELECT SUM(TOTAL) AS total_sales FROM SALES WHERE CREATED_AT > ?";
        PreparedStatement statement = connection.prepareStatement(totalQuery);
        statement.setString(1, midnight);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getFloat("total_sales");
        }

        return 0.0f;
    }

    private static String getMidnightTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);
        return today + " 00:00:00";
    }
}
