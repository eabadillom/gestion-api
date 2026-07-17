package com.ferbo.gestion.api.repository;

import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IInventarioRepo;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InventarioRepo implements IInventarioRepo
{
    private static Logger log = LogManager.getLogger(InventarioRepo.class);
    
    @Autowired
    private SpringTransactManager transactManager;

    @Override
    public BigDecimal getCantidad(Integer idCliente, LocalDate fecha) 
    {
        return transactManager.executeRead(em -> {
            try {
                String sql = "select sum(cantidad) as cantidad\n"
                    + "from (\n"
                    + "	select\n"
                    + "		cdd.CTE_CVE,\n"
                    + "		(p.CANTIDAD_TOTAL - coalesce(sal.cantidad, 0)) as cantidad,\n"
                    + "		(p.PESO_TOTAL - coalesce(sal.peso, 0)) as peso\n"
                    + "	from constancia_de_deposito cdd\n"
                    + "	inner join partida p on cdd.FOLIO = p.FOLIO\n"
                    + "	left outer join (\n"
                    + "		select\n"
                    + "			dcs.PARTIDA_CVE,\n"
                    + "			SUM(dcs.CANTIDAD) as CANTIDAD,\n"
                    + "			SUM(dcs.PESO) as PESO\n"
                    + "		from constancia_salida cs \n"
                    + "		inner join detalle_constancia_salida dcs on cs.ID = dcs.CONSTANCIA_CVE \n"
                    + "		where (cs.status = 1)\n"
                    + "		group by dcs.PARTIDA_CVE\n"
                    + "	) sal on p.PARTIDA_CVE = sal.PARTIDA_CVE\n"
                    + "	where (cdd.status = 1) and (cdd.CTE_CVE = :idCliente or :idCliente is null)\n"
                    + "	and cdd.FECHA_INGRESO <= :fechaCorte\n"
                    + ") inv\n"
                    + "where inv.cantidad > 0\n"
                    + "group by cte_cve";

                Query query = em.createNativeQuery(sql)
                    .setParameter("idCliente", idCliente)
                    .setParameter("fechaCorte", fecha);

                BigDecimal cantidad = (BigDecimal)query.getSingleResult();

                return cantidad;
            } catch (NoResultException e) {
                return null;
            }
        });
    }
    
}
