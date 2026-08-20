package com.ferbo.gestion.api.business;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferbo.gestion.api.dto.EmpleadoDTO;
import com.ferbo.gestion.api.dto.SistemaDTO;
import com.ferbo.gestion.api.dto.UsuarioMovilDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class SistemaAuthBL extends AbstractGestionApiBL
{
    private static Logger log = LogManager.getLogger(SistemaAuthBL.class);
    
    public SistemaDTO autenticaUsuario(String username) throws GestionApiException
    {
        SistemaDTO respuesta = null;
        
        String url = null;
        HttpUriRequest request = null;
        CloseableHttpResponse response = null;
        
        Gson prettyGson = null;
        String jsonResponse = null;
        
        int httpStatus = -1;

        try {
            String host = this.basePath;
            String context = String.format("/sgp-api/movil/sistema/%s", username);
            url = String.join("", host, context);
            
            request = createGetRequest(url);
            response = httpClient.execute(request);
            httpStatus = response.getStatusLine().getStatusCode();
            
            if(httpStatus < 200 || httpStatus >= 300)
            	throw new GestionApiException("Respuesta no satisfactoria del SGP-API.");
            
            //La solicitud si está en el rango 200
            jsonResponse = this.getResponseBody(response);
            
            prettyGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
            respuesta = prettyGson.fromJson(jsonResponse, SistemaDTO.class);
        } catch (GestionApiException ex) {
            log.error("Se presentó un problema en la comunicación con el SGP-API...", ex);
            String message = this.getErrorMessage(response);
            throw new GestionApiException(message);
        } catch (IOException ex) {
            log.error("Se presentó un problema en la comunicación con el SGP-API...", ex);
            throw new GestionApiException(ex);
        }
        
        return respuesta;
    }
    
    public UsuarioMovilDTO obtenerUsuario(String usuario, String contrasenia, UsuarioMovilDTO body) throws GestionApiException 
    {
        CloseableHttpResponse response = null;
        UsuarioMovilDTO respuesta = null;
        
        String url = null;
        
        Gson prettyGson   = null;
        String jsonResponse = null;
        
        int httpStatus = -1;
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            String host = this.basePath;
            String context = "/sgp-api/movil/generar";
            url = String.join("", host, context);
            
            httpClient = HttpClients.createDefault();
            
            HttpGetWithBody httpGet = new HttpGetWithBody(url);
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            
            String authStr = String.format("%s:%s", usuario, contrasenia);
            String base64Auth = Base64.getEncoder().encodeToString(authStr.getBytes(StandardCharsets.UTF_8));
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + base64Auth);
            
            String jsonBody = mapper.writeValueAsString(body);
            
            StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
            httpGet.setEntity(entity);
            
            response = httpClient.execute(httpGet);
            httpStatus = response.getStatusLine().getStatusCode();
            
            if(httpStatus < 200 || httpStatus >= 300)
            	throw new GestionApiException("Respuesta no satisfactoria del SGP-API.");
            
            jsonResponse = this.getResponseBody(response);
            prettyGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
            respuesta = prettyGson.fromJson(jsonResponse, UsuarioMovilDTO.class);
        } catch(GestionApiException ex) {
            log.error("Se presentó un problema en la comunicación con el SGP-API...", ex);
            throw new GestionApiException(ex.getMessage());
        } catch(IOException ex) {
            log.error("Se presentó un problema en la comunicación con el SGP-API...", ex);
            throw new GestionApiException(ex);
        }

        return respuesta;
    }
    
    public EmpleadoDTO obtenerEmpleado(String token, String numeroUsuario) throws GestionApiException 
    {
        EmpleadoDTO respuesta = null;
        
        String url = null;
        HttpGet request = null;
        CloseableHttpResponse response = null;
        
        Gson prettyGson   = null;
        String jsonResponse = null;
        
        int httpStatus = -1;
        
        try {
            String host = this.basePath;
            String context = String.format("/sgp-api/movil/empleado/%s", numeroUsuario);
            url = String.join("", host, context);
            
            request = createGetRequest(url);
            request.setHeader("Authorization", token);
            response = httpClient.execute(request);
            httpStatus = response.getStatusLine().getStatusCode();
            
            if(httpStatus < 200 || httpStatus >= 300)
            	throw new GestionApiException("Respuesta no satisfactoria del SGP-API.");
			
            //La solicitud si está en el rango 200
            jsonResponse = this.getResponseBody(response);
            
            prettyGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
            respuesta = prettyGson.fromJson(jsonResponse, EmpleadoDTO.class);
        } catch(GestionApiException ex) {
            log.error("Se presentó un problema en la comunicación con el SGP-API...", ex);
            String message = this.getErrorMessage(response);
            throw new GestionApiException(message);
        } catch(IOException ex) {
            log.error("Se presentó un problema en la comunicación con el SGP-API...", ex);
            throw new GestionApiException(ex);
        }

        return respuesta;
    }
    
    public String deshabilitar(String token) throws GestionApiException 
    {
        String respuesta = null;
        
        String url = null;
        HttpGet request = null;
        CloseableHttpResponse response = null;
        
        Gson prettyGson   = null;
        String jsonResponse = null;
        
        int httpStatus = -1;
        
        try {
            String host = this.basePath;
            String context = "/sgp-api/movil/deshabilitar";
            url = String.join("", host, context);
            
            request = createGetRequest(url);
            request.setHeader("Authorization", token);
            response = httpClient.execute(request);
            httpStatus = response.getStatusLine().getStatusCode();
            
            if(httpStatus < 200 || httpStatus >= 300)
            	throw new GestionApiException("Respuesta no satisfactoria del SGP-API.");
			
            //La solicitud si está en el rango 200
            jsonResponse = this.getResponseBody(response);
            
            respuesta = jsonResponse;
        } catch(GestionApiException ex) {
            log.error("Se presentó un problema en la comunicación con el SGP-API...", ex);
            String message = this.getErrorMessage(response);
            throw new GestionApiException(message);
        } catch(IOException ex) {
            log.error("Se presentó un problema en la comunicación con el SGP-API...", ex);
            throw new GestionApiException(ex);
        }

        return respuesta;
    }
    
}
