package test;

import br.com.druid.ctfwsgw.message.MiddlewareRequest;
import org.junit.Assert;
import org.junit.Test;

/**
 * ctfwsgw
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 6/14/17.
 */
public class MiddlewareRequestTest {
    private static final String valid_request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://broadhop.com/unifiedapi/soap/types\">\n"+
            "   <soapenv:Header/>\n"+
            "   <soapenv:Body>\n"+
            "      <GetSubscriberRequest>\n"+
            "         <audit>\n"+
            "            <id>System_NAME</id>\n"+
            "         </audit>\n"+
            "         <networkId>552199998888</networkId>\n"+
            "         <returnSessions>false</returnSessions>\n"+
            "         <returnBalances>true</returnBalances>\n"+
            "         <includeExpiredData>false</includeExpiredData>\n"+
            "         <excludeReservationsFromCreditTotal>true</excludeReservationsFromCreditTotal>\n"+
            "      </GetSubscriberRequest>\n"+
            "   </soapenv:Body>\n"+
            "</soapenv:Envelope>\n";

    private static final String invalid_request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://broadhop.com/unifiedapi/soap/types\">\n"+
            "   <soapenv:Header/>\n"+
            "   <soapenv:Body>\n"+
            "      <GetSubscriberRequest>\n"+
            "         <audit>\n"+
            "            <id>System_NAME</id>\n"+
            "         </audit>\n"+
            "         <returnSessions>false</returnSessions>\n"+
            "         <returnBalances>true</returnBalances>\n"+
            "         <includeExpiredData>false</includeExpiredData>\n"+
            "         <excludeReservationsFromCreditTotal>true</excludeReservationsFromCreditTotal>\n"+
            "      </GetSubscriberRequest>\n"+
            "   </soapenv:Body>\n"+
            "</soapenv:Envelope>\n";

    @Test
    public void validate() throws Exception {
        MiddlewareRequest valid_middleware_request = new MiddlewareRequest(valid_request);
        Assert.assertEquals(valid_middleware_request.validate(), true);

        MiddlewareRequest invalid_middleware_request = new MiddlewareRequest(invalid_request);
        Assert.assertEquals(invalid_middleware_request.validate(), false);
    }

    @Test
    public void getContent() throws Exception {
    }

    @Test
    public void getMsisdn() throws Exception {
    }

}