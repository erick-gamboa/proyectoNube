
package mx.desarrollo.ui;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mx.desarrollo.entity.Carrito;
import mx.desarrollo.entity.DetalleCarrito;
import mx.desarrollo.entity.Vinilo;
import mx.desarrollo.facade.FacadeCarrito;
import mx.desarrollo.facade.FacadeDetalleCarrito;
import mx.desarrollo.integration.ServiceFacadeLocator;

@Named("carritoBean")
@SessionScoped
public class CarritoBean implements Serializable {

    @Inject
    private LoginBeanUI loginUI;

    public void agregar(Vinilo vinilo) {

        if (vinilo == null || vinilo.getIdVinilo() == null) {
            return;
        }

        if (loginUI.getUsuario() == null ||
                loginUI.getUsuario().getIdUsuario() == null) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Iniciar sesión",
                            "Necesitas iniciar sesión para agregar productos"));
            return;
        }

        FacadeCarrito facadeCarrito =
                ServiceFacadeLocator.getInstanceFacadeCarrito();

        FacadeDetalleCarrito facadeDetalle =
                ServiceFacadeLocator.getInstanceFacadeDetalleCarrito();

        // Consultar las existencias actuales
        Vinilo viniloActual = null;

        for (Vinilo v : ServiceFacadeLocator
                .getInstanceFacadeVinilo().obtenerVinilos()) {

            if (Objects.equals(v.getIdVinilo(), vinilo.getIdVinilo())) {
                viniloActual = v;
                break;
            }
        }

        if (viniloActual == null ||
                viniloActual.getCantidad() == null ||
                viniloActual.getCantidad() <= 0) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Sin existencias",
                            "Este vinilo no está disponible"));
            return;
        }

        // Buscar el carrito del usuario
        Carrito carrito = null;

        for (Carrito c : facadeCarrito.obtenerCarritos()) {

            if (c.getUsuario() != null &&
                    Objects.equals(
                            c.getUsuario().getIdUsuario(),
                            loginUI.getUsuario().getIdUsuario())) {

                carrito = c;
                break;
            }
        }

        // Buscar si el vinilo ya está agregado
        DetalleCarrito existente = null;

        if (carrito != null) {

            for (DetalleCarrito d : facadeDetalle.obtenerDetalles()) {

                if (d.getCarrito() != null &&
                        Objects.equals(d.getCarrito().getId(), carrito.getId()) &&
                        d.getVinilo() != null &&
                        Objects.equals(d.getVinilo().getIdVinilo(),
                                vinilo.getIdVinilo())) {

                    existente = d;
                    break;
                }
            }
        }

        // Si existe, aumentar su cantidad
        if (existente != null) {

            if (existente.getCantidad() == null ||
                    existente.getCantidad() >= viniloActual.getCantidad()) {

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Stock insuficiente",
                                "Solo hay " + viniloActual.getCantidad()
                                        + " unidades disponibles"));
                return;
            }

            existente.setCantidad(existente.getCantidad() + 1);
            facadeDetalle.modificarDetalle(existente);

        } else {

            // Crear carrito únicamente si no existe
            if (carrito == null) {

                carrito = new Carrito();
                carrito.setUsuario(loginUI.getUsuario());

                facadeCarrito.agregarCarrito(carrito);

                if (carrito.getId() == null) {
                    throw new IllegalStateException(
                            "No se generó el ID del carrito. "
                                    + "Revisa saveCarrito()");
                }
            }

            DetalleCarrito detalle = new DetalleCarrito();
            detalle.setCarrito(carrito);
            detalle.setVinilo(viniloActual);
            detalle.setCantidad(1);

            facadeDetalle.agregarDetalle(detalle);
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Carrito",
                        "Se agregó " + viniloActual.getNombre()
                                + " al carrito"));
    }

    public List<DetalleCarrito> getDetalles() {

        List<DetalleCarrito> detallesUsuario = new ArrayList<>();

        if (loginUI.getUsuario() == null ||
                loginUI.getUsuario().getIdUsuario() == null) {
            return detallesUsuario;
        }

        List<DetalleCarrito> todos = ServiceFacadeLocator
                .getInstanceFacadeDetalleCarrito()
                .obtenerDetalles();

        if (todos == null) {
            return detallesUsuario;
        }

        for (DetalleCarrito detalle : todos) {

            if (detalle.getCarrito() != null &&
                    detalle.getCarrito().getUsuario() != null &&
                    Objects.equals(
                            detalle.getCarrito().getUsuario().getIdUsuario(),
                            loginUI.getUsuario().getIdUsuario())) {

                detallesUsuario.add(detalle);
            }
        }

        return detallesUsuario;
    }

    public void aumentarCantidad(DetalleCarrito detalle) {

        if (detalle == null || detalle.getId() == null) {
            return;
        }

        DetalleCarrito encontrado = null;

        for (DetalleCarrito d : getDetalles()) {
            if (Objects.equals(d.getId(), detalle.getId())) {
                encontrado = d;
                break;
            }
        }

        if (encontrado == null || encontrado.getVinilo() == null) {
            return;
        }

        // Consultar el stock actualizado
        Integer stock = null;

        for (Vinilo v : ServiceFacadeLocator
                .getInstanceFacadeVinilo().obtenerVinilos()) {

            if (Objects.equals(v.getIdVinilo(),
                    encontrado.getVinilo().getIdVinilo())) {

                stock = v.getCantidad();
                break;
            }
        }

        if (stock == null ||
                encontrado.getCantidad() == null ||
                encontrado.getCantidad() >= stock) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Stock insuficiente",
                            "No puedes agregar más unidades de este vinilo"));
            return;
        }

        encontrado.setCantidad(encontrado.getCantidad() + 1);

        ServiceFacadeLocator.getInstanceFacadeDetalleCarrito()
                .modificarDetalle(encontrado);
    }

    public void disminuirCantidad(DetalleCarrito detalle) {

        if (detalle == null || detalle.getId() == null) {
            return;
        }

        DetalleCarrito encontrado = null;

        for (DetalleCarrito d : getDetalles()) {
            if (Objects.equals(d.getId(), detalle.getId())) {
                encontrado = d;
                break;
            }
        }

        if (encontrado == null ||
                encontrado.getCantidad() == null ||
                encontrado.getCantidad() <= 1) {
            return;
        }

        encontrado.setCantidad(encontrado.getCantidad() - 1);

        ServiceFacadeLocator.getInstanceFacadeDetalleCarrito()
                .modificarDetalle(encontrado);
    }

    public void eliminar(DetalleCarrito detalle) {

        if (detalle == null ||
                detalle.getId() == null ||
                loginUI.getUsuario() == null ||
                loginUI.getUsuario().getIdUsuario() == null) {
            return;
        }

        DetalleCarrito encontrado = null;

        for (DetalleCarrito d : getDetalles()) {

            if (Objects.equals(d.getId(), detalle.getId())) {
                encontrado = d;
                break;
            }
        }

        if (encontrado == null) {
            return;
        }

        ServiceFacadeLocator.getInstanceFacadeDetalleCarrito()
                .eliminarDetalle(encontrado);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Carrito",
                        "Producto eliminado del carrito"));
    }

    public double getSubtotal() {

        double subtotal = 0;

        for (DetalleCarrito detalle : getDetalles()) {

            if (detalle.getVinilo() != null &&
                    detalle.getVinilo().getPrecio() != null &&
                    detalle.getCantidad() != null) {

                subtotal += detalle.getVinilo().getPrecio()
                        * detalle.getCantidad();
            }
        }

        return subtotal;
    }
}
