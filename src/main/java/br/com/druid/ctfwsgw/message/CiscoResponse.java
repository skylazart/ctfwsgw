package br.com.druid.ctfwsgw.message;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/14/17.
 */
public class CiscoResponse implements Response {
    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public String getContent() {
        return null;
    }
}
