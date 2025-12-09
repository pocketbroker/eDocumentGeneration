package com.pocketbroker.edocument.util;

import com.pocketbroker.edocument.config.DocuSignConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for loading configuration from properties files.
 */
public class ConfigLoader {

    private ConfigLoader() {
        // Utility class
    }

    /**
     * Loads DocuSign configuration from a properties file.
     *
     * @param propertiesFilePath path to the properties file
     * @return DocuSignConfig object populated with values from the file
     * @throws IOException if the file cannot be read
     */
    public static DocuSignConfig loadFromPropertiesFile(String propertiesFilePath) throws IOException {
        Properties properties = new Properties();
        
        try (FileInputStream fis = new FileInputStream(propertiesFilePath)) {
            properties.load(fis);
        }
        
        return createConfigFromProperties(properties);
    }

    /**
     * Loads DocuSign configuration from a properties file on the classpath.
     *
     * @param resourcePath path to the properties file in the classpath
     * @return DocuSignConfig object populated with values from the file
     * @throws IOException if the resource cannot be read
     */
    public static DocuSignConfig loadFromClasspath(String resourcePath) throws IOException {
        Properties properties = new Properties();
        
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            properties.load(is);
        }
        
        return createConfigFromProperties(properties);
    }

    /**
     * Creates a DocuSignConfig from Properties object.
     *
     * @param properties the properties object
     * @return DocuSignConfig object
     */
    private static DocuSignConfig createConfigFromProperties(Properties properties) {
        DocuSignConfig config = new DocuSignConfig();
        
        config.setIntegrationKey(properties.getProperty("docusign.integrationKey"));
        config.setUserId(properties.getProperty("docusign.userId"));
        config.setAccountId(properties.getProperty("docusign.accountId"));
        config.setPrivateKeyPath(properties.getProperty("docusign.privateKeyPath"));
        
        // Optional properties with defaults
        String basePath = properties.getProperty("docusign.basePath");
        if (basePath != null && !basePath.isEmpty()) {
            config.setBasePath(basePath);
        }
        
        String oAuthBasePath = properties.getProperty("docusign.oAuthBasePath");
        if (oAuthBasePath != null && !oAuthBasePath.isEmpty()) {
            config.setOAuthBasePath(oAuthBasePath);
        }
        
        return config;
    }
}
