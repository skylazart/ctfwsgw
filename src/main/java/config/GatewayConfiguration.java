package config;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/4/17.
 */
public interface GatewayConfiguration {
    String getListenAddress();
    Integer getListenPort();

    String getOracleUrl();
    String getCiscoUrl();
}
