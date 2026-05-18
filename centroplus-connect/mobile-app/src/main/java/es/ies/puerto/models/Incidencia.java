package es.ies.puerto.models;

public class Incidencia {
    private int id;
    private int idUsuario;
    private String asunto;
    private String descripcion;
    private String fecha;
    private String estado; // ABIERTA, EN_PROCESO, CERRADA

    public Incidencia() {}

    public Incidencia(int idUsuario, String asunto, String descripcion, String fecha) {
        this.idUsuario = idUsuario;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = "ABIERTA";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Incidencia{id=" + id + ", asunto='" + asunto + "', estado='" + estado + "'}";
    }
}
