package com.ferbo.gestion.api.repository;

import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IConstanciaSalidaRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.inventario.salida.ConstanciaSalida;
import com.ferbo.gestion.core.model.inventario.salida.ConstanciaSalidaServicio;
import com.ferbo.gestion.core.model.inventario.salida.DetalleConstanciaSalida;

public class ConstanciaSalidaRepo extends BaseDAO<ConstanciaSalida, Integer> implements IConstanciaSalidaRepo
{
    private static Logger log = LogManager.getLogger(ConstanciaSalidaRepo.class);
    
    public ConstanciaSalidaRepo(SpringTransactManager transactManager){
        super(ConstanciaSalida.class, transactManager);
    }

    @Override
    public List<ConstanciaSalida> buscarPorClientePlantaPeriodo(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente, Integer idPlanta, Integer idCanara) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ConstanciaSalida> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT DISTINCT cs "
                    + " FROM ConstanciaSalida cs "
                    + " INNER JOIN cs.detalles det"
                    + " INNER JOIN cs.cliente cl "
                    + " WHERE (:idCliente IS NULL OR cl.id = :idCliente) "
                    + " AND (cs.fecha BETWEEN :fhInicio and :fhFin) ", ConstanciaSalida.class)
                .setParameter("idCliente", idCliente)
                .setParameter("fhInicio", fechaInicio)
                .setParameter("fhFin", fechaFin)
                .getResultList();
        });
    }

    @Override
    public ConstanciaSalida buscarPorFolio(String folio) {
        return transactManager.executeRead(em -> {
            return em.createNamedQuery("ConstanciaSalida.findByNumero", ConstanciaSalida.class)
                .setParameter("numero", folio)
                .getSingleResult(); 
        });
    }
    
    @Override
    public ConstanciaSalida obtenerConstanciaDetalle(Integer id) {
        return transactManager.executeRead(em -> {
            ConstanciaSalida constanciaSalida = em.createQuery("SELECT DISTINCT cs "
                    + " FROM ConstanciaSalida cs "
                    + " INNER JOIN cs.cliente cl "
                    + " WHERE cs.id = :id ", ConstanciaSalida.class)
                .setParameter("id", id)
                .getSingleResult();
            
            for(DetalleConstanciaSalida detalleConstanciaSalida : constanciaSalida.getDetalles())
            {
                log.debug(detalleConstanciaSalida);
                log.debug(detalleConstanciaSalida.getPartida());
                log.debug(detalleConstanciaSalida.getPartida().getTarima());
            }
            
            if(!constanciaSalida.getServicios().isEmpty())
            {
                for(ConstanciaSalidaServicio constanciaSalidaServicio : constanciaSalida.getServicios())
                {
                    log.debug(constanciaSalidaServicio.toString());
                }
            }
            
            return constanciaSalida;
        });
    }
    
}
