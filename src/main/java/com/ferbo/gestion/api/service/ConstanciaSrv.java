package com.ferbo.gestion.api.service;

import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.TipoConstanciaDTO;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ferbo.gestion.api.idao.IConstanciaStrategyRepo;
import java.util.Collections;
import javax.persistence.NoResultException;

@Service
public class ConstanciaSrv 
{
    private static Logger log = LogManager.getLogger(ConstanciaSrv.class);
    
    private final Map<TipoConstanciaDTO, IConstanciaStrategyRepo> estrategias = new EnumMap<>(TipoConstanciaDTO.class);

    public ConstanciaSrv(List<IConstanciaStrategyRepo> lista) 
    {
        for (IConstanciaStrategyRepo e : lista) {
            if (estrategias.put(e.tipo(), e) != null) {
                throw new IllegalStateException("Estrategia duplicada para " + e.tipo());
            }
        }
        
        for (TipoConstanciaDTO t : TipoConstanciaDTO.values()) {
            if (!estrategias.containsKey(t)) {
                log.warn("Todavía no hay estrategia de consulta para {}", t);
                throw new IllegalStateException("Falta estrategia de consulta para " + t);
            }
        }
    }

    public List<ConstanciaDTO> listarConstancias(TipoConstanciaDTO tipo, Integer idCliente, LocalDate inicio, LocalDate fin, String folioCliente) 
    {
        boolean hayFolio = folioCliente != null && !folioCliente.trim().isEmpty();
        boolean hayFechas = inicio != null || fin != null;
        
        if (!hayFolio && !hayFechas) {
            throw new IllegalArgumentException("Indique un folio de cliente o fecha de inicio y fecha de fin");
        }
        
        IConstanciaStrategyRepo e = estrategias.get(tipo);
        
        if (e == null) {
            throw new IllegalArgumentException("Consulta no implementada para " + tipo);
        }
        
        if (hayFolio) {
            log.info("Buscando la constancia por el parametro de {}.", folioCliente);
            try {
                ConstanciaDTO dto = e.buscarPorFolio(folioCliente.trim());
                return dto == null ? Collections.<ConstanciaDTO>emptyList() : Collections.singletonList(dto);
            } catch (NoResultException ex) {
                log.info("No se encontró ninguna constancia con el folio {}.", folioCliente);
                String resultado = String.format("No se encontró ninguna constancia con el folio %s", folioCliente);
                throw new IllegalArgumentException(resultado);
            }
        } 
        
        log.info("Buscando las constancias por los parametros de idCliente: {}, fecha de inicio: {} y fecha fin: {}", idCliente, inicio, fin);
        return e.listarConstancias(idCliente, inicio, fin);
    }
    
}
