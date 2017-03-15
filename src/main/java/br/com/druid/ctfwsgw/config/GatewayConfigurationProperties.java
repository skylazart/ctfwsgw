package br.com.druid.ctfwsgw.config;

import br.com.druid.ctfwsgw.entity.Msisdn;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.BuilderParameters;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.ArrayList;
import java.util.List;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public class GatewayConfigurationProperties implements GatewayConfiguration {
    private static final String CONFIG_DEFAULT_PATHNAME = "configuration.properties";

    private static final String HTTPSERVER_LISTEN_ADDRESS = "httpserver.listen.address";
    private static final String HTTPSERVER_LISTEN_PORT = "httpserver.listen.port";
    private static final String CISCO_URL = "cisco.url";
    private static final String ORACLE_URL = "oracle.url";
    private static final String ROUTE_CISCO_DDD = "route.cisco.ddd";
    private static final String ROUTE_ORACLE_DDD = "route.oracle.ddd";
    private static final String ROUTE_CISCO_MSISDN = "route.cisco.msisdn";
    private static final String ROUTE_ORACLE_MSISDN = "route.oracle.msisdn";

    private Configuration config;

    public GatewayConfigurationProperties() throws ConfigurationException {
        this(CONFIG_DEFAULT_PATHNAME);
    }

    public GatewayConfigurationProperties(String pathname) throws ConfigurationException {
        Configurations configs = new Configurations();

        PropertiesConfiguration config = new PropertiesConfiguration();
        config.setListDelimiterHandler(new DefaultListDelimiterHandler(','));

    }

    public String getListenAddress() {
        return config.getString(HTTPSERVER_LISTEN_ADDRESS);
    }

    public Integer getListenPort() {
        return config.getInt(HTTPSERVER_LISTEN_PORT);
    }

    @Override
    public String getOracleUrl() {
        return config.getString(CISCO_URL);
    }

    @Override
    public String getCiscoUrl() {
        return config.getString(ORACLE_URL);
    }

    @Override
    public List<String> getRouteCiscoDdd() {
        return (List<String>) config.getList(String.class, ROUTE_CISCO_DDD);
    }

    @Override
    public List<String> getRouteOracleDdd() {
        return config.getList(String.class, ROUTE_ORACLE_DDD);
    }

    @Override
    public List<Msisdn> getRouteCiscoMsisdn() {
        List<String> t = config.getList(String.class, ROUTE_CISCO_MSISDN);
        List<Msisdn> msisdnList = new ArrayList<>();

        for (String s: t)
            msisdnList.add(new Msisdn(s));

        return msisdnList;
    }

    @Override
    public List<Msisdn> getRouteOracleMsisdn() {
        List<String> t = config.getList(String.class, ROUTE_ORACLE_MSISDN);
        List<Msisdn> msisdnList = new ArrayList<>();

        for (String s: t)
            msisdnList.add(new Msisdn(s));

        return msisdnList;
    }
}
