package quickshop;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

import quickshop.db.Connection;
import quickshop.db.ConnectionShutDownHook;
import quickshop.entity.User;
import quickshop.services.EnvironmentService;
import quickshop.services.ItemsService;
import quickshop.services.LocalizationService;
import quickshop.services.SalesService;
import quickshop.services.UsersService;
import quickshop.services.UsersService.UserType;
import quickshop.util.Util;
import quickshop.views.CashierWindow;
import quickshop.views.ItemsWindow;
import quickshop.views.LoginWindow;
import quickshop.views.ManagerWindow;
import quickshop.views.QuickSetupWindow;
import quickshop.views.ReportsWindow;
import quickshop.views.SettingsWindow;
import quickshop.views.UsersWindow;

public class App {
    static final ClassLoader loader = App.class.getClassLoader();

    static Logger logger;
    static EnvironmentService environmentService;
    static ItemsService itemsService;
    static UsersService usersService;
    static SalesService salesService;
    static LocalizationService localizationService;
    static User currentUser;

    public static void main(String[] args) throws Exception {
        logger = Logger.getLogger(App.class.getName());
        var fh = new FileHandler(
                "./logs/main_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        Connection.createConnection();

        environmentService = new EnvironmentService(Connection.getConnection(), logger);

        if (!environmentService.tablesExists()) {
            environmentService.createTables();
        }

        Runtime.getRuntime().addShutdownHook(new ConnectionShutDownHook(logger));

        usersService = new UsersService(Connection.getConnection(), logger);
        itemsService = new ItemsService(Connection.getConnection(), logger);
        salesService = new SalesService(Connection.getConnection(), logger, itemsService);
        localizationService = new LocalizationService(environmentService);

        environmentService.setDependancies(usersService, itemsService);

        try {
            UIManager.setLookAndFeel(new FlatCyanLightIJTheme());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        if (!environmentService.isSetupCompleted()) {
            // Begin quick setup
            showQuickSetupWindow();
        } else {
            showLoginWindow();
        }
    }

    private static void afterLogin(User user, JFrame window) {
        window.setVisible(false);
        window.dispose();

        currentUser = user;

        if (user.type() == UserType.ADMIN) {
            // Show manager window
            showManagerWindow();
        } else {
            // Show cashier window
            showCashierWindow();
        }
    }

    private static void showManagerWindow() {
        try {
            new ManagerWindow(salesService, itemsService,
                    ManagerWindow -> showItemsWindow(),
                    managerWindow -> showUsersWindow(),
                    managerWindow -> showReportsWindow(),
                    managerWindow -> showSettingsWindow(),
                    managerWindow -> {
                        managerWindow.setVisible(false);
                        managerWindow.dispose();

                        showLoginWindow();
                    }).setVisible(true);
        } catch (SQLException e) {
            logger.severe("Failed to open manager window (SQLException)");
            JOptionPane.showMessageDialog(null, "Failed to open manager window (SQLException)",
                    "Error Occured", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showCashierWindow() {
        new CashierWindow(localizationService, logger, currentUser, itemsService, salesService, environmentService,
                App::showLoginWindow).setVisible(true);
    }

    private static void showQuickSetupWindow() {
        try {
            new QuickSetupWindow(logger, localizationService, (window, data) -> {
                try {
                    environmentService.setup(data);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();

                    logger.severe("Failed to finish quick setup");
                    JOptionPane.showMessageDialog(window, "Failed to finish quick setup (SQLException)",
                            "Error Occured", JOptionPane.ERROR_MESSAGE);
                    return;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();

                    logger.severe("Failed to finish quick setup (IOException)");
                    JOptionPane.showMessageDialog(window, "Failed to finish quick setup (IOException)",
                            "Error Occured", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                window.setVisible(false);
                window.dispose();

                showLoginWindow();
            }).setVisible(true);
        } catch (IOException e) {
            logger.severe("Failed to open quick setup window (IOException)");
            JOptionPane.showMessageDialog(null, "Failed to open quick setup window (IOException)",
                    "Error Occured", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showItemsWindow() {
        new ItemsWindow(logger, itemsService).setVisible(true);
    }

    private static void showUsersWindow() {
        new UsersWindow(logger, usersService).setVisible(true);
    }

    private static void showReportsWindow() {
        new ReportsWindow(logger, environmentService, salesService).setVisible(true);
    }

    private static void showSettingsWindow() {
        try {
            new SettingsWindow(localizationService, logger, environmentService.getCompanyLogoPath(), environmentService.getCompanyName(),
                    (logo, name, language, window) -> {
                        logo.ifPresent(newLogoFilePath -> {
                            // Delete previous logo file
                            environmentService.deleteLogoFile();

                            var copiedFilePath = new File(
                                    "./images/logo." + Util.getFileExtension(newLogoFilePath.toFile())).toPath();
                            try {
                                Files.copy(newLogoFilePath, copiedFilePath);
                            } catch (IOException e) {
                                logger.severe("Failed to copy logo image file (IOException)");
                                JOptionPane.showMessageDialog(window, "Failed to save changes (IOException)",
                                        "Error Occured", JOptionPane.ERROR_MESSAGE);
                            }
                        });

                        name.ifPresent(newCompanyName -> {
                            try {
                                environmentService.updateCompanyName(newCompanyName);
                            } catch (SQLException e) {
                                logger.severe("Failed to save company name (SQLException)");
                                JOptionPane.showMessageDialog(window, "Failed to save changes (SQLException)",
                                        "Error Occured", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                        language.ifPresent(newLanguage -> {
                            localizationService.setLanguage(newLanguage);
                        });

                        window.setVisible(false);
                        window.dispose();
                    }).setVisible(true);
        } catch (SQLException e) {
            logger.severe("Failed to open settings window (SQLException)");
            JOptionPane.showMessageDialog(null, "Failed to open settings window (SQLException)",
                    "Error Occured", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private static void showLoginWindow() {
        new LoginWindow(usersService, App::afterLogin).setVisible(true);
    }
}
