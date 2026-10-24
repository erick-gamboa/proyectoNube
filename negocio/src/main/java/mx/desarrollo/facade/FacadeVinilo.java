
package mx.desarrollo.facade;

import mx.desarrollo.delegate.DelegateVinilo;
import mx.desarrollo.entity.Vinilo;

import java.util.List;

public class FacadeVinilo {

    private final DelegateVinilo delegateVinilo;

    public FacadeVinilo() {
        this.delegateVinilo = new DelegateVinilo();
    }

    public void agregarVinilo(Vinilo vinilo) {
        delegateVinilo.saveVinilo(vinilo);
        System.out.println("facade: agregarVinilo");
    }

    public void eliminarVinilo(Vinilo vinilo) {
        delegateVinilo.eliminarVinilo(vinilo);
    }

    public void modificarVinilo(Vinilo vinilo) {
        delegateVinilo.actualizarVinilo(vinilo);
    }

    public List<Vinilo> obtenerVinilos() {
        return delegateVinilo.getVinilos();
    }

    public void actualizarStock(Integer idVinilo, Integer cantidad) {
        delegateVinilo.actualizarStock(idVinilo, cantidad);
    }
}
