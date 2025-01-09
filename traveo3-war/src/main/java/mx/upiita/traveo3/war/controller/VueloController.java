package mx.upiita.traveo3.war.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.upiita.traveo3.ejb.model.Ruta;
import mx.upiita.traveo3.ejb.model.Vuelo;
import mx.upiita.traveo3.ejb.service.RutaServiceLocal;
import mx.upiita.traveo3.ejb.service.VueloServiceLocal;

import java.io.Serializable;
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

    private List<Ruta> rutas;
    private Vuelo nuevoVuelo;
    private Integer idRutaSeleccionada;

    @PostConstruct
    public void init() {
        nuevoVuelo = new Vuelo();
        cargarRutas();
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
}
