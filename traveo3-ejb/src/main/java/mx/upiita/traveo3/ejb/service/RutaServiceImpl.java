package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import mx.upiita.traveo3.ejb.model.Ruta;
import mx.upiita.traveo3.ejb.model.Ruta;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class RutaServiceImpl extends AbstractFacade implements  RutaServiceLocal{

    private Logger logger = Logger.getLogger(RutaServiceImpl.class.getName());

    @PersistenceContext(name = "Traveo2PU")
    private EntityManager em;


    public RutaServiceImpl() {
        super(Ruta.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Ruta> listar() {
        logger.log(Level.INFO, "Iniciando consulta de Ruta");
        return em.createQuery("select p from Ruta p", Ruta.class).getResultList();

    }

    @Override
    public Ruta crear(Ruta entity) {
        return (Ruta) super.create(entity);
    }

    @Override
    public Ruta actualizar(Ruta entity) {
        return (Ruta) super.actualizar(entity);
    }

    @Override
    public void eliminar(Ruta entity) {

    }



}
