package com.ferbo.gestion.api.service;

import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ferbo.gestion.api.business.SistemaAuthBL;
import com.ferbo.gestion.api.dto.EmpleadoDTO;
import com.ferbo.gestion.api.dto.UsuarioMovilDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.idao.IUsuarioRepo;
import com.ferbo.gestion.api.model.ControlMovil;
import com.ferbo.gestion.api.repository.ControlMovilRepo;
import com.ferbo.gestion.api.tool.SecurityTool;
import com.ferbo.tools.exception.RuleException;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ToolException;
import com.ferbo.tools.exception.ValidationException;

@Service
public class ControlMovilSrv 
{
    private static Logger log = LogManager.getLogger(ControlMovilSrv.class);
    
    @Autowired
    private SecurityTool securityTool;
    
    @Autowired
    private ControlMovilRepo controlMovilRepo;
    
    @Autowired
    private SistemaAuthBL sistemaAuthBL;
    
    private final IUsuarioRepo usuarioDAO;
    
    public ControlMovilSrv(IUsuarioRepo usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
    
   /*  public UsuarioMovilDTO obtenerUsuario(String authHeader, UsuarioMovilDTO body) throws GestionApiException {
        UsuarioMovilDTO usuarioDTO = null;
        
        LocalDate hoy = LocalDate.now();
        //Tiempo de expiración del token.
        LocalDate fechaExpiracion = hoy.plusDays(7);

        log.info("Inicia proceso de extraccion de credenciales");
        String[] credenciales = securityTool.extractBasicAuth(authHeader);
        log.info("Finaliza proceso de extraccion de credenciales");
        
        log.info("Inicia proceso de obtencion del sistema"); 
        SistemaDTO sistemaReferencia = sistemaAuthBL.autenticaUsuario(credenciales[0]); 
        log.info("Finaliza proceso de obtencion del sistema");
        
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean passCorrecta = encoder.matches(credenciales[1], sistemaReferencia.getPassword());
        
        if(!credenciales[0].equals(sistemaReferencia.getNombre()) || !passCorrecta){
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
                EmpleadoDTO empleado = sistemaAuthBL.obtenerEmpleado(nuevoToken.getToken(), body.getNumeroUsuario());
                usuarioDTO = asignarUsuarioMovil(empleado, nuevoToken);
            } else {
                //Pedir un nuevo token a sgp api si el existente ya no es valido o expiro  
                log.info("Token no valido o expiro, pedir uno nuevo");
                usuarioDTO = sistemaAuthBL.obtenerUsuario(credenciales[0], credenciales[1], body);
                nuevoToken = asignarToken(usuarioDTO, sistemaReferencia.getNombre(), fechaExpiracion);
                controlMovilRepo.guardar(nuevoToken);
            }
        } else {
            //Pedir token si no existe en bd o no hay reciente
            log.info("Token no encontrado, pedir uno nuevo");
            usuarioDTO = sistemaAuthBL.obtenerUsuario(credenciales[0], credenciales[1], body);
            nuevoToken = asignarToken(usuarioDTO, sistemaReferencia.getNombre(), fechaExpiracion);
            controlMovilRepo.guardar(nuevoToken);
        }
        usuarioDTO.setPerfil(usuario.getPerfil().getId());
        log.info("Finaliza proceso de para notificar del token existente");
        
        return usuarioDTO;
    }*/
    
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
    
    /*public ControlMovil asignarToken(UsuarioMovilDTO usuario, String nombreSistema, LocalDate fechaExpiracion) 
    {
        ControlMovil token = new ControlMovil();
        token.setToken(usuario.getToken());
        token.setExpiracion(fechaExpiracion);
        token.setValido(Boolean.TRUE);
        token.setUsuarioSistema(nombreSistema);
        
        return token;
    }*/
    
    public String deshabilitarToken(String authHeader) throws GestionApiException 
    {
        String token = authHeader.replace("Bearer ", "");
        String tokenDeshabilitado = sistemaAuthBL.deshabilitar(authHeader);
        log.info("Token deshabilitado de SGP-API: {}", tokenDeshabilitado);
        
        ControlMovil controlMovil = controlMovilRepo.findByToken(token);
        controlMovil.setValido(Boolean.FALSE);
        controlMovilRepo.actualizar(controlMovil);
        
        return "El proceso finalizo exitosamente";
    }
    
    public ControlMovil deshabilitarPorToken(String token) {
        
        if (token == null || "".equalsIgnoreCase(token)) {
            throw new ValidationException("El tokne no puede ser vacío");
        }
        
        ControlMovil controlMovil = controlMovilRepo.findByToken(token);
        
        if (controlMovil == null) {
            throw new SystemException("El token no se encuentra registrado en el sistema");
        }
        
        if (!controlMovil.getValido()) {
            throw new RuleException("El token ya se encuentra desahabilitado");
        }
        
        controlMovil.setValido(Boolean.FALSE);
        
        controlMovilRepo.actualizar(controlMovil);
        
        return controlMovil;
    } 

    public synchronized void deshabilitarPorDemanda(ControlMovil controlMovil) {

        if (controlMovil == null) {
            throw new ValidationException("El control movil a deshabilitar no puede ser vacío");
        }

        if (!controlMovil.getValido()) {
            throw new RuleException("El token ya se encuentra desahabilitado");
        }

        controlMovil.setValido(Boolean.FALSE);

        controlMovilRepo.actualizar(controlMovil);


    }

    private LocalDate calcularFechaExpiracionToken() {
        return LocalDate.now(ZoneId.of("America/Mexico_City")).plusDays(7);
    }

    private ControlMovil crearNuevoControlMovil(String token, String usuarioSistema) {
        ControlMovil controlMovil = new ControlMovil();
            controlMovil.setToken(token);
            controlMovil.setUsuarioSistema(usuarioSistema);
            LocalDate fechaExpiracion = calcularFechaExpiracionToken(); 
            controlMovil.setExpiracion(fechaExpiracion);
            controlMovil.setValido(Boolean.TRUE);
            return controlMovil;
    }

    public synchronized ControlMovil construirUsuarioMovilCompleto(
            String usuarioSistema,
            UsuarioMovilDTO usuarioMovilDTO,
            String token) {

        if (usuarioSistema == null || usuarioSistema.trim().isEmpty()) {
            throw new ValidationException(
                    "El usuario del sistema no puede estar vacío");
        }

        if (usuarioMovilDTO == null) {
            throw new ValidationException(
                    "La información del usuario móvil no puede estar vacía");
        }

        if (token == null || token.trim().isEmpty()) {
            throw new ValidationException(
                    "El token del usuario no puede estar vacío");
        }

        ControlMovil controlMovil = controlMovilRepo.findByUser(usuarioSistema);

        if (controlMovil == null || !controlMovil.getValido()) {
            return crearNuevoControlMovil(token, usuarioSistema);
        }

        LocalDate hoy = LocalDate.now(
                ZoneId.of("America/Mexico_City"));

        if (controlMovil.getExpiracion().isBefore(hoy)) {
            deshabilitarPorDemanda(controlMovil);

            return crearNuevoControlMovil(token, usuarioSistema);
        }

        return controlMovil;
    }

    public synchronized ControlMovil guardarControlMovil(ControlMovil controlMovil) {

        if (controlMovil == null) {
            throw  new ValidationException("El control movil a guardar no pueder ser vacío");
        }

        if (controlMovil.getId() == null) {
            controlMovilRepo.guardar(controlMovil);
        }

        return  controlMovil;
    }
    

    public UsuarioMovilDTO completarUsuarioMovil(UsuarioMovilDTO usuario, ControlMovil controlMovil, int perfil) {

        usuario.setToken(controlMovil.getToken());
        usuario.setRefreshToken(controlMovil.getToken());
        usuario.setPerfil(perfil);

        return usuario;
    }

    public int extrarPerfilDelUsuarioDesdeToken(UsuarioMovilDTO usuarioMovilDTO) {

        if (usuarioMovilDTO == null) {
            throw new ValidationException("El usuario movil no puede ser vacío");
        }

        int perfil = -1;

        try {
            perfil = Integer.parseInt(usuarioMovilDTO.getToken());
            usuarioMovilDTO.setToken(null);
        } catch (NumberFormatException e) {
            throw new ToolException("El texto recibido no es un número válido.");
        }
        return perfil;
    }

    public  String obtenerSistemaPorToken(String token) {

        if (token == null || token.trim().isEmpty()) {
            throw new ValidationException("El token no puede ser vacío");
        }

        ControlMovil controlMovil = controlMovilRepo.findByToken(token);

        if (controlMovil == null) {
            throw new RuleException("El toke recibido no se encuentra registrado en el sistema");
        }

        String sistema = controlMovil.getUsuarioSistema();

        return  sistema;

    }
}
