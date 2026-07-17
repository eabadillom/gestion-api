package com.ferbo.gestion.api.exception;

import com.ferbo.gestion.api.response.MovilResponse;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseBuilder 
{
    public static ResponseEntity<MovilResponse> construirErrorMovil(HttpStatus status, String tipoError, Exception ex)
    {
        MovilResponse movilResponse = new MovilResponse();
        
        movilResponse.setCodigoError(status.value());
        movilResponse.setTipoError(tipoError);
        movilResponse.setMensajeError(ex.getMessage());
        movilResponse.setTiempoError(OffsetDateTime.now());
        
        return new ResponseEntity<>(movilResponse, status);
    }
    
}
