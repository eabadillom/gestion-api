package com.ferbo.gestion.api.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.service.ClienteSrv;
import com.ferbo.gestion.api.tool.IOUtil;
import com.ferbo.gestion.core.model.cliente.Cliente;
import com.ferbo.gestion.core.model.cliente.contacto.ClienteContacto;
import com.ferbo.gestion.core.model.cliente.contacto.Contacto;
import com.ferbo.gestion.core.model.cliente.contacto.Mail;
import com.ferbo.gestion.core.model.cliente.contacto.MedioContacto;
import com.ferbo.gestion.core.model.sistema.Usuario;
import com.ferbo.mail.MailHelper;
import com.ferbo.mail.beans.Adjunto;
import com.ferbo.mail.beans.Correo;
import com.ferbo.tools.MailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMailEvidenciasBL 
{
    private static Logger log = LogManager.getLogger(SendMailEvidenciasBL.class);
    
    @Autowired
    private ClienteSrv clienteSrv;
    
    private List<Adjunto> alFiles = null;
    List<Correo> alTo = null;
    List<Correo> alCC = null;
    private String folioCliente = null;
    Cliente cliente = null;
    Integer idCliente = null;
    Usuario loggedUser = null;

    public SendMailEvidenciasBL() 
    {
        alTo = new ArrayList<Correo>();
        alCC = new ArrayList<Correo>();
    }
    
    public void send() throws GestionApiException 
    {
    	try 
        {
            this.processContacts();
            this.processUsers();
            this.sendMail();
        } catch(Exception ex) {
            log.error("Problema en el envío por correo de las evidencias...", ex);
            throw new GestionApiException("Problema en el envío de las evidencias del correo.");
    	} finally {
            limpiarListas();
        }
    	
    }
    
    public void addAttachment(String nombreArchivo, String tipoArchivo, byte[] contenido) throws MailException 
    {
        Adjunto archivo = new Adjunto(nombreArchivo, tipoArchivo, contenido);
        this.addAttachment(archivo);
    }

    public void addAttachment(Adjunto adjunto) throws MailException 
    {
        if(alFiles == null)
            alFiles = new ArrayList<Adjunto>();

        if(adjunto == null)
            throw new MailException("El archivo indicado es incorrecto o no se ha seleccionado.");

        if(adjunto.getNombreArchivo() == null || "".equalsIgnoreCase(adjunto.getNombreArchivo()))
            throw new MailException("Debe indicar un nombre de archivo.");

        if(adjunto.getTipoArchivo() == null || "".equalsIgnoreCase(adjunto.getTipoArchivo()))
            throw new MailException("Debe inidcar un tipo de archivo.");

        if(adjunto.getContenido() == null || adjunto.getContenido().length <= 0)
            throw new MailException("El contenido del archivo se encuentra vacío.");

        switch(adjunto.getTipoArchivo()) {
            case Adjunto.TP_ARCHIVO_JPEG:
            case Adjunto.TP_ARCHIVO_PDF:
            case Adjunto.TP_ARCHIVO_PNG:
            case Adjunto.TP_ARCHIVO_XLS:
            case Adjunto.TP_ARCHIVO_XML:
            case Adjunto.TP_ARCHIVO_ZIP:
            case Adjunto.TP_ARCHIVO_GENERICO:
                alFiles.add(adjunto);
                break;
            default:
                throw new MailException("Tipo de archivo no soportado.");
        }
    }
    
    public void processContacts() throws GestionApiException 
    {
        try{
            if(idCliente == null) {
                throw new GestionApiException("Problema al obtener datos del cliente.");
            }
            
            cliente = clienteSrv.buscarPorId(idCliente);
            List<ClienteContacto> clienteContactoList = cliente.getClienteContactoList();
            for(ClienteContacto cteContacto : clienteContactoList) {
                Contacto contacto = cteContacto.getContacto();

                if(cteContacto.getRecibeInventario() == false) {
                    continue;
                }
                processMail(contacto);
            }
        } catch(GestionApiException ex) {
            log.error("Problema en el procesamiento de los contactos del cliente...", ex);
            throw new GestionApiException("Problema en el procesamiento de los contactos del cliente.");
    	}
    }
    
    private void processMail(Contacto contacto) throws GestionApiException 
    {
        try{
            List<MedioContacto> medioCntList = contacto.getMediosContacto();
            for(MedioContacto medioCnt : medioCntList) 
            {
                Mail mail = medioCnt.getMail();
                String nombreBuzon = String.format("%s, %s, %s", contacto.getNombre(), contacto.getPrimerApellido(), contacto.getSegundoApellido());
                Correo correo = new Correo(mail.getMail(), nombreBuzon);
                alTo.add(correo);
            }
        } catch(Exception ex) {
            log.error("Problema en el procesamiento del correo...", ex);
            throw new GestionApiException("Problema al obtener el correo.");
    	}
    }
    
    public void processUsers() throws GestionApiException 
    {
        try{
            String destinatario = null;
            String sMail = null;
            Correo mailLoggedUser = null;

            if(this.loggedUser == null) {
                return;
            }

            destinatario = String.format("%s %s %s",
                this.loggedUser.getNombre() == null ? "" : this.loggedUser.getNombre(),
                this.loggedUser.getApellido1() == null ? "" : this.loggedUser.getApellido1(),
                this.loggedUser.getApellido2() == null ? "" : this.loggedUser.getApellido2());

            sMail = this.loggedUser.getMail();
            mailLoggedUser = new Correo(sMail, destinatario);
            alCC.add(mailLoggedUser);
        } catch(Exception ex) {
            log.error("Problema al obtener el correo del almacenista...", ex);
            throw new GestionApiException("Problema al procesar el correo del almacenista.");
    	}
    }
    
    private void sendMail() throws IOException, MailException, GestionApiException  
    {
        String     mailHTML = null;
        File       mailFile = null;
        FileReader mailReader = null;
        BufferedReader reader = null;
        StringBuilder sb = null;
        String subject = null;
        try{
            subject = String.format("Envio de evidencias del folio %s - FERBO ", this.folioCliente);
            log.info("Asunto: {}", subject);
            mailHTML = "/mail/mailEnvioEvidencias.html";
            mailFile = new File( getClass().getResource(mailHTML).getFile());
            mailReader = new FileReader(mailFile);
            reader = new BufferedReader(mailReader);
            sb = new StringBuilder();
            String linea = null;
            while((linea = reader.readLine()) != null) {
                sb.append(linea);
            }
            
            String html = sb.toString();
            html = html.replace("@folio@", folioCliente);
            
            MailHelper helper = new MailHelper();
            for(Correo m : alTo) {
                helper.addTo(m);
            }
            
            for(Correo m : alCC) {
                helper.addCC(m);
            }       
            
            helper.setSubject(subject);
            helper.setMailBody(html);
            
            for(Adjunto adjunto : alFiles) {
                helper.addAttachment(adjunto);
            }
            
            helper.sendMessage(MailHelper.JNDI_MAIL_INVENTARIO);
        } catch (Exception ex){
            log.error("Problema general para el envio de evidencias ", ex);
            throw new GestionApiException("Problema general al enviar las evidencias mediante el correo.");
        } finally {
            IOUtil.close(reader);
        }
    }
    
    public void limpiarListas() {
        alFiles = null;
        alTo.clear();
        alCC.clear();
    }

    public List<Adjunto> getAlFiles() {
        return alFiles;
    }

    public void setAlFiles(List<Adjunto> alFiles) {
        this.alFiles = alFiles;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getFolioCliente() {
        return folioCliente;
    }

    public void setFolioCliente(String folioCliente) {
        this.folioCliente = folioCliente;
    }

    public Usuario getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Usuario loggedUser) {
        this.loggedUser = loggedUser;
    }
    
}
