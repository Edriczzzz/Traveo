package mx.upiita.traveo.ejb.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "vuelos")
public class Vuelo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_vuelo")
    private Integer idVuelo;


    @Column(name = "Numero_vuelo", length=8, nullable = false)
    private String numeroVuelo;

    @Column(name = "ID_aerolinea")
    private Integer idAerolinea;

    @Column(name = "ID_ruta")
    private Integer idRuta;

    @Temporal(TemporalType.DATE)
    @Column(name = "Fecha_salida")
    private Date fechaSalida;

    @Temporal(TemporalType.DATE)
    @Column(name = "Fecha_llegada")
    private Date fechaLlegada;

    @Column(name = "Precio", precision = 10, scale = 2)
    private BigDecimal precio;

    @Size(max = 20)
    @Column(name = "Estado", nullable = false)
    private String estado;

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

    public Integer getIdAerolinea() {
        return idAerolinea;
    }

    public void setIdAerolinea(Integer idAerolinea) {
        this.idAerolinea = idAerolinea;
    }

    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
