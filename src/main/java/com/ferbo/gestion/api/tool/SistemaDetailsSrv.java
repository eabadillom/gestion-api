package com.ferbo.gestion.api.tool;

import com.ferbo.gestion.api.business.AbstractGestionApiBL;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SistemaDetailsSrv extends AbstractGestionApiBL implements UserDetailsService 
{
    private static Logger log = LogManager.getLogger(SistemaDetailsSrv.class);

    public SistemaDetailsSrv() {
        super();
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
        UserDetails user = null;
        
        try {
            user = User.withUsername(this.user)
                .password(new BCryptPasswordEncoder().encode(this.password))
                .authorities(Collections.emptyList())
                .build();
        }catch(Exception ex) {
            log.error("Problema para extraer el usuario: " + username, ex);
        }
        
        return user;
    }
    
}
