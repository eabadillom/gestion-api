package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.cliente.Cliente;
import java.util.List;

public interface IClienteRepo extends GenericDAO<Cliente, Integer> 
{
    public List<Cliente> buscarClientesActivos();
}
