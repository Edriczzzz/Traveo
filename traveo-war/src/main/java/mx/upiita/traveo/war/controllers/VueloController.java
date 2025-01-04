package mx.upiita.traveo.war.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.upiita.traveo.ejb.modelo.Vuelo;
import mx.upiita.traveo.ejb.servicios.VueloService;
import mx.upiita.traveo.war.util.Message;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class VueloController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private VueloService vueloService;

    private Vuelo vuelo;
    private Vuelo vueloSeleccionado;
    private List<Vuelo> vuelos;

    public void loadVuelos(){
        try{
            this.vuelos = vueloService.findAll();
        }catch(Exception e){
            Message.messageError("Error: "+ e.getMessage());
        }
    }
    public void resetForm(){
        this.vuelo = new Vuelo();
        this.vueloSeleccionado = null;
    }

    @PostConstruct
    public void init(){
        this.vuelo = new Vuelo();
        this.vueloSeleccionado = null;
        this.loadVuelos();
    }

    public void saveVuelo(){
        try{
            if(vuelo.getIdVuelo() == null){
                vueloService.update(vuelo);
                Message.messageInfo("Update: Exitoso");
            }else{
                vueloService.insert(vuelo);
                Message.messageInfo("Insert: Exitoso");
            }
            this.loadVuelos();
            this.resetForm();
        }catch(Exception e){
            Message.messageError("Error en save vuelos: ");
        }
    }
    public void selectVuelo(SelectEvent e){
        this.vueloSeleccionado = (Vuelo) e.getObject();
    }

    public void editVuelo(){
        try{
            if(this.vueloSeleccionado != null){
                this.vuelo = vueloSeleccionado;
            }else{
                Message.messageError("Error: No se ha seleccionado ningun vuelo");
            }
        }catch(Exception e){
            Message.messageError("Error en edit vuelos: "+ e.getMessage());
        }
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    public Vuelo getVueloSeleccionado() {
        return vueloSeleccionado;
    }

    public void setVueloSeleccionado(Vuelo vueloSeleccionado) {
        this.vueloSeleccionado = vueloSeleccionado;
    }

    public List<Vuelo> getVuelos() {
        return vuelos;
    }

    public void setVuelos(List<Vuelo> vuelos) {
        this.vuelos = vuelos;
    }
}
