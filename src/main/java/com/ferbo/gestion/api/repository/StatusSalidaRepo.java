package com.ferbo.gestion.api.repository;

import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IStatusSalidaRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.inventario.salida.orden.StatusSalida;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatusSalidaRepo extends BaseDAO<StatusSalida, Integer> implements IStatusSalidaRepo
{
    private static Logger log = LogManager.getLogger(StatusSalidaRepo.class);

    public StatusSalidaRepo(SpringTransactManager transactManager) {
        super(StatusSalida.class, transactManager);
    }

    @Override
    public StatusSalida buscarPorClave(String clave) {
        return transactManager.executeRead(em -> {
            return em.createNamedQuery("StatusSalida.findByClave", StatusSalida.class)
                .setParameter("clave", clave)
                .getSingleResult();
        });
    }
    
}
