package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarrollo.entity.Vinilo;
import mx.desarrollo.facade.FacadeVinilo;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Named("viniloBean")
@SessionScoped
public class ViniloBean implements Serializable {

    private FacadeVinilo facade;

    private String nombre;
    private String genero;
    private String descripcion;
    private String artista;
    private Integer cantidad;
    private Double precio;
    private String imagen;

    private String filtroNombre;

    private Vinilo viniloSeleccionado;
    private List<Vinilo> listaVinilos;

    public ViniloBean() {

    }


    private FacadeVinilo getFacade() {
        if (this.facade == null) {
            this.facade = ServiceFacadeLocator.getInstanceFacadeVinilo();
        }
        return this.facade;
    }

    @PostConstruct
    public void init() {
        try {
            cargarLista();
        } catch (Exception e) {

            FacesContext ctx = FacesContext.getCurrentInstance();
            if (ctx != null) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "No se pudo cargar la lista de vinilos inicialmente: " + e.getMessage()));
            }
        }
    }

    public void guardarVinilo() {
        try {
            FacadeVinilo f = getFacade();
            if (f == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo inicializar la capa de negocio"));
                return;
            }

            Vinilo vinilo = new Vinilo();
            vinilo.setNombre(nombre);
            vinilo.setGenero(genero);
            vinilo.setDescripcion(descripcion);
            vinilo.setArtista(artista);
            vinilo.setCantidad(cantidad);
            vinilo.setPrecio(precio);
            vinilo.setImagen(imagen);

            f.agregarVinilo(vinilo);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Vinilo guardado correctamente"));

            limpiarCampos();
            cargarLista();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar el vinilo: " + e.getMessage()));
        }
    }

    public void modificarVinilo() {
        try {
            if (viniloSeleccionado == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Selecciona un vinilo de la tabla antes de modificar"));
                return;
            }

            FacadeVinilo f = getFacade();
            if (f == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo inicializar la capa de negocio"));
                return;
            }

            viniloSeleccionado.setNombre(nombre);
            viniloSeleccionado.setGenero(genero);
            viniloSeleccionado.setDescripcion(descripcion);
            viniloSeleccionado.setArtista(artista);
            viniloSeleccionado.setCantidad(cantidad);
            viniloSeleccionado.setPrecio(precio);
            viniloSeleccionado.setImagen(imagen);

            f.modificarVinilo(viniloSeleccionado);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Vinilo modificado correctamente"));

            limpiarCampos();
            cargarLista();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo modificar el vinilo: " + e.getMessage()));
        }
    }

    public void eliminarVinilo() {
        try {
            if (viniloSeleccionado == null) {FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Selecciona un vinilo de la tabla antes de eliminar"));
                return;
            }

            FacadeVinilo f = getFacade();
            if (f == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo inicializar la capa de negocio"));
                return;
            }

            f.eliminarVinilo(viniloSeleccionado);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Vinilo eliminado correctamente"));

            limpiarCampos();
            cargarLista();


        } catch (Exception e) {FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el vinilo: " + e.getMessage()));
        }
    }

    public void limpiarCampos() {
        this.nombre = "";
        this.genero = "";
        this.descripcion = "";
        this.artista = "";
        this.cantidad = 0;
        this.precio = 0.0;
        this.imagen = "";
        this.viniloSeleccionado = null;
    }


    public void cargarViniloSeleccionado() {
        if (viniloSeleccionado != null) {
            this.nombre = viniloSeleccionado.getNombre();
            this.genero = viniloSeleccionado.getGenero();
            this.descripcion = viniloSeleccionado.getDescripcion();
            this.artista = viniloSeleccionado.getArtista();
            this.cantidad = viniloSeleccionado.getCantidad();
            this.precio = viniloSeleccionado.getPrecio();
            this.imagen = viniloSeleccionado.getImagen();
        }
    }

    private void cargarLista() {
        FacadeVinilo f = getFacade();
        if (f != null) {
            this.listaVinilos = f.obtenerVinilos();
        }
        if (this.listaVinilos == null) {
            this.listaVinilos = new ArrayList<>();
        }
    }

    public List<Vinilo> buscarVinilos() {
        if (listaVinilos == null || listaVinilos.isEmpty()) {
            cargarLista();
        }

        if (filtroNombre == null || filtroNombre.trim().isEmpty()) {
            return listaVinilos;
        }

        String filtro = filtroNombre.toLowerCase().trim();
        return listaVinilos.stream().filter(v -> v.getNombre() != null && v.getNombre().toLowerCase().contains(filtro)).collect(Collectors.toList());
    }





    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFiltroNombre() {
        return filtroNombre;
    }

    public void setFiltroNombre(String filtroNombre) {
        this.filtroNombre = filtroNombre;
    }

    public Vinilo getViniloSeleccionado() {
        return viniloSeleccionado;
    }

    public void setViniloSeleccionado(Vinilo viniloSeleccionado) {
        this.viniloSeleccionado = viniloSeleccionado;
    }

    public List<Vinilo> getListaVinilos() {
        return listaVinilos;
    }
}
