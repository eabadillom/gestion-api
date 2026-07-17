package com.ferbo.gestion.api.controller;

import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.response.FileResponse;
import com.ferbo.gestion.api.service.OcupacionPlantaSrv;
import com.ferbo.gestion.api.service.UsuarioSrv;
import com.ferbo.gestion.api.model.OcupacionPlanta;
import com.ferbo.gestion.core.model.sistema.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/movil")
public class OcupacionCamaraController 
{
    private static Logger log = LogManager.getLogger(OcupacionCamaraController.class);
    
    @Autowired
    private UsuarioSrv usuarioSrv;
    
    @Autowired
    private OcupacionPlantaSrv ocupacionPlantaSrv;
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @GetMapping(value="/ocupacion/planta/{numUsuario}", produces = "application/json")
    public ResponseEntity<?> ocupacionPlanta(@PathVariable String numUsuario, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, @RequestParam(required = false) List<Integer> clientes) {
        List<OcupacionPlanta> listOcupacionPlanta = null;
        
        try{
            if(!validarNumeroUsuario(numUsuario)){
                throw new GestionApiException("Numero del usuario esta vacio o no completo");
            }

            log.info("Iniciando metodo para obtener usuario con su identificador");
            Usuario usuario = usuarioSrv.buscarUsuarioPorNumero(numUsuario);
            log.info("Terminando metodo para obtener usuario con su identificador");
            
            log.info("Iniciando metodo para obtener la ocupacion por camara");
            switch (usuario.getPerfil().getId()) {
                case 2:
                case 3:
                    listOcupacionPlanta = ocupacionPlantaSrv.buscarOcupacionCamara(fecha, (clientes == null) ? null : clientes, null, null);
                    break;
                case 1:
                case 4:
                    listOcupacionPlanta = ocupacionPlantaSrv.buscarOcupacionCamara(fecha, (clientes == null) ? null : clientes, usuario.getIdPlanta(), null);
                    break;
            }
            log.info("Terminando metodo para obtener la ocupacion por camara");
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(listOcupacionPlanta);
    }
    
    @GetMapping(value = "/ocupacion/reporte/{numUsuario}", produces = "application/json")
    public ResponseEntity<?> generarPdf(@PathVariable String numUsuario, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, @RequestParam(required = false) List<Integer> clientes) 
    {
        FileResponse response = null;
        try{
            if(!validarNumeroUsuario(numUsuario)){
                throw new GestionApiException("Numero del usuario esta vacio o no completo");
            }

            log.info("Iniciando metodo para obtener usuario con su identificador");
            Usuario usuario = usuarioSrv.buscarUsuarioPorNumero(numUsuario);
            log.info("Terminando metodo para obtener usuario con su identificador");
            
            log.info("Iniciando metodo para obtener el PDF de la ocupacion por camara");
            switch (usuario.getPerfil().getId()) {
                case 2:
                case 3:
                    response = ocupacionPlantaSrv.getPdfAsBase64(fecha, (clientes == null) ? null : clientes, null);
                    break;
                case 1:
                case 4:
                    response = ocupacionPlantaSrv.getPdfAsBase64(fecha, (clientes == null) ? null : clientes, usuario.getIdPlanta());
                    break;
            }
            log.info("Terminando metodo para obtener el PDF de la ocupacion por camara");
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(response);
    }
    
    public boolean validarNumeroUsuario(String texto) 
    {
        if (texto == null || texto.isEmpty()) {
            return false;
        }

        return texto.trim().matches("\\d{4}");
    }
    
}
