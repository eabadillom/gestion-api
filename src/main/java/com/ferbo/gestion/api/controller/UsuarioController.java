package com.ferbo.gestion.api.controller;

import com.ferbo.gestion.api.dto.UsuarioMovilDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.service.UsuarioSrv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movil")
public class UsuarioController 
{
    private static Logger log = LogManager.getLogger(UsuarioController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private UsuarioSrv usuarioSrv;
    
    @GetMapping("/usuario")
    public ResponseEntity<?> obtenerUsuario(@RequestParam(required = true) String numeroUsuario) 
    {
        UsuarioMovilDTO usuarioDTO = null;
        
        try{
            log.info("Inicia el proceso para obtener el usuario");
            usuarioDTO = usuarioSrv.buscarUsuario(numeroUsuario);
            log.info("Finaliza el proceso para obtener el usuario");
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(usuarioDTO);
    }
    
}
