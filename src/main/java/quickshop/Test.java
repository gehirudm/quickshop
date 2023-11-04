// package quickshop;

// import java.io.IOException;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.logging.*;

// import javax.swing.UIManager;

// import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

// import quickshop.db.ConnectionShutDownHook;
// import quickshop.services.UsersService;
// import quickshop.views.LoginWindow;

// public class Test {
//     public static void main(String[] args) throws SQLException, SecurityException, IOException {
//         Logger logger = Logger.getLogger(App.class.getName());
        
//         var fh = new FileHandler("./logs/main.log");  
//         logger.addHandler(fh);
//         SimpleFormatter formatter = new SimpleFormatter();  
//         fh.setFormatter(formatter); 

//         quickshop.db.Connection.createConnection();
//         Runtime.getRuntime().addShutdownHook(new ConnectionShutDownHook(logger));

//         try {
//             UIManager.setLookAndFeel(new FlatCyanLightIJTheme());
//         } catch (Exception ex) {
//             System.err.println("Failed to initialize LaF");
//         }

//         var loginWindow = new LoginWindow(new UsersService(quickshop.db.Connection.getConnection(), logger));

//         loginWindow.setVisible(true);
//     }
//     // public static void main(String[] args) {

//     // Connection con = null;
//     // Statement st = null;

//     // String url = "jdbc:derby:testdb;create=true";

//     // try {
//     // System.setProperty("derby.system.home", "C:\\derby");

//     // con = DriverManager.getConnection(url);
//     // st = con.createStatement();
//     // st.executeUpdate("CREATE TABLE CARS(ID INT PRIMARY KEY,"
//     // + "NAME VARCHAR(30), PRICE INT)");
//     // st.executeUpdate("INSERT INTO CARS VALUES(1, 'Audi', 52642)");
//     // st.executeUpdate("INSERT INTO CARS VALUES(2, 'Mercedes', 57127)");
//     // st.executeUpdate("INSERT INTO CARS VALUES(3, 'Skoda', 9000)");
//     // st.executeUpdate("INSERT INTO CARS VALUES(4, 'Volvo', 29000)");
//     // st.executeUpdate("INSERT INTO CARS VALUES(5, 'Bentley', 350000)");
//     // st.executeUpdate("INSERT INTO CARS VALUES(6, 'Citroen', 21000)");
//     // st.executeUpdate("INSERT INTO CARS VALUES(7, 'Hummer', 41400)");
//     // st.executeUpdate("INSERT INTO CARS VALUES(8, 'Volkswagen', 21600)");
//     // DriverManager.getConnection("jdbc:derby:;shutdown=true");

//     // } catch (SQLException ex) {
//     // if (((ex.getErrorCode() == 50000)
//     // && ("XJ015".equals(ex.getSQLState())))) {

//     // System.out.println("Derby shut down normally");
//     // } else
//     // System.out.println(ex.getMessage());

//     // } finally {
//     // try {
//     // if (st != null)
//     // st.close();
//     // if (con != null)
//     // con.close();

//     // } catch (SQLException ex) {
//     // System.out.println(ex.getMessage());
//     // }
//     // }
//     // }
// }
