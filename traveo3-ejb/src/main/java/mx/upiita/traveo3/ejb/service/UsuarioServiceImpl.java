package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Stateless;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import mx.upiita.traveo3.ejb.model.Usuario;
import mx.upiita.traveo3.ejb.model.Vuelo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

@Stateless
public class UsuarioServiceImpl extends AbstractFacade implements UsuarioServiceLocal {

    private static final String USER_SESSION_KEY = "authenticatedUser";

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
        entity.setVuelo(null);
        return (Usuario) super.create(entity);
    }
    @Transactional
    public Usuario actualizar(Usuario usuario, Integer vueloId) {
        logger.info("Buscando usuario con ID: " + usuario.getIdUsuario());

        try {
            Usuario usuarioExistente = em.createQuery("SELECT u FROM Usuario u WHERE u.idUsuario = :id", Usuario.class)
                    .setParameter("id", usuario.getIdUsuario())
                    .getSingleResult();

            if (usuarioExistente == null) {
                throw new IllegalArgumentException("Usuario no encontrado con ID: " + usuario.getIdUsuario());
            }

            // Forzar sincronización con la base de datos
            em.refresh(usuarioExistente);

            // Si el vueloId no es null, asignar el vuelo al usuario
            if (vueloId != null) {
                Vuelo vuelo = em.find(Vuelo.class, vueloId);
                if (vuelo != null) {
                    usuarioExistente.setVuelo(vuelo);
                    logger.info("Vuelo asignado al usuario con ID: " + usuario.getIdUsuario());
                } else {
                    throw new IllegalArgumentException("Vuelo no encontrado con ID: " + vueloId);
                }
            }

            // Guardar los cambios
            em.merge(usuarioExistente);
            return usuarioExistente;
        } catch (NoResultException e) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + usuario.getIdUsuario());
        }
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
            Usuario usuario = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.correo = :correo AND u.contrasena = :contrasena",
                            Usuario.class
                    )
                    .setParameter("correo", correo)
                    .setParameter("contrasena", contrasena)
                    .getSingleResult();


                if (usuario != null) {
                    logger.info("Usuario autenticado: " + correo);
                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                    session.setAttribute("authenticatedUser ", usuario);
                    session.setAttribute("userRole", usuario.getRol());
                } else {
                    logger.warning("Credenciales inválidas para: " + correo);
                    return null;
                }
            return usuario;
        } catch (NoResultException e) {
            logger.warning("Intento de login fallido para correo: " + correo);
            return null;
        }
    }


    @Override
    public void logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        logger.info("Usuario cerró sesión exitosamente");
    }

    @Override
    public Usuario obtenerUsuarioAutenticado() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(false);
            if (session != null) {
                return (Usuario) session.getAttribute("authenticatedUser ");
            }
        } catch (Exception e) {
            logger.severe("Error al obtener usuario autenticado: " + e.getMessage());
        }
        return null;
    }



    @Override
    public boolean isUsuarioAutenticado() {
        return obtenerUsuarioAutenticado() != null;
    }
}