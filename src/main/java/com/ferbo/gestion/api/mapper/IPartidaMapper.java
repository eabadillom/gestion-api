package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ProductoDTO;
import com.ferbo.gestion.core.model.inventario.entrada.Partida;
import com.ferbo.gestion.core.model.inventario.traspaso.TraspasoPartida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IPartidaMapper 
{
    @Named("depositoPartida")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "unidadProducto.producto.descripcion", target = "descripcion")
    @Mapping(source = "tarima.nombre", target = "tarima")
    @Mapping(source = "cantidadTotal", target = "piezas")
    @Mapping(source = "unidadProducto.unidadManejo.descripcion", target = "unidad")
    @Mapping(source = "pesoTotal", target = "peso")
    @Mapping(source = "camara.descripcion", target = "camara")
    ProductoDTO toDepositoPartidaDTO(Partida partida);
    
    @Named("traspasoPartida")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "constancia", target = "folioEntrada")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "partida.tarima.nombre", target = "tarima")
    @Mapping(source = "cantidad", target = "peso")
    @Mapping(source = "origen", target = "origen")
    @Mapping(source = "destino", target = "destino")
    ProductoDTO toTraspasoPartidaDTO(TraspasoPartida traspasoPartida);
    
}
