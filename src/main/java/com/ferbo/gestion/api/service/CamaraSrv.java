package com.ferbo.gestion.api.service;

import java.util.List;
import java.util.stream.Collectors;
import com.ferbo.gestion.api.dto.CamaraDTO;
import com.ferbo.gestion.api.idao.ICamaraRepo;
import com.ferbo.gestion.api.mapper.ICamaraMapper;
import com.ferbo.gestion.core.model.catalogo.almacen.Camara;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class CamaraSrv 
{
    private static Logger log = LogManager.getLogger(CamaraSrv.class);
    
    private final ICamaraRepo camaraRepo;
    
    @Autowired
    private ICamaraMapper iCamaraMapper;
    
    public CamaraSrv(ICamaraRepo camaraRepo){
        this.camaraRepo = camaraRepo;
    }
    
    public List<CamaraDTO> buscarCamaras(Integer idPlanta)
    {
        return camaraRepo.buscarPorPlanta(idPlanta)
            .stream()
            .map(this::convertirCamara).collect(Collectors.toList());
    }
    
    private CamaraDTO convertirCamara(Camara camara){
        return iCamaraMapper.toDTO(camara);
    }
    
}
