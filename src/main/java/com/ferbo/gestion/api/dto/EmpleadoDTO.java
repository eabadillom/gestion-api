package com.ferbo.gestion.api.dto;

public class EmpleadoDTO 
{
    private String numeroUsuario;
    private String nombreUsuario;
    private String primerApUsuario;
    private String segundoApUsuario;
    private String puesto;

    public EmpleadoDTO() {
    }

    public String getNumeroUsuario() {
        return numeroUsuario;
    }

    public void setNumeroUsuario(String numeroUsuario) {
        this.numeroUsuario = numeroUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPrimerApUsuario() {
        return primerApUsuario;
    }

    public void setPrimerApUsuario(String primerApUsuario) {
        this.primerApUsuario = primerApUsuario;
    }

    public String getSegundoApUsuario() {
        return segundoApUsuario;
    }

    public void setSegundoApUsuario(String segundoApUsuario) {
        this.segundoApUsuario = segundoApUsuario;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
    
}
