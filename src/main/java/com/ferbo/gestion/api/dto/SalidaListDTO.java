package com.ferbo.gestion.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SalidaListDTO 
{
    private Integer id;
    private String folio;
    private LocalDate fechaSalida;
    private LocalTime horaSalida;

    public SalidaListDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }
    
}
