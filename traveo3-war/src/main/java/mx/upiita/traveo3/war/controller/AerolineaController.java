package mx.upiita.traveo3.war.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.upiita.traveo3.ejb.model.Aerolinea;
import mx.upiita.traveo3.ejb.service.AerolineaServiceImpl;
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
    private Aerolinea nuevaAerolinea;

    @PostConstruct
    public void init() {

        nuevaAerolinea = new Aerolinea();
        cargarAerolineas();
    }

    public void cargarAerolineas() {
        try{
            aerolineas = aerolineaService.listar();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void guardarAerolinea() {
        aerolineaService.crear(nuevaAerolinea);
        nuevaAerolinea = new Aerolinea();
        cargarAerolineas();
    }

    public void eliminarAerolinea(Aerolinea aerolinea) {
        aerolineaService.eliminar(aerolinea);
        cargarAerolineas();
    }

    public List<Aerolinea> getAerolineas() {
        return aerolineas;
    }

    public Aerolinea getNuevaAerolinea() {
        return nuevaAerolinea;
    }

    public void setNuevaAerolinea(Aerolinea nuevaAerolinea) {
        this.nuevaAerolinea = nuevaAerolinea;
    }


}
