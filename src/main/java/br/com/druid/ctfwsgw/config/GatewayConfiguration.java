package br.com.druid.ctfwsgw.config;

import br.com.druid.ctfwsgw.entity.Msisdn;

import java.util.List;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public interface GatewayConfiguration {
    String getListenAddress();
    Integer getListenPort();

    String getOracleUrl();
    String getCiscoUrl();

    List<String> getRouteCiscoDdd();
    List<String> getRouteOracleDdd();

    List<Msisdn> getRouteCiscoMsisdn();
    List<Msisdn> getRouteOracleMsisdn();
}
