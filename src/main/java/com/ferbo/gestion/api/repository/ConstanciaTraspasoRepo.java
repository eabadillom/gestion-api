package com.ferbo.gestion.api.repository;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IConstanciaTraspasoRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.inventario.traspaso.ConstanciaTraspaso;
import com.ferbo.gestion.core.model.inventario.traspaso.TraspasoPartida;
import com.ferbo.gestion.core.model.inventario.traspaso.TraspasoServicio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConstanciaTraspasoRepo extends BaseDAO<ConstanciaTraspaso, Integer> implements IConstanciaTraspasoRepo
{
    private static Logger log = LogManager.getLogger(ConstanciaTraspasoRepo.class);
    
    public ConstanciaTraspasoRepo(SpringTransactManager transactManager){
        super(ConstanciaTraspaso.class, transactManager);
    }

    @Override
    public List<ConstanciaTraspaso> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) 
    {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT DISTINCT ct "
                    + " FROM ConstanciaTraspaso ct "
                    + " INNER JOIN ct.partidas dp "
                    + " INNER JOIN ct.cliente cl "
                    + " WHERE (:idCliente IS NULL OR cl.id = :idCliente) "
                    + " AND (ct.fecha BETWEEN :fhInicio and :fhFin) ", ConstanciaTraspaso.class)
                .setParameter("idCliente", idCliente)
                .setParameter("fhInicio", fechaInicio)
                .setParameter("fhFin", fechaFin)
                .getResultList();
        });
    }

    @Override
    public ConstanciaTraspaso buscarPorFolio(String folio) {
        return transactManager.executeRead(em -> {
            return em.createNamedQuery("ConstanciaTraspaso.findByNumero", ConstanciaTraspaso.class)
                .setParameter("numero", folio)
                .getSingleResult(); 
        });
    }

    @Override
    public ConstanciaTraspaso obtenerConstanciaDetalle(Integer id) {
        return transactManager.executeRead(em -> {
            ConstanciaTraspaso constanciaTraspaso = em.createQuery("SELECT DISTINCT ct "
                    + " FROM ConstanciaTraspaso ct "
                    + " INNER JOIN ct.partidas tp "
                    + " INNER JOIN ct.cliente cl "
                    + " WHERE ct.id = :id ", ConstanciaTraspaso.class)
                .setParameter("id", id)
                .getSingleResult();
            
            for(TraspasoPartida traspasoPartida : constanciaTraspaso.getPartidas()) 
            {
                log.debug(traspasoPartida);
                log.debug(traspasoPartida.getPartida());
                log.debug(traspasoPartida.getPartida().getTarima());
            }
            
            if(!constanciaTraspaso.getServicios().isEmpty())
            {
                for(TraspasoServicio traspasoServicio : constanciaTraspaso.getServicios()) 
                {
                    log.debug(traspasoServicio);
                }
            }
            
            return constanciaTraspaso;
        });
    }
    
}
