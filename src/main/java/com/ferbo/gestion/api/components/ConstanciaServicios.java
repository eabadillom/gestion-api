package com.ferbo.gestion.api.components;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.business.AbstractConsultaConstancia;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.api.dto.TipoConstanciaDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.service.ConstanciaServiciosSrv;
import org.springframework.stereotype.Component;

@Component
public class ConstanciaServicios extends AbstractConsultaConstancia
{
    private final ConstanciaServiciosSrv constanciaServiciosSrv; 

    public ConstanciaServicios(ConstanciaServiciosSrv constanciaServiciosSrv) {
        this.constanciaServiciosSrv = constanciaServiciosSrv;
    }
    
    @Override
    public TipoConstanciaDTO tipo() {
        return TipoConstanciaDTO.SERVICIO;
    }

    @Override
    public List<ConstanciaDTO> consultarLista(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin) {
        return constanciaServiciosSrv.consultarListas(fechaInicio, fechaFin, idCliente);
    }

    @Override
    public ConstanciaDTO buscarPorFolio(String folio) {
        return constanciaServiciosSrv.buscarPorFolio(folio);
    }

    @Override
    protected ConstanciaDetalleDTO consultarDetalle(Integer id) throws GestionApiException {
        return constanciaServiciosSrv.buscarConstanciaDetalle(id); 
    }
    
}
