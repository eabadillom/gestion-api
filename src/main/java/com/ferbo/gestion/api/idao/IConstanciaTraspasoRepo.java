package com.ferbo.gestion.api.idao;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.dto.ConstanciaDTO;

public interface IConstanciaTraspasoRepo 
{
    List<ConstanciaDTO> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente);
    ConstanciaDTO buscarPorFolio(String folio);
}
