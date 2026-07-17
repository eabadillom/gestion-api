package com.ferbo.gestion.api.controller;

import java.time.LocalDate;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.response.FileResponse;
import com.ferbo.gestion.api.service.ConstanciaSalidaSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("movil")
public class ConstanciaSalidaController 
{
    private static Logger log = LogManager.getLogger(ConstanciaSalidaController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private ConstanciaSalidaSrv constanciaSalidaSrv;
    
    @GetMapping(value = "/reporte/salida/{fechaInicio}/{fechaFin}", produces = "application/json")
    public ResponseEntity<?> generarPdfSalida(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, 
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin, @RequestParam(required = false) Integer cliente, 
        @RequestParam(required = false) Integer planta, @RequestParam(required = false) Integer camara) 
    {
        FileResponse response = null;
        try{
            
            log.info("Iniciando metodo para obtener el reporte de salida");
            response = constanciaSalidaSrv.getPdfSalida(fechaInicio, fechaFin, (cliente != null) ? cliente : null, (planta != null) ? planta : null, (camara != null) ? camara : null);
            log.info("Iniciando metodo para obtener el reporte de salida");
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(response);
    }
    
}
