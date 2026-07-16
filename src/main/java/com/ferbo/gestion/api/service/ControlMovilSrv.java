package com.ferbo.gestion.api.service;

import java.io.IOException;
import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import com.ferbo.gestion.api.business.AbstractGestionApiBL;
import com.ferbo.gestion.api.dto.EmpleadoDTO;
import com.ferbo.gestion.api.dto.SistemaDTO;
import com.ferbo.gestion.api.dto.UsuarioMovilDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.model.ControlMovil;
import com.ferbo.gestion.api.repository.ControlMovilRepo;
import com.ferbo.gestion.api.tool.SecurityTool;
import com.ferbo.gestion.api.idao.IUsuarioRepo;
import com.ferbo.gestion.core.model.sistema.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ControlMovilSrv extends AbstractGestionApiBL
{
    private static Logger log = LogManager.getLogger(ControlMovilSrv.class);
    
    @Autowired
    private SecurityTool securityTool;
    
    @Autowired
    private ControlMovilRepo controlMovilRepo;
    
    private final IUsuarioRepo usuarioDAO;
    
    public ControlMovilSrv(IUsuarioRepo usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
    
    public UsuarioMovilDTO obtenerUsuario(HttpServletRequest request, UsuarioMovilDTO body) throws GestionApiException 
    {
        UsuarioMovilDTO usuarioDTO = null;
        
        LocalDate hoy = LocalDate.now();
        //Tiempo de expiración del token.
        LocalDate fechaExpiracion = hoy.plusDays(7);

        log.info("Inicia proceso de extraccion de credenciales");
        String[] credenciales = securityTool.extractCredentials(request);
        log.info("Finaliza proceso de extraccion de credenciales");
        
        log.info("Inicia proceso de obtencion del sistema");
        SistemaDTO sistemaReferencia = new SistemaDTO();
        sistemaReferencia.setNombre(this.user);
        sistemaReferencia.setPassword(this.password);
        log.info("Finaliza proceso de obtencion del sistema");
        
        if(!credenciales[0].equals(sistemaReferencia.getNombre()) || !credenciales[1].equals(sistemaReferencia.getPassword())){
            throw new GestionApiException("Credenciales no validas");
        }
        
        ControlMovil nuevoToken = controlMovilRepo.findByUser(sistemaReferencia.getNombre());
        Usuario usuario = usuarioDAO.buscarUsuarioPorNumero(body.getNumeroUsuario()).orElseThrow(() -> new RuntimeException("Error, usuario no encontrado"));
        
        log.info("Inicia proceso de para notificar del token existente");
        if(nuevoToken != null)
        {
            int validezToken = hoy.compareTo(nuevoToken.getExpiracion());
            log.info("El ultimo token del sistema solicitante: {}", nuevoToken);
            if(validezToken <= 0 && nuevoToken.getValido()){
                //Mandar el token existente si es valido
                log.info("Ya existe un token valido, se mantiene");
                EmpleadoDTO empleado = obtenerEmpleado(nuevoToken.getToken(), body.getNumeroUsuario());
                usuarioDTO = asignarUsuarioMovil(empleado, nuevoToken);
            } else {
                //Pedir un nuevo token a sgp api si el existente ya no es valido o expiro  
                log.info("Token no valido o expiro, pedir uno nuevo");
                usuarioDTO = obtenerUsuario(sistemaReferencia.getNombre(), sistemaReferencia.getPassword(), body);
                nuevoToken = asignarToken(usuarioDTO, sistemaReferencia.getNombre(), fechaExpiracion);
                controlMovilRepo.guardar(nuevoToken);
            }
        } else {
            //Pedir token si no existe en bd o no hay reciente
            log.info("Token no encontrado, pedir uno nuevo");
            usuarioDTO = obtenerUsuario(sistemaReferencia.getNombre(), sistemaReferencia.getPassword(), body);
            nuevoToken = asignarToken(usuarioDTO, sistemaReferencia.getNombre(), fechaExpiracion);
            controlMovilRepo.guardar(nuevoToken);
        }
        usuarioDTO.setPerfil(usuario.getPerfil().getId());
        log.info("Finaliza proceso de para notificar del token existente");
        
        return usuarioDTO;
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
        usuario.setNumeroUsuario(empleado.getNumero());
        usuario.setNombreUsuario(empleado.getNombre());
        usuario.setPrimerApUsuario(empleado.getPrimerApellido());
        usuario.setSegundoApUsuario(empleado.getSegundoApellido());
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
        token.setUsuarioSistema(nombreSistema);
        
        return token;
    }
    
    public String deshabilitarToken(String authHeader) throws GestionApiException 
    {
        String token = authHeader.replace("Bearer ", "");
        String tokenDeshabilitado = deshabilitar(authHeader);
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
