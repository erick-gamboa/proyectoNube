package mx.desarrollo.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.desarrollo.entity.Carrito;
import mx.desarrollo.persistence.persistence.AbstractDAO;
import java.util.List;

public class CarritoDAO extends AbstractDAO<Carrito> {

    private final EntityManager entityManager;

    public CarritoDAO(EntityManager em) {
        super(Carrito.class);
        this.entityManager = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}