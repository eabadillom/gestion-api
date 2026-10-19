package com.ferbo.gestion.api.response;

public class RespuestaResponse 
{
    private String mensaje;
    private boolean exito;
    
    public RespuestaResponse(String mensaje, boolean exito) {
        this.mensaje = mensaje;
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }
    
}
