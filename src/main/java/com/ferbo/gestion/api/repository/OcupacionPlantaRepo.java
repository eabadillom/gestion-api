package com.ferbo.gestion.api.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IOcupacionPlantaRepo;
import com.ferbo.gestion.api.model.OcupacionPlanta;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OcupacionPlantaRepo implements IOcupacionPlantaRepo
{
    private static Logger log = LogManager.getLogger(OcupacionPlantaRepo.class);

    @Autowired
    private SpringTransactManager transactManager;

    @Override
    public List<OcupacionPlanta> ocupacionPlantaCamara(LocalDate fecha, List<Integer> idsClientes, Integer idPlanta, Integer idCamara) 
    {
        return transactManager.executeRead(em -> {
            String sql = "WITH i AS (\n" +
                "    SELECT\n" +
                "        c.CTE_NOMBRE AS cliente,\n" +
                "        plt.PLANTA_DS AS planta,\n" +
                "        cam.CAMARA_DS AS camara,\n" +
                "        cdd.FOLIO,\n" +
                "        p.PARTIDA_CVE,\n" +
                "        p.cantidad_total,\n" +
                "        (p.CANTIDAD_TOTAL - COALESCE(s.cantidad, 0)) AS cantidad,\n" +
                "        p.peso_total,\n" +
                "        (p.PESO_TOTAL - COALESCE(s.peso, 0)) AS peso,\n" +
                "        p.no_tarimas,\n" +
                "        p.cd_tarima\n" +
                "    FROM constancia_de_deposito cdd\n" +
                "    INNER JOIN cliente c ON cdd.CTE_CVE = c.CTE_CVE\n" +
                "    INNER JOIN partida p ON cdd.folio = p.folio\n" +
                "    INNER JOIN camara cam ON p.CAMARA_CVE = cam.CAMARA_CVE\n" +
                "    INNER JOIN planta plt ON cam.PLANTA_CVE = plt.PLANTA_CVE\n" +
                "    LEFT JOIN (\n" +
                "        SELECT\n" +
                "            dcs.PARTIDA_CVE,\n" +
                "            SUM(dcs.CANTIDAD) AS cantidad,\n" +
                "            SUM(dcs.PESO) AS peso\n" +
                "        FROM detalle_constancia_salida dcs\n" +
                "        INNER JOIN constancia_salida cs ON cs.ID = dcs.CONSTANCIA_CVE\n" +
                "        WHERE cs.STATUS = 1 AND cs.FECHA <= :fecha\n" +
                "        GROUP BY dcs.PARTIDA_CVE\n" +
                "    ) s ON p.PARTIDA_CVE = s.PARTIDA_CVE\n" +
                "    WHERE cdd.status = 1 AND cdd.FECHA_INGRESO <= :fecha AND plt.st_habilitado = TRUE\n" +
                "      AND (:idPlanta IS NULL OR plt.PLANTA_CVE = :idPlanta)\n" +
                "      AND (:idCamara IS NULL OR cam.CAMARA_CVE = :idCamara)\n" +
                "      {FILTRO_CLIENTES}\n" +
                "),\n" +
                "t1 AS (\n" +
                "    SELECT i.planta, i.camara , CEILING((i.peso / i.peso_total) * i.no_tarimas) AS tarimas\n" +
                "    FROM i\n" +
                "    WHERE i.no_tarimas IS NOT NULL AND i.cd_tarima IS NULL AND i.cantidad > 0\n" +
                "),\n" +
                "t2 AS (\n" +
                "    SELECT i.planta, i.camara , COUNT(DISTINCT i.cd_tarima) AS tarimas\n" +
                "    FROM i\n" +
                "    WHERE i.no_tarimas IS NULL AND i.cd_tarima IS NOT NULL AND i.cantidad > 0\n" +
                "    GROUP BY i.planta, i.camara \n" +
                ")\n" +
                "SELECT o.planta, o.camara, SUM(o.tarimas) AS tarimas\n" +
                "FROM (\n" +
                "    SELECT * FROM t1\n" +
                "    UNION ALL\n" +
                "    SELECT * FROM t2\n" +
                ") o\n" +
                "GROUP BY o.planta, o.camara\n" +
                "ORDER BY o.planta, o.camara";
            if (idsClientes != null && !idsClientes.isEmpty()) {
                sql = sql.replace("{FILTRO_CLIENTES}", "AND cdd.cte_cve IN (:idsClientes)\n");
            } else {
                sql = sql.replace("{FILTRO_CLIENTES}", ""); 
            }
            Query query = em.createNativeQuery(sql)
                .setParameter("fecha", fecha)
                .setParameter("idCamara", idCamara)
                .setParameter("idPlanta", idPlanta);
            if (idsClientes != null && !idsClientes.isEmpty()) {
                query.setParameter("idsClientes", idsClientes);
            }
            List<Object[]> listaObjetos = query.getResultList();
            List<OcupacionPlanta> listOcupacionPlantaCamara = new ArrayList<>();
            for(Object[] o: listaObjetos) {
                OcupacionPlanta op = new OcupacionPlanta();
                int id = 0;
                
                op.setPlanta((String) o[id++]);
                op.setCamara((String) o[id++]);
                op.setTarima((BigDecimal) o[id++]);
                
                listOcupacionPlantaCamara.add(op);
            }
            return listOcupacionPlantaCamara;
        }); 
    }
    
}
