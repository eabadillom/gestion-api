package com.ferbo.gestion.api.config;

import com.ferbo.gestion.api.repository.AsentamientoRepo;
import com.ferbo.gestion.api.repository.CamaraRepo;
import com.ferbo.gestion.api.repository.ClienteRepo;
import com.ferbo.gestion.api.repository.ConstanciaDepositoRepo;
import com.ferbo.gestion.api.repository.ConstanciaSalidaRepo;
import com.ferbo.gestion.api.repository.ControlMovilRepo;
import com.ferbo.gestion.api.repository.PlantaRepo;
import com.ferbo.gestion.api.repository.UsuarioRepo;
import com.ferbo.gestion.core.dao.cliente.detalle.CandadoSalidaDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfig 
{
    @Bean
    public AsentamientoRepo asentamientoRepo(SpringTransactManager transactManager) {
        return new AsentamientoRepo(transactManager);
    }
    
    @Bean
    public CamaraRepo camaraRepo (SpringTransactManager transactManager) {
        return new CamaraRepo(transactManager);
    }
    
    @Bean 
    public CandadoSalidaDAO candadoDAO (SpringTransactManager transactManager){
        return new CandadoSalidaDAO(transactManager);
    }
    
    @Bean
    public ClienteRepo clienteRepo (SpringTransactManager transactManager) {
        return new ClienteRepo(transactManager);
    }
    
    @Bean
    public ControlMovilRepo controlMovilRepo(SpringTransactManager transactManager) {
        return new ControlMovilRepo(transactManager);
    }
    
    @Bean 
    public ConstanciaDepositoRepo constanciaDepositoRepo(SpringTransactManager transactManager){
        return new ConstanciaDepositoRepo(transactManager);
    }
    
    @Bean
    public ConstanciaSalidaRepo constanciaSalidaRepo(SpringTransactManager transactManager){
        return new ConstanciaSalidaRepo(transactManager);
    }
    
    @Bean
    public PlantaRepo plantaRepo(SpringTransactManager transactManager){
        return new PlantaRepo(transactManager);
    }
    
    @Bean
    public UsuarioRepo usuarioRepo(SpringTransactManager transactManager) {
        return new UsuarioRepo(transactManager);
    }
    
}