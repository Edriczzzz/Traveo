package mx.upiita.traveo3.ejb.service;

import jakarta.persistence.EntityManager;

public abstract class AbstractFacade<T> {

    private Class<T> claseEntidad;

    public AbstractFacade(Class<T> claseEntidad) {
        this.claseEntidad = claseEntidad;
    }

    abstract protected EntityManager getEntityManager();

    public T create(T entidad) {
        getEntityManager().persist(entidad);
        return entidad;

    }

    public T actualizar(T entidad) {
        T entidadCopia;
        if(entidad != null) {
            entidadCopia = getEntityManager().merge(entidad);
            return entidadCopia;
        }
        return null;
    }

    public void borrar(T entidad) {
        getEntityManager().remove(this.actualizar(entidad));

    }

}
