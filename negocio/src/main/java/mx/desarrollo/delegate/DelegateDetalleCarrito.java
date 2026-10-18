
package mx.desarrollo.delegate;

import mx.desarrollo.entity.DetalleCarrito;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateDetalleCarrito {

    // Registrar
    public void saveDetalleCarrito(DetalleCarrito detalle) {
        ServiceLocator.getInstanceDetalleCarritoDAO().save(detalle);
    }

    // Consultar todos
    public List<DetalleCarrito> getDetallesCarrito() {
        return ServiceLocator.getInstanceDetalleCarritoDAO().findAll();
    }

    // Actualizar
    public void actualizarDetalleCarrito(DetalleCarrito detalle) {
        ServiceLocator.getInstanceDetalleCarritoDAO().update(detalle);
    }

    // Eliminar
    public void eliminarDetalleCarrito(DetalleCarrito detalle) {
        ServiceLocator.getInstanceDetalleCarritoDAO().delete(detalle);
    }
}
