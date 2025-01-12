package mx.upiita.traveo3.war.controller;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import mx.upiita.traveo3.ejb.model.Ciudad;
import mx.upiita.traveo3.ejb.service.CiudadServiceLocal;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named(value = "ciudadController")
@ViewScoped
public class CiudadController implements Serializable {

    private Logger logger = Logger.getLogger(CiudadController.class.getName());

    @Inject
    private CiudadServiceLocal ciudadService;

    private List<Ciudad> ciudades;
    private Ciudad nuevaCiudad;

    @PostConstruct
    public void init() {
        nuevaCiudad = new Ciudad(); // Inicializa el objeto para agregar una nueva ciudad
        cargarCiudades(); // Carga la lista de ciudades
    }

    public void cargarCiudades() {
        try {
            ciudades = ciudadService.listar(); // Obtiene la lista de ciudades desde el servicio
        } catch (Exception e) {
            logger.severe("Error al cargar ciudades: " + e.getMessage());
        }
    }

    public void guardarCiudad() {
        if (nuevaCiudad.getIdCiudad() == null) {
            ciudadService.crear(nuevaCiudad); // Crea una nueva ciudad
        } else {
            ciudadService.actualizar(nuevaCiudad); // Actualiza la ciudad existente
        }
        nuevaCiudad = new Ciudad(); // Reinicia el objeto para agregar una nueva ciudad
        cargarCiudades(); // Recarga la lista de ciudades
    }

    public void cargarCiudad(Ciudad ciudad) {
        this.nuevaCiudad = ciudad; // Carga la ciudad seleccionada para editar
    }

    public void eliminarCiudad(Ciudad ciudad) {
        try {
            ciudadService.eliminar(ciudad); // Elimina la ciudad seleccionada
            cargarCiudades(); // Recarga la lista de ciudades
        } catch (Exception e) {
            logger.severe("Error al eliminar ciudad: " + e.getMessage());
        }
    }

    public List<Ciudad> getCiudades() {
        return ciudades; // Devuelve la lista de ciudades
    }

    public Ciudad getNuevaCiudad() {
        return nuevaCiudad; // Devuelve el objeto de la nueva ciudad
    }

    public void setNuevaCiudad(Ciudad nuevaCiudad) {
        this.nuevaCiudad = nuevaCiudad; // Establece el objeto de la nueva ciudad
    }
}