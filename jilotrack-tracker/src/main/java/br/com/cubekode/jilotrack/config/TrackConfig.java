package br.com.cubekode.jilotrack.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.cubekode.jilotrack.util.IOUtil;

public class TrackConfig {

    private static final TrackConfig INSTANCE = new TrackConfig();

    private static final Logger LOG = Logger.getLogger(TrackConfig.class.getName());

    public static TrackConfig get() {
        return INSTANCE;
    }

    private Properties properties;

    public Properties getProperties() {
        if (properties == null) {
            synchronized (this) {
                if (properties == null) {
                    properties = new Properties();
                    InputStream configStream = IOUtil.getResourceStream("apptrack.config");
                    if (configStream != null) {
                        try {
                            properties.load(configStream);
                        } catch (IOException e) {
                            // Erro ao carregar properties.
                            if (LOG.isLoggable(Level.WARNING)) {
                                LOG.warning("Config properties not loaded. " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        return properties;
    }

    public void setProperty(String key, String value) {
        getProperties().setProperty(key, value);
    }

    public String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }
}
