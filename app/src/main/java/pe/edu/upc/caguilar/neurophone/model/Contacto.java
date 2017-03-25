package pe.edu.upc.caguilar.neurophone.model;

import java.util.List;

/**
 * Created by cagui on 19/11/2016.
 */

public class Contacto {

    private String id;
    private String nombre;
    private String numero;
    private String email;
    private String foto;
    private List<Mensaje> lstMensaje;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Mensaje> getLstMensaje() {
        return lstMensaje;
    }

    public void setLstMensaje(List<Mensaje> lstMensaje) {
        this.lstMensaje = lstMensaje;
    }
}
