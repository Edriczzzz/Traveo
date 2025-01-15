package mx.upiita.traveo3.war.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import mx.upiita.traveo3.ejb.model.*;
import mx.upiita.traveo3.ejb.service.AerolineaServiceLocal;
import mx.upiita.traveo3.ejb.service.RegistroServiceLocal;
import mx.upiita.traveo3.ejb.service.RutaServiceLocal;
import mx.upiita.traveo3.ejb.service.VueloServiceLocal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Named(value = "vueloController")
@ViewScoped
public class VueloController implements Serializable {

    private Logger logger = Logger.getLogger(VueloController.class.getName());

    @Inject
    private VueloServiceLocal vueloService;

    @Inject
    private RutaServiceLocal rutaService;

    @Inject
    private RegistroServiceLocal registroService;

    @Inject
    private AerolineaServiceLocal aerolineaService;

    private List<Ruta> rutas;
    private Vuelo nuevoVuelo;
    private Integer idRutaSeleccionada;
    private List<Vuelo> vuelosUsuario;

    private List<Vuelo> vuelosOferta;
    private List<Vuelo> vuelos;

    private List<Aerolinea> aerolineas;
    private Vuelo selectedVuelo;
    private List<String> estados;


    @PostConstruct
    public void init() {
        logger.info("Inicializando VueloController");
        nuevoVuelo = new Vuelo();
        selectedVuelo = new Vuelo();
        cargarRutas();
        cargarVuelosUsuario();
        cargarOfertas();
        cargarAerolineas();
        cargarVuelos();
        initEstados();
    }
    private void initEstados() {
        estados = new ArrayList<>();
        estados.add("Programado");
        estados.add("En Vuelo");
        estados.add("Completado");
        estados.add("Cancelado");
    }
    public void prepararEdicion(Vuelo vuelo) {
        this.selectedVuelo = vuelo;
        logger.info("Preparando edición del vuelo: " + vuelo.getIdVuelo());
    }

