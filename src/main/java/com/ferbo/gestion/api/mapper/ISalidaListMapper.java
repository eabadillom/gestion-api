package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.SalidaListDTO;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ISalidaListMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "folio", target = "folio")
    @Mapping(source = "fechaSalida", target = "fechaSalida")
    @Mapping(source = "horaSalida", target = "horaSalida")
    SalidaListDTO toDTO(Salida salida);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "folio", target = "folio")
    @Mapping(source = "fechaSalida", target = "fechaSalida")
    @Mapping(source = "horaSalida", target = "horaSalida")
    Salida toEntity(SalidaListDTO salidaDTO);
}
