
package mx.desarrollo.delegate;

import mx.desarrollo.entity.TicketCompra;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateTicketCompra {

    // Registrar
    public void saveTicketCompra(TicketCompra ticket) {
        ServiceLocator.getInstanceTicketCompraDAO().save(ticket);
    }

    // Consultar todos
    public List<TicketCompra> getTicketsCompra() {
        return ServiceLocator.getInstanceTicketCompraDAO().findAll();
    }

    // Actualizar
    public void actualizarTicketCompra(TicketCompra ticket) {
        ServiceLocator.getInstanceTicketCompraDAO().update(ticket);
    }

    // Eliminar
    public void eliminarTicketCompra(TicketCompra ticket) {
        ServiceLocator.getInstanceTicketCompraDAO().delete(ticket);
    }
}
