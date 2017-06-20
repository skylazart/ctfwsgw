package br.com.druid.ctfwsgw.httpserver;

import br.com.druid.ctfwsgw.httpclient.HttpClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLException;


/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger logger = LogManager.getLogger();

    //private final SslContext sslCtx = null;

    private final EventLoopGroup bossGroup;
    private final NioEventLoopGroup group;
    private final Bootstrap clientBootstrap;
    private final Bootstrap httpsClientBootstrap;

    public HttpServerInitializer(EventLoopGroup bossGroup) throws SSLException {
        logger.info("Initializing HTTP Client Netty...");

        SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        this.bossGroup = bossGroup;
        this.group = new NioEventLoopGroup();
        this.clientBootstrap = new Bootstrap();
        this.httpsClientBootstrap = new Bootstrap();

        clientBootstrap.group(this.bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitializer(null));

        httpsClientBootstrap.group(this.bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitializer(sslCtx));
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        logger.info("Receiving new connection from {}", socketChannel.remoteAddress());

//        if (sslCtx != null) {
//            pipeline.addLast(sslCtx.newHandler(socketChannel.alloc()));
//        }

        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpServerHandler(clientBootstrap, httpsClientBootstrap));
    }
}
