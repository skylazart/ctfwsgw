package httpserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by fsantos on 3/4/17.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LogManager.getLogger();

    private HttpRequest request;
    private final StringBuilder buf = new StringBuilder();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) msg;

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
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK);
                ctx.write(response);
            }
        }
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
