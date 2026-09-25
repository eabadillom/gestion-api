package com.ferbo.gestion.api.client.sgp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
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

    private final String user = DataSourceManager.getJndiParameter("gestionapi/user");
    private final String password = DataSourceManager.getJndiParameter("gestionapi/password");

    @Value("${sgp.api.movil.segment}")
    private String sgpApiMovilSegment;

    public SgpApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UsuarioMovilDTO validateMobile(
            String mobileAuthorization, UsuarioMovilDTO body) {

        String context = sgpApiMovilSegment + "/dispositivos/verificaciones";
        String url = String.join("", basePath, context);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Autenticación de gestión api contra sgp api
        headers.setBasicAuth(user, password);

        // Credenciales de cualquier usuario de gestion movil que spg api debe validar
        headers.set("Dispositivo-Mobile-Authorization", mobileAuthorization);

        HttpEntity<UsuarioMovilDTO> requestEntity = new HttpEntity<>(body, headers);
        
        ResponseEntity<UsuarioMovilDTO> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, UsuarioMovilDTO.class);
        
        return response.getBody();
        }


    public ControlMovilDTO solicitarCambiarContrasenia(String mobileAuthorization) throws RuntimeException, Exception, ValidationException, SystemException, RuleException, ToolException {

        String context = sgpApiMovilSegment + "/dispositivos/cambiarPassword";
        String url = String.join("", basePath, context);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Autenticación de gestión api contra sgp api
        headers.setBasicAuth(user, password);

        // Credenciales de cualquier usuario de gestion movil que spg api debe validar
        headers.set("Dispositivo-Mobile-Authorization", mobileAuthorization);

        HttpEntity<SistemaDTO> requestEntity = new HttpEntity<>(headers);
        
        ResponseEntity<ControlMovilDTO> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, ControlMovilDTO.class);
        
        return response.getBody();
    }
}
