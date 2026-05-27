package es.ies.puerto.repositories.sqlite;

import es.ies.puerto.models.Usuario;
import es.ies.puerto.repositories.UsuarioRepositoryInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSqliteRepository implements UsuarioRepositoryInterface {

    private final SqliteConnectionManager manager;

    public UsuarioSqliteRepository() {
        this(new SqliteConnectionManager());
    }

    public UsuarioSqliteRepository(SqliteConnectionManager manager) {
        this.manager = manager;
        new DatabaseInitializer(manager).createTables();
    }

    @Override
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapUsuario(rs));
                }
            }
            return usuarios;
        } catch (Exception e) {
            System.err.println("Error en listar todos los usuarios");
            return new ArrayList<>();
        }
    }

    @Override
    public Usuario findById(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUsuario(rs);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en buscar usuario por id = " + id);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id, nombre, dni, email, telefono, tipo_usuario) VALUES (?,?,?,?,?,?)";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, usuario.getId());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getDni());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getTipoUsuario());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error al guardar usuario");
            return false;
        }
    }

    @Override
    public boolean update(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, dni = ?, email = ?, telefono = ?, tipo_usuario = ? WHERE id = ?";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getDni());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getTelefono());
            ps.setString(5, usuario.getTipoUsuario());
            ps.setInt(6, usuario.getId());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario");
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error en borrar usuario por id = " + id);
            throw new RuntimeException(e);
        }
    }

    public Usuario mapUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("dni"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getString("tipo_usuario"));
    }
}