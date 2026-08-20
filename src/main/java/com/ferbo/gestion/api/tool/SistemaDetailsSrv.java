package com.ferbo.gestion.api.tool;

import com.ferbo.gestion.api.business.SistemaAuthBL;
import com.ferbo.gestion.api.dto.SistemaDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class SistemaDetailsSrv implements UserDetailsService 
{
    private static Logger log = LogManager.getLogger(SistemaDetailsSrv.class);
    
    @Autowired
    private SistemaAuthBL sistemaAuthBL;

    public SistemaDetailsSrv() {
        super();
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
        UserDetails user = null;
        SistemaDTO sistemaDTO = null;
        
        try {
            sistemaDTO = sistemaAuthBL.autenticaUsuario(username);
            user = User.withUsername(sistemaDTO.getNombre())
                .password(sistemaDTO.getPassword())
                .roles(sistemaDTO.getRol())
                .build();
        } catch(Exception ex) {
            log.error("Problema para extraer el usuario: " + username, ex);
        }
        
        return user;
    }
    
}
