package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import mx.upiita.traveo3.ejb.model.Vuelo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class VueloServiceImpl extends AbstractFacade implements VueloServiceLocal {

    private Logger logger = Logger.getLogger(VueloServiceImpl.class.getName());

    @PersistenceContext(name = "Traveo2PU")
    private EntityManager em;

    public VueloServiceImpl() {
        super(Vuelo.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Vuelo> listar() {
        logger.log(Level.INFO, "Iniciando consulta de Vuelos");
        TypedQuery<Vuelo> query = em.createQuery(
                "SELECT v FROM Vuelo v LEFT JOIN FETCH v.ruta LEFT JOIN FETCH v.aerolinea",
                Vuelo.class);
        return query.getResultList();
    }

    @Override
    public Vuelo crear(Vuelo entity) {
        logger.log(Level.INFO, "Creando vuelo: {0}", entity);
        em.persist(entity);
        return entity;
    }

    @Override
    public Vuelo actualizar(Vuelo entity) {
        logger.log(Level.INFO, "Actualizando vuelo: {0}", entity);
        try {
            Vuelo vueloActualizado = em.merge(entity);
            em.flush();
            return vueloActualizado;
        } catch (Exception e) {
            logger.severe("Error al actualizar vuelo: " + e.getMessage());
            throw new RuntimeException("Error al actualizar vuelo", e);
        }
    }

    @Override
    public void eliminar(Vuelo entity) {
        logger.log(Level.INFO, "Eliminando vuelo: {0}", entity);
        try {
            Vuelo vuelo = em.find(Vuelo.class, entity.getIdVuelo());
            if (vuelo != null) {
                em.remove(vuelo);
                em.flush();
            }
        } catch (Exception e) {
            logger.severe("Error al eliminar vuelo: " + e.getMessage());
            throw new RuntimeException("Error al eliminar vuelo", e);
        }
    }

    @Override
    public List<Vuelo> obtenerPrimerosVuelos(int cantidad) {
        logger.log(Level.INFO, "Consultando los primeros {0} vuelos", cantidad);
        TypedQuery<Vuelo> query = em.createQuery(
                "SELECT v FROM Vuelo v LEFT JOIN FETCH v.ruta LEFT JOIN FETCH v.aerolinea " +
                        "ORDER BY v.idVuelo",
                Vuelo.class);
        query.setMaxResults(cantidad);
        return query.getResultList();
    }

    @Override
    public Vuelo buscarPorId(Integer idVuelo) {
        try {
            TypedQuery<Vuelo> query = em.createQuery(
                    "SELECT v FROM Vuelo v " +
                            "LEFT JOIN FETCH v.ruta " +
                            "LEFT JOIN FETCH v.aerolinea " +
                            "WHERE v.idVuelo = :idVuelo",
                    Vuelo.class);
            query.setParameter("idVuelo", idVuelo);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.severe("Error al buscar el vuelo con ID " + idVuelo + ": " + e.getMessage());
            return null;
        }
    }
}