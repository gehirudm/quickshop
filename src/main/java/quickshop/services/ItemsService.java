package quickshop.services;

import java.io.File;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import quickshop.App;
import quickshop.entity.Item;

public class ItemsService {
    private java.sql.Connection connection;
    private Logger logger;

    public ItemsService(java.sql.Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    public Item createItem(String ID, String itemName, float price, int stock) throws SQLException {
        String sql = "INSERT INTO ITEMS (ID, NAME, UNIT_PRICE, STOCK) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, ID);
        statement.setString(2, itemName);
        statement.setDouble(3, price); // Set the initial unit price
        statement.setInt(4, stock); // Set the initial stock
        statement.executeUpdate();

        logger.info("Created new 'Item' with ID of " + ID);

        return new Item(
                ID,
                itemName,
                price,
                stock,
                getItemImagePath(ID));
    }

    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();

        String sql = "SELECT * FROM ITEMS";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            String itemID = resultSet.getString("ID");
            String name = resultSet.getString("NAME");
            float unitPrice = resultSet.getFloat("UNIT_PRICE");
            int currentStock = resultSet.getInt("STOCK");

            items.add(new Item(itemID, name, unitPrice, currentStock, getItemImagePath(itemID)));
        }

        return items;
    }

    public List<Item> getItemsInStock() throws SQLException {
        List<Item> items = new ArrayList<>();
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM ITEMS WHERE STOCK > 0";
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            String itemID = resultSet.getString("ID");
            String name = resultSet.getString("NAME");
            float unitPrice = resultSet.getFloat("UNIT_PRICE");
            int currentStock = resultSet.getInt("STOCK");

            items.add(new Item(itemID, name, unitPrice, currentStock, getItemImagePath(itemID)));
        }

        return items;
    }

    /**
     * @param id   Item's ID
     * @param diff A Negative or Postive value. +2 will increase the stock by 2, -2
     *             will reduce the stock by 2
     * @throws SQLException
     */
    public void updateItemStockByDiff(String id, int diff) throws SQLException {
        String stockQuery = "SELECT STOCK FROM ITEMS WHERE ID = ?";

        PreparedStatement stockStatement = connection.prepareStatement(stockQuery);
        stockStatement.setString(1, id);

        ResultSet stockResult = stockStatement.executeQuery();

        if (stockResult.next()) {
            int currentStock = stockResult.getInt("STOCK");

            int updatedStock = currentStock + diff;

            // Update the stock value in the "items" table
            String updateQuery = "UPDATE ITEMS SET STOCK = ? WHERE ID = ?";

            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

            updateStatement.setInt(1, updatedStock);
            updateStatement.setString(2, id);

            updateStatement.executeUpdate();
        }

        logger.info("Updated 'Item' with ID " + id + "'s stock from " + stockResult + "to" + stockResult + diff);
    }

    public void updateItemStock(String id, int newStock) throws SQLException {
        String stockQuery = "SELECT STOCK FROM ITEMS WHERE ID = ?";

        PreparedStatement stockStatement = connection.prepareStatement(stockQuery);
        stockStatement.setString(1, id);

        ResultSet stockResult = stockStatement.executeQuery();

        if (stockResult.next()) {
            // int currentStock = stockResult.getInt("STOCK");

            // Update the stock value in the "items" table
            String updateQuery = "UPDATE ITEMS SET STOCK = ? WHERE ID = ?";

            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

            updateStatement.setInt(1, newStock);
            updateStatement.setString(2, id);

            updateStatement.executeUpdate();
        }

        logger.info("Updated 'Item' with ID " + id + "'s stock from " + stockResult + "to" + newStock);
    }

    public Item updateItem(Item updatedItem) throws SQLException {
        String updateQuery = "UPDATE ITEMS SET NAME = ?, UNIT_PRICE = ?, STOCK = ? WHERE ID = ?";

        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setString(1, updatedItem.name());
        updateStatement.setFloat(2, updatedItem.price());
        updateStatement.setInt(3, updatedItem.stock());
        updateStatement.setString(4, updatedItem.ID());

        updateStatement.executeUpdate();

        return updatedItem;
    }

    public void deleteItem(String id) throws SQLException {
        String deleteQuery = "DELETE FROM ITEMS WHERE ID = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);

        deleteStatement.setString(1, id);
        deleteStatement.executeUpdate();

        logger.info("Deleted 'Item' with ID " + id);
    }

    public int getItemCount() throws SQLException {
        String countQuery = "SELECT COUNT(*) AS item_count FROM ITEMS";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(countQuery);

        if (resultSet.next()) {
            return resultSet.getInt("item_count");
        }

        return 0;
    }

    private Path getItemImagePath(String itemID) {
        // Check if an image exists for this item
        for (String imgExtension : new String[] { ".jpg", ".png", ".jpeg" }) {
            var path = "./images/" + itemID + imgExtension;
            var imgFile = new File(path);
            if (imgFile.exists()) {
                return imgFile.toPath();
            }
        }

        return getItemDefaultImagePath();
    }

    public static Path getItemDefaultImagePath() {
        return new File(App.class.getClassLoader().getResource("images/default_item.png").getPath()).toPath();
    }
}
