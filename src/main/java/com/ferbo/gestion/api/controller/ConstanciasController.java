package com.ferbo.gestion.api.controller;

import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.TipoConstanciaDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.service.ConstanciaSrv;
import java.time.LocalDate;
import java.util.List;
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
public class ConstanciasController 
{
    private static Logger log = LogManager.getLogger(ConstanciaDepositoController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private ConstanciaSrv constanciaSrv;
    
    @GetMapping("/constancia/listar/{tipo}")
    public ResponseEntity<?> listar(@PathVariable(required = true) TipoConstanciaDTO tipo, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, 
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin, @RequestParam(required = false) Integer idCliente, @RequestParam(required = false) String folioCliente) 
    {
        List<ConstanciaDTO> listConstancias = null;
        try{
            log.info("Inicia proceso para obtener las constancia.");
            listConstancias = constanciaSrv.listarConstancias(tipo, idCliente, fechaInicio, fechaFin, folioCliente);
            log.info("Inicia proceso para obtener las constancia.");
        } catch (RuntimeException ex) {
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch (Exception ex) {
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        return ResponseEntity.ok(listConstancias);
    }
    
    @GetMapping(value = "/constancia/detalle/{tipo}", produces = "application/json")
    public ResponseEntity<?> obtenerPorIdSalida(@PathVariable(required = true) TipoConstanciaDTO tipo, @RequestParam(required = true) Integer idConstancia)
    {
        ConstanciaDTO detalleConstancia = null;
        
        try{
            log.info("Inicia proceso para obtener la constancia.");
            detalleConstancia = constanciaSrv.obtenerDetalle(tipo, idConstancia);
            log.info("Finaliza proceso para obtener la constancia.");
        } catch (RuntimeException rtEx) {
            log.warn("Problema al obtener la constancia.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (Exception ex) {
            log.error("Problema desconocido al obtener la constancia.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(detalleConstancia);
    }
    
}
