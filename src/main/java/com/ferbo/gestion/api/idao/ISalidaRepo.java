package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import java.time.LocalDate;
import java.util.List;

public interface ISalidaRepo extends GenericDAO<Salida, Integer>
{
    public List<Salida> buscarPorPeriodoClientes(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin);
    public Salida buscarPorIdConDetalles(Integer idSalida);
}
