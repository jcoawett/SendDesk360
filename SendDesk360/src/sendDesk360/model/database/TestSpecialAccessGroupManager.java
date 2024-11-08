package sendDesk360.model.database;

import sendDesk360.model.encryption.EncryptionHelper;
import sendDesk360.model.encryption.EncryptionUtils;

import java.sql.*;

public class TestSpecialAccessGroupManager {
    public static void main(String[] args) {
        // Set up your H2 in-memory database connection
        String dbUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // H2 in-memory database URL
        String dbUser = "sa";  // Default H2 user
        String dbPassword = ""; // Default H2 password is empty

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // Create tables for testing (simplified schema)
            setupTestDatabaseSchema(connection);

            // Create an instance of SpecialAccessGroupManager
            SpecialAccessGroupManager manager = new SpecialAccessGroupManager(connection);

            // Test creating a new special access group with local test data
            long groupID = manager.createSpecialAccessGroup("Group 1", "Description of Group 1");
            System.out.println("Group created with ID: " + groupID);

            // Test adding a user as admin to the group with local test data
            long userID = 12345;  // Hardcoded userID for testing
            manager.addAdminToSpecialGroup(groupID, userID);
            System.out.println("Added admin with userID " + userID + " to group " + groupID);

            // Test adding an instructor to the group with local test data
            manager.addInstructorToSpecialGroup(groupID, userID);
            System.out.println("Added instructor with userID " + userID + " to group " + groupID);

            // Test adding a student to the group with local test data
            long studentID = 67890;  // Hardcoded studentID for testing
            manager.addStudentToSpecialGroup(groupID, studentID);
            System.out.println("Added student with userID " + studentID + " to group " + groupID);

            // Test encrypting and storing an article with local test data
            String articleBody = "This is the body of a secret article.";
            long articleID = 1;  // Hardcoded articleID for testing
            
            try {
                manager.encryptAndStoreArticle(groupID, articleID, articleBody);
                System.out.println("Article encrypted and stored in the database.");
            } catch (Exception e) {
                // Handle the encryption and storing exception
                System.err.println("Error occurred during article encryption and storage: " + e.getMessage());
                e.printStackTrace();
            }

            // Test decrypting an article
            try {
                String decryptedArticleBody = manager.decryptArticleBody(groupID, articleID, userID);
                System.out.println("Decrypted article body: " + decryptedArticleBody);
            } catch (Exception e) {
                // Handle decryption error
                System.err.println("Error occurred during article decryption: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle SQL exceptions properly
        }
    }

    // Helper method to set up a simplified test database schema for testing
    private static void setupTestDatabaseSchema(Connection connection) throws SQLException {
        // Simplified table creation for testing purposes
        String createSpecialAccessGroupsTable = "CREATE TABLE SpecialAccessGroups (" +
                "groupID IDENTITY PRIMARY KEY, " +
                "groupName VARCHAR(255), " +
                "description VARCHAR(255));";

        String createSpecialGroupAdminsTable = "CREATE TABLE SpecialGroupAdmins (" +
                "groupID BIGINT, " +
                "userID BIGINT, " +
                "PRIMARY KEY (groupID, userID));";

        String createSpecialGroupInstructorsTable = "CREATE TABLE SpecialGroupInstructors (" +
                "groupID BIGINT, " +
                "userID BIGINT, " +
                "PRIMARY KEY (groupID, userID));";

        String createSpecialGroupStudentsTable = "CREATE TABLE SpecialGroupStudents (" +
                "groupID BIGINT, " +
                "userID BIGINT, " +
                "PRIMARY KEY (groupID, userID));";

        String createSpecialGroupRolesTable = "CREATE TABLE SpecialGroupRoles (" +
                "groupID BIGINT, " +
                "userID BIGINT, " +
                "roleType VARCHAR(255), " +
                "PRIMARY KEY (groupID, userID));";

        String createSpecialAccessArticlesTable = "CREATE TABLE SpecialAccessArticles (" +
                "groupID BIGINT, " +
                "articleID BIGINT, " +
                "encryptedBody VARBINARY, " +
                "iv VARBINARY, " +
                "PRIMARY KEY (groupID, articleID));";

        // Execute the table creation queries
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createSpecialAccessGroupsTable);
            stmt.execute(createSpecialGroupAdminsTable);
            stmt.execute(createSpecialGroupInstructorsTable);
            stmt.execute(createSpecialGroupStudentsTable);
            stmt.execute(createSpecialGroupRolesTable);
            stmt.execute(createSpecialAccessArticlesTable);
        }
    }
}
