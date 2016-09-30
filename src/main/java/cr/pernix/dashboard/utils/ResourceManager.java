package cr.pernix.dashboard.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResourceManager {

    private static final Log LOGGER = LogFactory.getLog(ResourceManager.class);
    private static final String CONFIG_PROPERTIES = "mail.properties";
    private static final String MISSING_FILE_ERROR = "Missing '" + CONFIG_PROPERTIES + "' file in the classpath";
    private Properties properties = new Properties();

    public ResourceManager(String resourceName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error(MISSING_FILE_ERROR + e.getMessage());
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            LOGGER.error(MISSING_FILE_ERROR + e.getMessage());
        }
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }


    public static String getStringFromTemplate(String fileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File file = new File(classloader.getResource(fileName).getFile());

        String stringFile = "";
        try {
            stringFile = FileUtils.readFileToString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringFile;
    }
}
