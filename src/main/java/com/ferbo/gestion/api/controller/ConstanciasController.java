package com.ferbo.gestion.api.controller;

import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.model.Foto;
import com.ferbo.gestion.api.dto.TipoConstanciaDTO;
import com.ferbo.gestion.api.exception.ErrorResponseBuilder;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.response.RespuestaResponse;
import com.ferbo.gestion.api.service.ConstanciaSrv;
import com.ferbo.tools.MailException;
import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movil")
public class ConstanciasController 
{
    private static Logger log = LogManager.getLogger(ConstanciaDepositoController.class);
    
    private static final String TIPO_ERROR_ACCESO = "Acceso";
    
    @Autowired
    private ConstanciaSrv constanciaSrv;
    
    @GetMapping("/constancia/{tipo}/listar")
    public ResponseEntity<?> listarConstancias(@PathVariable(required = true) TipoConstanciaDTO tipo, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, 
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin, @RequestParam(required = false) Integer idCliente, @RequestParam(required = false) String folioCliente) 
    {
        List<ConstanciaDTO> listConstancias = null;
        try{
            log.info("Inicia proceso para obtener las constancia.");
            listConstancias = constanciaSrv.listarConstancias(tipo, idCliente, fechaInicio, fechaFin, folioCliente);
            log.info("Inicia proceso para obtener las constancia.");
        } catch (RuntimeException ex) {
            log.warn("Hubo un problema al obtener los datos. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        } catch (Exception ex) {
            log.error("Problema desconocido. {}", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.INTERNAL_SERVER_ERROR, TIPO_ERROR_ACCESO, ex);
        }
        return ResponseEntity.ok(listConstancias);
    }
    
    @GetMapping(value = "/constancia/{tipo}/detalle", produces = "application/json")
    public ResponseEntity<?> obtenerConstanciaPorIdSalida(@PathVariable(required = true) TipoConstanciaDTO tipo, @RequestParam(required = true) Integer idConstancia)
    {
        ConstanciaDTO detalleConstancia = null;
        
        try{
            log.info("Inicia proceso para obtener la constancia.");
            detalleConstancia = constanciaSrv.obtenerDetalle(tipo, idConstancia);
            log.info("Finaliza proceso para obtener la constancia.");
        } catch (RuntimeException rtEx) {
            log.warn("Problema al obtener la constancia.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (Exception ex) {
            log.error("Problema desconocido al obtener la constancia.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        
        return ResponseEntity.ok(detalleConstancia);
    }
    
    @PostMapping(value = "/constancia/{tipo}/evidencia", produces = "application/json")
    public ResponseEntity<?> enviarEvidenciasConstancia(@PathVariable(required = true) TipoConstanciaDTO tipo, 
            @RequestParam(required = true) Integer idConstancia, @RequestParam(required = true) String numEmpleado, @RequestBody List<Foto> fotos)
    {
        RespuestaResponse respuesta = null;
        try{
            log.info("Inicia proceso para enviar las evidencias de la constancia de {} por correo.", tipo);
            String mensaje = constanciaSrv.enviarEvidencia(tipo, idConstancia, fotos, numEmpleado);
            respuesta = new RespuestaResponse(mensaje, true);
            log.info("Termina proceso para enviar las evidencias de la constancia de {} por correo.", tipo);
        } catch (RuntimeException rtEx) {
            log.warn("Problema al obtener la constancia.", rtEx);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, rtEx);
        } catch (GestionApiException | MailException ex) {
            log.error("Problema desconocido al obtener la constancia.", ex);
            return ErrorResponseBuilder.construirErrorMovil(HttpStatus.NOT_FOUND, TIPO_ERROR_ACCESO, ex);
        }
        return ResponseEntity.ok(respuesta);
    }
    
}
