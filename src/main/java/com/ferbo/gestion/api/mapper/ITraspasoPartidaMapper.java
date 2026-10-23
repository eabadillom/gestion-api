package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ProductoDTO;
import com.ferbo.gestion.core.model.inventario.traspaso.TraspasoPartida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ITraspasoPartidaMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "cantidad", target = "peso")
    @Mapping(source = "constancia", target = "folioEntrada")
    @Mapping(source = "origen", target = "origen")
    @Mapping(source = "destino", target = "destino")
    ProductoDTO toSalidaDetallesDTO(TraspasoPartida traspasoPartida);
    
}
