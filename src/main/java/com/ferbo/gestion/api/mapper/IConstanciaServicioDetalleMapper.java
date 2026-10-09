package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ServicioDTO;
import com.ferbo.gestion.core.model.inventario.servicio.ConstanciaServicioDetalle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IConstanciaServicioDetalleMapper 
{
    @Named("servicioConstancia")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "servicio.descripcion", target = "descripcion")
    @Mapping(source = "cantidad", target = "cantidad")
    ServicioDTO toTraspasoServicioDTO(ConstanciaServicioDetalle servicioDetalle);
    
}
