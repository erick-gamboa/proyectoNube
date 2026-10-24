package mx.desarrollo.delegate;

import mx.desarrollo.entity.Usuario;
import mx.desarrollo.persistence.integration.ServiceLocator;


import java.util.List;

public class DelegateUsuario {
    public Usuario login(String correo, String password){
        List<Usuario> usuarios = ServiceLocator.getInstanceUsuarioDAO().findAll();

        for(Usuario us:usuarios){
            if(us.getCorreo().equalsIgnoreCase(correo) && us.getPassword().equals(password)){
                return us;
            }
        }
        return null;//Si no se encuentra el usuario
    }

    public void saveUsuario(Usuario usuario){
        ServiceLocator.getInstanceUsuarioDAO().save(usuario);
    }

    public List<Usuario> obtenerUsuarios(){
        return ServiceLocator.getInstanceUsuarioDAO().findAll();
    }

    public void actualizarRol(Integer idUsuario, String rol){
        Usuario u = ServiceLocator.getInstanceUsuarioDAO().find(idUsuario).orElse(null);
        if(u != null){
            u.setRol(rol);
            ServiceLocator.getInstanceUsuarioDAO().update(u);
        }
    }

}