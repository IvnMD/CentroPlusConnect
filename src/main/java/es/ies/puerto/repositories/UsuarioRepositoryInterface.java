package es.ies.puerto.repositories;

import es.ies.puerto.models.Usuario;
import java.util.List;

public interface UsuarioRepositoryInterface {
    List<Usuario> findAll();
    Usuario findById(int id);
    boolean save(Usuario usuario);
    boolean update(Usuario usuario);
    boolean delete(int id);
}
