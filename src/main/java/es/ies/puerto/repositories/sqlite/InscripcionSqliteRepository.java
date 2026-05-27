package es.ies.puerto.repositories.sqlite;

import es.ies.puerto.models.Inscripcion;
import es.ies.puerto.repositories.InscripcionRepositoryInterface;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InscripcionSqliteRepository implements InscripcionRepositoryInterface {

    private final SqliteConnectionManager manager;

    public InscripcionSqliteRepository() {
        this(new SqliteConnectionManager());
    }

    public InscripcionSqliteRepository(SqliteConnectionManager manager) {
        this.manager = manager;
        new DatabaseInitializer(manager).createTables();
    }

    @Override
    public List<Inscripcion> findAll() {
        String sql = "SELECT * FROM inscripciones";
        List<Inscripcion> inscripciones = new ArrayList<>();
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inscripciones.add(mapInscripcion(rs));
                }
            }
            return inscripciones;
        } catch (Exception e) {
            System.err.println("Error en listar todas las inscripciones");
            return new ArrayList<>();
        }
    }

    @Override
    public Inscripcion findById(int id) {
        String sql = "SELECT * FROM inscripciones WHERE id = ?";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapInscripcion(rs);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en buscar inscripcion por id = " + id);
        }
        return null;
    }

    @Override
    public List<Inscripcion> findByUsuario(int idUsuario) {
        String sql = "SELECT * FROM inscripciones WHERE id_usuario = ?";
        List<Inscripcion> inscripciones = new ArrayList<>();
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inscripciones.add(mapInscripcion(rs));
                }
            }
            return inscripciones;
        } catch (Exception e) {
            System.err.println("Error en buscar inscripciones por idUsuario = " + idUsuario);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Inscripcion> findByActividad(int idActividad) {
        String sql = "SELECT * FROM inscripciones WHERE id_actividad = ?";
        List<Inscripcion> inscripciones = new ArrayList<>();
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idActividad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    inscripciones.add(mapInscripcion(rs));
                }
            }
            return inscripciones;
        } catch (Exception e) {
            System.err.println("Error en buscar inscripciones por idActividad = " + idActividad);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean save(Inscripcion inscripcion) {
        String sql = "INSERT INTO inscripciones (id, id_usuario, id_actividad, fecha, estado) VALUES (?,?,?,?,?)";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, inscripcion.getId());
            ps.setInt(2, inscripcion.getIdUsuario());
            ps.setInt(3, inscripcion.getIdActividad());
            ps.setString(4, inscripcion.getFecha().toString());
            ps.setString(5, inscripcion.getEstado());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error al guardar inscripcion");
            return false;
        }
    }

    @Override
    public boolean update(Inscripcion inscripcion) {
        String sql = "UPDATE inscripciones SET id_usuario = ?, id_actividad = ?, fecha = ?, estado = ? WHERE id = ?";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, inscripcion.getIdUsuario());
            ps.setInt(2, inscripcion.getIdActividad());
            ps.setString(3, inscripcion.getFecha().toString());
            ps.setString(4, inscripcion.getEstado());
            ps.setInt(5, inscripcion.getId());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error al actualizar inscripcion");
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM inscripciones WHERE id = ?";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error en borrar inscripcion por id = " + id);
            throw new RuntimeException(e);
        }
    }

    public Inscripcion mapInscripcion(ResultSet rs) throws SQLException {
        return new Inscripcion(
                rs.getInt("id"),
                rs.getInt("id_usuario"),
                rs.getInt("id_actividad"),
                LocalDate.parse(rs.getString("fecha")),
                rs.getString("estado"));
    }
}