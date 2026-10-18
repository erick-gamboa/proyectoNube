
package mx.desarrollo.delegate;

import mx.desarrollo.entity.DetalleTicket;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateDetalleTicket {

    // Registrar
    public void saveDetalleTicket(DetalleTicket detalle) {
        ServiceLocator.getInstanceDetalleTicketDAO().save(detalle);
    }

    // Consultar todos
    public List<DetalleTicket> getDetallesTicket() {
        return ServiceLocator.getInstanceDetalleTicketDAO().findAll();
    }

    // Actualizar
    public void actualizarDetalleTicket(DetalleTicket detalle) {
        ServiceLocator.getInstanceDetalleTicketDAO().update(detalle);
    }

    // Eliminar
    public void eliminarDetalleTicket(DetalleTicket detalle) {
        ServiceLocator.getInstanceDetalleTicketDAO().delete(detalle);
    }
}
