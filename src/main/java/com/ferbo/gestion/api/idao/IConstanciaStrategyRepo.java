package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.api.dto.TipoConstanciaDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import java.time.LocalDate;
import java.util.List;

public interface IConstanciaStrategyRepo 
{
    TipoConstanciaDTO tipo();
    List<ConstanciaDTO> listarConstancias(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin);
    ConstanciaDTO buscarPorFolio(String folio);
    ConstanciaDetalleDTO buscarDetalle(Integer id) throws GestionApiException ;
}
