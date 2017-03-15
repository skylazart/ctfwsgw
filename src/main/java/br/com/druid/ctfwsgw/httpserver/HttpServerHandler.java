package br.com.druid.ctfwsgw.httpserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LogManager.getLogger();

    private HttpRequest request;
    private final StringBuilder buf = new StringBuilder();
    private boolean keepAlive = false;
    private HttpVersion httpVersion = HttpVersion.HTTP_1_0;

    public HttpServerHandler() {
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) msg;

            this.httpVersion = request.protocolVersion();

            if (HttpUtil.isKeepAlive(request)) {
                this.keepAlive = true;
            }

            if (HttpUtil.is100ContinueExpected(request)) {
                sendContinue(ctx);
            }

            HttpHeaders headers = request.headers();
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());

        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                buf.append(content.toString(CharsetUtil.UTF_8));
                buf.append("\r\n");
            } else {
                buf.append("<html><body>teste</body></html>");
                sendHttpResponse(ctx);
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
}
