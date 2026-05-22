package com.ferbo.gestion.api.controller;

import java.util.List;
import com.ferbo.gestion.api.dto.CamaraDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.service.CamaraSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("movil")
public class CamaraController 
{
    private static Logger log = LogManager.getLogger(CamaraController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private CamaraSrv camaraSrv;
    
    @GetMapping(value = "/camaras", produces = "application/json")
    public ResponseEntity<?> obtenerCamaras(@RequestParam(required = false) Integer idPlanta)
    {
        List<CamaraDTO> listPlantas = null;
        
        try {
            log.info("Inicia proceso para obtener las camaras.");
            listPlantas = camaraSrv.buscarCamaras((idPlanta != null) ? idPlanta : null);
            log.info("Finaliza proceso para obtener las camaras"); 
        } catch (RuntimeException rtEx) {
            log.warn("Problema al obtener las constancia de deposito.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (Exception ex) {
            log.error("Problema desconocido al obtener las constancia de deposito.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(listPlantas);
    }
    
}
