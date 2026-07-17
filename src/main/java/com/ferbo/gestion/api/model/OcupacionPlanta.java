package com.ferbo.gestion.api.model;

import java.math.BigDecimal;

public class OcupacionPlanta 
{
    private String planta;
    private String camara;
    private BigDecimal tarima;

    public OcupacionPlanta() {
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public String getCamara() {
        return camara;
    }

    public void setCamara(String camara) {
        this.camara = camara;
    }

    public BigDecimal getTarima() {
        return tarima;
    }

    public void setTarima(BigDecimal tarima) {
        this.tarima = tarima;
    }

    @Override
    public String toString() {
        return "OcupacionPlanta[" + "planta=" + planta + ", camara=" + camara + ", tarima=" + tarima + ']';
    }
    
}
