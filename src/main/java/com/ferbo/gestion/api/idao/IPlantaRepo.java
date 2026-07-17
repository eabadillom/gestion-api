package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.catalogo.almacen.Planta;
import java.util.List;

public interface IPlantaRepo extends GenericDAO<Planta, Integer>
{
    public List<Planta> buscarActivos(Integer idPlanta);
}
