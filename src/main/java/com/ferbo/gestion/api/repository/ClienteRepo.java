package com.ferbo.gestion.api.repository;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IClienteRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.cliente.Cliente;
import com.ferbo.gestion.core.model.cliente.contacto.ClienteContacto;
import com.ferbo.gestion.core.model.cliente.contacto.MedioContacto;

public class ClienteRepo extends BaseDAO<Cliente, Integer> implements IClienteRepo
{
    private static Logger log = LogManager.getLogger(ClienteRepo.class);
    
    public ClienteRepo(SpringTransactManager transactManager){
        super(Cliente.class, transactManager);
    }

    @Override
    public List<Cliente> buscarClientesActivos() 
    {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT c FROM Cliente c WHERE c.habilitado = TRUE ORDER BY c.nombre", Cliente.class)
                .getResultList();
        });
    }

    @Override
    public Cliente buscarClientePorId(Integer id) {
        return transactManager.executeRead(em -> {
            Cliente cliente = em.createQuery("SELECT c FROM Cliente c WHERE c.id = :id", Cliente.class)
                .setParameter("id", id)
                .getSingleResult();
            
            for(ClienteContacto clienteContacto : cliente.getClienteContactoList()) {
                log.debug(clienteContacto.toString());
                log.debug(clienteContacto.getContacto().toString());
                List<MedioContacto> listMedioContacto = clienteContacto.getContacto().getMediosContacto();
                for(MedioContacto medioContacto :listMedioContacto){
                    log.debug(medioContacto.getContacto().toString());
                    log.debug(medioContacto.getMail().toString());
                }
            }
            
            return cliente;
        });
    }
    
}
