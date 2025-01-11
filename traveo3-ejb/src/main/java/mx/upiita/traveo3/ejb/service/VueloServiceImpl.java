package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import mx.upiita.traveo3.ejb.model.Aerolinea;
import mx.upiita.traveo3.ejb.model.Vuelo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class VueloServiceImpl extends AbstractFacade implements VueloServiceLocal{
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
        return em.createQuery("select p from Vuelo p", Vuelo.class).getResultList();

    }

    @Override
    public Vuelo crear(Vuelo entity) {
        return (Vuelo) super.create(entity);
    }

    @Override
    public Vuelo actualizar(Vuelo entity) {
        return (Vuelo) super.actualizar(entity);
    }

    @Override
    public void eliminar(Vuelo entity) {

    }
    @Override
    public List<Vuelo> obtenerPrimerosVuelos(int cantidad) {
        logger.log(Level.INFO, "Consultando los primeros {0} vuelos", cantidad);
        return em.createQuery("SELECT v FROM Vuelo v ORDER BY v.idVuelo", Vuelo.class)
                .setMaxResults(cantidad)
                .getResultList();
    }
}
