package com.ferbo.gestion.api.controller;

import com.ferbo.gestion.api.dto.UsuarioMovilDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.service.ControlMovilSrv;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movil")
public class MovilController 
{
    private static Logger log = LogManager.getLogger(MovilController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private ControlMovilSrv controlMovilSrv;
    
    @GetMapping("/generar")
    public ResponseEntity<?> inicioPantalla(HttpServletRequest request, @RequestBody UsuarioMovilDTO body) {
        UsuarioMovilDTO usuario = null;
        
        try{
            usuario = controlMovilSrv.obtenerUsuario(request, body);
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(usuario);
    }
    
    @GetMapping("/verificar")
    public ResponseEntity<?> verificarToken() {
        return ResponseEntity.ok("Acceso autorizado");
    }
    
    @GetMapping("/deshabilitar")
    public ResponseEntity<?> deshabilitarToken(@RequestHeader(value = "Authorization", required = true) String authHeader)
    {
        String respuesta = null;
        
        if (!authHeader.startsWith("Bearer ")) {
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.BAD_REQUEST, TIPO_ERROR_ACCESO, new RuntimeException("Formato de token inválido"));
        }

        try {
            log.info("Inicia proceso para desahibilitar el token del sistema.");
            respuesta = controlMovilSrv.deshabilitarToken(authHeader);
            log.info("Finaliza proceso para desahibilitar el token del sistema.");
        } catch (RuntimeException ex) {
            log.warn("Hubo un problema al desahibilitar el token del sistema. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch (Exception ex) {
            log.error("Hubo un problema al desahibilitar el token del sistema. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }

        return ResponseEntity.ok(respuesta);
    }
    
}
