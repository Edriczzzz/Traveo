package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Local;
import mx.upiita.traveo3.ejb.model.Ruta;
import mx.upiita.traveo3.ejb.model.Vuelo;

import java.util.List;

@Local
public interface RutaServiceLocal {

    public List<Ruta> listar();
    public Ruta crear(Ruta entity);
    public Ruta actualizar(Ruta entity);
    public void eliminar(Ruta entity);
}
