package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Local;
import mx.upiita.traveo3.ejb.model.Usuario;

import java.util.List;

@Local
public interface UsuarioServiceLocal {
    List<Usuario> listar();
    Usuario crear(Usuario entity);
    Usuario actualizar(Usuario entity, Integer vueloId);
    void eliminar(Usuario entity);
    Usuario buscarPorCorreo(String correo);
    Usuario login(String correo, String contrasena);
    void logout();
    Usuario obtenerUsuarioAutenticado();
    boolean isUsuarioAutenticado();

}
