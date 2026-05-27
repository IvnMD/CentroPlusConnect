package es.ies.puerto.services;

import es.ies.puerto.models.Usuario;
import es.ies.puerto.repositories.UsuarioRepositoryInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService implements UsuarioServiceInterface {
    private final UsuarioRepositoryInterface repository;
    private static final String EMAIL_PATRON = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public InscripcionService(InscripcionRepositoryInterface inscripcionRepository,
                               UsuarioRepositoryInterface usuarioRepository,
                               ActividadRepositoryInterface actividadRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.actividadRepository = actividadRepository;
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Override
    public Usuario findById(int id) {
        if (id <= 0) {
            return null;
        }
        return repository.findById(id);
    }

    @Override
    public boolean save(Usuario usuario) {
        validarUsuario(usuario);
        if (repository.findById(usuario.getId()) != null) {
            return false;
        }
        return repository.save(usuario);
    }

    @Override
    public boolean update(Usuario usuario) {
        validarUsuario(usuario);
        if (repository.findById(usuario.getId()) == null) {
            return false;
        }
        return repository.update(usuario);
    }

    @Override
    public boolean delete(int id) {
        if (id <= 0) {
            return false;
        }
        return repository.delete(id);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        if (usuario.getId() <= 0) {
            throw new IllegalArgumentException("El id debe ser positivo");
        }
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (usuario.getNombre().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        }
        if (usuario.getDni() == null || usuario.getDni().isBlank()) {
            throw new IllegalArgumentException("El dni no puede ser nulo o vacío");
        }
        if (usuario.getEmail() == null || !usuario.getEmail().matches(EMAIL_PATRON)) {
            throw new IllegalArgumentException("El email no es correcto");
        }
        if (usuario.getTelefono() == null || usuario.getTelefono().length() != 9) {
            throw new IllegalArgumentException("El teléfono debe tener 9 dígitos");
        }
        if (usuario.getTipoUsuario() == null || usuario.getTipoUsuario().isBlank()) {
            throw new IllegalArgumentException("El tipo de usuario no puede ser nulo o vacío");
        }
    }
}