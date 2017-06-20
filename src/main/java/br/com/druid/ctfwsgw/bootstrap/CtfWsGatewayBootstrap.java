package br.com.druid.ctfwsgw.bootstrap;

import br.com.druid.ctfwsgw.config.GatewayConfiguration;
import br.com.druid.ctfwsgw.config.GatewayConfigurationProperties;
import br.com.druid.ctfwsgw.httpclient.HttpClient;
import br.com.druid.ctfwsgw.httpserver.HttpServer;
import br.com.druid.ctfwsgw.route.Router;
import br.com.druid.ctfwsgw.route.RouterSingleton;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import br.com.druid.ctfwsgw.route.Router;
import br.com.druid.ctfwsgw.route.RouterSingleton;

import java.io.IOException;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class CtfWsGatewayBootstrap {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        GatewayConfiguration config = null;
        try {
            if (args.length > 0)
                config = new GatewayConfigurationProperties(args[0]);
            else
                config = new GatewayConfigurationProperties();


            // Creating the http client connections

            // Configuring router singleton
            Router<HttpClient> router = RouterSingleton.getInstance();

            logger.info("Starting HTTP Server {}:{}", config.getListenAddress(), config.getListenPort());
            HttpServer httpServer = new HttpServer(config.getListenAddress(), config.getListenPort());
            Thread httpServerThread = new Thread(httpServer);
            httpServerThread.start();

        } catch (ConfigurationException|IOException e) {
            logger.error("Error opening or parsing configurations", e);
        }
    }
}
