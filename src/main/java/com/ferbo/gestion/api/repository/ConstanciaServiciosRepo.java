package com.ferbo.gestion.api.repository;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IConstanciaServiciosRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.inventario.servicio.ConstanciaServicio;
import com.ferbo.gestion.core.model.inventario.servicio.ConstanciaServicioDetalle;
import com.ferbo.gestion.core.model.inventario.servicio.PartidaServicio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConstanciaServiciosRepo extends BaseDAO<ConstanciaServicio, Integer> implements IConstanciaServiciosRepo
{
    private static Logger log = LogManager.getLogger(ConstanciaTraspasoRepo.class);
    
    public ConstanciaServiciosRepo(SpringTransactManager transactManager){
        super(ConstanciaServicio.class, transactManager);
    }

    @Override
    public List<ConstanciaServicio> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT DISTINCT cs "
                    + " FROM ConstanciaServicio cs "
                    + " INNER JOIN cs.cliente cl "
                    + " WHERE (:idCliente IS NULL OR cl.id = :idCliente) "
                    + " AND (cs.fecha BETWEEN :fhInicio and :fhFin) ", ConstanciaServicio.class)
                .setParameter("idCliente", idCliente)
                .setParameter("fhInicio", fechaInicio)
                .setParameter("fhFin", fechaFin)
                .getResultList();
        });
    }

    @Override
    public ConstanciaServicio buscarPorFolio(String folio)
    {
        return transactManager.executeRead(em -> {
            return em.createNamedQuery("ConstanciaServicio.findByFolioCliente", ConstanciaServicio.class)
                .setParameter("folioCliente", folio)
                .getSingleResult();
        });
    }

    @Override
    public ConstanciaServicio obtenerConstanciaDetalle(Integer id) {
        return transactManager.executeRead(em -> {
            ConstanciaServicio constanciaServicio = em.createQuery("SELECT DISTINCT cs "
                    + " FROM ConstanciaServicio cs "
                    + " INNER JOIN cs.cliente cl "
                    + " WHERE cs.folio = :id ", ConstanciaServicio.class)
                .setParameter("id", id)
                .getSingleResult();
            
            if(!constanciaServicio.getServicios().isEmpty()) {
                for(ConstanciaServicioDetalle servicioDetalle : constanciaServicio.getServicios()) {
                    log.debug(servicioDetalle);
                }
            }
            
            if(!constanciaServicio.getPartidas().isEmpty()) {
                for(PartidaServicio partidaServicio : constanciaServicio.getPartidas()) {
                    log.debug(partidaServicio);
                }
            }
            
            return constanciaServicio;
        });
    }
    
}
