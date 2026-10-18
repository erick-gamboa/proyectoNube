import mx.desarrollo.entity.Vinilo;
import mx.desarrollo.persistence.dao.ViniloDAO;
import mx.desarrollo.persistence.persistence.HibernateUtil;

public class testDAO {

    public static void main(String[] args) {

        ViniloDAO viniloDAO =
                new ViniloDAO(HibernateUtil.getEntityManager());

        for (Vinilo vinilo : viniloDAO.findAll()) {
            System.out.println(
                    "ID: " + vinilo.getIdVinilo()
                            + " | Nombre: " + vinilo.getNombre()
                            + " | Género: " + vinilo.getGenero()
                            + " | Artista: " + vinilo.getArtista()
                            + " | Cantidad: " + vinilo.getCantidad()
                            + " | Precio: " + vinilo.getPrecio()
                            + " | Imagen: " + vinilo.getImagen()
            );
        }
    }
}