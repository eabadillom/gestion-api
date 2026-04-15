package com.ferbo.gestion.api.config;

import java.util.function.Consumer;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.ferbo.gestion.core.config.TransactionManager;

@Component
public class SpringTransactManager implements TransactionManager 
{
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public <T> T executeRead(Function<EntityManager, T> action) {
        return action.apply(em);
    }

    @Override
    @Transactional
    public <T> T executeWrite(Function<EntityManager, T> action) {
        return action.apply(em);
    }

    @Override
    @Transactional
    public void executeVoid(Consumer<EntityManager> action) {
        action.accept(em);
    }
}
