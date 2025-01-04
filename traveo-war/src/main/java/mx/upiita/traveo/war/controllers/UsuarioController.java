package mx.upiita.traveo.war.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.upiita.traveo.ejb.modelo.Usuario;
import mx.upiita.traveo.ejb.servicios.UsuarioService;
import mx.upiita.traveo.war.util.Message;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UsuarioController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Usuario usuarioSeleccionado;
    private List<Usuario> usuarios;

    @PostConstruct
    public void init() {
        this.usuario = new Usuario();
        this.usuarioSeleccionado = null;
        this.loadUsuarios();
    }

    public void loadUsuarios() {
        try {
            this.usuarios = usuarioService.findAll();
        } catch (Exception e) {
            Message.messageError("Error al cargar usuarios: " + e.getMessage());
        }
    }

    public void resetForm() {
        this.usuario = new Usuario();
        this.usuarioSeleccionado = null;
    }

    public void saveUsuario() {
        try {
            if (usuario.getIdUsuario() == null) {
                usuarioService.insert(usuario);
                Message.messageInfo("Usuario registrado con éxito");
            } else {
                usuarioService.update(usuario);
                Message.messageInfo("Usuario actualizado con éxito");
            }
            this.loadUsuarios();
            this.resetForm();
        } catch (Exception e) {
            Message.messageError("Error al guardar usuario: " + e.getMessage());
        }
    }

    public void selectUsuario(SelectEvent e) {
        this.usuarioSeleccionado = (Usuario) e.getObject();
    }

    public void editUsuario() {
        if (this.usuarioSeleccionado != null) {
            this.usuario = usuarioSeleccionado;
        } else {
            Message.messageError("Error: No se ha seleccionado ningún usuario");
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
