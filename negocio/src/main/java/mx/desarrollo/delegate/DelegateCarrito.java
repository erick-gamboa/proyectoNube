
package mx.desarrollo.delegate;

import mx.desarrollo.entity.Carrito;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateCarrito {

    // Registrar
    public void saveCarrito(Carrito carrito) {
        ServiceLocator.getInstanceCarritoDAO().save(carrito);
    }

    // Consultar todos
    public List<Carrito> getCarritos() {
        return ServiceLocator.getInstanceCarritoDAO().findAll();
    }

    // Actualizar
    public void actualizarCarrito(Carrito carrito) {
        ServiceLocator.getInstanceCarritoDAO().update(carrito);
    }

    // Eliminar
    public void eliminarCarrito(Carrito carrito) {
        ServiceLocator.getInstanceCarritoDAO().delete(carrito);
    }
}
