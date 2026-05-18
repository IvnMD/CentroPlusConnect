package es.ies.puerto.repositories.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public  class SqliteConnectionManager {
    protected final String url;

    public SqliteConnectionManager() {
        this("jdbc:sqlite:src/main/resources/data/centroplus.db");
    }

    public SqliteConnectionManager(String url) {
        this.url = url;
    }

    public  Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
