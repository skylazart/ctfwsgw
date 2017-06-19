package br.com.druid.ctfwsgw.pcrf_oracle;

import br.com.druid.ctfwsgw.config.GatewayConfiguration;
import br.com.druid.ctfwsgw.entity.Msisdn;
import br.com.druid.ctfwsgw.httpclient.HttpClient;
import br.com.druid.ctfwsgw.httpclient.HttpClientAdapter;
import br.com.druid.ctfwsgw.httpclient.HttpClientHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ctfwsgw
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 6/14/17.
 */
public class OracleHttpClient {
    private static final Logger logger = LogManager.getLogger();
    private HttpClient httpClient;
    private String url;
    private Timeout timeout = null;

    public OracleHttpClient(GatewayConfiguration configuration) {
        super();

        this.url = configuration.getCiscoUrl();

        logger.info("Creating instance to handle PCRF Oracle requests {}", this.url);
        httpClient = new HttpClient("Name:" + this.url);
    }

    void sendRequest(Msisdn msisdn) {
        ByteBuf bbuf = Unpooled.copiedBuffer("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "                 <soapenv:Header/>\n" +
                "                 <soapenv:Body>\n" +
                "                 <processTransaction xmlns=\"http://webservice.blueslice.com\"><![CDATA[<tx>\n" +
                "                 <req name=\"operation\" resonly=\"y\">\n" +
                "                 <oper ent=\"Subscription\" name=\"GetPoolID\" ns=\"global\">\n" +
                "                 <expr>\n" +
                "                 <param name=\"MSISDN\"/>\n" +
                "                 <op value=\"=\"/>\n" +
                "                 <value val=\"" + msisdn.getMsisdn() + "\"/>\n" +
                "                 </expr>\n" +
                "                 </oper>\n" +
                "                 </req>\n" +
                "                 </tx>]]></processTransaction>\n" +
                "                 </soapenv:Body>\n" +
                "                 </soapenv:Envelope>", StandardCharsets.UTF_8);

        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
        request.headers().set("Content-Type", "application/x-www-form-urlencoded");
        request.headers().set("Accept", "text/xml; charset=utf-8");
        request.headers().set("Content-Length", bbuf.readableBytes());
        request.content().clear().writeBytes(bbuf);


        Timer timer = new HashedWheelTimer();
        timeout = timer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println("Timeout");
            }
        }, 10, TimeUnit.SECONDS);

        httpClient.connect(url, request, new HttpClientHandler(new HttpClientAdapter() {
            @Override
            public void onDataRead(HttpResponse httpResponse, List<HttpContent> httpContentList) {
                if (timeout != null && !timeout.isExpired())
                    timeout.cancel();
            }

            @Override
            public void onError(ChannelHandlerContext ctx, Throwable cause) {

            }
        }));
    }
}
