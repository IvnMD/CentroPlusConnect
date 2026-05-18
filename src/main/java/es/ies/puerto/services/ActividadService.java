package es.ies.puerto.services;

import es.ies.puerto.models.Actividad;
import es.ies.puerto.repositories.ActividadRepositoryInterface;

import java.util.List;

public class ActividadService implements ActividadServiceInterface {
    private final ActividadRepositoryInterface repository;

    public ActividadService(ActividadRepositoryInterface repository) {
        this.repository = repository;
    }

    public List<Actividad> findAll() {
        return repository.findAll();
    }

    public Actividad findById(int id) {
        if (id <= 0) return null;
        return repository.findById(id);
    }

    public boolean save(Actividad actividad) {
        validarActividad(actividad);
        if (repository.findById(actividad.getId()) != null) return false;
        return repository.save(actividad);
    }

    public boolean update(Actividad actividad) {
        validarActividad(actividad);
        if (repository.findById(actividad.getId()) == null) return false;
        return repository.update(actividad);
    }

    public boolean delete(int id) {
        if (id <= 0) return false;
        return repository.delete(id);
    }

    public boolean reservarPlaza(int id) {
        Actividad actividad = repository.findById(id);
        if (actividad == null) return false;
        actividad.reservarPlaza();
        return repository.update(actividad);
    }

    public boolean cancelarPlaza(int id) {
        Actividad actividad = repository.findById(id);
        if (actividad == null) return false;
        actividad.cancelarPlaza();
        return repository.update(actividad);
    }

    public double calcularIngresosTotales() {
        return repository.findAll().stream()
                .mapToDouble(Actividad::calcularIngresosPrevistos)
                .sum();
    }

    public List<Actividad> findCompletas() {
        return repository.findAll().stream()
                .filter(Actividad::estaCompleta)
                .toList();
    }

    private void validarActividad(Actividad actividad) {
        if (actividad == null) throw new IllegalArgumentException("La actividad no puede ser null");
        if (actividad.getId() <= 0) throw new IllegalArgumentException("El id debe ser positivo");
        if (actividad.getNombre() == null || actividad.getNombre().isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (actividad.getNombre().length() < 3) throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        if (actividad.getTipoActividad() == null) throw new IllegalArgumentException("El tipo es obligatorio");
        if (actividad.getDuracionMinutos() <= 0) throw new IllegalArgumentException("La duración debe ser positiva");
        if (actividad.getPrecio() < 0) throw new IllegalArgumentException("El precio no puede ser negativo");
        if (actividad.getPlazasMaximas() <= 0) throw new IllegalArgumentException("Las plazas máximas deben ser positivas");
        if (actividad.getPlazasOcupadas() < 0) throw new IllegalArgumentException("Las plazas ocupadas no pueden ser negativas");
        if (actividad.getPlazasOcupadas() > actividad.getPlazasMaximas()) throw new IllegalArgumentException("Las plazas ocupadas no pueden superar las máximas");
    }
    private boolean tipoActividadValido(String tipoActividad) {
        return "ACADEMICA".equals(tipoActividad)
                || "DEPORTIVA".equals(tipoActividad);
    }
}
