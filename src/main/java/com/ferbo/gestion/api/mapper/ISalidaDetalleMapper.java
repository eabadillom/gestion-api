package com.ferbo.gestion.api.mapper;

import java.util.List;
import com.ferbo.gestion.api.dto.SalidaDetalleDTO;
import com.ferbo.gestion.core.model.inventario.salida.orden.SalidaDetalle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ISalidaDetalleMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "partida.unidadProducto.producto.descripcion", target = "descripcion")
    @Mapping(source = "cantidad", target = "cantidad")
    @Mapping(source = "peso", target = "peso")
    SalidaDetalleDTO toDTO(SalidaDetalle salidaDetalle);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "partida.unidadProducto.producto.descripcion")
    @Mapping(source = "cantidad", target = "cantidad")
    @Mapping(source = "peso", target = "peso")
    SalidaDetalle toDTO(SalidaDetalleDTO salidaDetalle);
    
    List<SalidaDetalleDTO> toDTOList(List<SalidaDetalle> entities);
    
    List<SalidaDetalle> toEntityList(List<SalidaDetalleDTO> dtos);
    
}
