package com.ferbo.gestion.api.dto;

public class CamaraDTO 
{
    private Integer id;
    private String descripcion;

    public CamaraDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
