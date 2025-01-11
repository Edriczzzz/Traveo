package mx.upiita.traveo3.ejb.service;


import jakarta.ejb.Local;
import mx.upiita.traveo3.ejb.model.Aerolinea;
import mx.upiita.traveo3.ejb.model.Vuelo;

import java.util.List;

@Local
public interface VueloServiceLocal {
    public List<Vuelo> listar();
    public Vuelo crear(Vuelo entity);
    public Vuelo actualizar(Vuelo entity);
    public void eliminar(Vuelo entity);
    List<Vuelo> obtenerPrimerosVuelos(int cantidad);
}
