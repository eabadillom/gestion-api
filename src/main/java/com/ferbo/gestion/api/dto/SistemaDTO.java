package com.ferbo.gestion.api.dto;

public class SistemaDTO 
{
    private String nombre;
    private String password;
    private String rol;

    public SistemaDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "SistemaDTO[" + "nombre=" + nombre + ", password=" + password + ", rol=" + rol + ']';
    }
    
}
