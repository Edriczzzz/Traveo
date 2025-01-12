package mx.upiita.traveo3.war.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.upiita.traveo3.ejb.model.Usuario;
import mx.upiita.traveo3.ejb.service.UsuarioServiceLocal;

import java.io.Serializable;

@Named(value = "usuarioController")
@ViewScoped
public class UsuarioController implements Serializable {

    @Inject
    private UsuarioServiceLocal usuarioService;

    private Usuario usuario;
    private String correo;
    private String contrasena;

    @PostConstruct
    public void init() {
        usuario = new Usuario(); // Inicialización del usuario
    }

    public String registrar() {
        try {
            // Verificar si el correo ya existe
            if (usuarioService.buscarPorCorreo(usuario.getCorreo()) != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "El correo ya está registrado"));
                return null;
            }

            usuarioService.crear(usuario);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Éxito", "Usuario registrado correctamente"));
            return "login?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Error al registrar usuario: " + e.getMessage()));
            return null;
        }
    }

    public String login() {
        try {
            Usuario usuarioEncontrado = usuarioService.login(correo, contrasena);
            if (usuarioEncontrado != null) {
                // Guardar el usuario en la sesión
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .put("usuario", usuarioEncontrado);
                if (usuarioEncontrado.getRol() == Usuario.Rol.ADMINISTRADOR) {
                    return "/admin/dashboard?faces-redirect=true"; // Página del administrador
                } else {
                    return "/cliente/inicio?faces-redirect=true"; // Página del cliente
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "Credenciales inválidas"));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Error al iniciar sesión: " + e.getMessage()));
            return null;
        }
    }

    public String getNombreHeader(){
        Usuario usuarioAutenticado = usuarioService.obtenerUsuarioAutenticado();
        return usuarioAutenticado.getNombre();
    }

    public String logout() {
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();
        return "login?faces-redirect=true";
    }

    // Getters y Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
