package com.ferbo.gestion.api.config;

import com.ferbo.gestion.core.provider.EntityManagerProvider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class SpringEntityManagerProvider implements EntityManagerProvider 
{
    @PersistenceContext
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void close(EntityManager em) {
    }
    
}
