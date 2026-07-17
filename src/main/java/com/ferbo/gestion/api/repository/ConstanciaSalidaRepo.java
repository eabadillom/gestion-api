package com.ferbo.gestion.api.repository;

import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IConstanciaSalidaRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.inventario.salida.ConstanciaSalida;

public class ConstanciaSalidaRepo extends BaseDAO<ConstanciaSalida, Integer> implements IConstanciaSalidaRepo
{
    private static Logger log = LogManager.getLogger(ConstanciaSalidaRepo.class);
    
    public ConstanciaSalidaRepo(SpringTransactManager transactManager){
        super(ConstanciaSalida.class, transactManager);
    }

    @Override
    public List<ConstanciaSalida> buscarPorClientePlantaPeriodo(LocalDate fechaInicio, LocalDate fechaFin, Integer idCliente, Integer idPlanta, Integer idCanara) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
