package com.ferbo.gestion.api.components;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.business.AbstractConsultaConstancia;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.api.dto.TipoConstanciaDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.service.ConstanciaDepositoSrv;
import org.springframework.stereotype.Component;

@Component
public class ConstanciaDeposito extends AbstractConsultaConstancia
{
    private final ConstanciaDepositoSrv constanciaDepositoSrv;

    public ConstanciaDeposito(ConstanciaDepositoSrv srv) {
        this.constanciaDepositoSrv = srv;
    }

    @Override
    public TipoConstanciaDTO tipo() {
        return TipoConstanciaDTO.DEPOSITO;
    }

    @Override
    public List<ConstanciaDTO> consultarLista(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin) {
        return constanciaDepositoSrv.consultarLista(fechaInicio, fechaFin, idCliente);
    }

    @Override
    public ConstanciaDTO buscarPorFolio(String folio) {
        return constanciaDepositoSrv.buscarPorFolio(folio);
    }

    @Override
    protected ConstanciaDetalleDTO consultarDetalle(Integer id) throws GestionApiException {
        return constanciaDepositoSrv.buscarConstanciaDetalle(id); 
    }

}
