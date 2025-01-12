package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import mx.upiita.traveo3.ejb.model.Aerolinea;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class AerolineaServiceImpl extends AbstractFacade implements AerolineaServiceLocal{

    private Logger logger = Logger.getLogger(AerolineaServiceImpl.class.getName());

    @PersistenceContext(name = "Traveo2PU")
    private EntityManager em;

    public AerolineaServiceImpl() {
        super(Aerolinea.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Aerolinea> listar() {
        logger.log(Level.INFO, "Iniciando consulta de Aerolineas");
        return em.createQuery("select p from Aerolinea p", Aerolinea.class).getResultList();

    }

    @Override
    public Aerolinea crear(Aerolinea entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Aerolinea actualizar(Aerolinea entity) {
        return (Aerolinea) super.actualizar(entity);
    }

    @Override
    public void eliminar(Aerolinea entity) {
        if (entity != null) {
            em.remove(em.contains(entity) ? entity : em.merge(entity));
        }
    }
}
