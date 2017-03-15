package br.com.druid.ctfwsgw.message;

import br.com.druid.ctfwsgw.entity.Msisdn;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/14/17.
 */
public class OracleRequest implements Request {
    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public Msisdn getMsisdn() {
        return null;
    }
}
