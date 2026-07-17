package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.CandadoSalidaDTO;
import com.ferbo.gestion.core.model.cliente.detalle.CandadoSalida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ICandadoSalidaMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "habilitado", target = "habilitado")
    @Mapping(source = "numSalidas", target = "numSalidas")
    @Mapping(source = "salidaTotal", target = "salidaTotal")
    CandadoSalidaDTO toDTO(CandadoSalida candadoSalida);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "habilitado", target = "habilitado")
    @Mapping(source = "numSalidas", target = "numSalidas")
    @Mapping(source = "salidaTotal", target = "salidaTotal")
    CandadoSalida toEntity(CandadoSalidaDTO candadoSalidaDTO);
}
