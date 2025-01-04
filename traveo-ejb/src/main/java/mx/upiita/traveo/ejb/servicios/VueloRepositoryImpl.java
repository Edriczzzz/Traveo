package mx.upiita.traveo.ejb.servicios;

import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import mx.upiita.traveo.ejb.modelo.Vuelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Named
public class VueloRepositoryImpl implements VueloRepository, Serializable {
    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "TraveoPU")
    private EntityManager em;

    @Override
    public Integer insert(Vuelo vuelo) throws Exception {
        em.persist(vuelo);
        return vuelo.getIdVuelo();
    }

    @Override
    public Integer update(Vuelo vuelo) throws Exception {
        em.persist(vuelo);
        return vuelo.getIdVuelo();
    }

    @Override
    public Integer delete(Vuelo vuelo) throws Exception {
        em.persist(vuelo);
        return 1;
    }

    @Override
    public List<Vuelo> findAll() throws Exception {
        List<Vuelo> vuelo = new ArrayList<>();
        TypedQuery<Vuelo> query = em.createQuery("SELECT v FROM Vuelo v", Vuelo.class);
        vuelo = query.getResultList();


        return vuelo;
    }

    @Override
    public Vuelo findById(Vuelo id) throws Exception {
        List<Vuelo> vuelo = new ArrayList<>();
        TypedQuery<Vuelo> query = em.createQuery("SELECT v FROM Vuelo v WHERE v.idVuelo = ?1", Vuelo.class);
        query.setParameter(1, id.getIdVuelo());
        vuelo = query.getResultList();

        if(vuelo != null && !vuelo.isEmpty()){
            return vuelo.get(0);
        }else {
            return new Vuelo();
        }

    }

    @Override
    public Vuelo findByCodigo(String codigo) throws Exception {
        List<Vuelo> vuelo = new ArrayList<>();
        TypedQuery<Vuelo> query = em.createQuery("SELECT v FROM Vuelo v WHERE v.numeroVuelo = ?1", Vuelo.class);
        query.setParameter(1,codigo.toUpperCase());
        vuelo = query.getResultList();

        if(vuelo != null && !vuelo.isEmpty()){
            return vuelo.get(0);
        }else {
            return new Vuelo();
        }
    }
}
