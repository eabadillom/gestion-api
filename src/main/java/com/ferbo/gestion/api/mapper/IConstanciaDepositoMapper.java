package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDepositoDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.core.model.inventario.entrada.ConstanciaDeposito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={IPartidaMapper.class, IServicioMapper.class})
public interface IConstanciaDepositoMapper 
{
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fechaIngreso", target = "fechaIngreso")
    @Mapping(source = "folioCliente", target = "folioCliente")
    @Mapping(source = "cliente.nombre", target = "nombre")
    ConstanciaDepositoDTO toDTO(ConstanciaDeposito planta);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fechaIngreso", target = "fechaIngreso")
    @Mapping(source = "folioCliente", target = "folioCliente")
    @Mapping(source = "nombre", target = "cliente.nombre")
    ConstanciaDeposito toEntity(ConstanciaDepositoDTO clienteDTO);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fechaIngreso", target = "fecha")
    @Mapping(source = "folioCliente", target = "folioCliente")
    @Mapping(source = "cliente.nombre", target = "nombre")
    ConstanciaDTO toListDTO(ConstanciaDeposito planta);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fechaIngreso", target = "fecha")
    @Mapping(source = "folioCliente", target = "folioCliente")
    @Mapping(source = "cliente.nombre", target = "nombre")
    @Mapping(source = "nombreTransportista", target = "nombreTransportista")
    @Mapping(source = "observaciones", target = "observaciones")
    @Mapping(source = "placasTransporte", target = "placasTransporte")
    @Mapping(source = "temperatura", target = "temperatura")
    @Mapping(source = "partidas", target = "productos", qualifiedByName = "depositoPartida")
    @Mapping(source = "servicios", target = "servicios", qualifiedByName = "depositoServicio")
    ConstanciaDetalleDTO toConstanciaDetalleDTO(ConstanciaDeposito constanciaDeposito);
    
}
