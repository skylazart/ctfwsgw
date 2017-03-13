package httpclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
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
        this.name = name;
        this.bossGroup = new NioEventLoopGroup(4);
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();

        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitializer());
    }

    public boolean connect(String url, HttpClientHandler httpClientHandler) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            int port = uri.getPort();

            bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
                Channel ch = future.channel();
                ch.pipeline().addLast(httpClientHandler);

                HttpRequest request = new DefaultFullHttpRequest(
                        HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());

                //ch.writeAndFlush(request).addListener(future1 -> logger.debug("Data sent"));
                ch.writeAndFlush(request);
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
