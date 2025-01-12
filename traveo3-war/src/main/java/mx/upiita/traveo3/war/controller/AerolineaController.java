package mx.upiita.traveo3.war.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.upiita.traveo3.ejb.model.Aerolinea;
import mx.upiita.traveo3.ejb.service.AerolineaServiceLocal;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Named(value = "aerolineaController")
@ViewScoped
public class AerolineaController implements Serializable {

    private Logger logger = Logger.getLogger(AerolineaController.class.getName());

    @Inject
    private AerolineaServiceLocal aerolineaService;

    private List<Aerolinea> aerolineas;
    private Aerolinea aerolinea;
    private Aerolinea aerolineaSeleccionada;

    @PostConstruct
    public void init() {
        aerolinea = new Aerolinea();
       aerolineaSeleccionada = new Aerolinea();
        cargarAerolineas();
        cargarAerolinea(aerolinea);
    }

    public void cargarAerolineas() {
        try {
            aerolineas = aerolineaService.listar();
        } catch (Exception e) {
            logger.severe("Error al cargar aerolíneas: " + e.getMessage());
        }
    }

    public void cargarAerolinea(Aerolinea aerolinea) {
        this.aerolineaSeleccionada = aerolinea;
    }

    public void guardarAerolinea() {
        if (aerolineaSeleccionada.getId() == null) {
            aerolineaService.crear(aerolineaSeleccionada);
        } else {
            aerolineaService.actualizar(aerolineaSeleccionada);
        }
        aerolineaSeleccionada = new Aerolinea(); // Reiniciar el objeto
        cargarAerolineas(); // Recargar la lista
    }


    public void eliminarAerolinea(Aerolinea aerolinea) {
        try {
            aerolineaService.eliminar(aerolinea);
            cargarAerolineas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Aerolínea eliminada con éxito"));
        } catch (Exception e) {
            logger.severe("Error al eliminar aerolínea: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar aerolínea", e.getMessage()));
        }
    }

    public List<Aerolinea> getAerolineas() {
        return aerolineas;
    }

    public Aerolinea getAerolineaSeleccionada() {
        return aerolineaSeleccionada;
    }

    public void setAerolineaSeleccionada(Aerolinea aerolineaSeleccionada) {
        this.aerolineaSeleccionada = aerolineaSeleccionada;
    }
}