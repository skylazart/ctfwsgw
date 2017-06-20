package br.com.druid.ctfwsgw.httpclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/7/17.
 */
public class HttpClient implements Comparable<HttpClient> {
    private static final Logger logger = LogManager.getLogger();

    private final EventLoopGroup bossGroup;
    private final NioEventLoopGroup group;
    private final Bootstrap bootstrap;

    private final String name;

    public HttpClient(String name) {
        if (name == null)
            throw new IllegalArgumentException("Name can't be null");

        this.name = name;
        this.bossGroup = new NioEventLoopGroup(4);
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();

        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitializer(null));
    }

    public boolean connect(String url, FullHttpRequest httpRequest, HttpClientHandler httpClientHandler) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            int port = uri.getPort();

            bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
                Channel ch = future.channel();
                ch.pipeline().addLast(httpClientHandler);

                //ch.writeAndFlush(request).addListener(future1 -> logger.debug("Data sent"));
                ch.writeAndFlush(httpRequest);
            });
            return true;
        } catch (URISyntaxException e) {
            logger.error("Error parsing URL {}", url, e);
            return false;
        }
    }

    public void finish() {
        bossGroup.shutdownGracefully();
        group.shutdownGracefully();
    }

    @Override
    public int compareTo(HttpClient o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpClient)) return false;

        HttpClient that = (HttpClient) o;

        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "HttpClient{" +
                "name='" + name + '\'' +
                '}';
    }
}
