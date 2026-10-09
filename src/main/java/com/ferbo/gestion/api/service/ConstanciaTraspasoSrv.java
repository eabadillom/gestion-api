package com.ferbo.gestion.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.idao.IConstanciaTraspasoRepo;
import com.ferbo.gestion.api.mapper.IConstanciaTraspasoMapper;
import com.ferbo.gestion.core.model.inventario.traspaso.ConstanciaTraspaso;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ConstanciaTraspasoSrv 
{
    private static Logger log = LogManager.getLogger(ConstanciaTraspasoSrv.class);
    
    private final IConstanciaTraspasoRepo constanciaTraspasoRepo;
    
    @Autowired
    private IConstanciaTraspasoMapper iConstanciaTraspasoMapper;

    public ConstanciaTraspasoSrv(IConstanciaTraspasoRepo constanciaTraspasoRepo) 
    {
        this.constanciaTraspasoRepo = constanciaTraspasoRepo;
    }
    
    public List<ConstanciaDTO> consultarListas(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) 
    {
        return this.constanciaTraspasoRepo.buscarPorParametros(fechaInicio, fechaFin, idCliente)
            .stream()
            .map(this::convertirConstanciaDTO)
            .collect(Collectors.toList());
    }
    
    public ConstanciaDTO buscarPorFolio(String folio) 
    {
        ConstanciaTraspaso constanciaTraspaso = constanciaTraspasoRepo.buscarPorFolio(folio);
        
        return this.convertirConstanciaDTO(constanciaTraspaso);
    }
    
    public ConstanciaDetalleDTO buscarConstanciaDetalle(Integer id) throws GestionApiException
    {
        ConstanciaTraspaso constanciaTraspaso = null;
        
        try{
            constanciaTraspaso = constanciaTraspasoRepo.obtenerConstanciaDetalle(id);
        }catch(NoResultException ex) {
            String resultado = String.format("No se encontró la constancia con el parametro dado");
            throw new GestionApiException(resultado);
        }
        
        return convertirDetalles(constanciaTraspaso);
    }
    
    private ConstanciaDTO convertirConstanciaDTO(ConstanciaTraspaso constanciaTraspaso) 
    {
        return iConstanciaTraspasoMapper.toConstanciaDTO(constanciaTraspaso);
    }
    
    private ConstanciaDetalleDTO convertirDetalles(ConstanciaTraspaso constanciaTraspaso) 
    {
        return iConstanciaTraspasoMapper.toConstanciaDetalleDTO(constanciaTraspaso);
    }
    
}
