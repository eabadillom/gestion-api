package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ProductoDTO;
import com.ferbo.gestion.core.model.inventario.salida.DetalleConstanciaSalida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IDetalleConstanciaSalidaMapper 
{
    @Named("salidaPartidaDetalles")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "producto", target = "descripcion")
    @Mapping(source = "partida.tarima.nombre", target = "tarima")
    @Mapping(source = "cantidad", target = "piezas")
    @Mapping(source = "unidad", target = "unidad")
    @Mapping(source = "peso", target = "peso")
    @Mapping(source = "folioEntrada", target = "folioEntrada")
    @Mapping(source = "camara", target = "camara")
    ProductoDTO toSalidaDetallesDTO(DetalleConstanciaSalida detalleConstanciaSalida);
    
}
