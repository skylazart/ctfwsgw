package bootstrap;

import config.GatewayConfiguration;
import config.GatewayConfigurationProperties;
import config.GatewayConfigurationException;
import httpclient.HttpClient;
import httpclient.HttpClientAdapter;
import httpclient.HttpClientHandler;
import httpserver.HttpServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class Bootstrap {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            GatewayConfiguration config = new GatewayConfigurationProperties();
        } catch (ConfigurationException e) {
            logger.error("Error opening or parsing configurations", e);
        }
    }
}
