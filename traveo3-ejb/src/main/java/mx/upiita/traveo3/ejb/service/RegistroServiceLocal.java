package mx.upiita.traveo3.ejb.service;

import jakarta.ejb.Local;
import mx.upiita.traveo3.ejb.model.Registro;

import java.util.List;

@Local
public interface RegistroServiceLocal {
    List<Registro> listar();
    Registro crear(Registro entity);
    Registro actualizar(Registro entity);
    void eliminar(Registro entity);
    List<Registro> buscarPorUsuarioId(Integer usuarioId);
    List<Registro> buscarPorVueloId(Integer vueloId);
    Registro buscarPorUsuarioYVuelo(Integer usuarioId, Integer vueloId);

}
