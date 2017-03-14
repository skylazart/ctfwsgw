package test;

import httpclient.HttpClient;
import httpclient.HttpClientAdapter;
import httpclient.HttpClientHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/8/17.
 */
public class HttpClientTest {
    @Test
    public void connect() throws Exception {
        HttpClient httpClient = new HttpClient("test");
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        AtomicInteger atomicInteger = new AtomicInteger(0);

        for (int i = 0; i < 256; i++) {
            HttpClientHandler httpClientHandler = new HttpClientHandler(new HttpClientAdapter() {
                @Override
                public void onDataRead(HttpResponse httpResponse, List<HttpContent> httpContentList) {
                    System.out.println(atomicInteger.incrementAndGet() + " - " + httpResponse.status());
                    atomicInteger.decrementAndGet();
                }

                @Override
                public void onError(ChannelHandlerContext ctx, Throwable cause) {

                }
            });

            executorService.execute(() -> httpClient.connect("http://www.google.com:80", httpClientHandler));
        }

        Thread.sleep(10000);

        System.out.println("Finished");
        httpClient.finish();
    }
}