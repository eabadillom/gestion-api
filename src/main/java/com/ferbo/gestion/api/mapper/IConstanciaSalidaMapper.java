package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.api.dto.ConstanciaSalidaDTO;
import com.ferbo.gestion.core.model.inventario.salida.ConstanciaSalida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={IDetalleConstanciaSalidaMapper.class, IServicioMapper.class})
public interface IConstanciaSalidaMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "numero", target = "numero")
    ConstanciaSalidaDTO toDTO(ConstanciaSalida planta);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "numero", target = "numero")
    ConstanciaSalida toEntity(ConstanciaSalidaDTO clienteDTO);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "numero", target = "folioCliente")
    @Mapping(source = "clientee.nombre", target = "nombre")
    ConstanciaDTO toConstanciaDTO(ConstanciaSalida planta);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fecha", target = "fecha")
    @Mapping(source = "numero", target = "folioCliente")
    @Mapping(source = "clientee.nombre", target = "nombre")
    @Mapping(source = "nombreTransportista", target = "nombreTransportista")
    @Mapping(source = "observaciones", target = "observaciones")
    @Mapping(source = "placasTransporte", target = "placasTransporte")  
    @Mapping(source = "detalles", target = "productos", qualifiedByName = "salidaPartidaDetalles")
    @Mapping(source = "servicios", target = "servicios", qualifiedByName = "salidaServicio")
    ConstanciaDetalleDTO toConstanciaDetalleDTO(ConstanciaSalida constanciaSalida);
    
}
