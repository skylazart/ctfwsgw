package config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class GatewayConfigurationProperties implements GatewayConfiguration {
    private static final Logger logger = LogManager.getLogger();

    private static final String CONFIG_DEFAULT_PATHNAME = "configuration.properties";

    private static final String HTTPSERVER_LISTEN_ADDRESS = "httpserver.listen.address";
    private static final String HTTPSERVER_LISTEN_PORT = "httpserver.listen.port";
    private static final String CISCO_URL = "cisco.url";
    private static final String ORACLE_URL = "oracle.url";

    private Configuration config;

    public GatewayConfigurationProperties() throws ConfigurationException {
        this(CONFIG_DEFAULT_PATHNAME);
    }

    public GatewayConfigurationProperties(String pathname) throws ConfigurationException {
        config = new Configurations().properties(new File(pathname));
    }

    public String getListenAddress() {
        return config.getString(HTTPSERVER_LISTEN_ADDRESS);
    }

    public Integer getListenPort() {
        return config.getInt(HTTPSERVER_LISTEN_PORT);
    }

    @Override
    public String getOracleUrl() {
        return config.getString(CISCO_URL);
    }

    @Override
    public String getCiscoUrl() {
        return config.getString(ORACLE_URL);
    }
}
