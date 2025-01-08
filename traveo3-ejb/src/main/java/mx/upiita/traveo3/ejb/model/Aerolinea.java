package mx.upiita.traveo3.ejb.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "aerolineas")
public class Aerolinea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name ="ID_aerolinea")
    private Integer id;

    @Size(max = 25)
    @Column(name = "Nombre")
    private String nombre;

    @Size(max = 6)
    @Column(name = "Codigo")
    private String codigo;

    @Size(max = 20)
    @Column(name = "Pais")
    private String pais;


    public Aerolinea(Integer id, String nombre, String codigo, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.pais = pais;
    }

    public Aerolinea() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @Size(max = 6) String getCodigo() {
        return codigo;
    }

    public void setCodigo(@Size(max = 6) String codigo) {
        this.codigo = codigo;
    }

    public @Size(max = 25) String getNombre() {
        return nombre;
    }

    public void setNombre(@Size(max = 25) String nombre) {
        this.nombre = nombre;
    }

    public @Size(max = 20) String getPais() {
        return pais;
    }

    public void setPais(@Size(max = 20) String pais) {
        this.pais = pais;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aerolinea aerolinea = (Aerolinea) o;
        return Objects.equals(id, aerolinea.id) && Objects.equals(nombre, aerolinea.nombre) && Objects.equals(codigo, aerolinea.codigo) && Objects.equals(pais, aerolinea.pais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, codigo, pais);
    }

    @Override
    public String toString() {
        return "Aerolinea{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}
