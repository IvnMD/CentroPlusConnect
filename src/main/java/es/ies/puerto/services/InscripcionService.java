package es.ies.puerto.services;

import es.ies.puerto.models.Constantes;

import es.ies.puerto.models.Actividad;
import es.ies.puerto.models.Inscripcion;
import es.ies.puerto.models.Usuario;
import es.ies.puerto.repositories.ActividadRepositoryInterface;
import es.ies.puerto.repositories.InscripcionRepositoryInterface;
import es.ies.puerto.repositories.UsuarioRepositoryInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InsufficientResourcesException;

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
    public boolean update(Inscripcion inscripcion) {
        return inscripcionRepository.update(inscripcion);

    }

    @Override
    public boolean delete(int id) {
        if (id < 1) {
            return false;
        }
        return inscripcionRepository.delete(id);
    }

    @Override
    public List<Inscripcion> findAll() {

        return inscripcionRepository.findAll();
    }

    @Override
    public Inscripcion findById(int id) {
        if (id < 1) {
            return null;
        }
        return inscripcionRepository.findById(id);
    }

    @Override
    public List<Inscripcion> findByUsuario(int idUsuario) {
        if (idUsuario < 1) {
            return new ArrayList<>();
        }
        return inscripcionRepository.findByUsuario(idUsuario);
    }

    @Override
    public List<Inscripcion> findByActividad(int idActividad) {
        if (idActividad < 1) {
            return new ArrayList<>();
        }
        return inscripcionRepository.findByUsuario(idActividad);
    }

    @Override
    public boolean save(Inscripcion inscripcion) {
        validarInscripcion(inscripcion);
        validarFecha(inscripcion);
        return inscripcionRepository.save(inscripcion);
    }

    @Override
    public boolean cancelar(int id) {
        
        return actividadRepository.delete(id);
    }


    private void validarInscripcion(Inscripcion inscripcion) {
        if (inscripcion == null)
            throw new IllegalArgumentException("La inscripcion no puede ser null");
        if (inscripcion.getId() <= 0)
            throw new IllegalArgumentException("El id debe ser positivo");
        if (inscripcion.getIdActividad() <= 0)
            throw new IllegalArgumentException("El id debe ser positivo");
        if (inscripcion.getIdUsuario() <= 0)
            throw new IllegalArgumentException("El id debe ser positivo");

    }

    private boolean validarFecha(Inscripcion inscripcion) {
        if (LocalDate.now().isAfter(inscripcion.getFecha())){
            return false;
        }

        return true;
    }
}
