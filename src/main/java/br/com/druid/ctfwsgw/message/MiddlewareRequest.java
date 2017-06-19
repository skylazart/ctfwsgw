package br.com.druid.ctfwsgw.message;

import br.com.druid.ctfwsgw.entity.InvalidMsisdn;
import br.com.druid.ctfwsgw.entity.Msisdn;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/14/17.
 */
public class MiddlewareRequest implements Request {
    private final XPathFactory xpathFactory = XPathFactory.newInstance();
    private final XPath xpath = xpathFactory.newXPath();
    private final String request;
    private boolean validated;
    private Msisdn msisdn;

    public MiddlewareRequest(String request) {
        this.request = request;
        validate(request);
    }

    private void validate(String request) {
        try {
            InputSource source = new InputSource(new StringReader(request));
            Document doc = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
            //TODO: check for optimizations
            String msisdn = xpath.evaluate("/*/*/GetSubscriberRequest/networkId", doc);
            this.msisdn = new Msisdn(msisdn);
            validated = true;
        } catch (XPathExpressionException|InvalidMsisdn e) {
            validated = false;
        }
    }

    @Override
    public boolean validate() {
        return validated;
    }

    @Override
    public String getContent() {
        return request;
    }

    @Override
    public Msisdn getMsisdn() {
        return msisdn;
    }
}
