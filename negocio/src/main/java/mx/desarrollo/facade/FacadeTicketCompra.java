package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateTicketCompra;
import mx.desarrollo.entity.DetalleCarrito;
import mx.desarrollo.entity.DetalleTicket;
import mx.desarrollo.entity.TicketCompra;
import mx.desarrollo.entity.Usuario;

import java.util.List;

public class FacadeTicketCompra {

    private final DelegateTicketCompra delegateTicketCompra;

    public FacadeTicketCompra() {
        this.delegateTicketCompra = new DelegateTicketCompra();
    }

    public TicketCompra registrarCompra(Usuario usuario, List<DetalleCarrito> detalles, double total) {
        return delegateTicketCompra.registrarCompra(usuario, detalles, total);
    }

    public List<TicketCompra> obtenerTickets() {
        return delegateTicketCompra.getTickets();
    }

    public List<DetalleTicket> obtenerDetalles(TicketCompra ticket) {
        return delegateTicketCompra.getDetallesDeTicket(ticket);
    }
}