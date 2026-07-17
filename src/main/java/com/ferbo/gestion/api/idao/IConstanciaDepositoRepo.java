package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.inventario.entrada.ConstanciaDeposito;
import java.time.LocalDate;
import java.util.List;

public interface IConstanciaDepositoRepo extends GenericDAO<ConstanciaDeposito, Integer>
{
    List<ConstanciaDeposito> buscarPorKardex(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente, Integer idPlanta);
}
