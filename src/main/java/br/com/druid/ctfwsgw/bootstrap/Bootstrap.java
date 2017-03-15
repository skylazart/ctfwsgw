package br.com.druid.ctfwsgw.bootstrap;

import br.com.druid.ctfwsgw.config.GatewayConfiguration;
import br.com.druid.ctfwsgw.config.GatewayConfigurationProperties;
import br.com.druid.ctfwsgw.entity.Msisdn;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class Bootstrap {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            GatewayConfiguration config = new GatewayConfigurationProperties();

            List<String> ciscoDddList = config.getRouteCiscoDdd();
            for (String s: ciscoDddList)
                System.out.println(s);

        } catch (ConfigurationException e) {
            logger.error("Error opening or parsing configurations", e);
        }
    }
}
