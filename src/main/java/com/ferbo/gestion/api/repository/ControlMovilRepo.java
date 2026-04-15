package com.ferbo.gestion.api.repository;

import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.model.ControlMovil;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.TypedQuery;

public class ControlMovilRepo extends BaseDAO<ControlMovil, Integer>
{

    public ControlMovilRepo(SpringTransactManager transactManager) {
        super(ControlMovil.class, transactManager);
    }
    
    public ControlMovil findByUser(String username) {
        return transactManager.executeRead(em -> {
            String query = "SELECT cm FROM ControlMovil cm WHERE cm.clienteSistema = :nombreUsuario AND cm.valido = true ORDER BY cm.expiracion DESC";
            
            TypedQuery<ControlMovil> entity = em.createQuery(query, ControlMovil.class);
            entity.setParameter("nombreUsuario", username);
            entity.setMaxResults(1);
            
            List<ControlMovil> results = entity.getResultList();

            return results.isEmpty() ? null : results.get(0);
        });
    }
    
    public ControlMovil findByToken(String token) {
        return transactManager.executeRead(em -> {
            ControlMovil model = em.createQuery("SELECT cm FROM ControlMovil cm WHERE cm.token = :token", ControlMovil.class)
                .setParameter("token", token)
                .getSingleResult();

            return model;
        });
    }
    
    public ControlMovil findByFechaExpiracion(LocalDate fecha) {
        return transactManager.executeRead(em -> {
            ControlMovil model = em.createQuery("SELECT cm FROM ControlMovil cm WHERE cm.expiracion = :fecha", ControlMovil.class)
                .setParameter("fecha", fecha)
                .getSingleResult();

            return model;
        });
    }
    
    public List<ControlMovil> findByFecha(LocalDate fecha) {
        return transactManager.executeRead(em -> {
            List<ControlMovil> list = em.createQuery("SELECT cm FROM ControlMovil cm WHERE cm.expiracion <= :fecha", ControlMovil.class)
                .setParameter("fecha", fecha)
                .getResultList();

            return list;
        });
    }
    
    public List<ControlMovil> findByPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        return transactManager.executeRead(em -> {
            List<ControlMovil> list = em.createQuery("SELECT cm FROM ControlMovil cm WHERE (cm.expiracion >= :fechaInicio and cm.expiracion <= :fechaFin)", ControlMovil.class)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();

            return list;
        });
    }
    
}
