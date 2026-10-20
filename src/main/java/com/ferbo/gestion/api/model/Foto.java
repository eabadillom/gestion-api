package com.ferbo.gestion.api.model;

public class Foto 
{
    private String nombre;
    private String mimeType;
    private String base64;

    public Foto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @Override
    public String toString() {
        return "FotoEnvioDTO[" + "nombre=" + nombre + ", mimeType=" + mimeType + ", base64=" + base64 + ']';
    }
    
    public long getTamBytes() 
    {
        if (this.base64 == null || this.base64.isEmpty()) {
            return 0;
        }

        String limpia = this.base64.contains(",") ? this.base64.split(",")[1] : this.base64;

        int padding = 0;
        if (limpia.endsWith("==")) {
            padding = 2;
        } else if (limpia.endsWith("=")) {
            padding = 1;
        }

        return (limpia.length() * 3L / 4L) - padding;
    }
    
}
