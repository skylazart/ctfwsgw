package br.com.druid.ctfwsgw.message;

import br.com.druid.ctfwsgw.entity.InvalidMsisdn;
import br.com.druid.ctfwsgw.entity.Msisdn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger();

    private final String request;
    private boolean validated = false;
    private Msisdn msisdn;

    public MiddlewareRequest(String request) {
        this.request = request;
        validate(request);
    }

    private void validate(String request) {
        int idx1 = request.indexOf("<networkId>");
        int idx2 = request.indexOf("</networkId>");

        try {
            if (idx1 < 0 || idx2 < 0) {
                logger.error("Invalid request. Element networkId not found");
                return;
            }

            this.msisdn = new Msisdn(request.substring(idx1 + 11, idx2));
            validated = true;
        } catch (InvalidMsisdn invalidMsisdn) {
            logger.error("Invalid request {}", request);
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
