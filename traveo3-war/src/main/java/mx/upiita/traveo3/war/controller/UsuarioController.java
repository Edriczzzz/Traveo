package mx.upiita.traveo3.war.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.upiita.traveo3.ejb.model.Usuario;
import mx.upiita.traveo3.ejb.service.AerolineaServiceImpl;
import mx.upiita.traveo3.ejb.service.UsuarioServiceLocal;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Named(value = "usuarioController")
@ViewScoped
public class UsuarioController implements Serializable {

    private Logger logger = Logger.getLogger(AerolineaServiceImpl.class.getName());

    @Inject
    private UsuarioServiceLocal usuarioService;

    private Usuario usuario;
    private String correo;
    private String contrasena;

    private List<Usuario> usuarios;
    private Usuario selectedUsuario;
    private List<SelectItem> roles;

    @PostConstruct
    public void init() {
        usuario = new Usuario();
        selectedUsuario = new Usuario();
        usuarios = usuarioService.listar();// Inicialización del usuario
        initRoles();
        cargarUsuarios();
    }
    private void initRoles() {
        roles = new ArrayList<>();
        for (Usuario.Rol rol : Usuario.Rol.values()) {
            roles.add(new SelectItem(rol, rol.toString()));
        }
    }

    private void cargarUsuarios() {
        try {
            usuarios = usuarioService.listar();
        } catch (Exception e) {
            logger.severe("Error al cargar usuarios: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al cargar la lista de usuarios");
        }
    }

    public void actualizarUsuario(Usuario usuario) {
        try {
            if (selectedUsuario == null || selectedUsuario.getIdUsuario() == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay usuario seleccionado");
                return;
            }

            // Validaciones básicas
            if (selectedUsuario.getNombre() == null || selectedUsuario.getNombre().trim().isEmpty()) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre es requerido");
                return;
            }

            if (selectedUsuario.getCorreo() == null || selectedUsuario.getCorreo().trim().isEmpty()) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "El correo es requerido");
                return;
            }

            // Realizar la actualización
            Integer vueloId = selectedUsuario.getVuelo() != null ? selectedUsuario.getVuelo().getIdVuelo() : null;
            usuarioService.actualizar(selectedUsuario, vueloId);

            // Recargar la lista de usuarios
            cargarUsuarios();

            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario actualizado correctamente");

        } catch (Exception e) {
            logger.severe("Error al actualizar usuario: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al actualizar el usuario: " + e.getMessage());
        }
    }

    public void eliminarUsuario(Usuario usuario) {
        try {
            if (usuario == null || usuario.getIdUsuario() == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario no válido");
                return;
            }

            usuarioService.eliminar(usuario);
            cargarUsuarios();
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario eliminado correctamente");

        } catch (Exception e) {
            logger.severe("Error al eliminar usuario: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al eliminar usuario: " + e.getMessage());
        }
    }



    public void prepararEdicion(Usuario usuario) {
        try {
            if (usuario == null || usuario.getIdUsuario() == null) {
                logger.warning("Intento de editar usuario nulo o sin ID");
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario no válido");
                return;
            }

            // Buscar el usuario en la base de datos
            Usuario usuarioDb = usuarioService.find(usuario.getIdUsuario());
            if (usuarioDb == null) {
                logger.warning("No se encontró el usuario con ID: " + usuario.getIdUsuario());
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario no encontrado");
                return;
            }

            // Crear una copia del usuario para la edición
            selectedUsuario = new Usuario();
            selectedUsuario.setIdUsuario(usuarioDb.getIdUsuario());
            selectedUsuario.setNombre(usuarioDb.getNombre());
            selectedUsuario.setCorreo(usuarioDb.getCorreo());
            selectedUsuario.setTelefono(usuarioDb.getTelefono());
            selectedUsuario.setRol(usuarioDb.getRol());
            selectedUsuario.setFechaNacimiento(usuarioDb.getFechaNacimiento());
            selectedUsuario.setAsiento(usuarioDb.getAsiento());
            selectedUsuario.setVuelo(usuarioDb.getVuelo());

            logger.info("Usuario cargado para edición: " + selectedUsuario.getIdUsuario());
        } catch (Exception e) {
            logger.severe("Error al preparar la edición: " + e.getMessage());
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al cargar el usuario");
            selectedUsuario = new Usuario();
        }
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
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().invalidateSession();
            String contextPath = facesContext.getExternalContext().getRequestContextPath();
            facesContext.getExternalContext().redirect(contextPath + "/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // No es necesario devolver una cadena porque ya se realizó la redirección.
    }
    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
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
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    public List<SelectItem> getRoles() {
        return roles;
    }

    public void setRoles(List<SelectItem> roles) {
        this.roles = roles;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}

