package com.ferbo.gestion.api.components;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.business.AbstractConsultaConstancia;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.api.dto.TipoConstanciaDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.service.ConstanciaTraspasoSrv;
import org.springframework.stereotype.Component;

@Component
public class ConstanciaTraspaso extends AbstractConsultaConstancia
{
    private final ConstanciaTraspasoSrv constanciaTraspasoSrv;

    public ConstanciaTraspaso(ConstanciaTraspasoSrv constanciaTraspasoSrv) {
        this.constanciaTraspasoSrv = constanciaTraspasoSrv;
    }

    @Override
    public TipoConstanciaDTO tipo() {
        return TipoConstanciaDTO.TRASPASO;
    }
    
    @Override
    public List<ConstanciaDTO> consultarLista(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin) {
        return constanciaTraspasoSrv.consultarListas(fechaInicio, fechaFin, idCliente);
    }

    @Override
    public ConstanciaDTO buscarPorFolio(String folio) {
        return constanciaTraspasoSrv.buscarPorFolio(folio);
    }

    @Override
    protected ConstanciaDetalleDTO consultarDetalle(Integer id) throws GestionApiException {
        return constanciaTraspasoSrv.buscarConstanciaDetalle(id); 
    }
    
}
