
package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateTicketCompra;
import mx.desarrollo.entity.TicketCompra;

import java.util.List;

public class FacadeTicketCompra {

    private final DelegateTicketCompra delegateTicket;

    public FacadeTicketCompra() {
        this.delegateTicket = new DelegateTicketCompra();
    }

    public void agregarTicket(TicketCompra ticket) {
        delegateTicket.saveTicketCompra(ticket);
    }

    public void eliminarTicket(TicketCompra ticket) {
        delegateTicket.eliminarTicketCompra(ticket);
    }

    public void modificarTicket(TicketCompra ticket) {
        delegateTicket.actualizarTicketCompra(ticket);
    }

    public List<TicketCompra> obtenerTickets() {
        return delegateTicket.getTicketsCompra();
    }
}
