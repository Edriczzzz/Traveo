package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Local;
import mx.upiita.traveo3.ejb.model.Ciudad;

import java.util.List;

@Local
public interface CiudadServiceLocal {
    public List<Ciudad> listar();
    public Ciudad crear(Ciudad entity);
    public Ciudad actualizar(Ciudad entity);
    public void eliminar(Ciudad entity);
}
