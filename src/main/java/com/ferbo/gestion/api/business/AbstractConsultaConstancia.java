package com.ferbo.gestion.api.business;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.idao.IConstanciaStrategyRepo;

public abstract class AbstractConsultaConstancia implements IConstanciaStrategyRepo 
{
    
    @Override
    public final List<ConstanciaDTO> listarConstancias(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin) 
    {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Fecha inicio y fecha fin son obligatorias");
        }
        
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("Fecha fin no puede ser anterior a fecha fnicio");
        }
        
        return consultarLista(idCliente, fechaInicio, fechaFin);
    }

    protected abstract List<ConstanciaDTO> consultarLista(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin);
    
    @Override
    public final ConstanciaDetalleDTO buscarDetalle(Integer id) throws GestionApiException
    {
        if (id == null) {
            throw new IllegalArgumentException("Parametro es obligatorio");
        }
        
        return consultarDetalle(id);
    }

    protected abstract ConstanciaDetalleDTO consultarDetalle(Integer id) throws GestionApiException ;
    
}
