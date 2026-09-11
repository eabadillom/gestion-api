package com.ferbo.gestion.api.dto;

import java.math.BigDecimal;

public class SalidaDetalleDTO 
{
    private Integer id;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal peso;

    public SalidaDetalleDTO() {
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }
    
}
