package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import mx.upiita.traveo3.ejb.model.Usuario;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

@Stateless
public class UsuarioServiceImpl extends AbstractFacade implements UsuarioServiceLocal {

    private Logger logger = Logger.getLogger(UsuarioServiceImpl.class.getName());

    @PersistenceContext(name = "Traveo2PU")
    private EntityManager em;

    public UsuarioServiceImpl() {
        super(Usuario.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Usuario> listar() {
        logger.log(Level.INFO, "Iniciando consulta de Usuarios");
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    @Override
    public Usuario crear(Usuario entity) {
        entity.setFechaRegistro(new Date());
        entity.setVuelo(null); // Aseguramos que el vuelo sea nulo al crear
        return (Usuario) super.create(entity);
    }

    @Override
    public Usuario actualizar(Usuario entity) {
        return (Usuario) super.actualizar(entity);
    }

    @Override
    public void eliminar(Usuario entity) {
        super.borrar(entity);

    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.correo = :correo", Usuario.class)
                    .setParameter("correo", correo)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Usuario login(String correo, String contrasena) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.correo = :correo AND u.contrasena = :contrasena", Usuario.class)
                    .setParameter("correo", correo)
                    .setParameter("contrasena", contrasena)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
