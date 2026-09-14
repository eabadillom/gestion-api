package com.ferbo.gestion.api.client;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ferbo.gestion.api.dto.ControlMovilDTO;
import com.ferbo.gestion.api.dto.SistemaDTO;
import com.ferbo.gestion.api.dto.UsuarioMovilDTO;
import com.ferbo.gestion.api.tool.DataSourceManager;
import com.ferbo.tools.exception.RuleException;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ToolException;
import com.ferbo.tools.exception.ValidationException;

@Component
public class SgpApiClient {

    private final RestTemplate restTemplate;

    private final String basePath = DataSourceManager.getJndiParameter("gestionapi/api");

    private static Logger log = LogManager.getLogger(SgpApiClient.class);

    public SgpApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UsuarioMovilDTO generarToken(HttpServletRequest request, UsuarioMovilDTO body) throws RuntimeException, Exception {

        String context = "/sgp-api/movil/generar";

        String url = String.join("", basePath, context);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        String authorization =
        request.getHeader(HttpHeaders.AUTHORIZATION);
        headers.set(HttpHeaders.AUTHORIZATION,authorization);

        HttpEntity<UsuarioMovilDTO> requestToSGP
                = new HttpEntity<>(body, headers);

    
            ResponseEntity<UsuarioMovilDTO> response
                    = restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            requestToSGP,
                            UsuarioMovilDTO.class
                    );

            return response.getBody();
    }

    public ControlMovilDTO cambiarPalabra(HttpServletRequest request, SistemaDTO body) throws RuntimeException, Exception, ValidationException, SystemException, RuleException, ToolException {

        String context = "/sgp-api/movil/cambiarPalabra";

        String url = String.join("", basePath, context);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new RuntimeException("No se encontró un Bearer Token válido");
        }

        headers.set(HttpHeaders.AUTHORIZATION, authorization);

        HttpEntity<SistemaDTO> requestToSGP
                = new HttpEntity<>(body, headers);

        ResponseEntity<ControlMovilDTO> response
                = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        requestToSGP,
                        ControlMovilDTO.class
                );

        return response.getBody();
    }   
}
