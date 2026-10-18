
package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateDetalleTicket;
import mx.desarrollo.entity.DetalleTicket;

import java.util.List;

public class FacadeDetalleTicket {

    private final DelegateDetalleTicket delegateDetalle;

    public FacadeDetalleTicket() {
        this.delegateDetalle = new DelegateDetalleTicket();
    }

    public void agregarDetalle(DetalleTicket detalle) {
        delegateDetalle.saveDetalleTicket(detalle);
    }

    public void eliminarDetalle(DetalleTicket detalle) {
        delegateDetalle.eliminarDetalleTicket(detalle);
    }

    public void modificarDetalle(DetalleTicket detalle) {
        delegateDetalle.actualizarDetalleTicket(detalle);
    }

    public List<DetalleTicket> obtenerDetalles() {
        return delegateDetalle.getDetallesTicket();
    }
}
