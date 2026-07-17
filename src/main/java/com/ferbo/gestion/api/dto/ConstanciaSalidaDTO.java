package com.ferbo.gestion.api.dto;

import java.time.LocalDate;

public class ConstanciaSalidaDTO 
{
    private Integer id;
    private LocalDate fecha;
    private String numero;

    public ConstanciaSalidaDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
}
