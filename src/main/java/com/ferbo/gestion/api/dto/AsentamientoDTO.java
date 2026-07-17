package com.ferbo.gestion.api.dto;

import java.util.Objects;

public class AsentamientoDTO 
{
    private Integer id;
    private String descripcion;
    private String cp;
    private String entidadPostalDescripcion;
    private String tipoAsentamientoDescripcion;

    public AsentamientoDTO() {
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

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getEntidadPostalDescripcion() {
        return entidadPostalDescripcion;
    }

    public void setEntidadPostalDescripcion(String entidadPostalDescripcion) {
        this.entidadPostalDescripcion = entidadPostalDescripcion;
    }

    public String getTipoAsentamientoDescripcion() {
        return tipoAsentamientoDescripcion;
    }

    public void setTipoAsentamientoDescripcion(String tipoAsentamientoDescripcion) {
        this.tipoAsentamientoDescripcion = tipoAsentamientoDescripcion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AsentamientoDTO other = (AsentamientoDTO) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "AsentamientoDTO[" + "id=" + id + ", descripcion=" + descripcion + ", cp=" + cp + ", entidadPostalDescripcion=" + entidadPostalDescripcion + ", tipoAsentamientoDescripcion=" + tipoAsentamientoDescripcion + ']';
    }
    
}
