package mx.upiita.traveo.ejb.servicios;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import mx.upiita.traveo.ejb.modelo.Vuelo;

import java.io.Serializable;
import java.util.List;
@Named
public class VueloServiceImpl implements VueloService, Serializable {
    private static final long serialVersionUID = 1L;


    @Inject
    private VueloRepository vueloRepository;

    @Transactional
    @Override
    public Integer insert(Vuelo vuelo) throws Exception {
        return vueloRepository.insert(vuelo);
    }

    @Transactional
    @Override
    public Integer update(Vuelo vuelo) throws Exception {
        return vueloRepository.update(vuelo);
    }
    @Transactional
    @Override
    public Integer delete(Vuelo vuelo) throws Exception {
        return vueloRepository.delete(vuelo);
    }

    @Override
    public List<Vuelo> findAll() throws Exception {
        return vueloRepository.findAll();
    }

    @Override
    public Vuelo findById(Vuelo vuelo) throws Exception {
        return vueloRepository.findById(vuelo);
    }

    @Override
    public Vuelo findByCodigo(String codigo) throws Exception {
        return vueloRepository.findByCodigo(codigo);
    }
}
