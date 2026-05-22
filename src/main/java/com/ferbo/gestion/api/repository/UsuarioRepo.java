package com.ferbo.gestion.api.repository;

import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IUsuarioRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.sistema.Usuario;

public class UsuarioRepo extends BaseDAO<Usuario, Integer> implements IUsuarioRepo
{
    private static Logger log = LogManager.getLogger(UsuarioRepo.class);
    
    public UsuarioRepo(SpringTransactManager transactManager) {
        super(Usuario.class, transactManager);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorNumero(String numEmpleado) {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.numEmpleado = :numEmpleado", Usuario.class)
                .setParameter("numEmpleado", numEmpleado)
                .getResultStream()
                .findFirst(); 
        }); 
    }
    
}
