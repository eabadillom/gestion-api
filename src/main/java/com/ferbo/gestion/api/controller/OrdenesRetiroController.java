package com.ferbo.gestion.api.controller;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.dto.SalidaListDTO;
import com.ferbo.gestion.api.dto.SalidaViewDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.service.SalidaSrv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movil")
public class OrdenesRetiroController 
{
    private static Logger log = LogManager.getLogger(OrdenesRetiroController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private SalidaSrv salidaSrv;
    
    @GetMapping(value = "/salidas", produces = "application/json")
    public ResponseEntity<?> obtenerSalidas(@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, 
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin, 
            @RequestParam(required = false) Integer idCliente)
    {
        List<SalidaListDTO> listSalidas = null;
        
        try{
            log.info("Inicia proceso para obtener las ordenes de retiro.");
            listSalidas = salidaSrv.buscarPorPeriodoCliente((idCliente != null) ? idCliente : null, fechaInicio, fechaFin);
            log.info("Finaliza proceso para obtener las ordenes de retiro.");
        } catch (RuntimeException rtEx) {
            log.warn("Problema al obtener las ordenes de retiro.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (Exception ex) {
            log.error("Problema desconocido al obtener las ordenes de retiro.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(listSalidas);
    }
    
    @GetMapping(value = "/salida", produces = "application/json")
    public ResponseEntity<?> obtenerPorIdSalida(@RequestParam(required = true) Integer idSalida)
    {
        SalidaViewDTO detalleSalida = null;
        
        try{
            log.info("Inicia proceso para obtener la orden de retiro.");
            detalleSalida = salidaSrv.buscarPorId(idSalida);
            log.info("Finaliza proceso para obtener la orden de retiro.");
        } catch (RuntimeException rtEx) {
            log.warn("Problema al obtener la orden de retiro.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (Exception ex) {
            log.error("Problema desconocido al obtener la orden de retiro.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(detalleSalida);
    }
    
    @PatchMapping("/salida/{idSalida}/cancelar")
    public ResponseEntity<?> cancelarSalida(@PathVariable Integer idSalida)
    {
        try{
            log.info("Inicia proceso para cancelar la orden de retiro.");
            salidaSrv.cancelarSalida(idSalida);
            log.info("Finaliza proceso para cancelar la orden de retiro.");
        } catch (RuntimeException rtEx) {
            log.warn("Problema al cancelar la orden de retiro.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (Exception ex) {
            log.error("Problema desconocido al cancelar la orden de retiro.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok("La salida fue cancelada correctamente");
    }
    
}
