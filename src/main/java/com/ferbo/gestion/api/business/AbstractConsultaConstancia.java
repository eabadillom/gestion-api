package com.ferbo.gestion.api.business;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.idao.IConstanciaStrategyRepo;

public abstract class AbstractConsultaConstancia implements IConstanciaStrategyRepo 
{
    
    @Override
    public final List<ConstanciaDTO> listarConstancias(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin) 
    {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("fechaInicio y fechaFin son obligatorias");
        }
        
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("fechaFin no puede ser anterior a fechaInicio");
        }
        
        return consultarLista(idCliente, fechaInicio, fechaFin);
    }

    protected abstract List<ConstanciaDTO> consultarLista(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin);
    
}
