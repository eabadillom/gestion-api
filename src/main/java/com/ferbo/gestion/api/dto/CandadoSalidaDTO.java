package com.ferbo.gestion.api.dto;

public class CandadoSalidaDTO 
{
    private Integer id;
    private boolean habilitado;
    private Integer numSalidas;
    private boolean salidaTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Integer getNumSalidas() {
        return numSalidas;
    }

    public void setNumSalidas(Integer numSalidas) {
        this.numSalidas = numSalidas;
    }

    public boolean getSalidaTotal() {
        return salidaTotal;
    }

    public void setSalidaTotal(boolean salidaTotal) {
        this.salidaTotal = salidaTotal;
    }

    @Override
    public String toString() {
        return "CandadoSalidaDTO[" + "id=" + id + ", habilitado=" + habilitado + ", numSalidas=" + numSalidas + ", salidaTotal=" + salidaTotal + ']';
    }
    
}
