package edu.gatech.cs4911.mintyfresh.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A DBHandler object handles all communication and configuration between client and database.
 */
public class DBHandler {
    /**
     * The configuration to use to authenticate with a remote database.
     * Contains username, password, hostname, port, and database fields.
     */
    private DatabaseConfig config;
    /**
     * A remote JDBC connection stream.
     */
    private Connection conn;
    /**
     * Constructs a new DBHandler object with provided server configuration.
     *
     * @param config Configuration to use to authenticate and connect to the remote database.
     * @throws java.sql.SQLException if the connection to the database was unsuccessful.
     */
    public DBHandler(DatabaseConfig config) throws SQLException {
        this.config = config;
        initJDBC();
    }

    /**
     * Initializes JDBC connection parameters.
     *
     * @throws java.sql.SQLException if the connection to the database was unsuccessful.
     */
    private void initJDBC() throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + config.hostname + ":" + config.port
                        + "/" + config.database,
                config.username,
                config.password);
    }

    // TODO: Complete query handling through conn
}
