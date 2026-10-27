package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mx.desarrollo.entity.DetalleCarrito;
import mx.desarrollo.integration.ServiceFacadeLocator;

@Named("checkoutBean")
@ViewScoped
public class CheckoutBean implements Serializable {

    private static final double ENVIO = 100.0;

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

    @PostConstruct
    public void init() {
        detalles = new ArrayList<>();
        Object idUsuario = loginUI.getUsuario().getIdUsuario();
        if (idUsuario == null) {
            return;
        }
        List<DetalleCarrito> todos = ServiceFacadeLocator.getInstanceFacadeDetalleCarrito().obtenerDetalles();
        if (todos == null) {
            return;
        }
        for (DetalleCarrito d : todos) {
            if (Objects.equals(d.getCarrito().getUsuario().getIdUsuario(), idUsuario)) {
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
        numeroTarjeta = null;
        titular = null;
        vencimiento = null;
        codigoSeguridad = null;
        mensaje(FacesMessage.SEVERITY_INFO, "Pago procesado correctamente");
        return null;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getCodigoSeguridad() {
        return codigoSeguridad;
    }

    public void setCodigoSeguridad(String codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }
}