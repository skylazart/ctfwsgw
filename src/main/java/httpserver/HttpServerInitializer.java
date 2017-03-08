package httpserver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;


/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    HttpServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(socketChannel.alloc()));
        }

        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpServerHandler());
    }
}
