package com.ferbo.gestion.api.dto;

import java.time.LocalDate;

public class ConstanciaDepositoDTO 
{
    private Integer id;
    private LocalDate fechaIngreso;
    private String folioCliente;

    public ConstanciaDepositoDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFolioCliente() {
        return folioCliente;
    }

    public void setFolioCliente(String folioCliente) {
        this.folioCliente = folioCliente;
    }
    
}
