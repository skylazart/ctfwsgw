package httpclient;

import io.netty.handler.codec.http.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/7/17.
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final HttpClientAdapter httpCLientAdapter;
    private HttpResponse httpResponse;
    private List<HttpContent> httpContentList = new ArrayList<>();

    public HttpClientHandler(HttpClientAdapter httpClientAdapter) {
        this.httpCLientAdapter = httpClientAdapter;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpResponse) {
            httpResponse = (HttpResponse) msg;
        }
        if (msg instanceof HttpContent) {
            httpContentList.add((HttpContent) msg);

            if (msg instanceof LastHttpContent) {
                ctx.close();

                if (httpCLientAdapter != null)
                    httpCLientAdapter.onDataRead(httpResponse, httpContentList);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();

        if (httpCLientAdapter != null)
            httpCLientAdapter.onError(ctx, cause);
    }
}
