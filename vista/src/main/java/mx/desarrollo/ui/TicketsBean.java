package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import mx.desarrollo.entity.DetalleTicket;
import mx.desarrollo.entity.TicketCompra;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Named("ticketsBean")
@ViewScoped
public class TicketsBean implements Serializable {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private List<TicketCompra> tickets = new ArrayList<>();
    private TicketCompra ticketSeleccionado;
    private List<DetalleTicket> detalles = new ArrayList<>();

    @PostConstruct
    public void init() {
        cargar();
    }

    public void cargar() {
        try {
            List<TicketCompra> lista = ServiceFacadeLocator.getInstanceFacadeTicketCompra().obtenerTickets();
            tickets = lista == null ? new ArrayList<>() : lista;
        } catch (Exception e) {
            tickets = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar los tickets: " + e.getMessage()));
        }
        ticketSeleccionado = null;
        detalles = new ArrayList<>();
    }

    public void cargarDetalles() {
        if (ticketSeleccionado == null) {
            detalles = new ArrayList<>();
            return;
        }
        try {
            List<DetalleTicket> lista = ServiceFacadeLocator.getInstanceFacadeTicketCompra().obtenerDetalles(ticketSeleccionado);
            detalles = lista == null ? new ArrayList<>() : lista;
        } catch (Exception e) {
            detalles = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar el detalle: " + e.getMessage()));
        }
    }

    public String folio(TicketCompra t) {
        return t == null || t.getId() == null ? "" : "NV-" + t.getId();
    }

    public String formatoFecha(LocalDateTime fecha) {
        return fecha == null ? "" : fecha.format(FORMATO);
    }

    public List<TicketCompra> getTickets() { return tickets; }

    public TicketCompra getTicketSeleccionado() { return ticketSeleccionado; }
    public void setTicketSeleccionado(TicketCompra ticketSeleccionado) { this.ticketSeleccionado = ticketSeleccionado; }

    public List<DetalleTicket> getDetalles() { return detalles; }
}