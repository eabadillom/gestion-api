package com.ferbo.gestion.api.tool;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SecurityTool 
{
    private static Logger log = LogManager.getLogger(SecurityTool.class);
    
    public String[] extractCredentials(HttpServletRequest request) 
    {
        String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            
            String base64Credentials = authorizationHeader.substring(6);
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            String[] values = credentials.split(":", 2);
            return values;
        }

        return null; 
    }
}
