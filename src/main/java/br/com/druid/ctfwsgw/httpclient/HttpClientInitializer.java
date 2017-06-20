package br.com.druid.ctfwsgw.httpclient;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.ssl.SslContext;

import java.net.InetSocketAddress;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/7/17.
 */
public class HttpClientInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public HttpClientInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpClientCodec());
    }
}
