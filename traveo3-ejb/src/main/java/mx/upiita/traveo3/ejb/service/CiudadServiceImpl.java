package mx.upiita.traveo3.ejb.service;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import mx.upiita.traveo3.ejb.model.Ciudad;
import mx.upiita.traveo3.ejb.model.Ciudad;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CiudadServiceImpl extends AbstractFacade implements CiudadServiceLocal{

    private Logger logger = Logger.getLogger(CiudadServiceImpl.class.getName());

    @PersistenceContext(name = "Traveo2PU")
    private EntityManager em;

    public CiudadServiceImpl() {
        super(Ciudad.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Ciudad> listar() {
        logger.log(Level.INFO, "Iniciando consulta de Ciudades");
        return em.createQuery("select p from Ciudad p", Ciudad.class).getResultList();
    }

    @Override
    public Ciudad crear(Ciudad entity) {
        return (Ciudad) super.create(entity);
    }

    @Override
    public Ciudad actualizar(Ciudad entity) {
        return (Ciudad) super.actualizar(entity);
    }

    @Override
    public void eliminar(Ciudad entity) {

    }

}
