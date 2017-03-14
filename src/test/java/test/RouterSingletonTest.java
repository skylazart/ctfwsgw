package test;

import entity.Msisdn;
import httpclient.HttpClient;
import org.junit.Assert;
import org.junit.Test;
import route.Router;
import route.RouterSingleton;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/12/17.
 */
public class RouterSingletonTest {
    @Test
    public void test() throws Exception {
        Router<HttpClient> router = RouterSingleton.getInstance();
        HttpClient rio = new HttpClient("RIO");
        HttpClient sp = new HttpClient("SP");
        HttpClient test = new HttpClient("TEST");

        router.addMsisdn(new Msisdn("21998519898"), test);
        router.addPrefix("21", rio);
        router.addPrefix("11", sp);

        HttpClient t = null;
        t = router.findRoute(new Msisdn("21998519898"));
        Assert.assertEquals(t.getName().compareTo("TEST"), 0);

        t = router.findRoute(new Msisdn("21999998888"));
        Assert.assertEquals(t.getName().compareTo("RIO"), 0);

        t = router.findRoute(new Msisdn("11999998888"));
        Assert.assertEquals(t.getName().compareTo("SP"), 0);

        rio.finish();
        sp.finish();
        test.finish();
    }
}