package pe.edu.upc.caguilar.neurophone.model;

/**
 * Created by cagui on 6/05/2017.
 */

public class Imagen {

    private String nombre;
    private String foto;
    private String urlFoto;

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
