package com.ferbo.gestion.api.controller;

import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.response.FileResponse;
import com.ferbo.gestion.api.service.InventarioSrv;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movil")
public class InventarioController 
{
    private static Logger log = LogManager.getLogger(InventarioController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private InventarioSrv inventarioSrv;
    
    /*Metodo para obtener el pdf del inventario*/
    @GetMapping(value = "/reporte/inventario/{fecha}", produces = "application/json")
    public ResponseEntity<?> generarPdfInventario(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, 
        @RequestParam(required = false) Integer cliente, @RequestParam(required = false) Integer planta)
    {
        FileResponse response = null;
        
        try{
            log.info("Iniciando metodo para obtener el PDF del reporte de inventrio");
            response = inventarioSrv.getPdfInventario(fecha, (cliente != null) ? cliente : null, (planta != null) ? planta : null);
            log.info("Finaliza metodo para obtener el PDF del reporte de inventario");
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener el reporte de inventario. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(response);
    }
    
}
