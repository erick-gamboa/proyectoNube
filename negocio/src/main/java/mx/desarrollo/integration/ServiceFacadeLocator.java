package mx.desarrollo.integration;

import mx.desarrollo.facade.FacadeUsuario;
import mx.desarrollo.facade.FacadeVinilo;
import mx.desarrollo.facade.FacadeCarrito;
import mx.desarrollo.facade.FacadeDetalleCarrito;
import mx.desarrollo.facade.FacadeTicketCompra;
import mx.desarrollo.facade.FacadeDetalleTicket;

public class ServiceFacadeLocator {

    private static FacadeUsuario facadeUsuario;
    private static FacadeVinilo facadeVinilo;
    private static FacadeCarrito facadeCarrito;
    private static FacadeDetalleCarrito facadeDetalleCarrito;
    private static FacadeTicketCompra facadeTicketCompra;
    private static FacadeDetalleTicket facadeDetalleTicket;

    public static FacadeUsuario getInstanceFacadeUsuario() {
        if (facadeUsuario == null) {
            facadeUsuario = new FacadeUsuario();
        }
        return facadeUsuario;
    }

    public static FacadeVinilo getInstanceFacadeVinilo() {
        if (facadeVinilo == null) {
            facadeVinilo = new FacadeVinilo();
        }
        return facadeVinilo;
    }

    public static FacadeCarrito getInstanceFacadeCarrito() {
        if (facadeCarrito == null) {
            facadeCarrito = new FacadeCarrito();
        }
        return facadeCarrito;
    }

    public static FacadeDetalleCarrito getInstanceFacadeDetalleCarrito() {
        if (facadeDetalleCarrito == null) {
            facadeDetalleCarrito = new FacadeDetalleCarrito();
        }
        return facadeDetalleCarrito;
    }

    public static FacadeTicketCompra getInstanceFacadeTicketCompra() {
        if (facadeTicketCompra == null) {
            facadeTicketCompra = new FacadeTicketCompra();
        }
        return facadeTicketCompra;
    }

    public static FacadeDetalleTicket getInstanceFacadeDetalleTicket() {
        if (facadeDetalleTicket == null) {
            facadeDetalleTicket = new FacadeDetalleTicket();
        }
        return facadeDetalleTicket;
    }
}