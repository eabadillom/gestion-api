package com.ferbo.gestion.api.repository;

import java.util.List;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ferbo.gestion.api.idao.ICamaraRepo;
import com.ferbo.gestion.core.model.catalogo.almacen.Camara;

public class CamaraRepo extends BaseDAO<Camara, Integer> implements ICamaraRepo
{
    private static Logger log = LogManager.getLogger(CamaraRepo.class);
    
    public CamaraRepo(SpringTransactManager transactManager){
        super(Camara.class, transactManager);
    }

    @Override
    public List<Camara> buscarPorPlanta(Integer idPlanta) 
    {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT c FROM Camara c WHERE (:idPlanta IS NULL OR c.planta.id = :idPlanta)", Camara.class)
                .setParameter("idPlanta", idPlanta)
                .getResultList();
        });
    }
    
}
