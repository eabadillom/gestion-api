package com.ferbo.gestion.api.service;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.ferbo.gestion.api.config.SpringEntityManagerProvider;
import com.ferbo.gestion.api.dto.ConstanciaDepositoDTO;
import com.ferbo.gestion.api.dto.KardexFiltroDTO;
import com.ferbo.gestion.api.exception.GestionApiException;
import com.ferbo.gestion.api.idao.IConstanciaDepositoRepo;
import com.ferbo.gestion.api.mapper.IConstanciaDepositoMapper;
import com.ferbo.gestion.api.response.FileResponse;
import com.ferbo.gestion.core.model.inventario.entrada.ConstanciaDeposito;
import com.ferbo.gestion.reports.jasper.KardexJR;
import com.ferbo.gestion.reports.jasper.ReporteEntradasJR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ConstanciaDepositoSrv 
{
    private static Logger log = LogManager.getLogger(ConstanciaDepositoSrv.class);
    
    private final IConstanciaDepositoRepo constanciaDepositoRepo;
    
    @Autowired
    private SpringEntityManagerProvider entityManagerProvider;
    
    @Autowired
    private IConstanciaDepositoMapper iConstanciaDepositoMapper;
    
    private KardexJR kardexJR;
    private ReporteEntradasJR reporteEntradasJR;
    
    public ConstanciaDepositoSrv(IConstanciaDepositoRepo constanciaDepositoRepo){
        this.constanciaDepositoRepo = constanciaDepositoRepo;
    }
    
    public List<ConstanciaDepositoDTO> buscarKardex(KardexFiltroDTO filtro)
    {
        List<ConstanciaDepositoDTO> listContanciasDepositoDTO = null;
        
        if(filtro.getFolioCliente() != null && !filtro.getFolioCliente().isEmpty()) {
            listContanciasDepositoDTO = new ArrayList();
            ConstanciaDeposito constanciaDeposito = constanciaDepositoRepo.buscarPorFolio(filtro.getFolioCliente());
            listContanciasDepositoDTO.add(convertirConstanciaDeposito(constanciaDeposito));
        } else{
            listContanciasDepositoDTO = constanciaDepositoRepo.buscarPorKardex(filtro.getFechaInicio(), filtro.getFechaFin(), (filtro.getCliente() != null) ? filtro.getCliente() : null, (filtro.getPlanta() != null) ? filtro.getPlanta() : null)
                .stream()
                .map(this::convertirConstanciaDeposito).collect(Collectors.toList());
        }
        
        return listContanciasDepositoDTO;
    }
    
    public FileResponse getPdfKardex(String folioCliente) throws IOException, GestionApiException 
    {
        FileResponse pdfResponse = null;
        String filename = String.format("Kardex%s%s.pdf", folioCliente, LocalDate.now());
        String logoPath = "/images/logo.jpeg";
        Connection connection = null;
        
        try{
            connection = this.entityManagerProvider.getConnection();
            
            if(connection == null){
                throw new Exception("Error al obtener la conexion, contacte al administrador del sistema");
            }
            
            this.kardexJR = new KardexJR(connection, logoPath);
            
            byte[] fileContent = this.kardexJR.getPDF(folioCliente);

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
    
    public FileResponse getPdfEntrada(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente, Integer idPlanta, Integer idCamara) throws IOException, GestionApiException 
    {
        FileResponse pdfResponse = null;
        String filename = String.format("Entradas%s.pdf", LocalDate.now());
        String logoPath = "/images/logo.jpeg";
        Connection connection = null;
        
        try{
            connection = this.entityManagerProvider.getConnection();
            
            if(connection == null){
                throw new Exception("Error al obtener la conexion, contacte al administrador del sistema");
            }
            
            this.reporteEntradasJR = new ReporteEntradasJR(connection, logoPath);
            
            ZoneId zone = ZoneId.of("GMT-6");
            byte[] fileContent = this.reporteEntradasJR.getPDF(Date.from(fechaInicio.atStartOfDay(zone).toInstant()), Date.from(fechaFin.atStartOfDay(zone).toInstant()), idCliente, idPlanta, idCamara);

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
    
    private ConstanciaDepositoDTO convertirConstanciaDeposito(ConstanciaDeposito constanciaDeposito){
        return iConstanciaDepositoMapper.toDTO(constanciaDeposito);
    }
    
}
