package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.DetalleTicket;
import mx.desarrollo.persistence.persistence.AbstractDAO;

public class DetalleTicketDAO extends AbstractDAO<DetalleTicket> {

    private final EntityManager entityManager;

    public DetalleTicketDAO(EntityManager em) {
        super(DetalleTicket.class);
        this.entityManager = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}