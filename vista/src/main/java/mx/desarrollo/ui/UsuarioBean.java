package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.facade.FacadeUsuario;
import mx.desarrollo.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named("usuarioBean")
@SessionScoped
public class UsuarioBean implements Serializable {

    private transient FacadeUsuario facade;
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private Map<Integer, String> rolesEditados = new HashMap<>();

    public UsuarioBean() {
    }

    @PostConstruct
    public void init() {
        cargarLista();
    }

    private FacadeUsuario getFacade() {
        if (facade == null) {
            facade = ServiceFacadeLocator.getInstanceFacadeUsuario();
        }
        return facade;
    }

    private void cargarLista() {
        FacadeUsuario f = getFacade();
        if (f == null) {
            listaUsuarios = new ArrayList<>();
        } else {
            listaUsuarios = f.obtenerUsuarios();
        }
        if (listaUsuarios == null) {
            listaUsuarios = new ArrayList<>();
        }
        rolesEditados.clear();
        for (Usuario usuario : listaUsuarios) {
            rolesEditados.put(usuario.getIdUsuario(), usuario.getRol());
        }
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public Map<Integer, String> getRolesEditados() {
        return rolesEditados;
    }

    public void guardarRoles() {
        FacesContext context = FacesContext.getCurrentInstance();
        FacadeUsuario f = getFacade();
        if (f == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudo acceder a la capa de negocio."));
            return;
        }

        int cambios = 0;
        try {
            for (Usuario usuario : listaUsuarios) {
                String rol = rolesEditados.get(usuario.getIdUsuario());
                if (!"user".equals(rol) && !"admin".equals(rol)) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Hay un rol no válido para el usuario " + usuario.getIdUsuario()));
                    return;
                }
            }
            for (Usuario usuario : listaUsuarios) {
                String rol = rolesEditados.get(usuario.getIdUsuario());
                if (!rol.equals(usuario.getRol())) {
                    f.actualizarRol(usuario.getIdUsuario(), rol);
                    usuario.setRol(rol);
                    cambios++;
                }
            }
            String mensaje = "Se actualizaron " + cambios + " usuario(s).";
            if (cambios == 0) {
                mensaje = "No había cambios por guardar.";
            }
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", mensaje));
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "No se pudieron guardar todos los cambios. Revisa los roles e inténtalo de nuevo."));
        }
    }
}
