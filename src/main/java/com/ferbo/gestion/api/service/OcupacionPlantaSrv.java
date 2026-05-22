package com.ferbo.gestion.api.service;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import com.ferbo.gestion.api.config.SpringEntityManagerProvider;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.idao.IOcupacionPlantaRepo;
import com.ferbo.gestion.api.response.FileResponse;
import com.ferbo.gestion.api.model.OcupacionPlanta;
import com.ferbo.gestion.reports.jasper.ReporteOcupacionCamaraJR;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OcupacionPlantaSrv 
{
    private static Logger log = LogManager.getLogger(OcupacionPlantaSrv.class);
    
    private final IOcupacionPlantaRepo ocupacionPlantaDAO;
    
    @Autowired
    private SpringEntityManagerProvider entityManagerProvider;
    
    private ReporteOcupacionCamaraJR reporteOcupacionCamara;
    
    public OcupacionPlantaSrv(IOcupacionPlantaRepo ocupacionPlantaDAO){
        this.ocupacionPlantaDAO = ocupacionPlantaDAO;
    }
    
    public List<OcupacionPlanta> buscarOcupacionCamara(LocalDate fecha, List<Integer> listCliente, Integer idPlanta, Integer idCamara){
        return ocupacionPlantaDAO.ocupacionPlantaCamara(fecha, listCliente, idPlanta, idCamara);
    }
    
    public FileResponse getPdfAsBase64(LocalDate fecha, List<Integer> listCliente, Integer idCamara) throws IOException, GestionApiException 
    {
        FileResponse pdfResponse = null;
        String filename = String.format("OcupacionCamaras%s.pdf", fecha);
        String logoPath = "/images/logo.jpeg";
        Connection connection = null;
        
        try{
            connection = this.entityManagerProvider.getConnection();
            
            if(connection == null){
                throw new Exception("Error al obtener la conexion, contacte al administrador del sistema");
            }
            
            this.reporteOcupacionCamara = new ReporteOcupacionCamaraJR(connection, logoPath);
            
            ZoneId zone = ZoneId.of("GMT-6");
            byte[] fileContent = this.reporteOcupacionCamara.getPDFReporteOcupacionCamara(Date.from(fecha.atStartOfDay(zone).toInstant()), listCliente, idCamara);

            if (fileContent == null || fileContent.length == 0) {
                throw new GestionApiException("El PDF no se generó correctamente.");
            }

            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            pdfResponse = new FileResponse(filename, encodedString);
        } catch(Exception ex) {
            this.entityManagerProvider.close(connection);
            throw new GestionApiException("Problema en el procesamiento del reporte (PDF) de ocupacion planta...", ex);
        } finally{
            this.entityManagerProvider.close(connection);
        }
        
        
        return pdfResponse;
    }
    
}
