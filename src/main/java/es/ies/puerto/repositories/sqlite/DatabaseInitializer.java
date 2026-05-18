package es.ies.puerto.repositories.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private final SqliteConnectionManager manager;

    public DatabaseInitializer(SqliteConnectionManager manager) {
        this.manager = manager;
    }

    public void createTables() {
        try (Connection connection = manager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id INTEGER PRIMARY KEY,
                        nombre TEXT NOT NULL,
                        dni TEXT NOT NULL UNIQUE,
                        email TEXT NOT NULL,
                        telefono TEXT,
                        tipo_usuario TEXT NOT NULL
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS actividades (
                        id INTEGER PRIMARY KEY,
                        nombre TEXT NOT NULL,
                        tipo_actividad TEXT NOT NULL,
                        duracion_minutos INTEGER NOT NULL,
                        precio REAL NOT NULL,
                        plazas_maximas INTEGER NOT NULL,
                        plazas_ocupadas INTEGER NOT NULL
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS inscripciones (
                        id INTEGER PRIMARY KEY,
                        id_usuario INTEGER NOT NULL,
                        id_actividad INTEGER NOT NULL,
                        fecha TEXT NOT NULL,
                        estado TEXT NOT NULL
                    )
                    """);
        } catch (SQLException exception) {
            throw new RuntimeException("No se pudieron crear las tablas", exception);
        }
    }
}
