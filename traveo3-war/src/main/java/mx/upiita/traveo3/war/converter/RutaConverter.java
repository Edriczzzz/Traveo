package mx.upiita.traveo3.war.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.upiita.traveo3.ejb.model.Ruta;
import mx.upiita.traveo3.ejb.service.RutaServiceLocal;

@Named
@ApplicationScoped
public class RutaConverter implements Converter<Ruta> {

    @Inject
    private RutaServiceLocal rutaService;

    @Override
    public Ruta getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return rutaService.buscarPorId(Integer.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Ruta ruta) {
        if (ruta == null || ruta.getIdRuta() == null) {
            return "";
        }
        return ruta.getIdRuta().toString();
    }
}