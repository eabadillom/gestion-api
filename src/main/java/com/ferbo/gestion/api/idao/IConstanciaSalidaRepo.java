package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.inventario.salida.ConstanciaSalida;
import java.time.LocalDate;
import java.util.List;

public interface IConstanciaSalidaRepo extends GenericDAO<ConstanciaSalida, Integer>
{
    List<ConstanciaSalida> buscarPorClientePlantaPeriodo(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente, Integer idPlanta, Integer idCanara);
}
