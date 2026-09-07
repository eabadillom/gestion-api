package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.StatusSalidaDTO;
import com.ferbo.gestion.core.model.inventario.salida.orden.StatusSalida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IStatusSalidaMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    StatusSalidaDTO toDTO(StatusSalida salida);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    StatusSalida toEntity(StatusSalidaDTO salidaDTO);
}
