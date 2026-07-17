package com.ferbo.gestion.api.repository;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IPlantaRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.catalogo.almacen.Planta;

public class PlantaRepo extends BaseDAO<Planta, Integer> implements IPlantaRepo
{
    private static Logger log = LogManager.getLogger(PlantaRepo.class);
    
    public PlantaRepo (SpringTransactManager transactManager){
        super(Planta.class, transactManager);
    }
    
    @Override
    public List<Planta> buscarActivos(Integer idPlanta) 
    {
        return transactManager.executeRead(em -> 
            em.createQuery("SELECT p FROM Planta p WHERE (:idPlanta IS NULL OR p.id = :idPlanta) AND p.habilitado = TRUE", Planta.class)
                .setParameter("idPlanta", idPlanta)
                .getResultList()
        );
    }
    
}
