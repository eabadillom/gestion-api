package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ConstanciaDepositoDTO;
import com.ferbo.gestion.core.model.inventario.entrada.ConstanciaDeposito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IConstanciaDepositoMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fechaIngreso", target = "fechaIngreso")
    @Mapping(source = "folioCliente", target = "folioCliente")
    ConstanciaDepositoDTO toDTO(ConstanciaDeposito planta);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fechaIngreso", target = "fechaIngreso")
    @Mapping(source = "folioCliente", target = "folioCliente")
    ConstanciaDeposito toEntity(ConstanciaDepositoDTO clienteDTO);
    
}
