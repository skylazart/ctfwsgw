package br.com.druid.ctfwsgw.httpserver;

import br.com.druid.ctfwsgw.httpclient.HttpClientAdapter;
import br.com.druid.ctfwsgw.httpclient.HttpClientHandler;
import br.com.druid.ctfwsgw.message.MiddlewareRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> implements HttpClientAdapter {
    private static final Logger logger = LogManager.getLogger();

    private final StringBuilder buf = new StringBuilder();
    private boolean keepAlive = false;
    private HttpVersion httpVersion = HttpVersion.HTTP_1_0;

    private HttpHeaders httpRequestHeader = null;

    private final Bootstrap clientBootstrap;
    private final Bootstrap httpsClientBootstrap;

    private Channel channelOracle = null;
    private Channel channelCisco = null;
    private ChannelHandlerContext ctx = null;

    public HttpServerHandler(Bootstrap clientBootstrap, Bootstrap httpsClientBootstrap) {
        this.clientBootstrap = clientBootstrap;
        this.httpsClientBootstrap = httpsClientBootstrap;

        connectCisco();
        connectOracle();
    }

    private void connectCisco() {
        clientBootstrap.connect("10.221.109.25", 8080).addListener((ChannelFutureListener) future -> {
            this.channelCisco = future.channel();
            this.channelCisco.pipeline().addLast(new HttpClientHandler(this));
        });
    }

    private void connectOracle() {
        clientBootstrap.connect("10.221.109.25", 8080).addListener((ChannelFutureListener) future -> {
            this.channelOracle = future.channel();
            this.channelOracle.pipeline().addLast(new HttpClientHandler(this));
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.ctx = ctx;

        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;

            this.httpVersion = request.protocolVersion();

            if (HttpUtil.isKeepAlive(request)) {
                this.keepAlive = true;
            }

            if (HttpUtil.is100ContinueExpected(request)) {
                sendContinue(ctx);
            }

            this.httpRequestHeader = request.headers();
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                buf.append(content.toString(CharsetUtil.UTF_8));
            }

            if (!content.isReadable() || httpContent instanceof DefaultLastHttpContent) {
                logger.debug("Processing request {}", buf.toString());
                MiddlewareRequest middlewareRequest = new MiddlewareRequest(buf.toString());

                if (!middlewareRequest.validate()) {
                    logger.error("Invalid request");
                    return;
                }

                FullHttpRequest request = new DefaultFullHttpRequest(
                        HttpVersion.HTTP_1_1, HttpMethod.POST, "/axis/services/MessageService");

                request.headers().set("Content-Type", "application/x-www-form-urlencoded");
                request.headers().set("Accept", "text/xml; charset=utf-8");
                ByteBuf bbuf = Unpooled.copiedBuffer(buf.toString(), StandardCharsets.UTF_8);
                request.headers().set("Content-Length", bbuf.readableBytes());
                request.content().clear().writeBytes(bbuf);

                channelOracle.writeAndFlush(request);
            }
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));

        response.headers().set("Content-Type", "Content-Type: text/html; charset=UTF-8");
        response.headers().set("Content-Length", response.content().readableBytes());
        if (keepAlive) {
            HttpUtil.setKeepAlive(response, true);
        }

        ctx.write(response).addListener((ChannelFutureListener) future -> {
            if (!keepAlive)
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        });
    }

    private void sendContinue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Exception caught", cause);
        ctx.close();
    }

    @Override
    public void onDataRead(HttpResponse httpResponse, List<HttpContent> httpContentList) {
        logger.debug("Receiving answer {}", httpResponse);

        StringBuilder sbuf = new StringBuilder();

        for (HttpContent httpContent : httpContentList) {
            ByteBuf content = httpContent.content();
            if (content.isReadable())
                sbuf.append(content.toString(CharsetUtil.UTF_8));
        }

        logger.debug("Http content: {}", sbuf);
    }

    @Override
    public void onError(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("Error {}", ctx.channel(), cause);
        logger.info("Finishing connection with Oracle and Cisco...");

        channelCisco.close();
        channelOracle.close();

        ctx.close();
    }
}
