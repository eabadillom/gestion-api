package com.ferbo.gestion.api.model;

import java.math.BigDecimal;

public class Saldo 
{
    private String numeroCliente = null;
    private String nombreCliente = null;
    private String emisorRFC = null;
    private String emisorNombre = null;

    private BigDecimal saldo = null;
    private BigDecimal enPlazo = null;
    private BigDecimal atraso8dias = null;
    private BigDecimal atraso15dias = null;
    private BigDecimal atraso30dias = null;
    private BigDecimal atraso60dias = null;
    private BigDecimal atrasoMayor60dias = null;

    public String getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(String numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getEmisorRFC() {
        return emisorRFC;
    }

    public void setEmisorRFC(String emisorRFC) {
        this.emisorRFC = emisorRFC;
    }

    public String getEmisorNombre() {
        return emisorNombre;
    }

    public void setEmisorNombre(String emisorNombre) {
        this.emisorNombre = emisorNombre;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getEnPlazo() {
        return enPlazo;
    }

    public void setEnPlazo(BigDecimal enPlazo) {
        this.enPlazo = enPlazo;
    }

    public BigDecimal getAtraso8dias() {
        return atraso8dias;
    }

    public void setAtraso8dias(BigDecimal atraso8dias) {
        this.atraso8dias = atraso8dias;
    }

    public BigDecimal getAtraso15dias() {
        return atraso15dias;
    }

    public void setAtraso15dias(BigDecimal atraso15dias) {
        this.atraso15dias = atraso15dias;
    }

    public BigDecimal getAtraso30dias() {
        return atraso30dias;
    }

    public void setAtraso30dias(BigDecimal atraso30dias) {
        this.atraso30dias = atraso30dias;
    }

    public BigDecimal getAtraso60dias() {
        return atraso60dias;
    }

    public void setAtraso60dias(BigDecimal atraso60dias) {
        this.atraso60dias = atraso60dias;
    }

    public BigDecimal getAtrasoMayor60dias() {
        return atrasoMayor60dias;
    }

    public void setAtrasoMayor60dias(BigDecimal astrasoMayor60dias) {
        this.atrasoMayor60dias = astrasoMayor60dias;
    }

    @Override
    public String toString() {
        return "com.ferbo.gestion.core.model.Saldo[\"numeroCliente\":\"" + numeroCliente + "\", \"nombreCliente\":\"" + nombreCliente
                + "\", \"emisorRFC\":\"" + emisorRFC + "\", \"emisorNombre\":\"" + emisorNombre + "\", \"saldo\":\""
                + saldo + "\", \"enPlazo\":\"" + enPlazo + "\", \"atraso8dias\":\"" + atraso8dias
                + "\", \"atraso15dias\":\"" + atraso15dias + "\", \"atraso30dias\":\"" + atraso30dias
                + "\", \"atraso60dias\":\"" + atraso60dias + "\", \"atrasoMayor60dias\":\"" + atrasoMayor60dias + "\"]";
    }
    
}
