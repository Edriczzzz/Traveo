package mx.upiita.traveo3.war.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import mx.upiita.traveo3.ejb.model.Ruta;
import mx.upiita.traveo3.ejb.model.Usuario;
import mx.upiita.traveo3.ejb.model.Vuelo;
import mx.upiita.traveo3.ejb.model.Registro;
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

    private List<Ruta> rutas;
    private Vuelo nuevoVuelo;
    private Integer idRutaSeleccionada;
    private List<Vuelo> vuelosUsuario;

    private List<Vuelo> vuelosOferta;
    private List<Vuelo> vuelos;


    @PostConstruct
    public void init() {
        nuevoVuelo = new Vuelo();
        cargarRutas();
        cargarVuelosUsuario();
        cargarOfertas();
    }

    public void cargarRutas() {
        try {
            rutas = rutaService.listar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agendarVuelo() {
        try {
            // Buscar la ruta seleccionada
            Ruta rutaSeleccionada = rutas.stream()
                    .filter(r -> r.getIdRuta().equals(idRutaSeleccionada))
                    .findFirst()
                    .orElse(null);

            if (rutaSeleccionada != null) {
                // Crear el vuelo de ida
                Vuelo vueloIda = new Vuelo();
                vueloIda.setRuta(rutaSeleccionada);
                vueloIda.setFechaSalida(nuevoVuelo.getFechaSalida());
                vueloIda.setFechaLlegada(nuevoVuelo.getFechaSalida()); // Asumiendo que es un vuelo directo
                vueloIda.setEstado("Programado");
                vueloService.crear(vueloIda);

                // Crear el vuelo de regreso (opcional)
                if (nuevoVuelo.getFechaLlegada() != null) {
                    Vuelo vueloRegreso = new Vuelo();
                    vueloRegreso.setRuta(rutaSeleccionada);
                    vueloRegreso.setFechaSalida(nuevoVuelo.getFechaLlegada());
                    vueloRegreso.setFechaLlegada(nuevoVuelo.getFechaLlegada());
                    vueloRegreso.setEstado("Programado");
                    vueloService.crear(vueloRegreso);
                }

                // Reiniciar el formulario
                nuevoVuelo = new Vuelo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarVuelosUsuario() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(false);
            Usuario usuarioAutenticado = (Usuario) session.getAttribute("authenticatedUser ");

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
            vuelos = vueloService.listar(); // Asegúrate de que esta línea esté correctamente configurada
            logger.info("Vuelos cargados: " + vuelos.size());
        } catch (Exception e) {
            logger.severe("Error al cargar los vuelos: " + e.getMessage());
        }
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
        return vuelos; // Asegúrate de tener este getter
    }
}
