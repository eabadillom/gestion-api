package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.core.model.inventario.traspaso.ConstanciaTraspaso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={IPartidaMapper.class, IServicioMapper.class})
public interface IConstanciaTraspasoMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "numero", target = "folioCliente")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "cliente.nombre", target = "nombre")
    ConstanciaDTO toConstanciaDTO(ConstanciaTraspaso constanciaTraspaso);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "numero", target = "folioCliente")
    @Mapping(source = "cliente.nombre", target = "nombre")
    @Mapping(source = "observacion", target = "observaciones")
    @Mapping(source = "partidas", target = "productos", qualifiedByName = "traspasoPartida")
    @Mapping(source = "servicios", target = "servicios", qualifiedByName = "traspasoServicio")
    ConstanciaDetalleDTO toConstanciaDetalleDTO(ConstanciaTraspaso constanciaTraspaso);
    
}
