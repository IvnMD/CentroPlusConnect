package es.ies.puerto.services;

import es.ies.puerto.models.Usuario;
import java.util.List;

public interface UsuarioServiceInterface {
    /**
     * Funcion que busca todos los usuarios
     * @return lista de usuarios
     */
    List<Usuario> findAll();
    
    /**
     * Funcion que busca un usuario por Id
     * @param id del usuario
     * @return true/false
     */
    Usuario findById(int id);

    /**
     * Funcion que crea/guarda un usuario
     * @param usuario a guardar/ crear
   * @return true/false
     */
    boolean save(Usuario usuario);

    /**
     * Funcion que actualiza un usuario
     * @param usuario que se actualiza
   * @return true/false
     */
    boolean update(Usuario usuario);

    /**
     * Funcion que elimina un usuario por Id
     * @param id del usuario a  eliminar
   * @return true/false
     */
    boolean delete(int id);
}
