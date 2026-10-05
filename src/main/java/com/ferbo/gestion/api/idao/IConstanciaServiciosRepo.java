package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.api.dto.ConstanciaDTO;
import java.time.LocalDate;
import java.util.List;

public interface IConstanciaServiciosRepo 
{
    List<ConstanciaDTO> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente);
    ConstanciaDTO buscarPorFolio(String folio);
}
