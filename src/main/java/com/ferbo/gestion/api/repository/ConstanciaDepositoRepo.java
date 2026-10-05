package com.ferbo.gestion.api.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.idao.IConstanciaDepositoRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.inventario.entrada.ConstanciaDeposito;

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
    public List<ConstanciaDTO> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) {
        return transactManager.executeRead(em -> {
            List<Object[]> filas = em.createQuery("SELECT c.id, c.folioCliente, c.fechaIngreso, cl.nombre "
                    + " FROM ConstanciaDeposito c "
                    + " INNER JOIN c.cliente cl "
                    + " WHERE (:idCliente IS NULL OR cl.id = :idCliente) "
                    + " AND (c.fechaIngreso BETWEEN :fhInicio and :fhFin) ", Object[].class)
                .setParameter("idCliente", idCliente)
                .setParameter("fhInicio", fechaInicio)
                .setParameter("fhFin", fechaFin)
                .getResultList();
            
            List<ConstanciaDTO> resultado = new ArrayList<>();
            
            for (Object[] f : filas) {
                ConstanciaDTO dto = new ConstanciaDTO();
                dto.setId((Integer) f[0]);
                dto.setFolioCliente((String) f[1]);
                dto.setFecha((LocalDate) f[2]);
                dto.setNombre((String) f[3]);
                resultado.add(dto);
            }
            
            return resultado;
        });
    }

    @Override
    public ConstanciaDTO buscarFolio(String folio) 
    {
        log.info("Folio del cliente: {}", folio);
        return transactManager.executeRead(em -> {
            ConstanciaDeposito constanciaDeposito = em.createQuery("SELECT c FROM ConstanciaDeposito c WHERE c.folioCliente = :folioCliente", ConstanciaDeposito.class)
                .setParameter("folioCliente", folio)
                .getSingleResult();
            
            ConstanciaDTO constanciaDTO = new ConstanciaDTO();
            constanciaDTO.setId(constanciaDeposito.getId());
            constanciaDTO.setFolioCliente(constanciaDeposito.getFolioCliente());
            constanciaDTO.setFecha(constanciaDeposito.getFechaIngreso());
            constanciaDTO.setNombre(constanciaDeposito.getCliente().getNombre());
            
            return constanciaDTO; 
        });
    }
    
}
