package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.AsentamientoDTO;
import com.ferbo.gestion.core.model.Asentamiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IAsentamientoMapper 
{
    @Mapping(source = "asentamientoPK.id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "cp", target = "cp")
    @Mapping(source = "entidadPostal.descripcion", target = "entidadPostalDescripcion")
    @Mapping(source = "tipoAsentamiento.descripcion", target = "tipoAsentamientoDescripcion")
    AsentamientoDTO toDTO(Asentamiento asentamiento);
    
    @Mapping(source = "id", target = "asentamientoPK.id")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "cp", target = "cp")
    @Mapping(source = "entidadPostalDescripcion", target = "entidadPostal.descripcion")
    @Mapping(source = "tipoAsentamientoDescripcion", target = "tipoAsentamiento.descripcion")
    Asentamiento toEntity(AsentamientoDTO asentamientoDTO);
    
}
