package com.ferbo.gestion.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ferbo.gestion.api.dto.ControlMovilDTO;
import com.ferbo.gestion.api.model.ControlMovil;

@Mapper(componentModel = "spring")
public interface IControlMovilMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "expiracion", target = "expiracion")
    @Mapping(source = "valido", target = "valido")
    @Mapping(target = "sistema", ignore = true)
    ControlMovilDTO toDTO(ControlMovil entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "expiracion", target = "expiracion")
    @Mapping(source = "valido", target = "valido")
    @Mapping(target = "usuarioSistema", ignore = true)
    ControlMovil toEntity(ControlMovilDTO dto);
}
