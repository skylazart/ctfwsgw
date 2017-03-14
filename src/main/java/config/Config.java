package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import validate.Validatable;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class Config implements GatewayConfiguration {
    private static final Logger logger = LogManager.getLogger();

    private static Config instance = null;

    private Params params = null;

    private Config() {
    }

    public static class Builder implements Validatable {
        private Params params;

        public Builder listenAddress (String listenAddress) {
            params.setListenAddress(listenAddress);
            return this;
        }

        public Builder listenPort(int listenPort) {
            params.setListenPort(listenPort);
            return this;
        }

        public Builder pcrfAddress(String pcrfAddress) {
            params.setPcrfAddress(pcrfAddress);
            return this;
        }

        public Builder pcrfPort(int pcrfPort) {
            params.setPcrfPort(pcrfPort);
            return this;
        }

        public Config build() throws GatewayConfigurationException {
            if (validate()) {
                if (Config.instance == null) {
                    Config.getInstance();
                }

                instance.params = params;

                return Config.getInstance();
            } else {
                throw new GatewayConfigurationException("Configuration is not valid");
            }
        }

        public boolean validate() {
            return params.validate();
        }
    }

    private static Config getInstance() {
        if (instance == null)
            instance = new Config();

        return instance;
    }

    public String getListenAddress() {
        return params.getListenAddress();
    }

    public Integer getListenPort() {
        return params.getListenPort();
    }

    @Override
    public String getPcrfAddress() {
        return params.getPcrfAddress();
    }

    @Override
    public Integer getPcrfPort() {
        return params.getPcrfPort();
    }
}
