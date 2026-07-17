package com.ferbo.gestion.api.controller;

import java.time.LocalDate;
import com.ferbo.gestion.api.dto.ValidacionSaldoDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.service.SaldoSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/movil")
public class SaldoController 
{
    private static Logger log = LogManager.getLogger(SaldoController.class);

    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private SaldoSrv saldoSrv;
    
    @GetMapping("/saldo")
    public ResponseEntity<?> obtenerSaldo(@RequestParam(required = false) Integer idCliente, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) 
    {
        ValidacionSaldoDTO validacionSaldoDTO = null;
        try{
            validacionSaldoDTO = saldoSrv.validarSaldo(idCliente, fecha);
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(validacionSaldoDTO);
    }
    
}
