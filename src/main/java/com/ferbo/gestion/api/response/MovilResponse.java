package com.ferbo.gestion.api.response;

import java.time.OffsetDateTime;

public class MovilResponse 
{
    private Integer codigoError;
    private String tipoError;
    private String mensajeError;
    private OffsetDateTime tiempoError;

    public Integer getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(Integer codigoError) {
        this.codigoError = codigoError;
    }

    public String getTipoError() {
        return tipoError;
    }

    public void setTipoError(String tipoError) {
        this.tipoError = tipoError;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public OffsetDateTime getTiempoError() {
        return tiempoError;
    }

    public void setTiempoError(OffsetDateTime tiempoError) {
        this.tiempoError = tiempoError;
    }

    @Override
    public String toString() {
        return "MovilResponse[" + "codigoError=" + codigoError + ", tipoError=" + tipoError + ", mensajeError=" + mensajeError + ", tiempoError=" + tiempoError + ']';
    }
    
}
