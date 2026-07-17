package com.ferbo.gestion.api.exception;

public class GestionApiException extends Exception 
{
    private static final long serialVersionUID = -5926570919082698332L;

    public GestionApiException() {
        super();
    }

    public GestionApiException(String message) {
        super(message);
    }

    public GestionApiException(Throwable cause) {
        super(cause);
    }

    public GestionApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
