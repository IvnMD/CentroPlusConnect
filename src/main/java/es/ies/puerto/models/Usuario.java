package es.ies.puerto.models;

import java.util.Objects;

public class Usuario {
    private int id;
    private String nombre;
    private String dni;
    private String email;
    private String telefono;
    private String tipoUsuario;

    public Usuario (int id){
        this.id = id;
    }
    public Usuario (int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public Usuario(int id, String nombre, String dni, String email, String telefono, String tipoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Integer.valueOf(id));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        return id == other.id;
    }

}
