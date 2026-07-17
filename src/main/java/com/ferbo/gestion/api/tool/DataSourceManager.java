package com.ferbo.gestion.api.tool;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DataSourceManager 
{
    private static Logger log = LogManager.getRootLogger();
    
    public static String getJniName(String name) 
    {
        InitialContext initContext = null;
        String jniName = null;

        try {
            initContext = new InitialContext();
            jniName = (String) initContext.lookup(name);
        } catch (NamingException ex) {
            log.error(ex);
        }

        return jniName;
    }
    
    public static String getJndiParameter(String name) 
    {
        Context initContext = null;
        String parameter = null;
        try {
            initContext = new InitialContext();
            parameter = (String) initContext.lookup(name);

        } catch (NamingException ex) {
            try {
                Context envContext = (Context) initContext.lookup("java:/comp/env");
                parameter = (String) envContext.lookup(name);
            } catch (NamingException inEx) {
                log.error("Problema para obtener el valor JNDI: " + name, inEx);
            }
        }

        return parameter;
    }
    
}
