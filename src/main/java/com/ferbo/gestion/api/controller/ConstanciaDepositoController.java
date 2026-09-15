package com.ferbo.gestion.api.controller;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.dto.ConstanciaDepositoDTO;
import com.ferbo.gestion.api.dto.KardexFiltroDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.response.FileResponse;
import com.ferbo.gestion.api.service.ConstanciaDepositoSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("movil")
public class ConstanciaDepositoController 
{
    private static Logger log = LogManager.getLogger(ConstanciaDepositoController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private ConstanciaDepositoSrv constanciaDepositoSrv;
    
    /*Metodo para obtener las consultas del kardex*/
    @GetMapping(value = "/constancias/kardex", produces = "application/json")
    public ResponseEntity<?> obtenerKardex(@ModelAttribute KardexFiltroDTO filtro) 
    {
        List<ConstanciaDepositoDTO> listConstanciaDeposito = null;
        
        try {
            log.info("Inicia proceso para obtener las constancia de deposito.");
            listConstanciaDeposito = constanciaDepositoSrv.buscarKardex(filtro);
            log.info("Finaliza proceso para obtenerlas constancia de deposito.");
        } catch (RuntimeException rtEx) {
            log.warn("Problema al obtener las constancia de deposito.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (Exception ex) {
            log.error("Problema desconocido al obtener las constancia de deposito.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(listConstanciaDeposito);
    }
    
    /*Metodo para obtener el pdf del kardex*/
    @GetMapping(value = "/reporte/kardex/{folioCliente}", produces = "application/json")
    public ResponseEntity<?> generarPdfKardex(@PathVariable String folioCliente) 
    {
        FileResponse response = null;
        
        try{
            if(folioCliente == null  || folioCliente.isEmpty()){
                throw new GestionApiException("El folio esta vacio o incompleto");
            }
            
            log.info("Iniciando metodo para obtener el PDF del Kardex");
            response = constanciaDepositoSrv.getPdfKardex(folioCliente);
            log.info("Finaliza metodo para obtener el PDF del Kardex");
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(response);
    }
    
    /*Metodo para obtener el pdf del entrada*/
    @GetMapping(value = "/reporte/entrada/{fechaInicio}/{fechaFin}", produces = "application/json")
    public ResponseEntity<?> generarPdfEntrada(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, 
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin, @RequestParam(required = false) Integer cliente, 
        @RequestParam(required = false) Integer planta, @RequestParam(required = false) Integer camara)
    {
        FileResponse response = null;
        
        try{
            log.info("Iniciando metodo para obtener el PDF del reporte de entrada");
            response = constanciaDepositoSrv.getPdfEntrada(fechaInicio, fechaFin, (cliente != null) ? cliente : null, (planta != null) ? planta : null, (camara != null) ? camara : null);
            log.info("Finaliza metodo para obtener el PDF del reporte de entrada");
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener el reporte de entrada. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(response);
    }
    
}
