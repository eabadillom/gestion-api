package com.ferbo.gestion.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferbo.gestion.api.client.SgpApiClient;
import com.ferbo.gestion.api.dto.ControlMovilDTO;
import com.ferbo.gestion.api.dto.SistemaDTO;
import com.ferbo.gestion.api.dto.UsuarioMovilDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.mapper.IControlMovilMapper;
import com.ferbo.gestion.api.model.ControlMovil;
import com.ferbo.gestion.api.service.ControlMovilSrv;
import com.ferbo.gestion.api.tool.SecurityTool;

@RestController
@RequestMapping("/movil")
public class MovilController 
{
    private static Logger log = LogManager.getLogger(MovilController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private ControlMovilSrv controlMovilSrv;
    
    @Autowired
    private SgpApiClient sgpApiClient;
    
    @Autowired
    private SecurityTool securityTool;
    
    @Autowired
    private IControlMovilMapper controlMovilMapper;
    
   /* @GetMapping("/generar")
    public ResponseEntity<?> inicioPantalla(@RequestHeader("Authorization") String authHeader, @RequestBody UsuarioMovilDTO body) {
        UsuarioMovilDTO usuario = null;
        
        try{
            log.info("Inicia el proceso para generar el usuario");
            usuario = controlMovilSrv.obtenerUsuario(authHeader, body);
            log.info("Finaliza el proceso para generar el usuario");
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(usuario);
    }*/
    
    @GetMapping("/verificar")
    public ResponseEntity<?> verificarToken() {
        return ResponseEntity.ok("Acceso autorizado");
    }
    
    /*@GetMapping("/deshabilitar")
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
    }*/
    
    @PostMapping("/generar")
    public ResponseEntity<?> inicioPantalla(HttpServletRequest request, @RequestBody UsuarioMovilDTO body) {
    
        try {
            log.info("Inicia proceso para generar el token comunicandoce con SGP-API");
            UsuarioMovilDTO usuarioMovilDTO = sgpApiClient.generarToken(request, body);
            log.info("Finaliza proceso para generar el token comunicandoce con SGP-API");

            log.info("Inicia proceso extraer las credenciales desde las cabeceras");
            String[] credenciales = securityTool.extractCredentials(request);
            log.info("Finaliza proceso extraer las credenciales desde las cabeceras");
            
            log.info("Inicia proceso para guardar el token recibido en gestion");
            ControlMovil controlMovil = controlMovilSrv.guardarToken(credenciales[0], usuarioMovilDTO);
            log.info("Finaliza proceso para guardar el token recibido en gestion");
          
            return ResponseEntity.ok(usuarioMovilDTO);
            
        } catch (RuntimeException ex) {
            log.warn("Error en tiempo de ejecución: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.CONFLICT, "", ex);
        } catch (Exception ex) {
            log.error("Error desconocido: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.BAD_GATEWAY, "Error desconocido", ex);
        }
        
    }
    
    @PostMapping("/cambiarPalabra")
    public ResponseEntity<?> cambiarPalabra(HttpServletRequest request, @RequestBody SistemaDTO body) {
        
        try {
            log.info("Inicia proceso para cambiar la palabra comuinicandoce con SGP-API");
            ControlMovilDTO controlMovilDTO = sgpApiClient.cambiarPalabra(request, body);
            log.info("Finaliza proceso para cambiar la palabra comuinicandoce con SGP-API");
            log.info("Inicia proceso para extraer el token de las cabeceras");
            String token = securityTool.extractBearerToken(request);
            log.info("Finaliza proceso para extraer el token de las cabeceras");
            log.info("Inicia proceo para deshabilitar el token en gestion");
            ControlMovil controlMovil = controlMovilSrv.deshabilitarPorToken(token);
            log.info("Finaliza proceo para deshabilitar el token en gestion");
            log.info("Inicia proceso para construir la respuesta a control movil");
            ControlMovilDTO controlMovilInterno = controlMovilMapper.toDTO(controlMovil);
            log.info("Finaliza proceso para construir la respuesta a control movil");
            return ResponseEntity.ok(controlMovilInterno);
        } catch (Exception ex) {
            log.warn("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorDesdeApiToMovil(ex);
        }
    }
}
