package br.com.druid.ctfwsgw.message;

import br.com.druid.ctfwsgw.entity.Msisdn;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/14/17.
 */
public interface Request extends Message {
    Msisdn getMsisdn();
}
