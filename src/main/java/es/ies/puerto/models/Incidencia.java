package es.ies.puerto.models;

import java.util.Objects;

public class Incidencia {

    private Long id;
    private Long idUsuario;
    private String asunto;
    private String descripcion;
    private String fecha;
    // private EstadoIncidencia estado;

    public Incidencia() {
    }

    public Incidencia(Long id) {
        this.id = id;
    }

    public Incidencia(Long id, Long idUsuario, String asunto, String descripcion, String fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        // this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    // public EstadoIncidencia getEstado() {
    //     return estado;
    // }

    // public void setEstado(EstadoIncidencia estado) {
    //     this.estado = estado;
    // }

    @Override
    public String toString() {
        return "Incidencia [id=" + id + ", idUsuario=" + idUsuario + ", asunto=" + asunto + ", descripcion="
                + descripcion + ", fecha=" + fecha + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof Incidencia)) {
            return false;
        }
        Incidencia incidencia = (Incidencia) obj;
        return Objects.equals(id, incidencia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
