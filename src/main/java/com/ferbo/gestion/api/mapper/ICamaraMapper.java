package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.CamaraDTO;
import com.ferbo.gestion.core.model.catalogo.almacen.Camara;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ICamaraMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    CamaraDTO toDTO(Camara camara);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    Camara toEntity(CamaraDTO clienteDTO);
}
