package com.ferbo.gestion.api.service;

import com.ferbo.gestion.api.dto.CandadoSalidaDTO;
import com.ferbo.gestion.api.mapper.ICandadoSalidaMapper;
import com.ferbo.gestion.core.dao.cliente.detalle.CandadoSalidaDAO;
import com.ferbo.gestion.core.model.cliente.detalle.CandadoSalida;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandadoSalidaSrv 
{
    private static Logger log = LogManager.getLogger(CandadoSalidaSrv.class);
    
    private final CandadoSalidaDAO candadoSalidaDAO;
    
    @Autowired
    private ICandadoSalidaMapper iCandadoSalidaMapper;
    
    public CandadoSalidaSrv(CandadoSalidaDAO candadoSalidaDAO){
        this.candadoSalidaDAO = candadoSalidaDAO;
    }
    
    public CandadoSalida obtenerCandodoPorCliente(Integer idCliente) {
        return candadoSalidaDAO.buscarPorCliente(idCliente);
    }
    
    public CandadoSalidaDTO obtenerCandodoDTO(Integer idCliente) {
        CandadoSalida candadoSalida = candadoSalidaDAO.buscarPorCliente(idCliente);
        return convertirCandadoSalida(candadoSalida);
    }
    
    public CandadoSalidaDTO actualizarCandado(CandadoSalidaDTO candadoSalidaDTO) {
        CandadoSalida candadoSalida = candadoSalidaDAO.buscarPorId(candadoSalidaDTO.getId()).orElseThrow(() -> new RuntimeException("No se encontro el candado de salida con ese identificador"));
        candadoSalida.setHabilitado(candadoSalidaDTO.isHabilitado());
        candadoSalida.setNumSalidas(candadoSalidaDTO.getNumSalidas());
        candadoSalida.setSalidaTotal(candadoSalidaDTO.getSalidaTotal());
        candadoSalidaDAO.actualizar(candadoSalida);
        
        return convertirCandadoSalida(candadoSalida);
    }
    
    private CandadoSalidaDTO convertirCandadoSalida(CandadoSalida candadoSalida) {
        return iCandadoSalidaMapper.toDTO(candadoSalida);
    }
    
}
