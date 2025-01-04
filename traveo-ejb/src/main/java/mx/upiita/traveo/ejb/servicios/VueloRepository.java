package mx.upiita.traveo.ejb.servicios;

import mx.upiita.traveo.ejb.modelo.Vuelo;

public interface VueloRepository extends JpaRepository<Vuelo>{
    Vuelo findByCodigo(String codigo) throws Exception;
}
