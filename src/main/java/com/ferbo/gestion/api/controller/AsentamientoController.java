package com.ferbo.gestion.api.controller;

import com.ferbo.gestion.api.dto.AsentamientoDTO;
import com.ferbo.gestion.api.service.AsentamientoSrv;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/gestion")
public class AsentamientoController 
{
    private static Logger log = LogManager.getLogger(AsentamientoController.class);
    
    @Autowired
    private AsentamientoSrv asentamientoSrv;
    
    @GetMapping(value = "/asentamiento/{cp}", produces = "application/json")
    public ResponseEntity<?> obtenerAsentamientoPorCP(@PathVariable String cp) 
    {
        List<AsentamientoDTO> asentamientoDTO = null;
        log.info("Iniciando metodo para obtener asentamiento");
        
        try {
            asentamientoDTO = asentamientoSrv.buscarPorCP(cp);
        } catch (RuntimeException rtEx) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rtEx.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Contacte con el administrador de sistemas");
        }
        
        log.info("Terminando metodo para obtener asentamiento");
        return ResponseEntity.ok(asentamientoDTO);
    }
    
}
