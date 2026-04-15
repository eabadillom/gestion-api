package com.ferbo.gestion.api.config;

import com.ferbo.gestion.api.repository.ControlMovilRepo;
import com.ferbo.gestion.core.dao.AsentamientoDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfig 
{
    @Bean
    public AsentamientoDAO asentamientoDAO(SpringTransactManager transactManager){
        return new AsentamientoDAO(transactManager);
    }
    
    @Bean
    public ControlMovilRepo controlMovilRepo(SpringTransactManager transactManager) {
        return new ControlMovilRepo(transactManager);
    }
    
}
