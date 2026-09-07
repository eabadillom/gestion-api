package com.ferbo.gestion.api.dto;

import java.util.List;

public class SalidaViewDTO extends SalidaListDTO
{
    private String nombreTransportista;
    private String placasTransporte;
    private String observaciones;
    private StatusSalidaDTO statusSalida;
    private List<SalidaDetalleDTO> salidaDetalles;

    public SalidaViewDTO() {
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public StatusSalidaDTO getStatusSalida() {
        return statusSalida;
    }

    public void setStatusSalida(StatusSalidaDTO statusSalida) {
        this.statusSalida = statusSalida;
    }

    public List<SalidaDetalleDTO> getSalidaDetalles() {
        return salidaDetalles;
    }

    public void setSalidaDetalles(List<SalidaDetalleDTO> salidaDetalles) {
        this.salidaDetalles = salidaDetalles;
    }
    
}
