package com.ferbo.gestion.api.repository;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ferbo.gestion.api.config.SpringTransactManager;
import com.ferbo.gestion.api.idao.IAsentamientoRepo;
import com.ferbo.gestion.core.commons.dao.BaseDAO;
import com.ferbo.gestion.core.model.domicilio.Asentamiento;

public class AsentamientoRepo extends BaseDAO<Asentamiento, Integer> implements IAsentamientoRepo
{
    private static Logger log = LogManager.getLogger(AsentamientoRepo.class);

    public AsentamientoRepo(SpringTransactManager stm) {
        super(Asentamiento.class, stm);
    }

    @Override
    public Asentamiento buscarPorAsentamiento(Integer idPais, Integer idEstado, Integer idMunicipio, Integer idCiudad, Integer idAsentamiento) 
    {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT a "
                    + "FROM Asentamiento a "
                    + "WHERE a.asentamientoPK.ciudad.ciudadPK.municipio.municipioPK.estado.estadoPK.pais.id = :idPais "
                    + "AND a.asentamientoPK.ciudad.ciudadPK.municipio.municipioPK.estado.estadoPK.id = :idEstado "
                    + "AND a.asentamientoPK.ciudad.ciudadPK.municipio.municipioPK.id = :idMunicipio "
                    + "AND a.asentamientoPK.ciudad.ciudadPK.id = :idCiudad "
                    + "AND a.asentamientoPK.id =:idAsentamiento", Asentamiento.class)
                .setParameter("idPais", idPais)
                .setParameter("idEstado", idEstado)
                .setParameter("idMunicipio", idMunicipio)
                .setParameter("idCiudad", idCiudad)
                .setParameter("idAsentamiento", idAsentamiento)
                .getSingleResult();
        });
    }
    
    @Override
    public Asentamiento buscaPorParametros(Integer idPais, Integer idEstado, Integer idMunicipio, Integer idCiudad, Integer idAsentamiento, Integer idTipoAsntmnto, Integer idEntidadPostal)
    {
        return transactManager.executeRead(em -> {
            return em.createQuery("SELECT a "
                    + "FROM Asentamiento a "
                    + "WHERE a.asentamientoPK.ciudad.ciudadPK.municipio.municipioPK.estado.estadoPK.pais.id = :idPais "
                    + "AND a.asentamientoPK.ciudad.ciudadPK.municipio.municipioPK.estado.estadoPK.id = :idEstado "
                    + "AND a.asentamientoPK.ciudad.ciudadPK.municipio.municipioPK.id = :idMunicipio "
                    + "AND a.asentamientoPK.ciudad.ciudadPK.id = :idCiudad "
                    + "AND a.asentamientoPK.id =:idAsentamiento "
                    + "AND a.tipoAsentamiento.id = :idTipoAsntmnto "
                    + "AND a.entidadPostal.id = :idEntidadPostal", Asentamiento.class)
                .setParameter("idPais", idPais)
                .setParameter("idEstado", idEstado)
                .setParameter("idMunicipio", idMunicipio)
                .setParameter("idCiudad", idCiudad)
                .setParameter("idAsentamiento", idAsentamiento)
                .setParameter("idTipoAsntmnto", idTipoAsntmnto)
                .setParameter("idEntidadPostal", idEntidadPostal)
                .getSingleResult();
        });
    }

    @Override
    public List<Asentamiento> buscaPorCP(String codigo) 
    {
        return transactManager.executeRead(em -> {
            List<Asentamiento> listado = em.createNamedQuery("Asentamiento.findByCp", Asentamiento.class)
                .setParameter("cp", codigo).getResultList();

            for (Asentamiento aux : listado) {
                log.debug("Asentamiento: {}", aux.getDescripcion());
                log.debug("Ciudad: {}", aux.getAsentamientoPK().getCiudad().getDescripcion());
                log.debug("Municipio: {}", aux.getAsentamientoPK().getCiudad().getCiudadPK().getMunicipio().getDescripcion());
                log.debug("Estado: {}", aux.getAsentamientoPK().getCiudad().getCiudadPK().getMunicipio().getMunicipioPK().getEstado().getDesccripcion());
                log.debug("Pais: {}", aux.getAsentamientoPK().getCiudad().getCiudadPK().getMunicipio().getMunicipioPK().getEstado().getEstadoPK().getPais().getDescripcion());
            }

            return listado;
        });
    }
    
}
