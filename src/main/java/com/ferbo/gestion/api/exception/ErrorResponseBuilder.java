package com.ferbo.gestion.api.exception;

import java.time.OffsetDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ferbo.gestion.api.response.MovilResponse;

public class ErrorResponseBuilder 
{
    private  final static Logger log = LogManager.getLogger();

    public static ResponseEntity<MovilResponse> construirErrorMovil(HttpStatus status, String tipoError, Exception ex)
    {
        MovilResponse movilResponse = new MovilResponse();
        
        movilResponse.setCodigoError(status.value());
        movilResponse.setTipoError(tipoError);
        movilResponse.setMensajeError(ex.getMessage());
        movilResponse.setTiempoError(OffsetDateTime.now());
        
        return new ResponseEntity<>(movilResponse, status);
    }
    
    public static ResponseEntity<?> construirErrorDesdeApiToMovil(Exception ex) {
        
        String excepcion = ex.getMessage();
        String excepcionLimpia = excepcion.replace("\\", "")
                .replace("{", "")
                .replace("}", "")
                .replace("\"", "");
        String[] partesExcepcion = excepcionLimpia.split(",");

        MovilResponse movilResponse = new MovilResponse();

        for (int i = 0; i< partesExcepcion.length; i++) {
            String[] mensaje = partesExcepcion[i].split(":");
            if (i == 0) {
                int codigoError = 0;
                try {
                    codigoError = Integer.parseInt(mensaje[2]); 
                } catch (NumberFormatException nFEx) {
                    log.warn("Error al parsear el código de error, se asigna por defecto el 500. {}", ex.getMessage(), ex);
                    codigoError = 500;
                }
                movilResponse.setCodigoError(codigoError);
            }

            if (i == 1) {
                movilResponse.setTipoError(mensaje[1]);
            }

            if (i == 2) {
                movilResponse.setMensajeError(mensaje[1]);
            }
        }

        movilResponse.setTiempoError(OffsetDateTime.now());
        
        return new ResponseEntity<>(movilResponse, HttpStatus.valueOf(movilResponse.getCodigoError()));
        
    }
    
}
