package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Local;
import mx.upiita.traveo3.ejb.model.Aerolinea;

import java.util.List;

@Local
public interface AerolineaServiceLocal {
    public List<Aerolinea> listar();
    public Aerolinea crear(Aerolinea entity);
    public Aerolinea actualizar(Aerolinea entity);
    public void eliminar(Aerolinea entity);
}
