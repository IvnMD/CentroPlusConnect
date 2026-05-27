package es.ies.puerto.services;

import es.ies.puerto.models.Actividad;
import es.ies.puerto.models.Constantes;
import es.ies.puerto.models.Inscripcion;
import es.ies.puerto.repositories.ActividadRepositoryInterface;
import es.ies.puerto.repositories.InscripcionRepositoryInterface;
import es.ies.puerto.repositories.UsuarioRepositoryInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InscripcionService implements InscripcionServiceInterface {

    private final InscripcionRepositoryInterface inscripcionRepository;
    private final UsuarioRepositoryInterface usuarioRepository;
    private final ActividadRepositoryInterface actividadRepository;

    public InscripcionService(InscripcionRepositoryInterface inscripcionRepository,
                               UsuarioRepositoryInterface usuarioRepository,
                               ActividadRepositoryInterface actividadRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.actividadRepository = actividadRepository;
    }

    @Override
    public List<Inscripcion> findAll() {
        return inscripcionRepository.findAll();
    }

    @Override
    public Inscripcion findById(int id) {
        if (id <= 0) return null;
        return inscripcionRepository.findById(id);
    }

    @Override
    public List<Inscripcion> findByUsuario(int idUsuario) {
        if (idUsuario <= 0) return new ArrayList<>();
        return inscripcionRepository.findByUsuario(idUsuario);
    }

    @Override
    public List<Inscripcion> findByActividad(int idActividad) {
        if (idActividad <= 0) return new ArrayList<>();
        return inscripcionRepository.findByActividad(idActividad);
    }

    @Override
    public boolean save(Inscripcion inscripcion) {
        validarInscripcion(inscripcion);

        // fecha futura → excepción
        if (inscripcion.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura");
        }

        // ID duplicado
        if (inscripcionRepository.findById(inscripcion.getId()) != null) {
            return false;
        }

        // usuario no existe
        if (usuarioRepository.findById(inscripcion.getIdUsuario()) == null) {
            return false;
        }

        // actividad no existe
        Actividad actividad = actividadRepository.findById(inscripcion.getIdActividad());
        if (actividad == null) {
            return false;
        }

        // actividad completa → excepción
        if (actividad.estaCompleta()) {
            throw new IllegalStateException("La actividad no tiene plazas disponibles");
        }

        // inscripción activa duplicada para mismo usuario+actividad
        boolean yaInscrito = inscripcionRepository.findByUsuario(inscripcion.getIdUsuario())
                .stream()
                .anyMatch(i -> i.getIdActividad() == inscripcion.getIdActividad()
                        && Constantes.ACTIVA.equals(i.getEstado()));
        if (yaInscrito) {
            return false;
        }

        return inscripcionRepository.save(inscripcion);
    }

    @Override
    public boolean update(Inscripcion inscripcion) {
        if (inscripcion == null) return false;
        if (inscripcionRepository.findById(inscripcion.getId()) == null) return false;
        return inscripcionRepository.update(inscripcion);
    }

    @Override
    public boolean delete(int id) {
        if (id <= 0) return false;
        return inscripcionRepository.delete(id);
    }

    @Override
    public boolean cancelar(int id) {
        if (id <= 0) return false;

        Inscripcion inscripcion = inscripcionRepository.findById(id);
        if (inscripcion == null) return false;

        // ya cancelada → no se puede volver a cancelar
        if (!inscripcion.estaActiva()) return false;

        // liberar plaza en la actividad
        Actividad actividad = actividadRepository.findById(inscripcion.getIdActividad());
        if (actividad != null) {
            actividad.cancelarPlaza();
            actividadRepository.update(actividad);
        }

        inscripcion.cancelar();
        return inscripcionRepository.update(inscripcion);
    }

    private void validarInscripcion(Inscripcion inscripcion) {
        if (inscripcion == null) {
            throw new IllegalArgumentException("La inscripción no puede ser null");
        }
        if (inscripcion.getId() <= 0) {
            throw new IllegalArgumentException("El id debe ser positivo");
        }
        if (inscripcion.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("El idUsuario debe ser positivo");
        }
        if (inscripcion.getIdActividad() <= 0) {
            throw new IllegalArgumentException("El idActividad debe ser positivo");
        }
    }
}