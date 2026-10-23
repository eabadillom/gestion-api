package com.ferbo.gestion.api.idao;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.inventario.servicio.ConstanciaServicio;

public interface IConstanciaServiciosRepo extends GenericDAO<ConstanciaServicio, Integer>
{
    List<ConstanciaServicio> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente);
    ConstanciaServicio buscarPorFolio(String folio);
    ConstanciaServicio obtenerConstanciaDetalle(Integer id);
}
