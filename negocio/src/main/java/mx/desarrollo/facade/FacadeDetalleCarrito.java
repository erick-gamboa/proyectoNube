
package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateDetalleCarrito;
import mx.desarrollo.entity.DetalleCarrito;

import java.util.List;

public class FacadeDetalleCarrito {

    private final DelegateDetalleCarrito delegateDetalle;

    public FacadeDetalleCarrito() {
        this.delegateDetalle = new DelegateDetalleCarrito();
    }

    public void agregarDetalle(DetalleCarrito detalle) {
        delegateDetalle.saveDetalleCarrito(detalle);
    }

    public void eliminarDetalle(DetalleCarrito detalle) {
        delegateDetalle.eliminarDetalleCarrito(detalle);
    }

    public void modificarDetalle(DetalleCarrito detalle) {
        delegateDetalle.actualizarDetalleCarrito(detalle);
    }

    public List<DetalleCarrito> obtenerDetalles() {
        return delegateDetalle.getDetallesCarrito();
    }
}
