package com.ferbo.gestion.api.service;

import java.time.LocalDate;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ferbo.gestion.api.business.SistemaAuthBL;
import com.ferbo.gestion.api.dto.EmpleadoDTO;
import com.ferbo.gestion.api.dto.SistemaDTO;
import com.ferbo.gestion.api.dto.UsuarioMovilDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.model.ControlMovil;
import com.ferbo.gestion.api.repository.ControlMovilRepo;
import com.ferbo.gestion.api.tool.SecurityTool;
import com.ferbo.gestion.api.idao.IUsuarioRepo;
import com.ferbo.gestion.core.model.sistema.Usuario;
import com.ferbo.tools.exception.RuleException;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ValidationException;
import com.ferbo.tools.util.date.DateFormatter;

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
    
    public UsuarioMovilDTO obtenerUsuario(String authHeader, UsuarioMovilDTO body) throws GestionApiException 
    {
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
        ControlMovil token = new ControlMovil();
        token.setToken(usuario.getToken());
        token.setExpiracion(fechaExpiracion);
        token.setValido(Boolean.TRUE);
        token.setUsuarioSistema(nombreSistema);
        
        return token;
    }
    
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

    private LocalDate calcularFechaExpiracionToken() {
        Date expirationDate = new Date();

        String expiracionString = DateFormatter.format(expirationDate, "dd-MM-yyyy");

        LocalDate expiracionLocalDate = DateFormatter.parseToLocalDate(expiracionString, "dd-MM-yyyy");

        LocalDate expiracion = expiracionLocalDate.plusDays(7);

        return expiracion;
    }

    public ControlMovil guardarToken(String usuarioSistema, UsuarioMovilDTO usuarioMovilDTO) {

        if (usuarioSistema == null || "".equalsIgnoreCase(usuarioSistema)) {
            throw new ValidationException("El usuario del sistema no puede estar vacío");
        }

        if (usuarioMovilDTO == null) {
            throw new ValidationException("La información del usuario movil no puede ser vacía");
        }

        String token = usuarioMovilDTO.getToken();

        if (token == null || "".equalsIgnoreCase(token)) {
            throw new RuleException("El token asignado al usuario no puede ser vaciío");
        }

        ControlMovil controlMovil = controlMovilRepo.findByUser(usuarioSistema);

        if (controlMovil == null || !token.equalsIgnoreCase(controlMovil.getToken())) {

            if (controlMovil != null) {

                controlMovil.setValido(Boolean.FALSE);
                controlMovilRepo.actualizar(controlMovil);

            }

            LocalDate expiracion = calcularFechaExpiracionToken();

            ControlMovil nuevoControlMovil = asignarToken(usuarioMovilDTO, usuarioSistema, expiracion);

            controlMovilRepo.guardar(nuevoControlMovil);

            return nuevoControlMovil;
        }

        return controlMovil;
    }
    
}
