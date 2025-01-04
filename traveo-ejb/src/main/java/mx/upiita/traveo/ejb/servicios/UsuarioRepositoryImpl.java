package mx.upiita.traveo.ejb.servicios;

import jakarta.ejb.Stateless;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import mx.upiita.traveo.ejb.modelo.Usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UsuarioRepositoryImpl implements UsuarioRepository, Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "TraveoPU")
    private EntityManager em;

    @Override
    public Integer insert(Usuario usuario) throws Exception {
        em.persist(usuario);
        return usuario.getIdUsuario();
    }

    @Override
    public Integer update(Usuario usuario) throws Exception {
        em.merge(usuario);
        return usuario.getIdUsuario();
    }

    @Override
    public Integer delete(Usuario usuario) throws Exception {
        em.remove(usuario);
        return 1;
    }

    @Override
    public List<Usuario> findAll() throws Exception {
        List<Usuario> usuarios = new ArrayList<Usuario>();

        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
        usuarios= query.getResultList();
        return usuarios;
    }

    @Override
    public Usuario findById(Usuario id) throws Exception {
        List<Usuario> usuarios = new ArrayList<Usuario>();

        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.idUsuario = ?1", Usuario.class);
        query.setParameter(1, id.getIdUsuario());
        usuarios= query.getResultList();

        if (usuarios != null && !usuarios.isEmpty())
            return usuarios.get(0);
        else
            return new Usuario();
    }
}
