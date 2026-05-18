package es.ies.puerto.repositories.sqlite;

import es.ies.puerto.models.Actividad;
import es.ies.puerto.repositories.ActividadRepositoryInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActividadSqliteRepository implements ActividadRepositoryInterface {

    private final SqliteConnectionManager manager;

    public ActividadSqliteRepository() {
        this(new SqliteConnectionManager());
        
    }

    public ActividadSqliteRepository(SqliteConnectionManager manager) {
        this.manager = manager;
        new DatabaseInitializer(manager).createTables();
    }

    // private int id;
    // private String nombre;
    // private String tipoActividad;
    // private int duracionMinutos;
    // private double precio;
    // private int plazasMaximas;
    // private int plazasOcupadas;

    @Override
    public List<Actividad> findAll() {
        String sql = "SELECT * FROM actividades";
        List<Actividad> actividades = new ArrayList<>();
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    actividades.add(mapActividad(rs));
                }
            }
            return actividades;
        } catch (Exception e) {
            System.err.println("Error en listar todas las actividades");
            return new ArrayList<>();
        }
    }

    @Override
    public Actividad findById(int id) {
        String sql = "SELECT * FROM actividades WHERE id = ?";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapActividad(rs);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en buscar actividad por id = " + id);
            throw new RuntimeException(e);
        }
        return null;

    }

    @Override
    public boolean save(Actividad actividad) {
        String sql = "INSERT INTO actividades (id, nombre, tipo_actividad, duracion_minutos, precio, plazas_maximas, plazas_ocupadas) VALUES (?,?,?,?,?,?,?)";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, actividad.getId());
            ps.setString(2, actividad.getNombre());
            ps.setString(3, actividad.getTipoActividad());
            ps.setInt(4, actividad.getDuracionMinutos());
            ps.setDouble(5, actividad.getPrecio());
            ps.setInt(6, actividad.getPlazasMaximas());
            ps.setInt(7, actividad.getPlazasOcupadas());

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error al crear actividad");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Actividad actividad) {
        String sql = "UPDATE actividades SET nombre = ?, tipo_actividad = ?, duracion_minutos = ?, precio = ?, plazas_maximas = ?, plazas_ocupadas= ? WHERE id = ?";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, actividad.getNombre());
            ps.setString(2, actividad.getTipoActividad());
            ps.setInt(3, actividad.getDuracionMinutos());
            ps.setDouble(4, actividad.getPrecio());
            ps.setInt(5, actividad.getPlazasMaximas());
            ps.setInt(6, actividad.getPlazasOcupadas());

            ps.setInt(7, actividad.getId());

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error al actualizar actividad");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM actividades WHERE id = ?";
        try (Connection cn = manager.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.err.println("Error en borrar actividad por id = " + id);
            throw new RuntimeException(e);
        }

    }

    public Actividad mapActividad(ResultSet rs) throws SQLException {
        return new Actividad(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("tipo_actividad"),
                rs.getInt("duracion_minutos"),
                rs.getInt("precio"),
                rs.getInt("plazas_maximas"),
                rs.getInt("plazas_ocupadas"));
    }
}
