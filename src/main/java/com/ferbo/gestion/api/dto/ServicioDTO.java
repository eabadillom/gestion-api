package com.ferbo.gestion.api.dto;

import java.math.BigDecimal;

public class ServicioDTO 
{
    private Integer id = null;
    private String descripcion = null;
    private BigDecimal cantidad = null;
    private BigDecimal precio = null;
    private BigDecimal subtotal = null;

    public ServicioDTO() {
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

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() 
    {
        return "ServicioDTO[" + "id: " + id + ", descripcion: " + descripcion + ", cantidad: " + cantidad + ", precio: " + precio + ", subtotal: " + subtotal + ']';
    }
    
}
