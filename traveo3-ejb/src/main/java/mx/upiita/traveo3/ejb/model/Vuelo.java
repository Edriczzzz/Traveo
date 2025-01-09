package mx.upiita.traveo3.ejb.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "vuelos")
public class Vuelo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_vuelo", nullable = false)
    private Integer idVuelo;

    @Column(name = "Numero_vuelo", nullable = false)
    private String numeroVuelo;

    @ManyToOne
    @JoinColumn(name = "ID_ruta", nullable = false)
    private Ruta ruta;

    @ManyToOne
    @JoinColumn(name = "ID_aerolinea", nullable = true)
    private Aerolinea aerolinea;

    @Column(name = "Fecha_salida", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSalida;

    @Column(name = "Fecha_llegada", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLlegada;

    @Column(name = "Precio", nullable = false)
    private Float precio;

    @Column(name = "Estado", nullable = false)
    private String estado;

    public Vuelo() {
    }

    // Getters y Setters
    public Integer getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(Integer idVuelo) {
        this.idVuelo = idVuelo;
    }

    public String getNumeroVuelo() {
        return numeroVuelo;
    }

    public void setNumeroVuelo(String numeroVuelo) {
        this.numeroVuelo = numeroVuelo;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Aerolinea getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(Date fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Métodos hashCode y equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vuelo vuelo = (Vuelo) o;
        return Objects.equals(idVuelo, vuelo.idVuelo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVuelo);
    }

    @Override
    public String toString() {
        return "Vuelo{" +
                "idVuelo=" + idVuelo +
                ", numeroVuelo='" + numeroVuelo + '\'' +
                ", ruta=" + (ruta != null ? ruta.getIdRuta() : null) +
                ", fechaSalida=" + fechaSalida +
                ", fechaLlegada=" + fechaLlegada +
                ", precio=" + precio +
                ", estado='" + estado + '\'' +
                '}';
    }
}
