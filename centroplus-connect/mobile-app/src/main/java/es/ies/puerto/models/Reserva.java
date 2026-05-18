package es.ies.puerto.models;

public class Reserva {
    private int id;
    private int idUsuario;
    private int idActividad;
    private String fecha;
    private String estado; // ACTIVA, CANCELADA

    public Reserva() {}

    public Reserva(int idUsuario, int idActividad, String fecha) {
        this.idUsuario = idUsuario;
        this.idActividad = idActividad;
        this.fecha = fecha;
        this.estado = "ACTIVA";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdActividad() { return idActividad; }
    public void setIdActividad(int idActividad) { this.idActividad = idActividad; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Reserva{id=" + id + ", idUsuario=" + idUsuario + ", idActividad=" + idActividad + ", estado='" + estado + "'}";
    }
}
