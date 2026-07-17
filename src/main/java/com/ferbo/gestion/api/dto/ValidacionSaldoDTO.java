package com.ferbo.gestion.api.dto;

import java.math.BigDecimal;

public class ValidacionSaldoDTO 
{
    private Boolean isHabilitarSalida = false;
    private BigDecimal saldoVencido = null;
    private String descripcion = null;

    public Boolean isIsHabilitarSalida() {
        return isHabilitarSalida;
    }

    public void setIsHabilitarSalida(Boolean isHabilitarSalida) {
        this.isHabilitarSalida = isHabilitarSalida;
    }

    public BigDecimal getSaldoVencido() {
        return saldoVencido;
    }

    public void setSaldoVencido(BigDecimal saldoVencido) {
        this.saldoVencido = saldoVencido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
