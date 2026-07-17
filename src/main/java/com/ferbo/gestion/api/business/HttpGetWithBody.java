package com.ferbo.gestion.api.business;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import java.net.URI;

public class HttpGetWithBody extends HttpEntityEnclosingRequestBase 
{
    public final static String METHOD_NAME = "GET";

    public HttpGetWithBody(String url) {
        super();
        setURI(URI.create(url));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
