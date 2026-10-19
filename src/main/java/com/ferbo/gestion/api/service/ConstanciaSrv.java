package com.ferbo.gestion.api.service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import com.ferbo.gestion.api.business.SendMailEvidenciasBL;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.dto.ConstanciaDetalleDTO;
import com.ferbo.gestion.api.dto.TipoConstanciaDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.idao.IConstanciaStrategyRepo;
import com.ferbo.gestion.api.model.Foto;
import com.ferbo.gestion.core.model.inventario.entrada.ConstanciaDeposito;
import com.ferbo.gestion.core.model.inventario.salida.ConstanciaSalida;
import com.ferbo.gestion.core.model.inventario.servicio.ConstanciaServicio;
import com.ferbo.gestion.core.model.inventario.traspaso.ConstanciaTraspaso;
import com.ferbo.gestion.core.model.sistema.Usuario;
import com.ferbo.tools.MailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ConstanciaSrv 
{
    private static Logger log = LogManager.getLogger(ConstanciaSrv.class);
    
    private final Map<TipoConstanciaDTO, IConstanciaStrategyRepo> estrategias = new EnumMap<>(TipoConstanciaDTO.class);
    
    private static final long MAX_BYTES_GMAIL = 25 * 1024 * 1024;
    
    @Autowired
    private ConstanciaDepositoSrv constanciaDepositoSrv;
    
    @Autowired
    private ConstanciaSalidaSrv constanciaSalidaSrv;
    
    @Autowired
    private ConstanciaServiciosSrv constanciaServicioSrv;
    
    @Autowired
    private ConstanciaTraspasoSrv constanciaTraspasoSrv;
    
    @Autowired
    private UsuarioSrv usuarioSrv; 
    
    @Autowired
    private SendMailEvidenciasBL sendMailEvidenciasBL; 
    
    public ConstanciaSrv(List<IConstanciaStrategyRepo> lista) 
    {
        for (IConstanciaStrategyRepo e : lista) {
            if (estrategias.put(e.tipo(), e) != null) {
                throw new IllegalStateException("Estrategia duplicada para " + e.tipo());
            }
        }
        
        for (TipoConstanciaDTO t : TipoConstanciaDTO.values()) {
            if (!estrategias.containsKey(t)) {
                log.warn("Todavía no hay estrategia de consulta para {}", t);
                throw new IllegalStateException("Falta estrategia de consulta para " + t);
            }
        }
    }

    public List<ConstanciaDTO> listarConstancias(TipoConstanciaDTO tipo, Integer idCliente, LocalDate inicio, LocalDate fin, String folioCliente) 
    {
        boolean hayFolio = folioCliente != null && !folioCliente.trim().isEmpty();
        boolean hayFechas = inicio != null || fin != null;
        
        if (!hayFolio && !hayFechas) {
            throw new IllegalArgumentException("Indique un folio de cliente o fecha de inicio y fecha de fin");
        }
        
        IConstanciaStrategyRepo e = estrategias.get(tipo);
        
        if (e == null) {
            throw new IllegalArgumentException("Consulta no implementada para " + tipo);
        }
        
        if (hayFolio) {
            log.info("Buscando la constancia de {} por el parametro de {}.", tipo, folioCliente);
            try {
                ConstanciaDTO dto = e.buscarPorFolio(folioCliente.trim());
                return dto == null ? Collections.<ConstanciaDTO>emptyList() : Collections.singletonList(dto);
            } catch (NoResultException ex) {
                String resultado = String.format("No se encontró ninguna constancia con el folio %s", folioCliente);
                log.info(resultado);
                throw new IllegalArgumentException(resultado);
            }
        } 
        
        log.info("Buscando las constancias por los parametros de idCliente: {}, fecha de inicio: {} y fecha fin: {}", idCliente, inicio, fin);
        return e.listarConstancias(idCliente, inicio, fin);
    }
    
    public ConstanciaDetalleDTO obtenerDetalle(TipoConstanciaDTO tipo, Integer id) throws GestionApiException
    {
        IConstanciaStrategyRepo e = estrategias.get(tipo);
        if (e == null) {
            throw new IllegalArgumentException("Consulta no implementada para " + tipo);
        }
        
        log.info("Buscando la constancia de {} por el parametro dado.", tipo);

        ConstanciaDetalleDTO dto = e.buscarDetalle(id);
        if (dto == null) {
            String resultado = String.format("No se encontró la constancia %s con el parametro dado", tipo);
            log.error(resultado);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, resultado);
        }
        
        return dto;
    }
    
    public String enviarEvidencia(TipoConstanciaDTO tipo, Integer id, List<Foto> listFotos, String numEmpleado) throws MailException, GestionApiException 
    {
        String resultado = null;
        
        IConstanciaStrategyRepo e = estrategias.get(tipo);
        if (e == null) {
            throw new IllegalArgumentException("Consulta no implementada para " + tipo);
        }
        
        if(listFotos == null || listFotos.isEmpty()) {
            throw new IllegalArgumentException("La lista de evidencias esta vacia.");
        }
        
        if(numEmpleado == null || numEmpleado.isEmpty()) {
            throw new IllegalArgumentException("El numero del empleado viene nulo o vacio.");
        }
        
        this.validarTamanioFotos(listFotos);
        
        for(Foto foto : listFotos) 
        {
            byte[] contenidoFoto = Base64.getDecoder().decode(foto.getBase64());
            sendMailEvidenciasBL.addAttachment(foto.getNombre(), foto.getMimeType(), contenidoFoto);
        }
        
        log.info("Buscando la constancia de {} por el parametro dado.", tipo);
        Usuario usuario = usuarioSrv.buscarUsuarioPorNumero(numEmpleado);
        sendMailEvidenciasBL.setLoggedUser(usuario);
        switch(tipo)
        {
            case DEPOSITO:
                ConstanciaDeposito constanciaDeposito = this.constanciaDepositoSrv.buscarConstanciaPorId(id);
                log.info("Enviando correo con evidencias para la constancia de {} con el folio: {}", tipo, constanciaDeposito.getFolioCliente());
                sendMailEvidenciasBL.setFolioCliente(constanciaDeposito.getFolioCliente());
                sendMailEvidenciasBL.setIdCliente(constanciaDeposito.getCliente().getId());
                resultado = String.format("Evidencia enviada exitosamente para la constancia de %s con el folio %s", tipo, constanciaDeposito.getFolioCliente());
                break;
            case SALIDA:
                ConstanciaSalida constanciaSalida = this.constanciaSalidaSrv.buscarConstanciaPorId(id);
                log.info("Enviando correo con evidencias para la constancia de {} con el folio: {}", tipo, constanciaSalida.getNumero());
                sendMailEvidenciasBL.setFolioCliente(constanciaSalida.getNumero());
                sendMailEvidenciasBL.setIdCliente(constanciaSalida.getClientee().getId());
                resultado = String.format("Evidencia enviada exitosamente para la constancia de %s con el folio %s", tipo, constanciaSalida.getNumero());
                break;
            case SERVICIO:
                ConstanciaServicio constanciaServicio = this.constanciaServicioSrv.buscarConstanciaPorId(id);
                log.info("Enviando correo con evidencias para la constancia de {} con el folio: {}", tipo, constanciaServicio.getFolioCliente());
                sendMailEvidenciasBL.setFolioCliente(constanciaServicio.getFolioCliente());
                sendMailEvidenciasBL.setIdCliente(constanciaServicio.getCliente().getId());
                resultado = String.format("Evidencia enviada exitosamente para la constancia de %s con el folio %s", tipo, constanciaServicio.getFolioCliente());
                break;
            case TRASPASO:
                ConstanciaTraspaso constanciaTraspaso = this.constanciaTraspasoSrv.buscarConstanciaPorId(id);
                log.info("Enviando correo con evidencias para la constancia de {} con el folio: {}", tipo, constanciaTraspaso.getNumero());
                sendMailEvidenciasBL.setFolioCliente(constanciaTraspaso.getNumero());
                sendMailEvidenciasBL.setIdCliente(constanciaTraspaso.getCliente().getId());                
                resultado = String.format("Evidencia enviada exitosamente para la constancia de %s con el folio %s", tipo, constanciaTraspaso.getNumero());
                break;
        }
        
        sendMailEvidenciasBL.send();
        
        return resultado;
    }
    
    private void validarTamanioFotos(List<Foto> fotos) 
    {
        if (fotos == null || fotos.isEmpty()) {
            throw new IllegalArgumentException("La lista de fotos está vacía.");
        }

        long tamanoTotalBytes = 0;

        for (Foto foto : fotos) {
            tamanoTotalBytes += foto.getTamBytes();
        }

        double tamanoTotalMB = (double) tamanoTotalBytes / (1024 * 1024);

        if (tamanoTotalBytes > MAX_BYTES_GMAIL) 
        {
            throw new IllegalArgumentException(String.format("El peso total de las imágenes es %.2f MB y supera el límite de 25 MB permitido por Gmail.", tamanoTotalMB));
        }

        log.info(String.format("Validación correcta, peso total de fotos = %.2f MB", tamanoTotalMB));
    }
    
}
