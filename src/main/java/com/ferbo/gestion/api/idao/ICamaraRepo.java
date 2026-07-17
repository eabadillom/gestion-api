package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.catalogo.almacen.Camara;
import java.util.List;

public interface ICamaraRepo extends GenericDAO<Camara, Integer>
{
    List<Camara> buscarPorPlanta(Integer idPlanta);
}
