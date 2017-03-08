package httpclient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;

import java.util.List;

/**
 * Created by fsantos on 3/7/17.
 */
public interface HttpClientAdapter {
    void onDataRead(HttpResponse httpResponse, List<HttpContent> httpContentList);
    void onError(ChannelHandlerContext ctx, Throwable cause);
}
