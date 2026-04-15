package com.ferbo.gestion.api.service;

import com.ferbo.gestion.api.dto.AsentamientoDTO;
import com.ferbo.gestion.core.idao.IAsentamientoDAO;
import com.ferbo.gestion.core.model.Asentamiento;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ferbo.gestion.api.mapper.IAsentamientoMapper;

@Service
public class AsentamientoSrv 
{
    private static Logger log = LogManager.getLogger(AsentamientoSrv.class);
    
    private final IAsentamientoDAO asentamientoDAO;
    
    @Autowired
    private IAsentamientoMapper iAsentamientoMapper;
    
    public AsentamientoSrv(IAsentamientoDAO asentamientoDAO){
        this.asentamientoDAO = asentamientoDAO;
    }
    
    public AsentamientoDTO buscarPorAsentamiento(Integer idPais, Integer idEstado, Integer idMunicipio, Integer idCiudad, Integer idAsentamiento) throws RuntimeException
    {
        Asentamiento asentamiento = asentamientoDAO.buscarPorAsentamiento(idPais, idEstado, idMunicipio, idCiudad, idAsentamiento);
        log.info(asentamiento.toString());
        AsentamientoDTO asentamientoDTO = this.convertirAsemtamiento(asentamiento);
        
        return asentamientoDTO;
    }
    
    public List<AsentamientoDTO> buscarPorCP(String cp) throws RuntimeException 
    {
        List<Asentamiento> listAsentamiento = asentamientoDAO.buscaPorCP(cp);
        
        for(Asentamiento aux : listAsentamiento){
            log.info(aux);
        }
        
        List<AsentamientoDTO> listAsentamientoDTO = listAsentamiento.stream()
                .map(this::convertirAsemtamiento).collect(Collectors.toList());
        return listAsentamientoDTO;
    }
    
    public AsentamientoDTO convertirAsemtamiento(Asentamiento incapacidad) 
    {
        return iAsentamientoMapper.toDTO(incapacidad);
    }
    
}
