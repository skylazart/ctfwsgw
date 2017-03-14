package route;

import entity.Msisdn;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/13/17.
 */
public interface Router<T> {
    boolean addPrefix(String prefix, T destination);
    boolean addMsisdn(Msisdn msisdn, T destination);
    T findRoute(Msisdn msisdn) throws RouteNotFound;
}
