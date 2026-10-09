package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.core.model.inventario.servicio.ConstanciaServicio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={IConstanciaServicioDetalleMapper.class})
public interface IConstanciaServicioMapper 
{
    @Mapping(source = "folio", target = "id")
    @Mapping(source = "folioCliente", target = "folioCliente")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "cliente.nombre", target = "nombre")
    ConstanciaDTO toConstanciaDTO(ConstanciaServicio constanciaServicio);
    
    @Mapping(source = "folio", target = "id")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "folioCliente", target = "folioCliente")
    @Mapping(source = "cliente.nombre", target = "nombre")
    @Mapping(source = "observaciones", target = "observaciones")
    @Mapping(source = "placasTransporte", target = "placasTransporte")
    @Mapping(source = "nombreTransportista", target = "nombreTransportista")
    @Mapping(source = "servicios", target = "servicios", qualifiedByName = "servicioConstancia")
    ConstanciaDetalleDTO toConstanciaDetalleDTO(ConstanciaServicio constanciaServicio);
}
