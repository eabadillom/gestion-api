package com.ferbo.gestion.api.service;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ferbo.gestion.api.idao.IPlantaRepo;
import com.ferbo.gestion.api.mapper.IPlantaMapper;
import com.ferbo.gestion.api.dto.PlantaDTO;
import com.ferbo.gestion.core.model.catalogo.almacen.Planta;

@Service
public class PlantaSrv 
{
    private static Logger log = LogManager.getLogger(PlantaSrv.class);
    
    private final IPlantaRepo plantaDAO;
    
    @Autowired
    private IPlantaMapper iPlantaMapper;
    
    public PlantaSrv (IPlantaRepo plantaDAO){
        this.plantaDAO = plantaDAO;
    }
    
    public List<PlantaDTO> obtenerPlantas(Integer idPlanta) 
    {
        return plantaDAO.buscarActivos(idPlanta).stream().map(this::convertirPlanta).collect(Collectors.toList());
    }
    
    private PlantaDTO convertirPlanta(Planta planta) 
    {
        return iPlantaMapper.toDTO(planta);
    }
    
}
