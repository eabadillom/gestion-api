package com.ferbo.gestion.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferbo.gestion.api.auth.JwtUtil;
import com.ferbo.gestion.api.client.sgp.SgpApiClient;
import com.ferbo.gestion.api.dto.ControlMovilDTO;
import com.ferbo.gestion.api.dto.SistemaDTO;
import com.ferbo.gestion.api.dto.UsuarioMovilDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.mapper.IControlMovilMapper;
import com.ferbo.gestion.api.model.ControlMovil;
import com.ferbo.gestion.api.service.ControlMovilSrv;
import com.ferbo.gestion.api.tool.SecurityTool;
import com.ferbo.gestion.api.tool.SistemaDetailsSrv;
import com.ferbo.tools.exception.RuleException;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ToolException;
import com.ferbo.tools.exception.ValidationException;

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

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired 
    private  SistemaDetailsSrv sistemaDetailsSrv;
    
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
    
    @PostMapping("/autenticacion/token")
    public ResponseEntity<?> autenticar(HttpServletRequest request, @RequestBody UsuarioMovilDTO usuario) {
    
        try {
            String credencialesMovil = request.getHeader(HttpHeaders.AUTHORIZATION);
            
            log.info("Inicia proceso para validar credenciales del usuario movil en SGP-API");
            UsuarioMovilDTO usuarioValidado = sgpApiClient.validateMobile(credencialesMovil, usuario);
            log.info("Finaliza proceso para validar credenciales del usuario movil en SGP-API");

            log.info("Inicia proceso para extraer el perfil del usuario");
            int pefil = controlMovilSrv.extrarPerfilDelUsuarioDesdeToken(usuarioValidado);
            log.info("Finaliza proceso para extraer el perfil del usuario");

            log.info("Inicia proceso extraer las credenciales desde las cabeceras");
            String[] credenciales = securityTool.extractCredentials(request);
            log.info("Finaliza proceso extraer las credenciales desde las cabeceras");
            
            log.info("Inicia proceso para generar el token del usuario movil");
            String token = jwtUtil.generateToken(credenciales[0]);
            log.info("Finaliza proceso para generar el token del usuario movil");

            log.info("Inicia proceso para crear el control movil del usuario");
            ControlMovil usuarioValido = controlMovilSrv.construirUsuarioMovilCompleto(credenciales[0], usuarioValidado, token);
            log.info("Finaliza proceso para crear el control movil del usuario");

            log.info("Inicia proceso para guardar el control movil en el sistema");
            usuarioValido = controlMovilSrv.guardarControlMovil(usuarioValido);
            log.info("Finaliza proceso para guardar el control movil en el sistema");

            UsuarioMovilDTO usuarioCompletoYValido = controlMovilSrv.completarUsuarioMovil(usuarioValidado, usuarioValido, pefil);
          
            return ResponseEntity.ok(usuarioCompletoYValido);
            
        } catch (ToolException ex) {
            log.info("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.UNAUTHORIZED, "Herramienta auxiliar", ex);
        } catch (ValidationException ex) {
            log.info("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.UNPROCESSABLE_ENTITY, "Validación", ex);
        } catch (SystemException ex) {
            log.warn("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, "Servicio externo", ex);
        } catch (RuleException ex) {
            log.warn("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.CONFLICT, "Regla de negocio", ex);
        } catch (Exception ex) {
            log.warn("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorDesdeApiToMovil(ex);
        }
        
    }

    @GetMapping("/verificar")
    public ResponseEntity<?> verificarToken() {
        return ResponseEntity.ok("Acceso autorizado");
    }
    
    @PostMapping("/autenticacion/cambiarPassword")
    public ResponseEntity<?> cambiarContrasenia(HttpServletRequest request, @RequestBody SistemaDTO body) {
        
        try {

            log.info("Inicia proceso para extraer el token de la solicitud");
            String token = securityTool.extractBearerToken(request);
            log.info("Finaliza proceso para extraer el token de la solicitud");

            log.info("Inicia proceso para obtener el sistema asociado al token");
            String sistema = controlMovilSrv.obtenerSistemaPorToken(token);
            log.info("Finaliza proceso para obtener el sistema asociado al token");

            log.info("Inicia proceso para generar la autenticacion básica del movil con la nueva contraseña");
            String basicAuthMovil = securityTool.generarBasicAuth(sistema, body.getPassword());
            log.info("Finaliza proceso para generar la autenticacion básica del movil con la nueva contraseña");

            log.info("Inicia proceso para solicitar el cambio de contraseña a SGP-API");
            ControlMovilDTO respuesta = sgpApiClient.solicitarCambiarContrasenia(basicAuthMovil);
            log.info("Finaliza proceso para solicitar el cambio de contraseña a SGP-API");

            log.info("Inicia proceso para deshabilitar el ultimo token registrado del dispositivo");
            ControlMovil controlMovilDeshabilitado = controlMovilSrv.deshabilitarPorToken(token);
            log.info("Finaliza proceso para deshabilitar el ultimo token registrado del dispositivo");
            
            log.info("Inicia proceso para contruir respuesta para el dispositivo movil");
            respuesta = controlMovilMapper.toDTO(controlMovilDeshabilitado);
            log.info("Finaliza proceso para contruir respuesta para el dispositivo movil");

            log.info("Se establece que el token ya no sera valído");
            respuesta.setValido(Boolean.FALSE);

            log.info("Se envía respuesta al dispositivo movil");
            return ResponseEntity.ok(respuesta);

        } catch (ToolException ex) {
            log.info("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.UNAUTHORIZED, "Herramienta auxiliar", ex);
        } catch (ValidationException ex) {
            log.info("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.UNPROCESSABLE_ENTITY, "Validación", ex);
        } catch (SystemException ex) {
            log.warn("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, "Servicio externo", ex);
        } catch (RuleException ex) {
            log.warn("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.CONFLICT, "Regla de negocio", ex);
        } catch (Exception ex) {
            log.warn("Error: {}", ex.getMessage(), ex);
            return ErrorResponseBuilder.construirErrorDesdeApiToMovil(ex);
        }
    }
}
