package es.ies.puerto.services;

import es.ies.puerto.models.Actividad;
import es.ies.puerto.models.Usuario;
import es.ies.puerto.repositories.UsuarioRepositoryInterface;

import java.util.List;

public class UsuarioService implements UsuarioServiceInterface {
    private final UsuarioRepositoryInterface repository;

    String emailPatron = "^[a-z]+@[a-z]+\\.[a-z]+$";

    public UsuarioService(UsuarioRepositoryInterface repository) {
        this.repository = repository;
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();

    }

    @Override
    public Usuario findById(int id) {
        if (id < 0)
            return null;
        return repository.findById(id);
    }

    @Override
    public boolean save(Usuario usuario) {
        validarUsuario(usuario);
        return repository.save(usuario);
    }

    @Override
    public boolean update(Usuario usuario) {
        validarUsuario(usuario);
        return repository.update(usuario);

    }

    @Override
    public boolean delete(int id) {
        if (id <= 0)
            return false;
        return repository.delete(id);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null)
            throw new IllegalArgumentException("La usuario no puede ser null");
        if (usuario.getId() <= 0)
            throw new IllegalArgumentException("El id debe ser positivo");
        if (usuario.getNombre() == null || usuario.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (usuario.getNombre().length() < 3)
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        if (usuario.getEmail() == null || !usuario.getEmail().matches(emailPatron)){
            throw new IllegalArgumentException("El email no es correcto");
        }
        if (usuario.getDni() == null || usuario.getDni().isBlank())
            throw new IllegalArgumentException("dni no puede ser nulo");
        if (usuario == null || usuario.getTelefono().length() != 9)
            throw new IllegalArgumentException("telefono debe tener 9 digitos o y no ser nulo");
        if (usuario.getTipoUsuario() == null || usuario.getTipoUsuario().isBlank())
            throw new IllegalArgumentException("TIPO DE SUSUAIRO NO PUEDE SER NEGATIVO O NULO");
    }

}
