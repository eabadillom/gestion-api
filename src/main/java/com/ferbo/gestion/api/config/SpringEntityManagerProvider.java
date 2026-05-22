package com.ferbo.gestion.api.config;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.ferbo.gestion.core.provider.EntityManagerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class SpringEntityManagerProvider implements EntityManagerProvider 
{
    private static Logger log = LogManager.getLogger(SpringEntityManagerProvider.class);
    
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private DataSource dataSource;

    @Override
    public synchronized EntityManager getEntityManager() {
        return em;
    }

    @Override
    public synchronized void close(EntityManager em) {
    }

    public synchronized Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public synchronized void close(Connection connection) 
    {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            log.error("Problema al cerrar el objeto Connection.", ex);
        } catch (Exception ex) {
            log.error("Problema general al cerrar el objeto Connection.", ex);
        } finally {
            connection = null;
        }
    }
    
}
