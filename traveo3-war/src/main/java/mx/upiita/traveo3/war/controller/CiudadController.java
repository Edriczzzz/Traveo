package mx.upiita.traveo3.war.controller;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import mx.upiita.traveo3.ejb.model.Ciudad;
import mx.upiita.traveo3.ejb.service.CiudadServiceLocal;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

public class CiudadController implements Serializable {

    private Logger logger = Logger.getLogger(CiudadController.class.getName());

    @Inject
    private CiudadServiceLocal ciudadService;

    private List<Ciudad> ciudades;
    private Ciudad nuevaCiudad;

    @PostConstruct
    public void init() {

        nuevaCiudad = new Ciudad();
        cargarCiudades();
    }

    public void cargarCiudades() {
        try {
            ciudades = ciudadService.listar();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void guardarCiudades() {
        ciudadService.crear(nuevaCiudad);
        nuevaCiudad = new Ciudad();
        cargarCiudades();
    }

    public void eliminarCiudades(Ciudad aerolinea) {
        ciudadService.eliminar(aerolinea);
        cargarCiudades();
    }

    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public Ciudad getNuevaCiudad() {
        return nuevaCiudad;
    }

    public void setNuevaCiudad(Ciudad nuevaCiudad) {
        this.nuevaCiudad = CiudadController.this.nuevaCiudad;

    }
}