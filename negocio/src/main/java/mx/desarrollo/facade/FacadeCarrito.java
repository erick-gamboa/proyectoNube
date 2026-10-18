
package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateCarrito;
import mx.desarrollo.entity.Carrito;

import java.util.List;

public class FacadeCarrito {

    private final DelegateCarrito delegateCarrito;

    public FacadeCarrito() {
        this.delegateCarrito = new DelegateCarrito();
    }

    public void agregarCarrito(Carrito carrito) {
        delegateCarrito.saveCarrito(carrito);
    }

    public void eliminarCarrito(Carrito carrito) {
        delegateCarrito.eliminarCarrito(carrito);
    }

    public void modificarCarrito(Carrito carrito) {
        delegateCarrito.actualizarCarrito(carrito);
    }

    public List<Carrito> obtenerCarritos() {
        return delegateCarrito.getCarritos();
    }
}
