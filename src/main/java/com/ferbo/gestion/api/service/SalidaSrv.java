package com.ferbo.gestion.api.service;

import com.ferbo.gestion.api.dto.SalidaListDTO;
import com.ferbo.gestion.api.dto.SalidaViewDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.idao.ISalidaRepo;
import com.ferbo.gestion.core.model.inventario.salida.orden.Salida;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ferbo.gestion.api.mapper.ISalidaListMapper;
import com.ferbo.gestion.api.mapper.ISalidaViewMapper;
import com.ferbo.gestion.core.model.inventario.salida.orden.StatusSalida;

@Service
public class SalidaSrv 
{
    private static Logger log = LogManager.getLogger(SalidaSrv.class);
    
    private final ISalidaRepo salidaRepo;
    
    @Autowired
    private StatusSalidaSrv statusSalidaSrv;
    
    @Autowired
    private ISalidaListMapper iSalidaListMapper;

    @Autowired
    private ISalidaViewMapper iSalidaViewMapper;
    
    public SalidaSrv(ISalidaRepo salidaRepo) {
        this.salidaRepo = salidaRepo;
    }
    
    public List<SalidaListDTO> buscarPorPeriodoCliente(Integer idCliente, LocalDate fechaInicio, LocalDate fechaFin) throws RuntimeException {
        List<Salida> salidas = salidaRepo.buscarPorPeriodoClientes(idCliente, fechaInicio, fechaFin);
        return salidas.stream().map(this::convertirSalida).collect(Collectors.toList());
    }
    
    public SalidaViewDTO buscarPorId(Integer idCliente) {
        Salida salida = salidaRepo.buscarPorIdConDetalles(idCliente);
        return convertirSalidaDetalle(salida);
    }
    
    public void cancelarSalida(Integer idSalida) throws GestionApiException {
        Salida salida = salidaRepo.buscarPorId(idSalida).orElseThrow(() -> new RuntimeException("Error al encontrar la orden de salida"));
        log.info("Iniciando la cancelación de la salida: {}", salida.getFolio());
        
        StatusSalida statusAceptado = statusSalidaSrv.obtenerStAceptado();
        
        if(salida.getStatus().equals(statusAceptado)) {
            throw new GestionApiException("La salida no puede ser cancelada porque ya fue aceptada.");
        }
        
        salida.setStatus(statusSalidaSrv.obtenerStCancelado());
        salida.setFechaModificacion(LocalDate.now());
        
        salidaRepo.actualizar(salida);
        log.info("Terminando la cancelación de la salida: {}", salida.getFolio());
    }
    
    private SalidaListDTO convertirSalida(Salida salida) {
        return iSalidaListMapper.toDTO(salida);
    }
    
    private SalidaViewDTO convertirSalidaDetalle(Salida salida) {
        return iSalidaViewMapper.toDTO(salida);
    }
    
}
