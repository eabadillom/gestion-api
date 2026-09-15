package com.ferbo.gestion.api.service;

import com.ferbo.gestion.api.idao.IStatusSalidaRepo;
import com.ferbo.gestion.core.model.inventario.salida.orden.StatusSalida;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class StatusSalidaSrv 
{
    private static Logger log = LogManager.getLogger(StatusSalidaSrv.class);
    
    private final IStatusSalidaRepo statusSalidaRepo;
    
    private static final String CLAVE_ACEPTADO = "A";
    private static final String CLAVE_CANCELADO = "C";

    public StatusSalidaSrv(IStatusSalidaRepo statusSalidaRepo) {
        this.statusSalidaRepo = statusSalidaRepo;
    }
    
    public StatusSalida obtenerStAceptado() 
    {
        return statusSalidaRepo.buscarPorClave(CLAVE_ACEPTADO);
    }
    
    public StatusSalida obtenerStCancelado()
    {
        return statusSalidaRepo.buscarPorClave(CLAVE_CANCELADO);
    }
    
}
