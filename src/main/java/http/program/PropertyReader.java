package http.program;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    private static Properties properties = LoadProperties();

    private static Properties LoadProperties(){
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(new File("config.properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
