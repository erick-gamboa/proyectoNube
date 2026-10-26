
package mx.desarrollo.ui;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.facade.FacadeUsuario;
import mx.desarrollo.integration.ServiceFacadeLocator;

@Named("registroBean")
@RequestScoped
public class RegistroBean {

    private Usuario usuario = new Usuario();
    private FacadeUsuario facade;

    public RegistroBean() {
        facade = ServiceFacadeLocator.getInstanceFacadeUsuario();
    }

    public String registrar() {

        if (usuario.getNombreUsuario() == null ||
                usuario.getNombreUsuario().isBlank() ||
                usuario.getCorreo() == null ||
                usuario.getCorreo().isBlank() ||
                usuario.getPassword() == null ||
                usuario.getPassword().isBlank()) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Aviso", "Completa todos los campos"));
            return null;
        }

        if (usuario.getNombreUsuario().length() > 50 ||
                usuario.getCorreo().length() > 50 ||
                usuario.getPassword().length() > 50) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Aviso", "Los campos no deben superar 50 caracteres"));
            return null;
        }

        for (Usuario us : facade.obtenerUsuarios()) {
            if (us.getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Aviso", "Ese correo ya esta registrado"));
                return null;
            }
        }

        usuario.setRol("CLIENTE");
        facade.saveUsuario(usuario);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Exito", "Cuenta creada correctamente"));

        return "/login.xhtml?faces-redirect=true";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

