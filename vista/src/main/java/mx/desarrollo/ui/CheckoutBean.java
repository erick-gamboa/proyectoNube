package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mx.desarrollo.entity.DetalleCarrito;
import mx.desarrollo.entity.TicketCompra;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.integration.ServiceFacadeLocator;

@Named("checkoutBean")
@ViewScoped
public class CheckoutBean implements Serializable {

    private static final double ENVIO = 100.0;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Inject
    private LoginBeanUI loginUI;

    private String estado;
    private String municipio;
    private String ciudad;
    private String calle;
    private String numeroTarjeta;
    private String titular;
    private String vencimiento;
    private String codigoSeguridad;
    private List<DetalleCarrito> detalles = new ArrayList<>();
    private boolean dialogoVisible;
    private boolean dialogoTicketVisible;

    // Datos del ticket (copia de lo comprado, para mostrarlo después del pago)
    private String ticketNumero;
    private String ticketFecha;
    private String ticketDireccion;
    private String ticketTarjeta;
    private String ticketTitular;
    private List<DetalleCarrito> ticketDetalles = new ArrayList<>();
    private double ticketSubtotal;
    private double ticketEnvio;
    private double ticketTotal;

    @PostConstruct
    public void init() {
        detalles = new ArrayList<>();
        Object idUsuario = loginUI == null || loginUI.getUsuario() == null
                ? null : loginUI.getUsuario().getIdUsuario();
        if (idUsuario == null) {
            return;
        }
        List<DetalleCarrito> todos = ServiceFacadeLocator.getInstanceFacadeDetalleCarrito().obtenerDetalles();
        if (todos == null) {
            return;
        }
        for (DetalleCarrito d : todos) {
            if (d != null && d.getCarrito() != null && d.getCarrito().getUsuario() != null
                    && Objects.equals(d.getCarrito().getUsuario().getIdUsuario(), idUsuario)) {
                detalles.add(d);
            }
        }
    }

    public String pagar() {
        if (detalles.isEmpty()) {
            mensaje(FacesMessage.SEVERITY_WARN, "Tu carrito está vacío");
            return null;
        }
        String digitos = numeroTarjeta == null ? "" : numeroTarjeta.replaceAll("\\D", "");
        if (digitos.length() != 16) {
            mensaje(FacesMessage.SEVERITY_ERROR, "El número de tarjeta debe tener 16 dígitos");
            return null;
        }
        if (!vencimientoValido()) {
            mensaje(FacesMessage.SEVERITY_ERROR, "La tarjeta está vencida o la fecha no es válida");
            return null;
        }
        String codigo = codigoSeguridad == null ? "" : codigoSeguridad.trim();
        if (!codigo.matches("\\d{3,4}")) {
            mensaje(FacesMessage.SEVERITY_ERROR, "El código de seguridad debe tener 3 o 4 dígitos");
            return null;
        }

        Usuario comprador = loginUI.getUsuario();
        if (comprador == null || comprador.getIdUsuario() == null) {
            mensaje(FacesMessage.SEVERITY_WARN, "Inicia sesión para completar tu compra");
            return null;
        }

        // Guarda ticket + detalles, descuenta stock y vacía el carrito en la base de datos
        TicketCompra ticket;
        try {
            ticket = ServiceFacadeLocator.getInstanceFacadeTicketCompra()
                    .registrarCompra(comprador, detalles, getTotal());
        } catch (Exception e) {
            mensaje(FacesMessage.SEVERITY_ERROR,
                    e.getMessage() == null ? "No se pudo procesar la compra" : e.getMessage());
            return null;
        }

        generarTicket(digitos, ticket);
        detalles = new ArrayList<>(); // el carrito queda vacío también en pantalla

        numeroTarjeta = null;
        titular = null;
        vencimiento = null;
        codigoSeguridad = null;
        dialogoVisible = true;
        mensaje(FacesMessage.SEVERITY_INFO, "Pago procesado correctamente");
        return null;
    }

    /** Guarda una copia de la compra para mostrarla en el ticket. */
    private void generarTicket(String digitosTarjeta, TicketCompra ticket) {
        ticketNumero = "NV-" + ticket.getId();
        ticketFecha = ticket.getFecha().format(FORMATO_FECHA);
        ticketDireccion = calle + ", " + ciudad + ", " + municipio + ", " + estado;
        ticketTarjeta = digitosTarjeta.substring(digitosTarjeta.length() - 4);
        ticketTitular = titular;
        ticketDetalles = new ArrayList<>(detalles);
        ticketSubtotal = getSubtotal();
        ticketEnvio = getEnvio();
        ticketTotal = getTotal();
    }

    public boolean isDialogoVisible() {
        return dialogoVisible;
    }

    public void setDialogoVisible(boolean dialogoVisible) {
        this.dialogoVisible = dialogoVisible;
    }

    public boolean isDialogoTicketVisible() {
        return dialogoTicketVisible;
    }

    public void setDialogoTicketVisible(boolean dialogoTicketVisible) {
        this.dialogoTicketVisible = dialogoTicketVisible;
    }

    /** Cierra las ventanas y regresa al catálogo. */
    public String cerrarDialogo() {
        this.dialogoVisible = false;
        this.dialogoTicketVisible = false;
        return "/index.xhtml?faces-redirect=true";
    }

    public void verTicket() {
        this.dialogoVisible = false;
        this.dialogoTicketVisible = true;
    }

    private boolean vencimientoValido() {
        if (vencimiento == null || !vencimiento.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        int mes = Integer.parseInt(vencimiento.substring(0, 2));
        int anio = 2000 + Integer.parseInt(vencimiento.substring(3, 5));
        if (mes < 1 || mes > 12) {
            return false;
        }
        return !YearMonth.of(anio, mes).isBefore(YearMonth.now());
    }

    private void mensaje(FacesMessage.Severity severidad, String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, texto, null));
    }

    public double getSubtotal() {
        double suma = 0;
        for (DetalleCarrito d : detalles) {
            suma += d.getVinilo().getPrecio() * d.getCantidad();
        }
        return suma;
    }

    public double getEnvio() {
        return detalles.isEmpty() ? 0 : ENVIO;
    }

    public double getTotal() {
        return getSubtotal() + getEnvio();
    }

    public List<DetalleCarrito> getDetalles() {
        return detalles;
    }

    public Usuario getUsuario() {
        return loginUI.getUsuario();
    }

    public String getTicketNumero() { return ticketNumero; }
    public String getTicketFecha() { return ticketFecha; }
    public String getTicketDireccion() { return ticketDireccion; }
    public String getTicketTarjeta() { return ticketTarjeta; }
    public String getTicketTitular() { return ticketTitular; }
    public List<DetalleCarrito> getTicketDetalles() { return ticketDetalles; }
    public double getTicketSubtotal() { return ticketSubtotal; }
    public double getTicketEnvio() { return ticketEnvio; }
    public double getTicketTotal() { return ticketTotal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }

    public String getVencimiento() { return vencimiento; }
    public void setVencimiento(String vencimiento) { this.vencimiento = vencimiento; }

    public String getCodigoSeguridad() { return codigoSeguridad; }
    public void setCodigoSeguridad(String codigoSeguridad) { this.codigoSeguridad = codigoSeguridad; }
}