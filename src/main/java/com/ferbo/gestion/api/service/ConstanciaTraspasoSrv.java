package com.ferbo.gestion.api.service;
import java.time.LocalDate;
import java.util.List;
import com.ferbo.gestion.api.dto.ConstanciaDTO;
import com.ferbo.gestion.api.idao.IConstanciaTraspasoRepo;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ConstanciaTraspasoSrv 
{
    private static Logger log = LogManager.getLogger(ConstanciaTraspasoSrv.class);
    
    private final IConstanciaTraspasoRepo constanciaTraspasoRepo;

    public ConstanciaTraspasoSrv(IConstanciaTraspasoRepo constanciaTraspasoRepo) 
    {
        this.constanciaTraspasoRepo = constanciaTraspasoRepo;
    }
    
    public List<ConstanciaDTO> consultarListas(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente) 
    {
        return this.constanciaTraspasoRepo.buscarPorParametros(fechaInicio, fechaFin, idCliente);
    }
    
    public ConstanciaDTO buscarPorFolio(String folio) 
    {
        return this.constanciaTraspasoRepo.buscarPorFolio(folio);
    }
    
}
