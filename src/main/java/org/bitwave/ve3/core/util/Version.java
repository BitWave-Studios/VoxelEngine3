package org.bitwave.ve3.core.util;

import java.io.InputStream;
import java.util.Properties;

public class Version {
    public String getVersion() throws Exception {
        Properties properties = new Properties();
        InputStream is = Version.class.getResourceAsStream("/version.properties");
        properties.load(is);
        return properties.getProperty("version");
    }
}
