package com.ferbo.gestion.api.idao;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.core.model.inventario.traspaso.ConstanciaTraspaso;

public interface IConstanciaTraspasoRepo 
{
    List<ConstanciaTraspaso> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente);
    ConstanciaTraspaso buscarPorFolio(String folio);
    ConstanciaTraspaso obtenerConstanciaDetalle(Integer id);
}
