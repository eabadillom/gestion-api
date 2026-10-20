package com.ferbo.gestion.api.repository;

import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IConstanciaDepositoRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.inventario.entrada.ConstanciaDeposito;
import com.ferbo.gestion.core.model.inventario.entrada.ConstanciaDepositoDetalle;
import com.ferbo.gestion.core.model.inventario.entrada.Partida;

public class ConstanciaDepositoRepo extends BaseDAO<ConstanciaDeposito, Integer> implements IConstanciaDepositoRepo
{
    private static Logger log = LogManager.getLogger(ConstanciaDepositoRepo.class);
    
    public ConstanciaDepositoRepo(SpringTransactManager transactManager){
        super(ConstanciaDeposito.class, transactManager);
    }

    @Override
    public List<ConstanciaDeposito> buscarPorKardex(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente, Integer idPlanta) 
    {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT DISTINCT (c) FROM ConstanciaDeposito c "
                    + " INNER JOIN c.partidas p "
                    + " WHERE (:idCliente IS NULL OR c.cliente.id = :idCliente) "
                    + " AND (:idPlanta IS NULL OR p.camara.planta.id = :idPlanta) "
                    + " AND (c.fechaIngreso BETWEEN :fhInicio and :fhFin)", ConstanciaDeposito.class)
                .setParameter("idCliente", idCliente)
                .setParameter("idPlanta", idPlanta)
                .setParameter("fhInicio", fechaInicio)
                .setParameter("fhFin", fechaFin)
                .getResultList();
        });
    }

    @Override
    public ConstanciaDeposito buscarPorFolio(String folio) 
    {
        return transactManager.executeRead(em -> {
            return em.createNamedQuery("ConstanciaDeposito.findByFolioCliente", ConstanciaDeposito.class)
                .setParameter("folioCliente", folio)
                .getSingleResult();
        });
    }

    @Override
    public List<ConstanciaDeposito> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT DISTINCT c "
                    + " FROM ConstanciaDeposito c "
                    + " INNER JOIN c.cliente cl "
                    + " WHERE (:idCliente IS NULL OR cl.id = :idCliente) "
                    + " AND (c.fechaIngreso BETWEEN :fhInicio and :fhFin) ", ConstanciaDeposito.class)
                .setParameter("idCliente", idCliente)
                .setParameter("fhInicio", fechaInicio)
                .setParameter("fhFin", fechaFin)
                .getResultList(); 
        });
    }

    @Override
    public ConstanciaDeposito obtenerConstanciaDetalle(Integer id) 
    {
        return transactManager.executeRead(em -> {
            ConstanciaDeposito constancia = em.createQuery("SELECT DISTINCT c "
                    + " FROM ConstanciaDeposito c "
                    + " INNER JOIN c.partidas pr "
                    + " INNER JOIN pr.tarima t "
                    + " INNER JOIN pr.unidadProducto up "
                    + " INNER JOIN up.producto p "
                    + " INNER JOIN up.unidadManejo um "
                    + " INNER JOIN c.cliente cl "
                    + " WHERE c.id = :id ", ConstanciaDeposito.class)
                .setParameter("id", id)
                .getSingleResult();
            
            for(Partida partida :constancia.getPartidas()) {
                log.debug(partida);
            }
            
            for(ConstanciaDepositoDetalle detalle : constancia.getServicios()) {
                log.debug(detalle);
            }
            
            return constancia;
        });
    }
    
}
