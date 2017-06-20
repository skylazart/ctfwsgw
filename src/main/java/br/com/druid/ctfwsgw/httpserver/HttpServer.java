package br.com.druid.ctfwsgw.httpserver;

import br.com.druid.ctfwsgw.httpclient.HttpClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class HttpServer implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private final String listenAddress;
    private final Integer httpPort;

    public HttpServer(String listenAddress, int httpPort) {
        this.listenAddress = listenAddress;
        this.httpPort = httpPort;
    }

    public HttpServer(int httpPort) {
        this("0.0.0.0", httpPort);
    }

    public void run() {
        logger.info("Starting HTTP server {}:{}", listenAddress, httpPort);

        EventLoopGroup bossGroup = new NioEventLoopGroup(4);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer(bossGroup));

            Channel ch = serverBootstrap.bind(listenAddress, httpPort).sync().channel();
            ch.closeFuture().sync();

        } catch (Exception e) {
            logger.error("Error binding HTTP Server port {}", httpPort, e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
