package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ClienteDTO;
import com.ferbo.gestion.core.model.cliente.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IClienteMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    ClienteDTO toDTO(Cliente planta);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "nombre", target = "nombre")
    Cliente toEntity(ClienteDTO clienteDTO);
    
}
