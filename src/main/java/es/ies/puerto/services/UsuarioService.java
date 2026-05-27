package es.ies.puerto.services;

import es.ies.puerto.models.Usuario;
import es.ies.puerto.repositories.UsuarioRepositoryInterface;

import java.util.List;

public class UsuarioService implements UsuarioServiceInterface {

    private final UsuarioRepositoryInterface repository;
    private static final String EMAIL_PATRON = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public UsuarioService(UsuarioRepositoryInterface repository) {
        this.repository = repository;
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Override
    public Usuario findById(int id) {
        if (id <= 0) return null;
        return repository.findById(id);
    }

    @Override
    public boolean save(Usuario usuario) {
        validarOLanzar(usuario);
        if (repository.findById(usuario.getId()) != null) return false;
        return repository.save(usuario);
    }

    @Override
    public boolean update(Usuario usuario) {
        validarOLanzar(usuario);
        if (repository.findById(usuario.getId()) == null) return false;
        return repository.update(usuario);
    }

    @Override
    public boolean delete(int id) {
        if (id <= 0) return false;
        if (repository.findById(id) == null) return false;
        return repository.delete(id);
    }

    private void validarOLanzar(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException("Usuario no puede ser null");
        if (usuario.getId() <= 0) throw new IllegalArgumentException("Id invalido");
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) throw new IllegalArgumentException("Nombre vacio");
        if (usuario.getDni() == null || usuario.getDni().isBlank()) throw new IllegalArgumentException("DNI vacio");
        if (usuario.getEmail() == null || !usuario.getEmail().matches(EMAIL_PATRON)) throw new IllegalArgumentException("Email invalido");
        if (usuario.getTelefono() == null || usuario.getTelefono().length() != 9) throw new IllegalArgumentException("Telefono invalido");
        if (usuario.getTipoUsuario() == null || usuario.getTipoUsuario().isBlank()) throw new IllegalArgumentException("Tipo usuario vacio");
    }
}
