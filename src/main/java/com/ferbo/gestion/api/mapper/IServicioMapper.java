package com.ferbo.gestion.api.mapper;

import com.ferbo.gestion.api.dto.ServicioDTO;
import com.ferbo.gestion.core.model.catalogo.servicio.Servicio;
import com.ferbo.gestion.core.model.inventario.entrada.ConstanciaDepositoDetalle;
import com.ferbo.gestion.core.model.inventario.salida.ConstanciaSalidaServicio;
import com.ferbo.gestion.core.model.inventario.traspaso.TraspasoServicio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IServicioMapper 
{
    @Named("depositoServicio")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "servicio.descripcion", target = "descripcion")
    ServicioDTO toDepositoServicioDTO(ConstanciaDepositoDetalle detalle);
    
    @Named("salidaServicio")
    @Mapping(source = "servicio.id", target = "id")
    @Mapping(source = "servicio.descripcion", target = "descripcion")
    ServicioDTO toSalidaServicioDTO(ConstanciaSalidaServicio constanciaSalidaServicio);
    
    @Named("traspasoServicio")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "servicio", target = "descripcion")
    @Mapping(source = "cantidad", target = "cantidad")
    @Mapping(source = "precio", target = "precio")
    ServicioDTO toTraspasoServicioDTO(TraspasoServicio servicio);
    
    @Named("servicio")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "servicio.descripcion", target = "cantidad")
    ServicioDTO toServicioDTO(Servicio servicio);
}
