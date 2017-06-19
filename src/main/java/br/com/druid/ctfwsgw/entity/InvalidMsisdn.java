package br.com.druid.ctfwsgw.entity;

/**
 * ctfwsgw
 * Created by Felipe Cerqueira - skylazart[at]gmail.com on 6/14/17.
 */
public class InvalidMsisdn extends Throwable {
    public InvalidMsisdn(String msg) {
        super(msg);
    }
}
