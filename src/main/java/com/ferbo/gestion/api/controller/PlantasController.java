package com.ferbo.gestion.api.controller;

import java.util.List;
import com.ferbo.gestion.api.dto.PlantaDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.service.PlantaSrv;
import com.ferbo.gestion.api.service.UsuarioSrv;
import com.ferbo.gestion.core.model.sistema.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movil")
public class PlantasController 
{
    private static Logger log = LogManager.getLogger(PlantasController.class);

    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private UsuarioSrv usuarioSrv;
    
    @Autowired
    private PlantaSrv plantaSrv;
    
    @GetMapping("/plantas/{numUsuario}")
    public ResponseEntity<?> obtenerTodos(@PathVariable String numUsuario) 
    {
        List<PlantaDTO> plantas = null;
        
        try{
            if(!validarNumeroUsuario(numUsuario)){
                throw new GestionApiException("Numero del usuario esta vacio o no completo");
            }
            
            log.info("Iniciando metodo para obtener usuario con su identificador");
            Usuario usuario = usuarioSrv.buscarUsuarioPorNumero(numUsuario);
            log.info("Terminando metodo para obtener usuario con su identificador");
            
            log.info("Inicia el proceso para generar la plantas");
            switch (usuario.getPerfil().getId()) {
                case 2:
                case 3:
                    plantas = plantaSrv.obtenerPlantas(null);
                    break;
                case 1:
                case 4:
                    plantas = plantaSrv.obtenerPlantas(usuario.getIdPlanta());
                    break;
            }
            log.info("Finaliza el proceso para generar la plantas");
        } catch(RuntimeException ex){
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch(Exception ex){
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(plantas);
    }
    
    public boolean validarNumeroUsuario(String texto) 
    {
        if (texto == null || texto.isEmpty()) {
            return false;
        }

        return texto.trim().matches("\\d{4}");
    }
    
}
