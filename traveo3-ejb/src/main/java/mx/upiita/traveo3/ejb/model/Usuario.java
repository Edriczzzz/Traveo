package mx.upiita.traveo3.ejb.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_usuario")
    private Integer idUsuario;

    @Column(name = "Nombre", length = 30,nullable = false)
    private String nombre;

    @Email
    @Column(name = "Correo", length = 50, nullable = false)
    private String correo;

    @Size(max = 15)
    @Column(name = "Telefono")
    private String telefono;

    @Size(max = 255)
    @Column(name = "Contrasena", nullable = false)
    private String contrasena;

    @Temporal(TemporalType.DATE)
    @Column(name = "Fecha_registro")
    private Date fechaRegistro;

    @Temporal(TemporalType.DATE)
    @Column(name = "Fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "Asiento", length = 3, nullable = false)
    private String asiento;

    @Column(name = "Monto_pagado")
    private double montoPagado;

    @ManyToOne
    @JoinColumn(name = "ID_vuelo", nullable = false)
    private Vuelo vuelo;

    @Version
    @Column(name = "version")
    private Integer version;

    @Enumerated(EnumType.STRING)
    private Rol rol;



    public Usuario(Integer idUsuario, String nombre, String correo, String telefono, String contrasena, Date fechaRegistro, Date fechaNacimiento, String asiento, double montoPagado, Vuelo vuelo, Integer version) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.fechaRegistro = fechaRegistro;
        this.fechaNacimiento = fechaNacimiento;
        this.asiento = asiento;
        this.montoPagado = montoPagado;
        this.vuelo = vuelo;
        this.version = version;
    }

    public Usuario() {

    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public @Email String getCorreo() {
        return correo;
    }

    public void setCorreo(@Email String correo) {
        this.correo = correo;
    }

    public @Size(max = 15) String getTelefono() {
        return telefono;
    }

    public void setTelefono(@Size(max = 15) String telefono) {
        this.telefono = telefono;
    }

    public @Size(max = 255) String getContrasena() {
        return contrasena;
    }

    public void setContrasena(@Size(max = 255) String contrasena) {
        this.contrasena = contrasena;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Rol getRol() { // Asegúrate de tener este método
        return rol;
    }

    public void setRol(Rol rol) { // Asegúrate de tener este método
        this.rol = rol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Double.compare(montoPagado, usuario.montoPagado) == 0 && Objects.equals(idUsuario, usuario.idUsuario) && Objects.equals(nombre, usuario.nombre) && Objects.equals(correo, usuario.correo) && Objects.equals(telefono, usuario.telefono) && Objects.equals(contrasena, usuario.contrasena) && Objects.equals(fechaRegistro, usuario.fechaRegistro) && Objects.equals(fechaNacimiento, usuario.fechaNacimiento) && Objects.equals(asiento, usuario.asiento) && Objects.equals(vuelo, usuario.vuelo) && Objects.equals(version, usuario.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, nombre, correo, telefono, contrasena, fechaRegistro, fechaNacimiento, asiento, montoPagado, vuelo, version);
    }
    public enum Rol {
        CLIENTE,
        ADMINISTRADOR
    }
}
