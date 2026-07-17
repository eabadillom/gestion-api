package com.ferbo.gestion.api.response;

public class FailModelResponse 
{
    String mensajeError = null;
    Integer codigoError = null;
    
    public String getMensajeError() {
        return mensajeError;
    }
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
    public Integer getCodigoError() {
        return codigoError;
    }
    public void setCodigoError(Integer codigoError) {
        this.codigoError = codigoError;
    }
    
}
