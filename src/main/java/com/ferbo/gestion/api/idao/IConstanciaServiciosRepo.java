package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.core.model.inventario.servicio.ConstanciaServicio;
import java.time.LocalDate;
import java.util.List;

public interface IConstanciaServiciosRepo 
{
    List<ConstanciaServicio> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente);
    ConstanciaServicio buscarPorFolio(String folio);
    ConstanciaServicio obtenerConstanciaDetalle(Integer id);
}
