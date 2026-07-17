package com.ferbo.gestion.api.controller;

import com.ferbo.gestion.api.dto.ClienteDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.service.ClienteSrv;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("movil")
public class ClientesController 
{
    private static Logger log = LogManager.getLogger(ClientesController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private ClienteSrv clienteSrv;
    
    @GetMapping(value = "/clientes", produces = "application/json")
    public ResponseEntity<?> obtenerClientes() 
    {
        List<ClienteDTO> listClientes = null;
        
        try {
            log.info("Inicia proceso para obtener clientes activos.");
            listClientes = clienteSrv.buscarClientes();
            log.info("Finaliza proceso para obtener clientes activos.");
        } catch (RuntimeException rtEx) {
            log.warn("Problema al obtener los clientes.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (Exception ex) {
            log.error("Problema desconocido al obtener los clientes.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        return ResponseEntity.ok(listClientes);
    }
    
}
