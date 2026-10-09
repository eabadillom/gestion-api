package com.ferbo.gestion.api.dto;

import java.math.BigDecimal;

public class ProductoDTO 
{
    private Integer id = null;
    private String descripcion = null;
    private String tarima = null;
    private Integer piezas = null;
    private String unidad = null;
    private BigDecimal cantidadCobro = null;
    private BigDecimal peso = null;
    private String folioEntrada = null;
    private String camara = null;
    private String origen = null;
    private String destino = null;

    public ProductoDTO() {
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

    public String getTarima() {
        return tarima;
    }

    public void setTarima(String tarima) {
        this.tarima = tarima;
    }

    public Integer getPiezas() {
        return piezas;
    }

    public void setPiezas(Integer piezas) {
        this.piezas = piezas;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public BigDecimal getCantidadCobro() {
        return cantidadCobro;
    }

    public void setCantidadCobro(BigDecimal cantidadCobro) {
        this.cantidadCobro = cantidadCobro;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getFolioEntrada() {
        return folioEntrada;
    }

    public void setFolioEntrada(String folioEntrada) {
        this.folioEntrada = folioEntrada;
    }

    public String getOrigen() {
        return origen;
    }

    public String getCamara() {
        return camara;
    }

    public void setCamara(String camara) {
        this.camara = camara;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    @Override
    public String toString() {
        return "ProductoDTO[" + "id: " + id + ", descripcion: " + descripcion + ", tarima: " + tarima + ", piezas: " + piezas + ", unidad: " + unidad + ", cantidadCobro: " + cantidadCobro + ", peso: " + peso + ", folioEntrada: " + folioEntrada + ", camara: " + camara + ", origen: " + origen + ", destino: " + destino + ']';
    }
    
}
