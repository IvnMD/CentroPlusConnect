package es.ies.puerto.models;

import java.time.LocalDate;
import java.util.Objects;

public class Inscripcion {
    private int id;
    private int idUsuario;
    private int idActividad;
    private LocalDate fecha;
    private String estado;

    public Inscripcion(int id, int idUsuario, int idActividad, LocalDate fecha, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idActividad = idActividad;
        this.fecha = fecha;
        this.estado = estado;
    }

    public void cancelar() {
        this.estado = Constantes.CANCELADA;
    }

    public boolean estaActiva() {
        return Constantes.ACTIVA.equals(estado);
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Inscripcion)) {
            return false;
        }
        Inscripcion that = (Inscripcion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}