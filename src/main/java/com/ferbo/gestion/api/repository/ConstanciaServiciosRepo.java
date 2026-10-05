package com.ferbo.gestion.api.repository;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.idao.IConstanciaServiciosRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.inventario.servicio.ConstanciaServicio;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConstanciaServiciosRepo extends BaseDAO<ConstanciaServicio, Integer> implements IConstanciaServiciosRepo
{
    private static Logger log = LogManager.getLogger(ConstanciaTraspasoRepo.class);
    
    public ConstanciaServiciosRepo(SpringTransactManager transactManager){
        super(ConstanciaServicio.class, transactManager);
    }

    @Override
    public List<ConstanciaDTO> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) {
        return transactManager.executeRead(em -> {
            List<Object[]> filas = em.createQuery("SELECT cs.folio, cs.folioCliente, cs.fecha, cl.nombre "
                    + " FROM ConstanciaServicio cs "
                    + " INNER JOIN cs.cliente cl "
                    + " WHERE (:idCliente IS NULL OR cl.id = :idCliente) "
                    + " AND (cs.fecha BETWEEN :fhInicio and :fhFin) ", Object[].class)
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
    public ConstanciaDTO buscarPorFolio(String folio)
    {
        return transactManager.executeRead(em -> {
            ConstanciaServicio constanciaServicio = em.createQuery("SELECT c FROM ConstanciaServicio c WHERE c.folioCliente = :folioCliente", ConstanciaServicio.class)
                .setParameter("folioCliente", folio)
                .getSingleResult();
            
            ConstanciaDTO constanciaDTO = new ConstanciaDTO();
            constanciaDTO.setId(constanciaServicio.getFolio());
            constanciaDTO.setFolioCliente(constanciaServicio.getFolioCliente());
            constanciaDTO.setFecha(constanciaServicio.getFecha());
            constanciaDTO.setNombre(constanciaServicio.getCliente().getNombre());
            
            return constanciaDTO; 
        });
    }
    
}
