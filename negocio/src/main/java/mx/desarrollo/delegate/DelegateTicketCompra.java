package mx.desarrollo.delegate;

import mx.desarrollo.entity.DetalleCarrito;
import mx.desarrollo.entity.DetalleTicket;
import mx.desarrollo.entity.TicketCompra;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.entity.Vinilo;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DelegateTicketCompra {

    /**
     * Registra una compra completa:
     * valida stock, guarda el ticket y sus detalles, descuenta el stock
     * y vacía los productos del carrito del usuario.
     * Lanza IllegalStateException con un mensaje listo para mostrar al usuario.
     */
    public TicketCompra registrarCompra(Usuario usuario, List<DetalleCarrito> detalles, double total) {
        if (usuario == null || usuario.getIdUsuario() == null) {
            throw new IllegalStateException("Inicia sesión para completar tu compra");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalStateException("Tu carrito está vacío");
        }

        var viniloDAO = ServiceLocator.getInstanceViniloDAO();

        // 1. Validar que haya stock suficiente de todo antes de guardar nada
        List<Vinilo> vinilosActuales = new ArrayList<>();
        for (DetalleCarrito d : detalles) {
            Vinilo v = viniloDAO.findFresh(d.getVinilo().getIdVinilo())
                    .orElseThrow(() -> new IllegalStateException("Un vinilo de tu carrito ya no existe"));
            if (v.getCantidad() == null || v.getCantidad() < d.getCantidad()) {
                throw new IllegalStateException("No hay suficiente stock de \"" + v.getNombre() + "\"");
            }
            vinilosActuales.add(v);
        }

        // 2. Guardar el ticket
        TicketCompra ticket = new TicketCompra();
        ticket.setUsuario(usuario);
        ticket.setFecha(LocalDateTime.now());
        ticket.setTotal(total);
        ServiceLocator.getInstanceTicketCompraDAO().save(ticket);

        // 3. Guardar detalles, descontar stock y quitar del carrito
        for (int i = 0; i < detalles.size(); i++) {
            DetalleCarrito d = detalles.get(i);
            Vinilo v = vinilosActuales.get(i);

            DetalleTicket dt = new DetalleTicket();
            dt.setTicket(ticket);
            dt.setVinilo(v);
            dt.setCantidad(d.getCantidad());
            dt.setPrecioUnitario(v.getPrecio());
            ServiceLocator.getInstanceDetalleTicketDAO().save(dt);

            v.setCantidad(v.getCantidad() - d.getCantidad());
            viniloDAO.update(v);

            ServiceLocator.getInstanceDetalleCarritoDAO().delete(d);
        }

        return ticket;
    }

    // Todos los tickets, los más recientes primero
    public List<TicketCompra> getTickets() {
        List<TicketCompra> lista = new ArrayList<>(ServiceLocator.getInstanceTicketCompraDAO().findAll());
        lista.sort(Comparator.comparing(TicketCompra::getFecha,
                Comparator.nullsLast(Comparator.reverseOrder())));
        return lista;
    }

    // Detalles de un ticket
    public List<DetalleTicket> getDetallesDeTicket(TicketCompra ticket) {
        if (ticket == null) {
            return new ArrayList<>();
        }
        return ServiceLocator.getInstanceDetalleTicketDAO().findByField("ticket", ticket);
    }
}