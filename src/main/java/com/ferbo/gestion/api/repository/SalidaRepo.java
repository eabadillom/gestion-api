package com.ferbo.gestion.api.repository;

import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.ISalidaRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SalidaRepo extends BaseDAO<Salida, Integer> implements ISalidaRepo
{
    private static Logger log = LogManager.getLogger(SalidaRepo.class);

    public SalidaRepo(SpringTransactManager stm) {
        super(Salida.class, stm);
    }
    
    @Override
    public List<Salida> buscarPorPeriodoClientes(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin) {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT s FROM Salida s WHERE (:idCliente IS NULL OR s.cliente = :idCliente) AND s.fechaSalida BETWEEN :fechaInicio AND :fechaFin", Salida.class)
                .setParameter("idCliente", idCliente)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
        });
    }

    @Override
    public Salida buscarPorIdConDetalles(Integer idSalida) {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT s FROM Salida s LEFT JOIN FETCH s.detalles WHERE s.id = :idSalida", Salida.class)
                .setParameter("idSalida", idSalida)
                .getSingleResult();
        });
    }
    
}
