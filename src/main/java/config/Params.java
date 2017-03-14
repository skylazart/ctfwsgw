package config;

import validate.Validatable;

/**
 * CTF Webservice Gateway
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 3/13/17.
 */
public class Params implements Validatable {
    private String listenAddress;
    private Integer listenPort;

    private String pcrfAddress;
    private Integer pcrfPort;

    public String getListenAddress() {
        return listenAddress;
    }

    public void setListenAddress(String listenAddress) {
        this.listenAddress = listenAddress;
    }

    public Integer getListenPort() {
        return listenPort;
    }

    public void setListenPort(Integer listenPort) {
        this.listenPort = listenPort;
    }

    public String getPcrfAddress() {
        return pcrfAddress;
    }

    public void setPcrfAddress(String pcrfAddress) {
        this.pcrfAddress = pcrfAddress;
    }

    public Integer getPcrfPort() {
        return pcrfPort;
    }

    public void setPcrfPort(Integer pcrfPort) {
        this.pcrfPort = pcrfPort;
    }

    @Override
    public String toString() {
        return "Params{" +
                "listenAddress='" + listenAddress + '\'' +
                ", listenPort=" + listenPort +
                ", pcrfAddress='" + pcrfAddress + '\'' +
                ", pcrfPort=" + pcrfPort +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Params)) return false;

        Params params = (Params) o;

        if (getListenAddress() != null ? !getListenAddress().equals(params.getListenAddress()) : params.getListenAddress() != null)
            return false;
        if (getListenPort() != null ? !getListenPort().equals(params.getListenPort()) : params.getListenPort() != null)
            return false;
        if (getPcrfAddress() != null ? !getPcrfAddress().equals(params.getPcrfAddress()) : params.getPcrfAddress() != null)
            return false;
        return getPcrfPort() != null ? getPcrfPort().equals(params.getPcrfPort()) : params.getPcrfPort() == null;
    }

    @Override
    public int hashCode() {
        int result = getListenAddress() != null ? getListenAddress().hashCode() : 0;
        result = 31 * result + (getListenPort() != null ? getListenPort().hashCode() : 0);
        result = 31 * result + (getPcrfAddress() != null ? getPcrfAddress().hashCode() : 0);
        result = 31 * result + (getPcrfPort() != null ? getPcrfPort().hashCode() : 0);
        return result;
    }

    @Override
    public boolean validate() {
        return false;
    }
}
