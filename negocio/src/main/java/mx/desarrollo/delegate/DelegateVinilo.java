
package mx.desarrollo.delegate;

import mx.desarrollo.entity.Vinilo;
import mx.desarrollo.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateVinilo {

    // CRUD DE VINILOS

    // Registrar
    public void saveVinilo(Vinilo vinilo) {
        if (validarVinilo(vinilo)) {
            ServiceLocator.getInstanceViniloDAO().save(vinilo);
            System.out.println("delegate: save");
        }
    }

    // Validar atributos
    public boolean validarVinilo(Vinilo vinilo) {
        if (vinilo == null) {
            return false;
        }

        if (vinilo.getNombre() == null || vinilo.getNombre().trim().isEmpty() || vinilo.getNombre().length() > 50) {
            return false;
        }

        if (vinilo.getGenero() == null || vinilo.getGenero().trim().isEmpty() || vinilo.getGenero().length() > 50) {
            return false;
        }

        if (vinilo.getArtista() == null || vinilo.getArtista().trim().isEmpty() || vinilo.getArtista().length() > 50) {
            return false;
        }

        if(vinilo.getDescripcion() == null || vinilo.getDescripcion().trim().isEmpty()){
            return false;
        }

        if (vinilo.getCantidad() == null || vinilo.getCantidad() < 0) {
            return false;
        }

        if (vinilo.getPrecio() == null || vinilo.getPrecio() < 0) {
            return false;
        }

        if (vinilo.getImagen() == null || vinilo.getImagen().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    // Consultar todos los vinilos
    public List<Vinilo> getVinilos() {
        return ServiceLocator.getInstanceViniloDAO().findAll();
    }

    // Actualizar
    public void actualizarVinilo(Vinilo vinilo) {
        if (validarVinilo(vinilo)) {
            ServiceLocator.getInstanceViniloDAO().update(vinilo);
        }
    }

    // Eliminar
    public void eliminarVinilo(Vinilo vinilo) {
        ServiceLocator.getInstanceViniloDAO().delete(vinilo);
    }

    // Actualizar solo stock
    public void actualizarStock(Integer idVinilo, Integer cantidad) {
        ServiceLocator.getInstanceViniloDAO().find(idVinilo).ifPresent(vinilo -> {
            vinilo.setCantidad(cantidad);
            ServiceLocator.getInstanceViniloDAO().update(vinilo);
        });
    }


}
