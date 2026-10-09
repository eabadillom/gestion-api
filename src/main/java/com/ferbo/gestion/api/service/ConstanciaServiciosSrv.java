package com.ferbo.gestion.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.idao.IConstanciaServiciosRepo;
import com.ferbo.gestion.api.mapper.IConstanciaServicioMapper;
import com.ferbo.gestion.core.model.inventario.servicio.ConstanciaServicio;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ConstanciaServiciosSrv 
{
    private static Logger log = LogManager.getLogger(ConstanciaServiciosSrv.class);
    
    private final IConstanciaServiciosRepo constanciaServiciosRepo;
    
    @Autowired
    private IConstanciaServicioMapper iConstanciaServicioMapper;

    public ConstanciaServiciosSrv(IConstanciaServiciosRepo constanciaServiciosRepo) {
        this.constanciaServiciosRepo = constanciaServiciosRepo;
    }
    
    public List<ConstanciaDTO> consultarListas(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) 
    {
        List<ConstanciaServicio> listConstanciasServicios = this.constanciaServiciosRepo.buscarPorParametros(fechaInicio, fechaFin, idCliente);
        List<ConstanciaDTO> resultado = new ArrayList<>();

        for (ConstanciaServicio constanciaServicio : listConstanciasServicios) {
            ConstanciaDTO dto = new ConstanciaDTO();
            dto.setId(constanciaServicio.getFolio());
            dto.setFolioCliente(constanciaServicio.getFolioCliente());
            dto.setFecha(constanciaServicio.getFecha());
            dto.setNombre(constanciaServicio.getCliente().getNombre());
            resultado.add(dto);
        }
            
        return this.constanciaServiciosRepo.buscarPorParametros(fechaInicio, fechaFin, idCliente)
            .stream()
            .map(this::convertirConstanciaDTO)
            .collect(Collectors.toList());
    }
    
    public ConstanciaDTO buscarPorFolio(String folio) 
    {
        ConstanciaServicio constanciaServicio = this.constanciaServiciosRepo.buscarPorFolio(folio);
        
        return this.convertirConstanciaDTO(constanciaServicio);
    }
    
    public ConstanciaDetalleDTO buscarConstanciaDetalle(Integer id) throws GestionApiException
    {
        ConstanciaServicio constanciaServicio = null;
        
        try{
            constanciaServicio = this.constanciaServiciosRepo.obtenerConstanciaDetalle(id);
        }catch(NoResultException ex) {
            String resultado = String.format("No se encontró la constancia con el parametro dado");
            throw new GestionApiException(resultado);
        }
        
        return convertirDetalles(constanciaServicio);
    }
    
    private ConstanciaDTO convertirConstanciaDTO(ConstanciaServicio constanciaServicio) 
    {
        return iConstanciaServicioMapper.toConstanciaDTO(constanciaServicio);
    }
    
    private ConstanciaDetalleDTO convertirDetalles(ConstanciaServicio constanciaServicio) 
    {
        return iConstanciaServicioMapper.toConstanciaDetalleDTO(constanciaServicio);
    }
    
}
