package mx.upiita.traveo3.ejb.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Rutas")
public class Ruta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ruta")
    private Integer idRuta;

    @ManyToOne
    @JoinColumn(name = "ID_ciudad_origen", nullable = false)
    private Ciudad ciudadOrigen;

    @ManyToOne
    @JoinColumn(name = "ID_ciudad_destino", nullable = false)
    private Ciudad ciudadDestino;

    @Column(name = "Distancia", nullable = false)
    private Float distancia;

    public Ruta() {
    }

    public Ruta(Integer idRuta, Ciudad ciudadOrigen, Ciudad ciudadDestino, Float distancia) {
        this.idRuta = idRuta;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.distancia = distancia;
    }

    // Getters y Setters
    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public Ciudad getCiudadOrigen() {
        return ciudadOrigen;
    }

    public void setCiudadOrigen(Ciudad ciudadOrigen) {
        this.ciudadOrigen = ciudadOrigen;
    }

    public Ciudad getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(Ciudad ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public Float getDistancia() {
        return distancia;
    }

    public void setDistancia(Float distancia) {
        this.distancia = distancia;
    }

    // Métodos hashCode y equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruta ruta = (Ruta) o;
        return Objects.equals(idRuta, ruta.idRuta) &&
                Objects.equals(ciudadOrigen, ruta.ciudadOrigen) &&
                Objects.equals(ciudadDestino, ruta.ciudadDestino) &&
                Objects.equals(distancia, ruta.distancia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRuta, ciudadOrigen, ciudadDestino, distancia);
    }

    @Override
    public String toString() {
        return "Ruta{" +
                "idRuta=" + idRuta +
                ", ciudadOrigen=" + ciudadOrigen +
                ", ciudadDestino=" + ciudadDestino +
                ", distancia=" + distancia +
                '}';
    }
}
