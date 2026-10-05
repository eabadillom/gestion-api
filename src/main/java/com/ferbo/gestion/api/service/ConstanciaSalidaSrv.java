package com.ferbo.gestion.api.service;

import com.ferbo.gestion.api.config.SpringEntityManagerProvider;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.idao.IConstanciaSalidaRepo;
import com.ferbo.gestion.api.response.FileResponse;
import com.ferbo.gestion.reports.jasper.ReporteSalidasJR;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstanciaSalidaSrv 
{
    private static Logger log = LogManager.getLogger(ConstanciaSalidaSrv.class);
    
    private final IConstanciaSalidaRepo constanciaSalidaRepo;
    
    @Autowired
    private SpringEntityManagerProvider entityManagerProvider;
    
    private ReporteSalidasJR reporteSalidasJR;

    public ConstanciaSalidaSrv(IConstanciaSalidaRepo constanciaSalidaRepo) 
    {
        this.constanciaSalidaRepo = constanciaSalidaRepo;
    }
    
    public FileResponse getPdfSalida(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente,  Integer idPlanta, Integer idCamara) throws IOException, GestionApiException 
    {
        FileResponse pdfResponse = null;
        String filename = String.format("Salidas%s.pdf", LocalDate.now());
        String logoPath = "/images/logo.jpeg";
        Connection connection = null;
        
        try{
            connection = this.entityManagerProvider.getConnection();
            
            if(connection == null){
                throw new Exception("Error al obtener la conexion, contacte al administrador del sistema");
            }
            
            this.reporteSalidasJR = new ReporteSalidasJR(connection, logoPath);
            
            ZoneId zone = ZoneId.of("GMT-6");
            byte[] fileContent = this.reporteSalidasJR.getPDF(Date.from(fechaInicio.atStartOfDay(zone).toInstant()), Date.from(fechaFin.atStartOfDay(zone).toInstant()), idCliente, idPlanta, idCamara);

            if (fileContent == null || fileContent.length == 0) {
                throw new GestionApiException("El PDF no se generó correctamente.");
            }

            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            pdfResponse = new FileResponse(filename, encodedString);
        } catch(Exception ex) {
            this.entityManagerProvider.close(connection);
            throw new GestionApiException("Problema en el procesamiento del reporte del kardex (PDF)...", ex);
        } finally{
            this.entityManagerProvider.close(connection);
        }
        
        return pdfResponse;
    }
    
    public List<ConstanciaDTO> consultarListas(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) 
    {
        return this.constanciaSalidaRepo.buscarPorParametros(fechaInicio, fechaFin, idCliente);
    }
    
    public ConstanciaDTO buscarPorFolio(String folio) 
    {
        return this.constanciaSalidaRepo.buscarPorFolio(folio);
    }
    
}
