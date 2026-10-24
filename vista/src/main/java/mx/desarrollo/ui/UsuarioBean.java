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
import java.util.List;

@Named("usuarioBean")
@SessionScoped
public class UsuarioBean implements Serializable {

    private FacadeUsuario facade;

    private List<Usuario> listaUsuarios;
    private Usuario usuarioSeleccionado;
    private String nuevoRol;

    public UsuarioBean() {
        this.facade = ServiceFacadeLocator.getInstanceFacadeUsuario();
        this.listaUsuarios = new ArrayList<Usuario>();
    }

    @PostConstruct
    public void init() {
        cargarLista();
    }

    private FacadeUsuario getFacade() {
        if (this.facade == null) {
            this.facade = ServiceFacadeLocator.getInstanceFacadeUsuario();
        }
        return this.facade;
    }

    private void cargarLista() {
        FacadeUsuario f = getFacade();
        if (f != null) {
            this.listaUsuarios = f.obtenerUsuarios();
        }
        if (this.listaUsuarios == null) {
            this.listaUsuarios = new ArrayList<Usuario>();
        }
    }

    public List<Usuario> getListaUsuarios() {
        if (listaUsuarios == null || listaUsuarios.isEmpty()) {
            cargarLista();
        }
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
        if (usuarioSeleccionado != null) {
            this.nuevoRol = usuarioSeleccionado.getRol();
        }
    }

    public String getNuevoRol() {
        return nuevoRol;
    }

    public void setNuevoRol(String nuevoRol) {
        this.nuevoRol = nuevoRol;
    }

    public void cambiarRol() {
        try {
            if (usuarioSeleccionado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Selecciona un usuario de la tabla"));
                return;
            }

            FacadeUsuario f = getFacade();
            if (f == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo inicializar la capa de negocio"));
                return;
            }

            f.actualizarRol(usuarioSeleccionado.getIdUsuario(), nuevoRol);
            cargarLista();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Rol actualizado correctamente"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cambiar el rol: " + e.getMessage()));
        }
    }
}
