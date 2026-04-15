package com.ferbo.gestion.api.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferbo.gestion.api.business.AbstractGestionApiBL;
import com.ferbo.gestion.api.dto.EmpleadoDTO;
import com.ferbo.gestion.api.dto.SistemaDTO;
import com.ferbo.gestion.api.dto.UsuarioMovilDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.model.ControlMovil;
import com.ferbo.gestion.api.repository.ControlMovilRepo;
import com.ferbo.gestion.api.tool.SecurityTool;

@Service
public class ControlMovilSrv extends AbstractGestionApiBL
{
    private static Logger log = LogManager.getLogger(ControlMovilSrv.class);
    
    @Autowired
    private SecurityTool securityTool;
    
    @Autowired
    private ControlMovilRepo controlMovilRepo;
    
    public UsuarioMovilDTO obtenerUsuario(HttpServletRequest request, UsuarioMovilDTO body) throws GestionApiException 
    {
        UsuarioMovilDTO usuario = null;
        
        LocalDate hoy = LocalDate.now();
        LocalDate fechaExpiracion = hoy.plusDays(7);

        log.info("Inicia proceso de extraccion de credenciales");
        String[] credenciales = securityTool.extractCredentials(request);
        log.info("Finaliza proceso de extraccion de credenciales");
        
        log.info("Inicia proceso de obtencion del sistema");
        SistemaDTO sistemaReferencia = new SistemaDTO();
        sistemaReferencia.setNombre(this.user);
        sistemaReferencia.setPassword(this.password);
        sistemaReferencia.setRol(this.role);
        log.info("Finaliza proceso de obtencion del sistema");
        
        if(!credenciales[0].equals(sistemaReferencia.getNombre()) || !credenciales[1].equals(sistemaReferencia.getPassword())){
            throw new GestionApiException("Credenciales no validas");
        }
        
        ControlMovil nuevoToken = controlMovilRepo.findByUser(sistemaReferencia.getNombre());
        
        if(nuevoToken != null)
        {
            int validezToken = hoy.compareTo(nuevoToken.getExpiracion());
            log.info("El ultimo token del sistema solicitante: {}", nuevoToken);
            if(validezToken <= 0 && nuevoToken.getValido()){
                //Mandar el token existente si es valido
                log.info("Ya existe un token valido, se mantiene");
                log.info("Inicia proceso de para notificar del token existente");
                EmpleadoDTO empleado = obtenerEmpleado(nuevoToken.getToken(), body.getNumeroUsuario());
                usuario = asignarUsuarioMovil(empleado, nuevoToken);
                log.info("Finaliza proceso de para notificar del token existente");
            } else {
                //Pedir un nuevo token a sgp api si el existente ya no es valido o expiro  
                usuario = obtenerUsuario(sistemaReferencia.getNombre(), sistemaReferencia.getPassword(), body);
                nuevoToken = asignarToken(usuario, sistemaReferencia.getNombre(), fechaExpiracion);
                controlMovilRepo.guardar(nuevoToken);
            }
        } else {
            //Pedir token si no existe en bd o no hay reciente
            usuario = obtenerUsuario(sistemaReferencia.getNombre(), sistemaReferencia.getPassword(), body);
            nuevoToken = asignarToken(usuario, sistemaReferencia.getNombre(), fechaExpiracion);
            controlMovilRepo.guardar(nuevoToken);
        }
        
        return usuario;
    }
    
    public UsuarioMovilDTO obtenerUsuario(String usuario, String contrasenia, UsuarioMovilDTO body) throws GestionApiException 
    {
        UsuarioMovilDTO respuesta = null;
        
        String url = null;
        HttpUriRequest request = null;
        CloseableHttpResponse response = null;
        
        Gson prettyGson   = null;
        String jsonResponse = null;
        
        int httpStatus = -1;
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            String host = this.basePath;
            String context = "/sgp-api/movil/generar";
            url = String.join("", host, context);
            
            String jsonBody = mapper.writeValueAsString(body);
            
            request = createGetRequest(url, jsonBody);
            response = httpClient.execute(request);
            httpStatus = response.getStatusLine().getStatusCode();
            
            if(httpStatus < 200 || httpStatus >= 300)
            	throw new GestionApiException("Respuesta no satisfactoria del SGP-API.");
			
            //La solicitud si está en el rango 200
            jsonResponse = this.getResponseBody(response);
            
            prettyGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
            respuesta = prettyGson.fromJson(jsonResponse, UsuarioMovilDTO.class);
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
    
    public UsuarioMovilDTO asignarUsuarioMovil(EmpleadoDTO empleado, ControlMovil token)
    {
        UsuarioMovilDTO usuario = new UsuarioMovilDTO();
        usuario.setNumeroUsuario(empleado.getNumeroUsuario());
        usuario.setNombreUsuario(empleado.getNombreUsuario());
        usuario.setPrimerApUsuario(empleado.getPrimerApUsuario());
        usuario.setSegundoApUsuario(empleado.getSegundoApUsuario());
        usuario.setPuesto(empleado.getPuesto());
        usuario.setToken(token.getToken());
        usuario.setRefreshToken(token.getToken());

        return usuario;
    }
    
    public ControlMovil asignarToken(UsuarioMovilDTO usuario, String nombreSistema, LocalDate fechaExpiracion) 
    {
        ControlMovil token =new ControlMovil();
        token.setToken(usuario.getToken());
        token.setExpiracion(fechaExpiracion);
        token.setValido(Boolean.TRUE);
        token.setClienteSistema(nombreSistema);
        
        return token;
    }
    
    public String deshabilitarToken(String authHeader) throws GestionApiException 
    {
        String token = authHeader.replace("Bearer ", "");
        String tokenDeshabilitado = deshabilitar(token);
        log.info("Token deshabilitado de SGP-API: {}", tokenDeshabilitado);
        
        ControlMovil controlMovil = controlMovilRepo.findByToken(token);
        controlMovil.setValido(Boolean.FALSE);
        controlMovilRepo.actualizar(controlMovil);
        
        return "El proceso finalizo exitosamente";
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
            request.setHeader("Authorization", "Bearer " + token);
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
