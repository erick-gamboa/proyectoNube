package mx.desarrollo.persistence.integration;

import jakarta.persistence.EntityManager;
import mx.desarrollo.persistence.dao.UsuarioDAO;
import mx.desarrollo.persistence.dao.ViniloDAO;
import mx.desarrollo.persistence.dao.CarritoDAO;
import mx.desarrollo.persistence.dao.DetalleCarritoDAO;
import mx.desarrollo.persistence.dao.TicketCompraDAO;
import mx.desarrollo.persistence.dao.DetalleTicketDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

public class ServiceLocator {

    private static UsuarioDAO usuarioDAO;
    private static ViniloDAO viniloDAO;
    private static CarritoDAO carritoDAO;
    private static DetalleCarritoDAO detalleCarritoDAO;
    private static TicketCompraDAO ticketCompraDAO;
    private static DetalleTicketDAO detalleTicketDAO;

    private static EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    public static UsuarioDAO getInstanceUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioDAO(getEntityManager());
        }
        return usuarioDAO;
    }

    public static ViniloDAO getInstanceViniloDAO() {
        if (viniloDAO == null) {
            viniloDAO = new ViniloDAO(getEntityManager());
        }
        return viniloDAO;
    }

    public static CarritoDAO getInstanceCarritoDAO() {
        if (carritoDAO == null) {
            carritoDAO = new CarritoDAO(getEntityManager());
        }
        return carritoDAO;
    }

    public static DetalleCarritoDAO getInstanceDetalleCarritoDAO() {
        if (detalleCarritoDAO == null) {
            detalleCarritoDAO = new DetalleCarritoDAO(getEntityManager());
        }
        return detalleCarritoDAO;
    }

    public static TicketCompraDAO getInstanceTicketCompraDAO() {
        if (ticketCompraDAO == null) {
            ticketCompraDAO = new TicketCompraDAO(getEntityManager());
        }
        return ticketCompraDAO;
    }

    public static DetalleTicketDAO getInstanceDetalleTicketDAO() {
        if (detalleTicketDAO == null) {
            detalleTicketDAO = new DetalleTicketDAO(getEntityManager());
        }
        return detalleTicketDAO;
    }
}