package quickshop.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import quickshop.App;
import quickshop.dto.SetupDto;
import quickshop.entity.Item;
import quickshop.services.LocalizationService.Language;
import quickshop.services.UsersService.UserType;
import quickshop.util.Util;

public class EnvironmentService {

    private Connection connection;
    private UsersService usersService;
    private ItemsService itemsService;
    private Logger logger;

    public EnvironmentService(Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    public void setDependancies(UsersService usersService, ItemsService itemsService) {
        this.usersService = usersService;
        this.itemsService = itemsService;
    }

    public boolean tablesExists() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        String[] tableNames = { "USERS", "SALES", "SALES_ITEMS", "ITEMS", "SYS_DATA" };

        var allTablesExists = true;

        for (String tableName : tableNames) {
            boolean tableExists = false;
            java.sql.ResultSet tables = metaData.getTables(null, null, tableName, null);
            while (tables.next()) {
                tableExists = true;
                break;
            }
            tables.close();

            if (!tableExists) {
                allTablesExists = false;
                break;
            }
        }

        return allTablesExists;
    }

    public void createTables() throws SQLException {
        Statement statement = connection.createStatement();

        // Create the "users" table
        statement.executeUpdate("CREATE TABLE USERS (" +
                "ID CHAR(36) PRIMARY KEY, " +
                "USERNAME VARCHAR(255) UNIQUE, " +
                "PASSWORD VARCHAR(255), " +
                "TYPE VARCHAR(10)" +
                ")");

        // Create the "sales" table
        statement.executeUpdate("CREATE TABLE SALES (" +
                "ID CHAR(36) PRIMARY KEY," +
                "CREATED_AT TIMESTAMP," +
                "TOTAL DECIMAL(10, 2)" +
                ")");

        // Create the "items" table
        statement.executeUpdate("CREATE TABLE ITEMS (" +
                "ID CHAR(36) PRIMARY KEY, " +
                "NAME VARCHAR(255), " +
                "UNIT_PRICE DECIMAL(10, 2), " +
                "STOCK INT" +
                ")");

        // Create the "sales" table
        statement.executeUpdate("CREATE TABLE SALES_ITEMS (" +
                "ID CHAR(36) PRIMARY KEY," +
                "SALES_ID CHAR(36)," +
                "ITEM_ID CHAR(36)," +
                "QUANTITY INT," +
                "FOREIGN KEY (SALES_ID) REFERENCES SALES (ID)," +
                "FOREIGN KEY (ITEM_ID) REFERENCES ITEMS (ID)" +
                ")");

        // Create the "preferences" table
        statement.executeUpdate("CREATE TABLE SYS_DATA (" +
                "NAME VARCHAR(255) PRIMARY KEY, " +
                "VALUE VARCHAR(255)" +
                ")");

        System.out.println("Tables created successfully.");

        statement.close();

        logger.info("Base tables created");
    }

    public boolean isSetupCompleted() throws SQLException {
        String sql = "SELECT * FROM SYS_DATA WHERE NAME = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "setup done");

        ResultSet resultSet = statement.executeQuery();
        boolean exists = resultSet.next(); // Check if a row exists

        resultSet.close();
        statement.close();

        return exists;
    }

    public void setup(SetupDto setupData) throws SQLException, IOException {
        // Create users
        usersService.createUser(setupData.adminUsername(), setupData.adminPassword(), UserType.ADMIN);
        usersService.createUser(setupData.cashierUsername(), setupData.cashierPassword(), UserType.CASHIER);

        // Create items
        for (Item item : setupData.items()) {
            itemsService.createItem(item.ID(), item.name(), item.price(), item.stock());
        }

        setCompanyName(setupData.companyName());
        setLang(setupData.language());

        // Copy selected company logo
        var copiedFilePath = new File("./images/logo." + Util.getFileExtension(setupData.logoPath().toFile())).toPath();
        Files.copy(setupData.logoPath(), copiedFilePath);

        setSetupCompleted();

        logger.info("Setup Completed");
    }

    public void setCompanyName(String name) throws SQLException {
        String insertQuery = "INSERT INTO SYS_DATA (NAME, VALUE) VALUES (?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setString(1, "company name");
        insertStatement.setString(2, name);
        insertStatement.executeUpdate();

        logger.info("Updated company name to " + name);
    }

    public void updateCompanyName(String newName) throws SQLException {
        String updateSQL = "UPDATE SYS_DATA SET VALUE = ? WHERE NAME = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
        preparedStatement.setString(1, newName);
        preparedStatement.setString(2, "company name");

        int rowsUpdated = preparedStatement.executeUpdate();

        if (rowsUpdated > 0) {
            logger.info("Updated company name to " + newName);
        } else {
            logger.info("Failed to update company name");
        }
    }

    private void setSetupCompleted() throws SQLException {
        String insertQuery = "INSERT INTO SYS_DATA (NAME, VALUE) VALUES (?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setString(1, "setup done");
        insertStatement.setString(2, "true");
        insertStatement.executeUpdate();

        logger.info("Set setup completed to true");
    }

    public void setLang(Language lang) throws SQLException {
        String insertQuery = "INSERT INTO SYS_DATA (NAME, VALUE) VALUES (?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setString(1, "language");
        insertStatement.setString(2, lang.lang);
        insertStatement.executeUpdate();

        logger.info("Updated language to " + lang.lang);
    }

    public String getCompanyName() throws SQLException {
        String selectQuery = "SELECT VALUE FROM SYS_DATA WHERE NAME = 'company name'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);

        if (resultSet.next()) {
            return resultSet.getString("VALUE");
        } else {
            return "No Name";
        }
    }

    public Language getLanguage() throws SQLException {
        String selectQuery = "SELECT VALUE FROM SYS_DATA WHERE NAME = 'language'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);

        if (resultSet.next()) {
            return switch (resultSet.getString("VALUE")) {
                case "English" -> Language.ENGLISH;
                case "German" -> Language.GERMAN;
                case "Sinhala" -> Language.SINHALA;
                default -> Language.ENGLISH;
            };
        } else {
            return Language.ENGLISH;
        }
    }

    public Path getCompanyLogoPath() {
        for (String imgExtension : new String[] { ".jpg", ".png", ".jpeg" }) {
            var path = "./images/logo" + imgExtension;
            var imgFile = new File(path);
            if (imgFile.exists()) {
                return imgFile.toPath();
            }
        }

        return new File(App.class.getClassLoader().getResource("images/logo.png").getPath()).toPath();
    }

    public boolean deleteLogoFile() {
        Path filePath = getCompanyLogoPath();

        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
                logger.info("Logo file deleted");

                return true;
            } catch (IOException e) {
                logger.severe("Failed to delete logo file");

                return false;
            }
        } else {
            return true;
        }
    }
}
