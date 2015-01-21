package edu.gatech.cs4911.mintyfresh.db;

/**
 * A grouped set of configurations to connect to an external database.
 */
public class DatabaseConfig {
    /**
     * The username to authenticate against the database.
     */
    protected String username;
    /**
     * The password to authenticate against the database.
     */
    protected String password;
    /**
     * The hostname to connect to.
     */
    protected String hostname;
    /**
     * The port number to open a remote socket to.
     */
    protected int port;
    /**
     * The database to connect to on the remote host.
     */
    protected String database;

    /**
     * Initializes a new DatabaseConfig with given attributes.
     *
     * @param username The username to authenticate with.
     * @param password The password to authenticate with.
     * @param hostname The hostname of the remote database to connect to.
     * @param port The port number to establish a remote socket on.
     * @param database The database to connect to.
     */
    public DatabaseConfig(String username, String password, String hostname,
                          int port, String database) {

        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
        this.database = database;
    }

    /**
     * Static database configuration for connecting to the MySQL database on Steakscorp.
     * This account is read-only (SELECT only).
     */
    public static DatabaseConfig STEAKSCORP_READ_ONLY = new DatabaseConfig("cs49114901-read",
            "1c792_e03de141", "steakscorp.org", 3306, "4901mintyfresh");
}