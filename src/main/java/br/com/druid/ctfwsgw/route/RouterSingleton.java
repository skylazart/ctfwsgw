package br.com.druid.ctfwsgw.route;

import br.com.druid.ctfwsgw.entity.Msisdn;
import br.com.druid.ctfwsgw.httpclient.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/12/17.
 */
public class RouterSingleton implements Router<HttpClient> {
    private static RouterSingleton instance = null;

    private Map<Msisdn, HttpClient> routeByMsisdn = new HashMap<>();
    private Map<String, HttpClient> routeByPrefix = new HashMap<>();

    private RouterSingleton() {

    }

    public static RouterSingleton getInstance() {
        if (instance == null)
            instance = new RouterSingleton();
        return instance;
    }

    @Override
    public boolean addPrefix(String prefix, HttpClient destination) {
        if (prefix.length() != 2) {
            return false;
        }

        routeByPrefix.put(prefix, destination);
        return true;
    }

    @Override
    public boolean addMsisdn(Msisdn msisdn, HttpClient destination) {
        routeByMsisdn.put(msisdn, destination);
        return true;
    }

    @Override
    public HttpClient findRoute(Msisdn msisdn) throws RouteNotFound {
        HttpClient destination = routeByMsisdn.get(msisdn);
        if (destination == null)
            destination = routeByPrefix.get(msisdn.getPrefix());

        if (destination == null)
            throw new RouteNotFound("Destination not found");

        return destination;
    }
}
