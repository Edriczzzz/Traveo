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

    @Override
    @Transactional
    public Usuario actualizar(Usuario usuario, Integer vueloId) {
        try {
            if (usuario == null || usuario.getIdUsuario() == null) {
                throw new IllegalArgumentException("Usuario o ID no válido");
            }

            // Primero verificamos si el usuario existe
            int count = em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE u.idUsuario = :id", Long.class)
                    .setParameter("id", usuario.getIdUsuario())
                    .getSingleResult().intValue();

            if (count == 0) {
                throw new RuntimeException("No existe usuario con ID: " + usuario.getIdUsuario());
            }

            // Actualizamos usando JPQL
            int updatedCount = em.createQuery(
                            "UPDATE Usuario u SET " +
                                    "u.nombre = :nombre, " +
                                    "u.correo = :correo, " +
                                    "u.telefono = :telefono, " +
                                    "u.rol = :rol, " +
                                    "u.fechaNacimiento = :fechaNacimiento, " +
                                    "u.asiento = :asiento " +
                                    "WHERE u.idUsuario = :id")
                    .setParameter("nombre", usuario.getNombre())
                    .setParameter("correo", usuario.getCorreo())
                    .setParameter("telefono", usuario.getTelefono())
                    .setParameter("rol", usuario.getRol())
                    .setParameter("fechaNacimiento", usuario.getFechaNacimiento())
                    .setParameter("asiento", usuario.getAsiento())
                    .setParameter("id", usuario.getIdUsuario())
                    .executeUpdate();

            // Si se especificó un vuelo, actualizamos la relación
            if (vueloId != null) {
                em.createQuery(
                                "UPDATE Usuario u SET u.vuelo.idVuelo = :vueloId WHERE u.idUsuario = :userId")
                        .setParameter("vueloId", vueloId)
                        .setParameter("userId", usuario.getIdUsuario())
                        .executeUpdate();
            }

            em.flush();

            // Retornamos el usuario actualizado
            return em.find(Usuario.class, usuario.getIdUsuario());

        } catch (Exception e) {
            logger.severe("Error al actualizar usuario: " + e.getMessage());
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void eliminar(Usuario usuario) {
        try {
            if (usuario == null || usuario.getIdUsuario() == null) {
                throw new IllegalArgumentException("Usuario o ID no válido");
            }

            // Verificamos si existe el usuario
            int count = em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE u.idUsuario = :id", Long.class)
                    .setParameter("id", usuario.getIdUsuario())
                    .getSingleResult().intValue();

            if (count == 0) {
                throw new RuntimeException("No existe usuario con ID: " + usuario.getIdUsuario());
            }

            // Eliminamos usando JPQL
            int deletedCount = em.createQuery(
                            "DELETE FROM Usuario u WHERE u.idUsuario = :id")
                    .setParameter("id", usuario.getIdUsuario())
                    .executeUpdate();

            em.flush();
            logger.info("Usuario eliminado correctamente. ID: " + usuario.getIdUsuario());

        } catch (Exception e) {
            logger.severe("Error al eliminar usuario: " + e.getMessage());
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage());
        }
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
    public Usuario find(Integer id) {
        try {
            if (id == null) {
                return null;
            }

            List<Usuario> usuarios = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.idUsuario = :id", Usuario.class)
                    .setParameter("id", id)
                    .getResultList();

            return usuarios.isEmpty() ? null : usuarios.get(0);

        } catch (Exception e) {
            logger.severe("Error al buscar usuario con ID " + id + ": " + e.getMessage());
            return null;
        }
    }


    @Override
    public boolean isUsuarioAutenticado() {
        return obtenerUsuarioAutenticado() != null;
    }
}