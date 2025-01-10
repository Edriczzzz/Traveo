package mx.upiita.traveo3.ejb.service;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import mx.upiita.traveo3.ejb.model.Registro;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class RegistroServiceImpl extends AbstractFacade implements RegistroServiceLocal{


    private Logger logger = Logger.getLogger(RegistroServiceImpl.class.getName());

    @PersistenceContext(name = "Traveo2PU")
    private EntityManager em;

    public RegistroServiceImpl() {
        super(Registro.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;

    }

    @Override
    public List<Registro> listar() {
        logger.log(Level.INFO, "Iniciando consulta de UsuariosVuelos");
        return em.createQuery("SELECT uv FROM Registro uv", Registro.class).getResultList();
    }

    @Override
    public Registro crear(Registro entity) {
        return (Registro) super.create(entity);
    }

    @Override
    @Transactional
    public Registro actualizar(Registro entity) {
        logger.info("Actualizando relación Usuario-Vuelo con ID: " + entity.getId());

        try {
            Registro existente = em.find(Registro.class, entity.getId());
            if (existente == null) {
                throw new IllegalArgumentException("Relación Usuario-Vuelo no encontrada con ID: " + entity.getId());
            }

            return (Registro) super.actualizar(entity);
        } catch (Exception e) {
            logger.severe("Error al actualizar relación Usuario-Vuelo: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void eliminar(Registro entity) {
        super.borrar(entity);
    }

    @Override
    public List<Registro> buscarPorUsuarioId(Integer usuarioId) {
        try {
            return em.createQuery(
                            "SELECT uv FROM Registro uv WHERE uv.usuario.idUsuario = :usuarioId",
                            Registro.class)
                    .setParameter("usuarioId", usuarioId)
                    .getResultList();
        } catch (Exception e) {
            logger.warning("Error al buscar vuelos por usuario ID: " + usuarioId);
            return null;
        }
    }

    @Override
    public List<Registro> buscarPorVueloId(Integer vueloId) {
        try {
            return em.createQuery(
                            "SELECT uv FROM Registro uv WHERE uv.vuelo.idVuelo = :vueloId",
                            Registro.class)
                    .setParameter("vueloId", vueloId)
                    .getResultList();
        } catch (Exception e) {
            logger.warning("Error al buscar usuarios por vuelo ID: " + vueloId);
            return null;
        }
    }

    @Override
    public Registro buscarPorUsuarioYVuelo(Integer usuarioId, Integer vueloId) {
        try {
            return em.createQuery(
                            "SELECT uv FROM Registro uv " +
                                    "WHERE uv.usuario.idUsuario = :usuarioId " +
                                    "AND uv.vuelo.idVuelo = :vueloId",
                            Registro.class)
                    .setParameter("usuarioId", usuarioId)
                    .setParameter("vueloId", vueloId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            logger.warning("Error al buscar relación Usuario-Vuelo");
            return null;
        }
    }


}
