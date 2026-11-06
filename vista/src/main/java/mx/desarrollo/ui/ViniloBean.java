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

    // Orden del catálogo: "az", "menor" (precio de menor a mayor) o "mayor" (precio de mayor a menor)
    private String orden;

    private String filtroNombre;
    private String busquedaSugerencias;
    private String filtroGenero;
    private String filtroStock;
    private Vinilo viniloSeleccionado;
    private List<Vinilo> listaVinilos;

    public ViniloBean() {
        this.facade = ServiceFacadeLocator.getInstanceFacadeVinilo();
        this.listaVinilos = new ArrayList<Vinilo>();
    }

    @PostConstruct
    public void init() {
        try {
            cargarLista();
        } catch (Exception e) {
            mensaje(FacesMessage.SEVERITY_WARN, "Aviso", "No se pudo cargar la lista: " + e.getMessage());
        }
    }

    private FacadeVinilo getFacade() {
        if (this.facade == null) {
            this.facade = ServiceFacadeLocator.getInstanceFacadeVinilo();
        }
        return this.facade;
    }

    private void mensaje(FacesMessage.Severity tipo, String titulo, String detalle) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            ctx.addMessage(null, new FacesMessage(tipo, titulo, detalle));
        }
    }

    /* ---------- Validaciones ---------- */

    /** Devuelve el primer error encontrado o null si todo está bien. */
    private String validarCampos(Integer idActual) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es obligatorio";
        }
        if (artista == null || artista.trim().isEmpty()) {
            return "El artista es obligatorio";
        }
        if (genero == null || genero.trim().isEmpty()) {
            return "Selecciona un género";
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "La descripción es obligatoria";
        }
        if (precio == null || precio.isNaN() || precio <= 0) {
            return "El precio debe ser mayor a 0";
        }
        if (precio > 1000000) {
            return "El precio es demasiado alto";
        }
        if (imagen == null || imagen.trim().isEmpty()) {
            return "La imagen es obligatoria";
        }
        String img = imagen.trim().toLowerCase();
        if (img.contains(" ")) {
            return "La imagen no puede contener espacios";
        }
        if (nombreRepetido(idActual)) {
            return "Ya existe un vinilo llamado \"" + nombre.trim() + "\"";
        }
        return null;
    }

    private boolean nombreRepetido(Integer idActual) {
        cargarLista();
        String buscado = nombre.trim();
        for (Vinilo v : listaVinilos) {
            if (v.getNombre() == null) {
                continue;
            }
            boolean mismoId = idActual != null && idActual.equals(v.getIdVinilo());
            if (!mismoId && v.getNombre().trim().equalsIgnoreCase(buscado)) {
                return true;
            }
        }
        return false;
    }

    /* ---------- CRUD ---------- */

    public void guardarVinilo() {
        try {
            FacadeVinilo f = getFacade();
            if (f == null) {
                mensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo inicializar la capa de negocio");
                return;
            }

            String error = validarCampos(null);
            if (error != null) {
                mensaje(FacesMessage.SEVERITY_ERROR, "No se pudo guardar", error);
                return;
            }

            Vinilo vinilo = new Vinilo();
            vinilo.setNombre(nombre.trim());
            vinilo.setGenero(genero);
            vinilo.setDescripcion(descripcion.trim());
            vinilo.setArtista(artista.trim());
            vinilo.setCantidad(0);
            vinilo.setPrecio(precio);
            vinilo.setImagen(imagen.trim());

            f.agregarVinilo(vinilo);

            mensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Vinilo guardado correctamente");

            limpiarCampos();
            cargarLista();
        } catch (Exception e) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar: " + e.getMessage());
        }
    }

    public void modificarVinilo() {
        try {
            if (viniloSeleccionado == null) {
                mensaje(FacesMessage.SEVERITY_WARN, "Atención", "Selecciona un vinilo de la tabla");
                return;
            }

            FacadeVinilo f = getFacade();
            if (f == null) {
                mensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo inicializar la capa de negocio");
                return;
            }

            String error = validarCampos(viniloSeleccionado.getIdVinilo());
            if (error != null) {
                mensaje(FacesMessage.SEVERITY_ERROR, "No se pudo modificar", error);
                return;
            }

            viniloSeleccionado.setNombre(nombre.trim());
            viniloSeleccionado.setGenero(genero);
            viniloSeleccionado.setDescripcion(descripcion.trim());
            viniloSeleccionado.setArtista(artista.trim());
            viniloSeleccionado.setPrecio(precio);
            viniloSeleccionado.setImagen(imagen.trim());

            f.modificarVinilo(viniloSeleccionado);

            mensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Vinilo modificado correctamente");

            limpiarCampos();
            cargarLista();
        } catch (Exception e) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo modificar: " + e.getMessage());
        }
    }

    public void eliminarVinilo() {
        try {
            if (viniloSeleccionado == null) {
                mensaje(FacesMessage.SEVERITY_WARN, "Atención", "Selecciona un vinilo de la tabla");
                return;
            }

            FacadeVinilo f = getFacade();
            if (f == null) {
                mensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo inicializar la capa de negocio");
                return;
            }

            f.eliminarVinilo(viniloSeleccionado);

            mensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Vinilo eliminado correctamente");

            limpiarCampos();
            cargarLista();
        } catch (Exception e) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar: " + e.getMessage());
        }
    }

    public void limpiarCampos() {
        this.nombre = "";
        this.genero = "";
        this.descripcion = "";
        this.artista = "";
        this.cantidad = 0;
        this.precio = null;
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
            this.listaVinilos = new ArrayList<Vinilo>();
        }
    }

    /* ---------- Búsqueda, filtros y orden ---------- */

    public List<Vinilo> buscarVinilos() {
        if (listaVinilos == null || listaVinilos.isEmpty()) {
            cargarLista();
        }

        List<Vinilo> resultado = new ArrayList<Vinilo>();
        if (filtroNombre != null && !filtroNombre.trim().isEmpty()) {
            String filtro = filtroNombre.toLowerCase().trim();
            for (Vinilo v : listaVinilos) {
                if (v.getNombre() != null && v.getNombre().toLowerCase().contains(filtro)) {
                    resultado.add(v);
                }
            }
        } else {
            resultado.addAll(listaVinilos);
        }

        if (filtroGenero != null && !filtroGenero.trim().isEmpty()) {
            List<Vinilo> filtrados = new ArrayList<Vinilo>();
            for (Vinilo v : resultado) {
                if (v.getGenero() != null && v.getGenero().trim().equalsIgnoreCase(filtroGenero.trim())) {
                    filtrados.add(v);
                }
            }
            resultado = filtrados;
        }

        if (hayFiltroStock()) {
            List<Vinilo> conStockFiltrado = new ArrayList<Vinilo>();
            for (Vinilo v : resultado) {
                Integer c = v.getCantidad();
                boolean cumple = "disponibles".equalsIgnoreCase(filtroStock)
                        ? (c != null && c > 0)
                        : (c != null && c <= 0);
                if (cumple) {
                    conStockFiltrado.add(v);
                }
            }
            resultado = conStockFiltrado;
        }

        // Orden (se aplica al final, sobre la lista ya filtrada)
        if ("az".equals(orden)) {
            resultado.sort((a, b) -> nombreDe(a).compareTo(nombreDe(b)));
        } else if ("menor".equals(orden)) {
            resultado.sort((a, b) -> Double.compare(precioDe(a), precioDe(b)));
        } else if ("mayor".equals(orden)) {
            resultado.sort((a, b) -> Double.compare(precioDe(b), precioDe(a)));
        }

        return resultado;
    }

    /** Sugerencias independientes de los filtros del catálogo. */
    public List<Vinilo> buscarSugerencias() {
        List<Vinilo> resultado = new ArrayList<>();
        if (busquedaSugerencias == null || busquedaSugerencias.trim().isEmpty()) {
            return resultado;
        }
        if (listaVinilos == null || listaVinilos.isEmpty()) {
            cargarLista();
        }
        String texto = busquedaSugerencias.trim().toLowerCase();
        for (Vinilo vinilo : listaVinilos) {
            if (vinilo.getNombre() != null && vinilo.getNombre().toLowerCase().contains(texto)) {
                resultado.add(vinilo);
            }
        }
        return resultado;
    }

    public boolean hayBusquedaSugerencias() {
        return busquedaSugerencias != null && !busquedaSugerencias.trim().isEmpty();
    }

    public String getBusquedaSugerencias() { return busquedaSugerencias; }
    public void setBusquedaSugerencias(String busquedaSugerencias) { this.busquedaSugerencias = busquedaSugerencias; }

    private String nombreDe(Vinilo v) {
        return v.getNombre() == null ? "" : v.getNombre().toLowerCase();
    }

    private double precioDe(Vinilo v) {
        return v.getPrecio() == null ? 0.0 : v.getPrecio();
    }

    public boolean hayFiltro() {
        return filtroNombre != null && !filtroNombre.trim().isEmpty();
    }

    public boolean hayFiltroGenero() {
        return filtroGenero != null && !filtroGenero.trim().isEmpty();
    }

    public boolean hayFiltroStock() {
        return filtroStock != null && !filtroStock.trim().isEmpty();
    }

    /** true si hay algún filtro u orden activo en el catálogo. */
    public boolean hayFiltros() {
        return hayFiltroGenero() || hayFiltroStock() || (orden != null && !orden.isEmpty());
    }

    public void quitarFiltros() {
        this.filtroGenero = null;
        this.filtroStock = null;
        this.orden = null;
    }

    /* Género: al pulsar el que ya está activo, se quita */

    private void alternarGenero(String g) {
        this.filtroGenero = g.equals(this.filtroGenero) ? null : g;
    }

    public void filtrarTodos() {
        this.filtroGenero = null;
    }

    public void filtrarRock() {
        alternarGenero("Rock");
    }

    public void filtrarPop() {
        alternarGenero("Pop");
    }

    public void filtrarJazz() {
        alternarGenero("Jazz");
    }

    public void filtrarElectronica() {
        alternarGenero("Electrónica");
    }

    /* Stock: al pulsar el que ya está activo, se quita */

    public void filtrarStockDisponibles() {
        this.filtroStock = "disponibles".equals(this.filtroStock) ? null : "disponibles";
    }

    public void filtrarStockAgotados() {
        this.filtroStock = "agotados".equals(this.filtroStock) ? null : "agotados";
    }

    public String getFiltroStock() {
        return filtroStock;
    }

    public void setFiltroStock(String filtroStock) {
        this.filtroStock = filtroStock;
    }

    /* Orden: al pulsar el que ya está activo, se quita */

    private void alternarOrden(String o) {
        this.orden = o.equals(this.orden) ? null : o;
    }

    public String getOrden() { return orden; }
    public void setOrden(String orden) { this.orden = orden; }

    public void ordenarAZ() {
        alternarOrden("az");
    }

    public void ordenarPrecioMenor() {
        alternarOrden("menor");
    }

    public void ordenarPrecioMayor() {
        alternarOrden("mayor");
    }

    public String seleccionar(Vinilo vinilo) {
        this.viniloSeleccionado = vinilo;
        this.busquedaSugerencias = "";
        if (vinilo != null && vinilo.getIdVinilo() != null) {
            return "/detalle.xhtml?vinilo=" + vinilo.getIdVinilo() + "&faces-redirect=true";
        }
        return "/index.xhtml?faces-redirect=true";
    }

    /* ---------- Stock ---------- */

    public Integer getCantidadStock(Integer idVinilo) {
        if (listaVinilos == null) {
            return 0;
        }
        for (Vinilo v : listaVinilos) {
            if (v.getIdVinilo() != null && v.getIdVinilo().equals(idVinilo)) {
                return v.getCantidad();
            }
        }
        return 0;
    }

    public void disminuirStock(Integer idVinilo) {
        Integer actual = getCantidadStock(idVinilo);
        if (actual != null && actual > 0) {
            ajustarStock(idVinilo, actual - 1);
        }
    }

    public void aumentarStock(Integer idVinilo) {
        Integer actual = getCantidadStock(idVinilo);
        ajustarStock(idVinilo, actual + 1);
    }

    public void ajustarStock(Integer idVinilo, Integer nuevaCantidad) {
        try {
            FacadeVinilo f = getFacade();
            if (f == null) {
                return;
            }
            f.actualizarStock(idVinilo, nuevaCantidad);
            cargarLista();
        } catch (Exception e) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar stock: " + e.getMessage());
        }
    }

    public boolean isSinStock(Integer idVinilo) {
        Integer cant = getCantidadStock(idVinilo);
        return cant != null && cant == 0;
    }

    /* ---------- Getters y setters ---------- */

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getFiltroNombre() { return filtroNombre; }
    public void setFiltroNombre(String filtroNombre) { this.filtroNombre = filtroNombre; }

    public String getFiltroGenero() { return filtroGenero; }
    public void setFiltroGenero(String filtroGenero) { this.filtroGenero = filtroGenero; }

    public Vinilo getViniloSeleccionado() { return viniloSeleccionado; }
    public void setViniloSeleccionado(Vinilo viniloSeleccionado) { this.viniloSeleccionado = viniloSeleccionado; }

    public List<Vinilo> getListaVinilos() { return listaVinilos; }
    public void setListaVinilos(List<Vinilo> listaVinilos) { this.listaVinilos = listaVinilos; }
}