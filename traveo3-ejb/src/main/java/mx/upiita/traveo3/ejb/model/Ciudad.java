package mx.upiita.traveo3.ejb.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Ciudades")
public class Ciudad implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ciudad")
    private Integer idCiudad;

    @Column(name = "Nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "Codigo", length = 10, nullable = false, unique = true)
    private String codigo;

    @Column(name = "Pais", length = 50, nullable = false)
    private String pais;

    public Ciudad() {
    }

    public Ciudad(Integer idCiudad, String nombre, String codigo, String pais) {
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.codigo = codigo;
        this.pais = pais;
    }

    // Getters y Setters
    public Integer getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Integer idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    // Métodos hashCode y equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ciudad ciudad = (Ciudad) o;
        return Objects.equals(idCiudad, ciudad.idCiudad) &&
                Objects.equals(nombre, ciudad.nombre) &&
                Objects.equals(codigo, ciudad.codigo) &&
                Objects.equals(pais, ciudad.pais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCiudad, nombre, codigo, pais);
    }

    @Override
    public String toString() {
        return "Ciudad{" +
                "idCiudad=" + idCiudad +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}
