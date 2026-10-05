package com.ferbo.gestion.api.service;

import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.idao.IConstanciaServiciosRepo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ConstanciaServiciosSrv 
{
    private static Logger log = LogManager.getLogger(ConstanciaServiciosSrv.class);
    
    private final IConstanciaServiciosRepo constanciaServiciosRepo;

    public ConstanciaServiciosSrv(IConstanciaServiciosRepo constanciaServiciosRepo) {
        this.constanciaServiciosRepo = constanciaServiciosRepo;
    }
    
    public List<ConstanciaDTO> consultarListas(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) 
    {
        return this.constanciaServiciosRepo.buscarPorParametros(fechaInicio, fechaFin, idCliente);
    }
    
    public ConstanciaDTO buscarPorFolio(String folio) 
    {
        return this.constanciaServiciosRepo.buscarPorFolio(folio);
    }
    
}
