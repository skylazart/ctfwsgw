package bootstrap;

import config.Config;
import config.GatewayConfigurationException;
import httpclient.HttpClient;
import httpclient.HttpClientAdapter;
import httpclient.HttpClientHandler;
import httpserver.HttpServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
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

    private volatile boolean finish = false;


    public static void main(String[] args) throws InterruptedException {
        new Bootstrap().run();
    }

    private void run2() throws InterruptedException {
        HttpClient httpClient = new HttpClient();
        try {
            for (int i = 0; i < 100; i++) {
                httpClient.connect("http://localhost:8080/", new HttpClientHandler(new HttpClientAdapter() {
                    @Override
                    public void onDataRead(HttpResponse httpResponse, List<HttpContent> httpContentList) {
                        logger.debug(httpResponse.status());
                    }

                    @Override
                    public void onError(ChannelHandlerContext ctx, Throwable cause) {
                        logger.error("HTTP client error");
                    }
                }));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            Thread.sleep(10000);
            httpClient.finish();
        }
    }

    private void run() throws InterruptedException {
        try {
            logger.info(ManagementFactory.getRuntimeMXBean().getName());

            Config.Builder builder = new Config.Builder();

            Config config = builder.httpPort(8089)
                    .listenAddress("0.0.0.0")
                    .build();

            logger.info("HTTP listen address {} port {}", config.getListenAddress(), config.getHttpPort());

            ExecutorService httpServerExecutorService = Executors.newFixedThreadPool(1);
            Future<?> future = httpServerExecutorService.submit(new HttpServer(config.getListenAddress(),
                    config.getHttpPort()));

            while (true) {
                Thread.sleep(10000);
                if (!future.isDone()) continue;

                logger.error("Restarting HTTP Server Thread");
                future = httpServerExecutorService.submit(new HttpServer(config.getListenAddress(),
                        config.getHttpPort()));

                if (finish)
                    break;
            }

        } catch (GatewayConfigurationException e) {
            e.printStackTrace();
        }
    }
}
