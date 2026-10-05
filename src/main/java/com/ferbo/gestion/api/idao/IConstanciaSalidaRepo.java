package com.ferbo.gestion.api.idao;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.inventario.salida.ConstanciaSalida;

public interface IConstanciaSalidaRepo extends GenericDAO<ConstanciaSalida, Integer>
{
    List<ConstanciaSalida> buscarPorClientePlantaPeriodo(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente, Integer idPlanta, Integer idCanara);
    List<ConstanciaDTO> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente);
    ConstanciaDTO buscarPorFolio(String folio);
}
