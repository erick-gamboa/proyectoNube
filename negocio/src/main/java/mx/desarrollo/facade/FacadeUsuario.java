package mx.desarrollo.facade;

import java.util.List;
import mx.desarrollo.delegate.DelegateUsuario;
import mx.desarrollo.entity.Usuario;

public class FacadeUsuario {

    private final DelegateUsuario delegateUsuario;

    public FacadeUsuario() {
        this.delegateUsuario = new DelegateUsuario();
    }

    public Usuario login(String correo, String password){

        if(correo == null || correo.isBlank()){
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        return delegateUsuario.login(correo, password);
    }

    public void saveUsuario(Usuario usuario){

        delegateUsuario.saveUsuario(usuario);
    }

    public List<Usuario> obtenerUsuarios(){
        return delegateUsuario.obtenerUsuarios();
    }

    public void actualizarRol(Integer idUsuario, String rol){
        delegateUsuario.actualizarRol(idUsuario, rol);
    }

}