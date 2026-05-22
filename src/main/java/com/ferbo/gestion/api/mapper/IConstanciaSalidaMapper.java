package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ConstanciaSalidaDTO;
import com.ferbo.gestion.core.model.inventario.salida.ConstanciaSalida;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
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
    
}
