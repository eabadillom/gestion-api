package com.ferbo.gestion.api.tool;

import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ToolException;
import com.ferbo.tools.exception.ValidationException;
import java.nio.charset.StandardCharsets;
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
    
    public String[] extractBasicAuth(String authHeader) {
        String base64Credentials = authHeader.substring(6).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        return credentials.split(":", 2);
    }
    
    public String extractBearerToken(HttpServletRequest request) {

        if (request == null) {
            throw new ValidationException("La solicitud no puede ser vacía");
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.trim().isEmpty()) {
            throw new SystemException("La solicitud no incluye el header Authorization");
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new ToolException("La solicitud no incluye un bearer token");
        }

        String token = authorization.substring(7).trim();

        if (token.isEmpty()) {
            throw new ToolException("La solicitud no incluye un bearer token");
        }

        return token;
    }

    public String generarBasicAuth(String usuario, String contrasena) {
        String credentials = usuario + ":" + contrasena;

        String encoded = Base64.getEncoder()
            .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        return "Basic " + encoded;
    }
}
