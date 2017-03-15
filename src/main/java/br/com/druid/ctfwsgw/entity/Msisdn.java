package br.com.druid.ctfwsgw.entity;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/13/17.
 */
public class Msisdn implements Comparable<Msisdn> {
    private final String msisdn;
    private final String prefix;

    public Msisdn(String s) {
        this.msisdn = s;
        this.prefix = s.substring(0, 2);
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public int compareTo(Msisdn o) {
        return msisdn.compareTo(o.msisdn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Msisdn)) return false;

        Msisdn msisdn1 = (Msisdn) o;

        return getMsisdn().equals(msisdn1.getMsisdn());
    }

    @Override
    public int hashCode() {
        return getMsisdn().hashCode();
    }

    @Override
    public String toString() {
        return "Msisdn{" +
                "msisdn='" + msisdn + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}
