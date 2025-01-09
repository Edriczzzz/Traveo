
package mx.upiita.traveo3.war.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.upiita.traveo3.ejb.model.*;
        import mx.upiita.traveo3.ejb.service.*;

        import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Named(value = "rutaController")
@ViewScoped
public class RutaController implements Serializable {

    private Logger logger = Logger.getLogger(RutaController.class.getName());

    @Inject
    private CiudadServiceLocal ciudadService;

    @Inject
    private RutaServiceLocal rutaService;

    @Inject
    private VueloServiceLocal vueloService;

    @Inject
    private UsuarioServiceLocal usuarioService;

    @Inject
    private AerolineaServiceLocal aerolineaService;

    private List<Ciudad> ciudades;
    private Integer idCiudadOrigen;
    private Integer idCiudadDestino;
    private Date fechaSalida;
    private Date fechaRegreso;

    private List<Aerolinea> aerolineas;
    private Integer idAerolineaSeleccionada;

    @PostConstruct
    public void init() {
        cargarCiudades();
        cargarAerolineas();
    }

    public void cargarAerolineas() {
        try {
            aerolineas = aerolineaService.listar();
            logger.info("Aerolíneas cargadas: " + aerolineas.size());
        } catch (Exception e) {
            logger.severe("Error al cargar aerolíneas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cargarCiudades() {
        try {
            ciudades = ciudadService.listar();
            logger.info("Ciudades cargadas: " + ciudades.size());
        } catch (Exception e) {
            logger.severe("Error al cargar ciudades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void guardarRutaYVuelo() {
        try {
            // Verificar si hay un usuario autenticado antes de continuar
            Usuario usuarioAutenticado = usuarioService.obtenerUsuarioAutenticado();
            if (usuarioAutenticado == null) {
                logger.severe("No hay usuario autenticado para crear la ruta y vuelo");
                // Aquí podrías agregar código para mostrar un mensaje al usuario
                return;
            }

            Ciudad ciudadOrigen = ciudades.stream()
                    .filter(c -> c.getIdCiudad().equals(idCiudadOrigen))
                    .findFirst()
                    .orElse(null);

            Ciudad ciudadDestino = ciudades.stream()
                    .filter(c -> c.getIdCiudad().equals(idCiudadDestino))
                    .findFirst()
                    .orElse(null);

            Aerolinea aerolineaSeleccionada = aerolineas.stream()
                    .filter(a -> a.getId().equals(idAerolineaSeleccionada))
                    .findFirst()
                    .orElse(null);

            if (ciudadOrigen != null && ciudadDestino != null && aerolineaSeleccionada != null) {
                // Crear y persistir la ruta
                Ruta nuevaRuta = new Ruta();
                nuevaRuta.setCiudadOrigen(ciudadOrigen);
                nuevaRuta.setCiudadDestino(ciudadDestino);
                nuevaRuta = rutaService.crear(nuevaRuta);
                logger.info("Ruta creada con ID: " + nuevaRuta.getIdRuta());

                // Crear y persistir el vuelo de ida
                Vuelo vueloIda = new Vuelo();
                vueloIda.setRuta(nuevaRuta);
                vueloIda.setFechaSalida(fechaSalida);
                vueloIda.setFechaLlegada(fechaSalida);
                vueloIda.setEstado("Programado");
                vueloIda.setNumeroVuelo("Vuelo-IDA-" + nuevaRuta.getIdRuta());
                vueloIda.setPrecio(generarPrecioAleatorio());
                vueloIda.setAerolinea(aerolineaSeleccionada);

                // Persistir el vuelo de ida y obtener la entidad persistida
                Vuelo vueloIdaPersistido = vueloService.crear(vueloIda);
                logger.info("Vuelo de ida creado con ID: " + vueloIdaPersistido.getIdVuelo());

                // Crear y persistir el vuelo de regreso
                Vuelo vueloRegreso = new Vuelo();
                vueloRegreso.setRuta(nuevaRuta);
                vueloRegreso.setFechaSalida(fechaRegreso);
                vueloRegreso.setFechaLlegada(fechaRegreso);
                vueloRegreso.setEstado("Programado");
                vueloRegreso.setNumeroVuelo("Vuelo-REG-" + nuevaRuta.getIdRuta());
                vueloRegreso.setPrecio(generarPrecioAleatorio());
                vueloRegreso.setAerolinea(aerolineaSeleccionada);
                Vuelo vueloRegresoPersistido = vueloService.crear(vueloRegreso);
                logger.info("Vuelo de regreso creado con ID: " + vueloRegresoPersistido.getIdVuelo());

                // Asignar el vuelo de ida al usuario y actualizar
                try {
                    usuarioAutenticado.setVuelo(vueloIdaPersistido);
                    Usuario usuarioActualizado = usuarioService.actualizar(usuarioAutenticado);
                    if (usuarioActualizado != null) {
                        logger.info("Vuelo asignado y usuario actualizado: " + usuarioActualizado.getCorreo());
                    } else {
                        logger.severe("Error al actualizar el usuario con el vuelo");
                    }
                    logger.info("Vuelo asignado al usuario con ID: " + usuarioAutenticado.getIdUsuario());
                } catch (Exception e) {
                    logger.severe("Error al actualizar el usuario con el vuelo: " + e.getMessage());
                    // Aquí podrías agregar código para mostrar un mensaje de error al usuario
                    throw e;
                }

                // Limpiar los campos del formulario
                limpiarCampos();
                logger.info("Proceso completado exitosamente");
            } else {
                logger.warning("Datos incompletos: Origen: " + ciudadOrigen + ", Destino: " + ciudadDestino +
                        ", Aerolínea: " + aerolineaSeleccionada);
                // Aquí podrías agregar código para mostrar un mensaje de error al usuario
            }
        } catch (Exception e) {
            logger.severe("Error al guardar la ruta y vuelos: " + e.getMessage());
            e.printStackTrace();
            // Aquí podrías agregar código para mostrar un mensaje de error al usuario
        }
    }

    private void limpiarCampos() {
        idCiudadOrigen = null;
        idCiudadDestino = null;
        fechaSalida = null;
        fechaRegreso = null;
        idAerolineaSeleccionada = null;
    }

    private float generarPrecioAleatorio() {
        // Modificado para generar un precio más realista entre 1000 y 5000
        return 1000 + (float)(Math.random() * 4000);
    }


    public List<Ciudad> getCiudades() {
        return ciudades;
    }

    public List<Aerolinea> getAerolineas() {
        return aerolineas;
    }

    public void setAerolineas(List<Aerolinea> aerolineas) {
        this.aerolineas = aerolineas;
    }

    public Integer getIdAerolineaSeleccionada() {
        return idAerolineaSeleccionada;
    }

    public void setIdAerolineaSeleccionada(Integer idAerolineaSeleccionada) {
        this.idAerolineaSeleccionada = idAerolineaSeleccionada;
    }

    public Integer getIdCiudadOrigen() {
        return idCiudadOrigen;
    }

    public void setIdCiudadOrigen(Integer idCiudadOrigen) {
        this.idCiudadOrigen = idCiudadOrigen;
    }

    public Integer getIdCiudadDestino() {
        return idCiudadDestino;
    }

    public void setIdCiudadDestino(Integer idCiudadDestino) {
        this.idCiudadDestino = idCiudadDestino;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getFechaRegreso() {
        return fechaRegreso;
    }

    public void setFechaRegreso(Date fechaRegreso) {
        this.fechaRegreso = fechaRegreso;
    }
}

