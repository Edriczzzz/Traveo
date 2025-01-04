package mx.upiita.traveo.ejb.servicios;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import mx.upiita.traveo.ejb.modelo.Usuario;

import java.io.Serializable;
import java.util.List;

@Stateless
public class UsuarioServiceImpl implements UsuarioService, Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioRepository usuarioRepository;

    @Transactional
    @Override
    public Integer insert(Usuario usuario) throws Exception {
        return usuarioRepository.insert(usuario);
    }

    @Transactional
    @Override
    public Integer update(Usuario usuario) throws Exception {
        return usuarioRepository.update(usuario);
    }

    @Transactional
    @Override
    public Integer delete(Usuario usuario) throws Exception {
        return usuarioRepository.delete(usuario);
    }

    @Override
    public List<Usuario> findAll() throws Exception {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario findById(Usuario usuario) throws Exception {
        return usuarioRepository.findById(usuario);
    }
}
