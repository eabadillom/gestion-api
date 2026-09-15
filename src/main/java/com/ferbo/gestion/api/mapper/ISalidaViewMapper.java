package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.SalidaViewDTO;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={IStatusSalidaMapper.class, ISalidaDetalleMapper.class})
public interface ISalidaViewMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "folio", target = "folio")
    @Mapping(source = "fechaSalida", target = "fechaSalida")
    @Mapping(source = "horaSalida", target = "horaSalida")
    @Mapping(source = "nombreTransportista", target = "nombreTransportista")
    @Mapping(source = "placasTransporte", target = "placasTransporte")
    @Mapping(source = "observaciones", target = "observaciones")
    @Mapping(source = "status", target = "statusSalida")
    @Mapping(source = "detalles", target = "salidaDetalles")
    SalidaViewDTO toDTO(Salida salida);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "folio", target = "folio")
    @Mapping(source = "fechaSalida", target = "fechaSalida")
    @Mapping(source = "horaSalida", target = "horaSalida")
    @Mapping(source = "nombreTransportista", target = "nombreTransportista")
    @Mapping(source = "placasTransporte", target = "placasTransporte")
    @Mapping(source = "observaciones", target = "observaciones")
    @Mapping(source = "statusSalida", target = "status")
    @Mapping(source = "salidaDetalles", target = "detalles")
    Salida toEntity(SalidaViewDTO salidaDTO);
    
}
