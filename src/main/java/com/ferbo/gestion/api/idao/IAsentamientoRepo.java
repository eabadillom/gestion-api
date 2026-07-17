package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.domicilio.Asentamiento;
import java.util.List;

public interface IAsentamientoRepo extends GenericDAO<Asentamiento, Integer> 
{
    public Asentamiento buscarPorAsentamiento(Integer idPais, Integer idEstado, Integer idMunicipio, Integer idCiudad, Integer idAsentamiento);
    public Asentamiento buscaPorParametros(Integer idPais, Integer idEstado, Integer idMunicipio, Integer idCiudad, Integer idAsentamiento, Integer idTipoAsntmnto, Integer idEntidadPostal);
    public List<Asentamiento> buscaPorCP(String codigo);
}
