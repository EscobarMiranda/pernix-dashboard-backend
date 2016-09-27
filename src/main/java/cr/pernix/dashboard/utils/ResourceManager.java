package cr.pernix.dashboard.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class ResourceManager {

    public static InputStream getResourceAsInputStream(String resourceName) {
        InputStream inputStreamFile = ClassLoader.getSystemResourceAsStream(resourceName);
        return inputStreamFile;
    }

    public static URL getHtmlXslAsInputStream() {
        return getResourceUrl("HtmlXsl.xsl");
    }

    public static URL getResourceUrl(String resourceName) {
        return ClassLoader.getSystemResource(resourceName);
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
