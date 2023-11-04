package quickshop.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import quickshop.App;

public class LocalizationService {
    private EnvironmentService environmentService;

    private Language currentLang = Language.ENGLISH;

    public enum Language {
        ENGLISH("English", "i18n/en.properties"),
        SINHALA("German", "i18n/si.properties"),
        GERMAN("Sinhala", "i18n/du.properties");

        public final String lang;
        public final String propFilePath;

        private Language(String lang, String propFilePath) {
            this.lang = lang;
            this.propFilePath = propFilePath;
        }
    }

    public LocalizationService(EnvironmentService environmentService) throws SQLException {
        this.environmentService = environmentService;
        currentLang = environmentService.getLanguage();
    }

    public Language getCurrentLanguage() {
        return currentLang;
    }

    public Properties getLanguageProperty() throws IOException {
        var languageProperty = new Properties();
        languageProperty.load(App.class.getClassLoader().getResourceAsStream(currentLang.propFilePath));

        return languageProperty;
    }

    public String getValue(String key, String defualt) {
        try {
            return getLanguageProperty().getProperty(key, defualt);
        } catch (IOException e) {
            return defualt;
        }
    }

    public void setLanguage(Language language) {
        currentLang = language;
    }

    public void saveLanguage() throws SQLException {
        environmentService.setLang(currentLang);
    }
}
