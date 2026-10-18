package mx.desarrollo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "vinilo")
public class Vinilo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVinilo", nullable = false)
    private Integer idVinilo;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "genero", nullable = false)
    private String genero;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;


    @NotNull
    @Column(name = "artista", nullable = false)
    private String artista;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Double precio;


        @NotNull
    @Column(name = "imagen", nullable = false)
    private String imagen;



    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getNombre() {return this.nombre;}

    public Integer getIdVinilo() {return this.idVinilo;
    }

    public String getGenero() {return this.genero;}

    public void setGenero(String genero) {
        this.genero = genero;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}