package mx.upiita.traveo.ejb.servicios;

import mx.upiita.traveo.ejb.modelo.Vuelo;

public interface VueloService extends CrudService<Vuelo>{
    Vuelo findByCodigo(String codigo) throws Exception;
}
