package es.ies.puerto.models;

public class Actividad {
    private int id;
    private String nombre;
    private String tipoActividad; // ACADEMICA, DEPORTIVA
    private int duracionMinutos;
    private double precio;
    private int plazasMaximas;
    private int plazasOcupadas;

    public Actividad() {}

    public Actividad(String nombre, String tipoActividad, int duracionMinutos,
                     double precio, int plazasMaximas) {
        this.nombre = nombre;
        this.tipoActividad = tipoActividad;
        this.duracionMinutos = duracionMinutos;
        this.precio = precio;
        this.plazasMaximas = plazasMaximas;
        this.plazasOcupadas = 0;
    }

    public int getPlazasDisponibles() {
        return plazasMaximas - plazasOcupadas;
    }

    public boolean isCompleta() {
        return plazasOcupadas >= plazasMaximas;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoActividad() { return tipoActividad; }
    public void setTipoActividad(String tipoActividad) { this.tipoActividad = tipoActividad; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getPlazasMaximas() { return plazasMaximas; }
    public void setPlazasMaximas(int plazasMaximas) { this.plazasMaximas = plazasMaximas; }
    public int getPlazasOcupadas() { return plazasOcupadas; }
    public void setPlazasOcupadas(int plazasOcupadas) { this.plazasOcupadas = plazasOcupadas; }

    @Override
    public String toString() {
        return "Actividad{id=" + id + ", nombre='" + nombre + "', plazasDisponibles=" + getPlazasDisponibles() + "}";
    }
}
