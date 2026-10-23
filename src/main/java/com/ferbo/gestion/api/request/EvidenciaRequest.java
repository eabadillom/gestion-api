package com.ferbo.gestion.api.request;

import com.ferbo.gestion.api.dto.TipoConstanciaDTO;
import com.ferbo.gestion.api.model.Foto;
import java.util.List;

public class EvidenciaRequest 
{
    private TipoConstanciaDTO tipo;
    private Integer idConstancia;
    private List<Foto> fotos;

    public TipoConstanciaDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoConstanciaDTO tipo) {
        this.tipo = tipo;
    }

    public Integer getIdConstancia() {
        return idConstancia;
    }

    public void setIdConstancia(Integer idConstancia) {
        this.idConstancia = idConstancia;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }
    
}
