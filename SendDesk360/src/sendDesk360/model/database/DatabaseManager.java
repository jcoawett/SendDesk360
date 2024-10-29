package sendDesk360.model.database;

import java.sql.*;

public class DatabaseManager {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/helpArticlesDB;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection connection;

    public DatabaseManager() throws SQLException, ClassNotFoundException {
        connect();
        createTables();
    }

    // Establish database connection
    private void connect() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // Close database connection
    public void close() throws SQLException {
        if (connection != null) connection.close();
    }

    // Create necessary tables
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
                + "articleID BIGINT NOT NULL, "
                + "relatedArticleID BIGINT NOT NULL, "
                + "FOREIGN KEY (articleID) REFERENCES Articles(articleID) ON DELETE CASCADE, "
                + "FOREIGN KEY (relatedArticleID) REFERENCES Articles(articleID)"
                + ");";

        Statement stmt = connection.createStatement();
        stmt.execute(createUsersTable);
        stmt.execute(createRolesTable);
        stmt.execute(createUserRolesTable);
        stmt.execute(createArticlesTable);
        stmt.execute(createKeywordsTable);
        stmt.execute(createReferencesTable);
        stmt.execute(createRelatedArticlesTable);
        stmt.close();
    }

    // Get the database connection
    public Connection getConnection() {
        return connection;
    }
}