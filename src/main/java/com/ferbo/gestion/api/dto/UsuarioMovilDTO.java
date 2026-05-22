package com.ferbo.gestion.api.dto;

public class UsuarioMovilDTO 
{
    private String numeroUsuario;
    private String nombreUsuario;
    private String primerApUsuario;
    private String segundoApUsuario;
    private String puesto;
    private int perfil;
    private String token;
    private String refreshToken;

    public UsuarioMovilDTO() {
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

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "UsuarioMovilDTO[" + "numeroUsuario=" + numeroUsuario + ", nombreUsuario=" + nombreUsuario + ", primerApUsuario=" + primerApUsuario + ", segundoApUsuario=" + segundoApUsuario + ", puesto=" + puesto + ", token=" + token + ", refreshToken=" + refreshToken + ']';
    }
    
}
