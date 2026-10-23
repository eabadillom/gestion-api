package com.ferbo.gestion.api.dto;

import java.util.ArrayList;
import java.util.List;

public class ConstanciaDetalleDTO extends ConstanciaDTO
{
    private String observaciones = null;
    private String nombreTransportista = null;
    private String placasTransporte = null;
    private String temperatura = null;
    private List<ProductoDTO> productos = new ArrayList();
    private List<ServicioDTO> servicios = new ArrayList();

    public ConstanciaDetalleDTO() {
        super();
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }

    public String getPlacasTransporte() {
        return placasTransporte;
    }

    public void setPlacasTransporte(String placasTransporte) {
        this.placasTransporte = placasTransporte;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public List<ProductoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoDTO> productos) {
        this.productos = productos;
    }

    public List<ServicioDTO> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServicioDTO> servicios) {
        this.servicios = servicios;
    }

    @Override
    public String toString() {
        return "ConstanciaDetalleDTO[" + "observaciones: " + observaciones + ", nombreTransportista: " + nombreTransportista + ", placasTransporte: " + placasTransporte + ", temperatura: " + temperatura + ']';
    }
    
}
