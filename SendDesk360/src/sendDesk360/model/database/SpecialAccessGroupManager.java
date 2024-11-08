package sendDesk360.model.database;

import sendDesk360.model.encryption.EncryptionHelper;
import sendDesk360.model.encryption.EncryptionUtils;

import java.sql.*;

/**
 * This class manages special access groups, including creating groups, adding/removing users with specific roles (admin, instructor, student),
 * and handling encrypted article access.
 */
public class SpecialAccessGroupManager {

    private Connection connection;
    private EncryptionHelper encryptionHelper;

    // Constructor to initialize the connection and encryption helper
    public SpecialAccessGroupManager(Connection connection) {
        this.connection = connection;
        this.encryptionHelper = new EncryptionHelper();
    }

    // Create a new special access group
    public long createSpecialAccessGroup(String groupName, String description) throws SQLException {
        String sql = "INSERT INTO SpecialAccessGroups (groupName, description) VALUES (?, ?)";
        return executeInsertAndReturnGeneratedKey(sql, groupName, description);
    }

    // Helper method to execute an insert and return the generated key
    private long executeInsertAndReturnGeneratedKey(String sql, String... params) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, params[i]);
            }
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);  // Return the generated ID
                }
            }
        }
        throw new SQLException("Failed to execute insert operation.");
    }

    // Add an admin to a special access group
    public void addAdminToSpecialGroup(long groupID, long userID) throws SQLException {
        String sql = "INSERT INTO SpecialGroupAdmins (groupID, userID) VALUES (?, ?)";
        executeUpdate(sql, groupID, userID);
    }

    // Remove an admin from a special access group
    public void removeAdminFromSpecialGroup(long groupID, long userID) throws SQLException {
        String sql = "DELETE FROM SpecialGroupAdmins WHERE groupID = ? AND userID = ?";
        executeUpdate(sql, groupID, userID);
    }

    // Add an instructor to a special access group
    public void addInstructorToSpecialGroup(long groupID, long userID) throws SQLException {
        String sql = "INSERT INTO SpecialGroupInstructors (groupID, userID) VALUES (?, ?)";
        executeUpdate(sql, groupID, userID);

        // If the first instructor, grant rights to view decrypted articles and admin rights
        if (getInstructorCountForGroup(groupID) == 1) {
            grantInstructorDecryptionRights(groupID, userID);
            grantAdminRightsForSpecialGroup(groupID, userID);
        }
    }

    // Remove an instructor from a special access group
    public void removeInstructorFromSpecialGroup(long groupID, long userID) throws SQLException {
        String sql = "DELETE FROM SpecialGroupInstructors WHERE groupID = ? AND userID = ?";
        executeUpdate(sql, groupID, userID);
    }

    // Add a student to a special access group
    public void addStudentToSpecialGroup(long groupID, long userID) throws SQLException {
        String sql = "INSERT INTO SpecialGroupStudents (groupID, userID) VALUES (?, ?)";
        executeUpdate(sql, groupID, userID);
    }

    // Remove a student from a special access group
    public void removeStudentFromSpecialGroup(long groupID, long userID) throws SQLException {
        String sql = "DELETE FROM SpecialGroupStudents WHERE groupID = ? AND userID = ?";
        executeUpdate(sql, groupID, userID);
    }

    // Assign a role to a user in the special access group (e.g., "viewer", "admin", etc.)
    public void assignRoleToUserInGroup(long groupID, long userID, String role) throws SQLException {
        String sql = "INSERT INTO SpecialGroupRoles (groupID, userID, roleType) VALUES (?, ?, ?)";
        executeUpdate(sql, groupID, userID, role);
    }

    // Helper method to execute update statements (INSERT, DELETE, UPDATE)
    private void executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        }
    }

    // Encrypt and store article for the group
    public void encryptAndStoreArticle(long groupID, long articleID, String articleBody) throws SQLException {
        byte[] iv = EncryptionUtils.generateRandomIV();
        byte[] encryptedBody;
        try {
            encryptedBody = encryptionHelper.encrypt(articleBody.getBytes(), iv);  // Encryption may throw an error
        } catch (Exception e) {
            throw new SQLException("Error during encryption of the article: " + e.getMessage(), e);
        }

        String sql = "INSERT INTO SpecialAccessArticles (groupID, articleID, encryptedBody, iv) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, groupID);
            pstmt.setLong(2, articleID);
            pstmt.setBytes(3, encryptedBody);
            pstmt.setBytes(4, iv);
            pstmt.executeUpdate();
        }
    }

    // Decrypt and retrieve an article body for users with appropriate rights
    public String decryptArticleBody(long groupID, long articleID, long userID) throws SQLException {
        if (!hasDecryptionRights(groupID, userID)) {
            throw new SQLException("User does not have decryption rights for this group.");
        }

        String sql = "SELECT encryptedBody, iv FROM SpecialAccessArticles WHERE groupID = ? AND articleID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, groupID);
            pstmt.setLong(2, articleID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    byte[] encryptedBody = rs.getBytes("encryptedBody");
                    byte[] iv = rs.getBytes("iv");

                    byte[] decryptedBody;
                    try {
                        decryptedBody = encryptionHelper.decrypt(encryptedBody, iv);  // Decryption may throw an error
                    } catch (Exception e) {
                        throw new SQLException("Error during decryption of the article: " + e.getMessage(), e);
                    }
                    return new String(decryptedBody);
                }
            }
        }
        throw new SQLException("Article not found or unable to decrypt.");
    }

    // Check if a user has decryption rights for a special access group
    private boolean hasDecryptionRights(long groupID, long userID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM SpecialGroupInstructors WHERE groupID = ? AND userID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, groupID);
            pstmt.setLong(2, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Grant rights to an instructor to view decrypted articles
    private void grantInstructorDecryptionRights(long groupID, long userID) throws SQLException {
        // Logic to grant rights (stub)
    }

    // Grant admin rights to a special access group
    private void grantAdminRightsForSpecialGroup(long groupID, long userID) throws SQLException {
        // Logic to grant admin rights (stub)
    }

    // Get the count of instructors in a special access group
    private int getInstructorCountForGroup(long groupID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM SpecialGroupInstructors WHERE groupID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, groupID);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}

