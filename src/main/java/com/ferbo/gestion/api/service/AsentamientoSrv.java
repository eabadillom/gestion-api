package com.ferbo.gestion.api.service;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ferbo.gestion.api.dto.AsentamientoDTO;
import com.ferbo.gestion.api.idao.IAsentamientoRepo;
import com.ferbo.gestion.api.mapper.IAsentamientoMapper;
import com.ferbo.gestion.core.model.domicilio.Asentamiento;

@Service
public class AsentamientoSrv 
{
    private static Logger log = LogManager.getLogger(AsentamientoSrv.class);
    
    private final IAsentamientoRepo asentamientoRepo;
    
    @Autowired
    private IAsentamientoMapper iAsentamientoMapper;
    
    public AsentamientoSrv(IAsentamientoRepo asentamientoRepo){
        this.asentamientoRepo = asentamientoRepo;
    }
    
    public AsentamientoDTO buscarPorAsentamiento(Integer idPais, Integer idEstado, Integer idMunicipio, Integer idCiudad, Integer idAsentamiento) throws RuntimeException
    {
        Asentamiento asentamiento = asentamientoRepo.buscarPorAsentamiento(idPais, idEstado, idMunicipio, idCiudad, idAsentamiento);
        log.info(asentamiento.toString());
        
        return this.convertirAsemtamiento(asentamiento);
    }
    
    public List<AsentamientoDTO> buscarPorCP(String cp) throws RuntimeException 
    {
        return asentamientoRepo.buscaPorCP(cp).stream().map(this::convertirAsemtamiento).collect(Collectors.toList());
    }
    
    private AsentamientoDTO convertirAsemtamiento(Asentamiento asentamiento) 
    {
        return iAsentamientoMapper.toDTO(asentamiento);
    }
    
}
