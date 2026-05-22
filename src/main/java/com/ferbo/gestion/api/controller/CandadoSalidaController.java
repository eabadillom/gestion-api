package com.ferbo.gestion.api.controller;

import com.ferbo.gestion.api.dto.CandadoSalidaDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.service.CandadoSalidaSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("movil")
public class CandadoSalidaController 
{
    private static Logger log = LogManager.getLogger(CandadoSalidaController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private CandadoSalidaSrv candadoSalidaSrv;
    
    /*Metodo para obtener la consulta del candado de salida*/
    @GetMapping(value = "/candadoSalida/{idCliente}", produces = "application/json")
    public ResponseEntity<?> obtenerCandadoSalida(@PathVariable Integer idCliente) 
    {
        CandadoSalidaDTO candadoSalida = null;
        
        try {
            log.info("Inicia proceso para obtener el candado de salida.");
            candadoSalida = candadoSalidaSrv.obtenerCandodoDTO(idCliente);
            log.info("Finaliza proceso para obtener el candado de salida.");
        } catch (RuntimeException rtEx) {
            log.warn("Problema al obtener el candado de salida.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (Exception ex) {
            log.error("Problema desconocido al obtener el candado de salida.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(candadoSalida);
    }
    
    @PatchMapping("/candadoSalida/{idCandado}")
    public ResponseEntity<?> actualizar(@PathVariable Integer idCandado, @RequestBody CandadoSalidaDTO candadoDTO) 
    {
        CandadoSalidaDTO candadoSalida = null;
        try {
            log.info("Inicia el proceso para el actualizado del candado de salida");
            candadoDTO.setId(idCandado);
            log.info(String.format("Actualizando candado: %s", candadoDTO.toString()));
            candadoSalida = candadoSalidaSrv.actualizarCandado(candadoDTO);
            log.info("Termina el proceso para el actualizado del candado de salida");
        } catch (RuntimeException rtEx) {
            log.warn("Problema al actualizar el candado de salida.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (Exception ex) {
            log.error("Problema desconocido al actualizar el candado de salida.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        
        return new ResponseEntity<>(candadoSalida, HttpStatus.OK);
    }
    
}
