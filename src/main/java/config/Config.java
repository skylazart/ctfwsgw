package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by fsantos on 3/4/17.
 */
public class Config implements GatewayConfiguration {
    private static final Logger logger = LogManager.getLogger();

    private static Config instance = null;

    private String listenAddress;
    private int httpPort;


    private Config() {

    }

    public static class Builder {
        private String listenAddress;
        private int httpPort;

        public Builder listenAddress (String listenAddress) {
            this.listenAddress = listenAddress;
            return this;
        }

        public Builder httpPort(int httpPort) {
            this.httpPort = httpPort;
            return this;
        }

        public Config build() throws GatewayConfigurationException {
            if (validate()) {
                if (Config.instance == null) {
                    Config.getInstance();
                }

                instance.httpPort = this.httpPort;
                instance.listenAddress = this.listenAddress;

                return Config.getInstance();
            } else {
                throw new GatewayConfigurationException("Configuration is not valid");
            }
        }

        private boolean validate() {
            return true;
        }
    }

    private static Config getInstance() {
        if (instance == null)
            instance = new Config();

        return instance;
    }

    public String getListenAddress() {
        return this.listenAddress;
    }

    public Integer getHttpPort() {
        return this.httpPort;
    }
}
