package com.ferbo.gestion.api.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.idao.IConstanciaTraspasoRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.inventario.traspaso.ConstanciaTraspaso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConstanciaTraspasoRepo extends BaseDAO<ConstanciaTraspaso, Integer> implements IConstanciaTraspasoRepo
{
    private static Logger log = LogManager.getLogger(ConstanciaTraspasoRepo.class);
    
    public ConstanciaTraspasoRepo(SpringTransactManager transactManager){
        super(ConstanciaTraspaso.class, transactManager);
    }

    @Override
    public List<ConstanciaDTO> buscarPorParametros(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) 
    {
        return transactManager.executeRead(em -> {
            List<Object[]> filas = em.createQuery("SELECT ct.id, ct.numero, ct.fecha, cl.nombre "
                    + " FROM ConstanciaTraspaso ct "
                    + " INNER JOIN ct.partidas dp "
                    + " INNER JOIN ct.cliente cl "
                    + " WHERE (:idCliente IS NULL OR cl.id = :idCliente) "
                    + " AND (ct.fecha BETWEEN :fhInicio and :fhFin) ", Object[].class)
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
    public ConstanciaDTO buscarPorFolio(String folio) {
        return transactManager.executeRead(em -> {
            ConstanciaTraspaso constanciaTraspaso = em.createQuery("SELECT c FROM ConstanciaTraspaso c WHERE c.numero = :folioCliente", ConstanciaTraspaso.class)
                .setParameter("folioCliente", folio)
                .getSingleResult();
            
            ConstanciaDTO constanciaDTO = new ConstanciaDTO();
            constanciaDTO.setId(constanciaTraspaso.getId());
            constanciaDTO.setFolioCliente(constanciaTraspaso.getNumero());
            constanciaDTO.setFecha(constanciaTraspaso.getFecha());
            constanciaDTO.setNombre(constanciaTraspaso.getNombreCliente());
            
            return constanciaDTO; 
        });
    }
    
}
