package com.ferbo.gestion.api.dto;

import java.time.LocalDate;

public class ConstanciaDTO 
{
    private Integer id;
    private String folioCliente;
    private LocalDate fecha;
    private String nombre;

    public ConstanciaDTO() {
    }

    public ConstanciaDTO(Integer id, String folioCliente, LocalDate fecha, String nombre) {
        this.id = id;
        this.folioCliente = folioCliente;
        this.fecha = fecha;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFolioCliente() {
        return folioCliente;
    }

    public void setFolioCliente(String folioCliente) {
        this.folioCliente = folioCliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "ConstanciaDTO[" + "id=" + id + ", folioCliente=" + folioCliente + ", fecha=" + fecha + ", nombre=" + nombre + ']';
    }
    
}
