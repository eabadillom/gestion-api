package com.ferbo.gestion.api.idao;

import com.ferbo.gestion.core.commons.dao.GenericDAO;
import com.ferbo.gestion.core.model.sistema.Usuario;
import java.util.Optional;

public interface IUsuarioRepo extends GenericDAO<Usuario, Integer> 
{
    public Optional<Usuario> buscarUsuarioPorNumero(String numero);
}