    public void actualizarVuelo(Vuelo vuelo) {
        try {
            vueloService.actualizar(vuelo);
            cargarVuelos();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Vuelo actualizado correctamente"));
        } catch (Exception e) {
            logger.severe("Error al actualizar vuelo: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el vuelo"));
        }
    }

    public void eliminarVuelo(Vuelo vuelo) {
        try {
            vueloService.eliminar(vuelo);
            cargarVuelos();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Vuelo eliminado correctamente"));
        } catch (Exception e) {
            logger.severe("Error al eliminar vuelo: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el vuelo"));
        }
    }

    public void cargarRutas() {
        try {
            rutas = rutaService.listar();
            logger.info("Rutas cargadas: " + rutas.size());
        } catch (Exception e) {
            logger.severe("Error al cargar rutas: " + e.getMessage());
        }
    }

    public void cargarAerolineas() {
        try {
            aerolineas = aerolineaService.listar();
            logger.info("Aerolíneas cargadas: " + aerolineas.size());
        } catch (Exception e) {
            logger.severe("Error al cargar aerolíneas: " + e.getMessage());
        }
    }

    public Vuelo buscarPorId(Integer idVuelo) {
        try {
            if (idVuelo != null) {
                return vueloService.buscarPorId(idVuelo);
            } else {
                logger.warning("El ID del vuelo proporcionado es nulo.");
            }
        } catch (Exception e) {
            logger.severe("Error al buscar el vuelo con ID " + idVuelo + ": " + e.getMessage());
        }
        return null;
    }

    public void agendarVuelo() {
        try {
            Ruta rutaSeleccionada = rutas.stream()
                    .filter(r -> r.getIdRuta().equals(idRutaSeleccionada))
                    .findFirst()
                    .orElse(null);

            if (rutaSeleccionada != null) {
                Vuelo vueloIda = new Vuelo();
                vueloIda.setRuta(rutaSeleccionada);
                vueloIda.setFechaSalida(nuevoVuelo.getFechaSalida());
                vueloIda.setFechaLlegada(nuevoVuelo.getFechaSalida());
                vueloIda.setEstado("Programado");
                vueloService.crear(vueloIda);

                if (nuevoVuelo.getFechaLlegada() != null) {
                    Vuelo vueloRegreso = new Vuelo();
                    vueloRegreso.setRuta(rutaSeleccionada);
                    vueloRegreso.setFechaSalida(nuevoVuelo.getFechaLlegada());
                    vueloRegreso.setFechaLlegada(nuevoVuelo.getFechaLlegada());
                    vueloRegreso.setEstado("Programado");
                    vueloService.crear(vueloRegreso);
                }

                nuevoVuelo = new Vuelo();
            }
        } catch (Exception e) {
            logger.severe("Error al agendar vuelo: " + e.getMessage());
        }
    }

    public void cargarVuelosUsuario() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(false);
            Usuario usuarioAutenticado = (Usuario) session.getAttribute("authenticatedUser");

            if (usuarioAutenticado != null) {
                List<Registro> relacionesRegistro = registroService.buscarPorUsuarioId(usuarioAutenticado.getIdUsuario());

                vuelosUsuario = new ArrayList<>();
                for (Registro usuarioVuelo : relacionesRegistro) {
                    vuelosUsuario.add(usuarioVuelo.getVuelo());
                }

                logger.info("Vuelos cargados para el usuario: " + usuarioAutenticado.getIdUsuario() +
                        ", Total vuelos: " + vuelosUsuario.size());
            }
        } catch (Exception e) {
            logger.severe("Error al cargar los vuelos del usuario: " + e.getMessage());
            vuelosUsuario = new ArrayList<>();
        }
    }

    public void cargarOfertas() {
        try {
            vuelos = vueloService.listar();
            logger.info("Vuelos cargados: " + vuelos.size());
        } catch (Exception e) {
            logger.severe("Error al cargar los vuelos: " + e.getMessage());
        }
    }


    public void cargarVuelo(Vuelo vuelo) {
        this.nuevoVuelo = vuelo;
        logger.info("Cargando vuelo para edición: " + vuelo);
    }

    public void cargarVuelos() {
        try {
            vuelos = vueloService.listar();
            logger.info("Vuelos cargados: " + vuelos.size());
        } catch (Exception e) {
            logger.severe("Error al cargar los vuelos: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar los vuelos."));
        }
    }

    public Vuelo getSelectedVuelo() {
        return selectedVuelo;
    }

    public void setSelectedVuelo(Vuelo selectedVuelo) {
        this.selectedVuelo = selectedVuelo;
    }

    public List<String> getEstados() {
        return estados;
    }
    public List<Ruta> getRutas() {
        return rutas;
    }

    public Vuelo getNuevoVuelo() {
        return nuevoVuelo;
    }

    public void setNuevoVuelo(Vuelo nuevoVuelo) {
        this.nuevoVuelo = nuevoVuelo;
    }

    public Integer getIdRutaSeleccionada() {
        return idRutaSeleccionada;
    }

    public void setIdRutaSeleccionada(Integer idRutaSeleccionada) {
        this.idRutaSeleccionada = idRutaSeleccionada;
    }

    public List<Vuelo> getVuelosUsuario() {
        return vuelosUsuario;
    }

    public void setVuelosUsuario(List<Vuelo> vuelosUsuario) {
        this.vuelosUsuario = vuelosUsuario;
    }

    public List<Vuelo> getVuelosOferta() {
        return vuelosOferta;
    }

    public void setVuelosOferta(List<Vuelo> vuelosOferta) {
        this.vuelosOferta = vuelosOferta;
    }

    public List<Vuelo> getVuelos() {
        return vueloService.listar();
    }

    public List<Aerolinea> getAerolineas() {
        return aerolineas;
    }
}