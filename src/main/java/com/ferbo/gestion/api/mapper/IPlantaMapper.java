package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.PlantaDTO;
import com.ferbo.gestion.core.model.catalogo.almacen.Planta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IPlantaMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    PlantaDTO toDTO(Planta planta);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    Planta toEntity(PlantaDTO clienteDTO);
    
}
