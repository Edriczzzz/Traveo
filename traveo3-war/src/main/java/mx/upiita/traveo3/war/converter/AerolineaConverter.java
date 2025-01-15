package mx.upiita.traveo3.war.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.upiita.traveo3.ejb.model.Aerolinea;
import mx.upiita.traveo3.ejb.service.AerolineaServiceLocal;

@Named
@ApplicationScoped
public class AerolineaConverter implements Converter<Aerolinea> {

    @Inject
    private AerolineaServiceLocal aerolineaService;

    @Override
    public Aerolinea getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return aerolineaService.buscarPorId(Integer.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Aerolinea aerolinea) {
        if (aerolinea == null || aerolinea.getId() == null) {
            return "";
        }
        return aerolinea.getId().toString();
    }
}