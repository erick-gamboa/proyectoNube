package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
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