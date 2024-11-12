package sendDesk360.model.database;

import java.sql.*;

/**
 * Manages the connection to the database and handles the creation of necessary tables.
 * This class initializes the database using H2 and provides methods to manage the database connection.
 */
public class DatabaseManager {

    /** JDBC driver for H2 database. */
    private static final String JDBC_DRIVER = "org.h2.Driver";

    /** Database connection URL for H2 database. */
    private static final String DB_URL = "jdbc:h2:~/helpArticlesDB;AUTO_SERVER=TRUE";    

    /** Database user for authentication. */
    private static final String USER = "sa";

    /** Database password for authentication. */
    private static final String PASS = "";

    /** The active database connection. */
    private Connection connection;

    /**
     * Constructs a DatabaseManager and initializes the connection to the database.
     * Creates necessary tables if they do not exist.
     * 
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public DatabaseManager() {
        try {
            connect();
            createTables();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error initializing the DatabaseManager: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
        }
    }

    /**
     * Establishes a connection to the database.
     *
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver class is not found
     */
    private void connect() throws SQLException, ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            throw e; // Rethrow the exception to propagate it upwards if necessary
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            throw e; // Rethrow the exception for clarity
        }
    }

    /**
     * Closes the database connection if it is not already closed.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection: " + e.getMessage());
        }
    }

    /**
     * Creates necessary tables in the database for users, roles, articles, keywords, references,
     * and related articles if they do not already exist.
     *
     * @throws SQLException if a database access error occurs during table creation
     */
    private void createTables() throws SQLException {
    	
    	// USERS TABLE
        String createUsersTable = "CREATE TABLE IF NOT EXISTS Users ("
                + "userID BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(255) UNIQUE NOT NULL, "
                + "password VARCHAR(512) NOT NULL, "
                + "password_iv VARCHAR(24) NOT NULL, "
                + "email VARCHAR(255), "
                + "flag BOOLEAN, "
                + "expireTime BIGINT, "
                + "firstName VARCHAR(255), "
                + "middleName VARCHAR(255), "
                + "lastName VARCHAR(255), "
                + "preferredName VARCHAR(255)"
                + ");";
        
        // USERS ROLE
        String createRolesTable = "CREATE TABLE IF NOT EXISTS Roles ("
        		+ "roleID BIGINT AUTO_INCREMENT PRIMARY KEY, "
        		+ "name VARCHAR(255), "
        		+ "privilege INT "
        		+ ");";
        
        String createUserRolesTable = "CREATE TABLE IF NOT EXISTS UserRoles ( "
        		+ "userID BIGINT, "
        		+ "roleID BIGINT, "
        		+ "FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE, "
        		+ "FOREIGN KEY (roleID) REFERENCES Roles(roleID) ON DELETE CASCADE, "
        		+ "PRIMARY KEY (userID, roleID) "
        		+ ");";

        // ARTICLES
        String createArticlesTable = "CREATE TABLE IF NOT EXISTS Articles ("
                + "articleID BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "uniqueID BIGINT UNIQUE NOT NULL, "
                + "title VARCHAR(1024) NOT NULL, "
                + "title_iv VARCHAR(24) NOT NULL, "
                + "shortDescription VARCHAR(2048), "
                + "shortDescription_iv VARCHAR(24), "
                + "difficulty VARCHAR(20) NOT NULL CHECK (difficulty IN ('beginner', 'intermediate', 'advanced', 'expert')), "
                + "body TEXT NOT NULL, "
                + "body_iv VARCHAR(24) NOT NULL"
                + ");";

        // KEYWORDS
        String createKeywordsTable = "CREATE TABLE IF NOT EXISTS Keywords ("
                + "articleID BIGINT NOT NULL, "
                + "keyword VARCHAR(255), "
                + "FOREIGN KEY (articleID) REFERENCES Articles(articleID) ON DELETE CASCADE"
                + ");";

        // REFRENCES
        String createReferencesTable = "CREATE TABLE IF NOT EXISTS References ("
                + "articleID BIGINT NOT NULL, "
                + "referenceLink VARCHAR(500), "
                + "FOREIGN KEY (articleID) REFERENCES Articles(articleID) ON DELETE CASCADE"
                + ");";

        // RELATED ARTICLES
        String createRelatedArticlesTable = "CREATE TABLE IF NOT EXISTS RelatedArticles ("
        		+ "articleID BIGINT NOT NULL,"
        		+ "relatedArticleID BIGINT NOT NULL,"
        		+ "FOREIGN KEY (articleID) REFERENCES Articles(articleID) ON DELETE CASCADE,"
        		+ "FOREIGN KEY (relatedArticleID) REFERENCES Articles(articleID) ON DELETE CASCADE"
        		+ ");";
        
        String createGroupsTable = "CREATE TABLE IF NOT EXISTS Groups ("
                + "groupID BIGINT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(255) UNIQUE NOT NULL"
                + ");";

        String createArticleGroupsTable = "CREATE TABLE IF NOT EXISTS ArticleGroups ("
                + "articleID BIGINT NOT NULL, "
                + "groupID BIGINT NOT NULL, "
                + "PRIMARY KEY (articleID, groupID), "
                + "FOREIGN KEY (articleID) REFERENCES Articles(articleID) ON DELETE CASCADE, "
                + "FOREIGN KEY (groupID) REFERENCES Groups(groupID) ON DELETE CASCADE"
                + ");";


        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createRolesTable);
            stmt.execute(createUserRolesTable);
            stmt.execute(createArticlesTable);
            stmt.execute(createKeywordsTable);
            stmt.execute(createReferencesTable);
            stmt.execute(createRelatedArticlesTable);
            stmt.execute(createGroupsTable); 
            stmt.execute(createArticleGroupsTable); 
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            throw e; // Rethrow to let the caller know about the failure
        }
    }

    // Get the database connection
    public Connection getConnection() {
        return connection;
    }
}