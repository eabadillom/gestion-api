package com.ferbo.gestion.api.components;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.business.AbstractConsultaConstancia;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.TipoConstanciaDTO;
import com.ferbo.gestion.api.service.ConstanciaSalidaSrv;
import org.springframework.stereotype.Component;

@Component
public class ConstanciaSalida extends AbstractConsultaConstancia 
{
    private final ConstanciaSalidaSrv constanciaSalidaSrv; 

    public ConstanciaSalida(ConstanciaSalidaSrv constanciaSalidaSrv) {
        this.constanciaSalidaSrv = constanciaSalidaSrv;
    }
    
    @Override
    public TipoConstanciaDTO tipo() {
        return TipoConstanciaDTO.SALIDA;
    }

    @Override
    public List<ConstanciaDTO> consultarLista(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin) {
        return constanciaSalidaSrv.consultarListas(fechaInicio, fechaFin, idCliente);
    }

    @Override
    public ConstanciaDTO buscarPorFolio(String folio) {
        return constanciaSalidaSrv.buscarPorFolio(folio);
    }
    
}
