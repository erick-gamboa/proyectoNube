/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.desarrollo.ui;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarrollo.entity.Usuario;
import mx.desarrollo.helper.LoginHelper;


import java.io.IOException;
import java.io.Serializable;

@Named("loginUI")
@SessionScoped
public class LoginBeanUI implements Serializable{
    private LoginHelper loginHelper;
    private Usuario usuario;
    
    public LoginBeanUI() {
        loginHelper = new LoginHelper();
    }
    
    /**
     * Metodo postconstructor todo lo que este dentro de este metodo
     * sera la primero que haga cuando cargue la pagina
     */
    @PostConstruct
    public void init(){
        usuario= new Usuario();
    }

     public void login() throws IOException{
         String appURL;

         if(usuario.getCorreo()==null || usuario.getCorreo().isEmpty()) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Correo vacio","Ingresa tu correo"));

             return;
         }
         if(usuario.getPassword()==null || usuario.getPassword().isEmpty()) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Contraseña vacia","Ingresa tu contraseña"));

             return;
         }

         // los atributos de usuario vienen del xhtml
         Usuario us = loginHelper.Login(usuario.getCorreo(), usuario.getPassword());
         if(us != null && us.getIdUsuario()!=null){
             // asigno el usuario encontrado al usuario de esta clase para que
             // se muestre correctamente en la pagina de informacion
             usuario=us;


             if ("admin".equalsIgnoreCase(usuario.getRol())) {
                 appURL = "/vinilos.xhtml";
             } else {
                 appURL = "/index.xhtml";
             }

         FacesContext.getCurrentInstance().getExternalContext().redirect(
                 FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
                         + appURL
         );



            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + appURL);
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario o contraseña incorrecta:", "Intente de nuevo"));
        }
    }

    
    /* getters y setters*/

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
    
    
    
    
    

    

    
}
