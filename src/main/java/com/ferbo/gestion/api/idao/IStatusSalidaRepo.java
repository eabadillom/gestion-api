package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.inventario.salida.orden.StatusSalida;

public interface IStatusSalidaRepo extends GenericDAO<StatusSalida, Integer>
{
    public StatusSalida buscarPorClave(String clave);
}
