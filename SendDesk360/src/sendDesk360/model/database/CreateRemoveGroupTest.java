package sendDesk360.model.database;

import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class CreateRemoveGroupTest {
    private Connection connection;
    private SpecialAccessGroupManager specialAccessGroupManager;

    @Before
    public void setUp() throws Exception {
        // Set up an in-memory H2 database for testing
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        // Create the necessary tables for the test
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE SpecialAccessGroups (" +
                    "groupID BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "groupName VARCHAR(255) NOT NULL, " +
                    "description VARCHAR(1024))");

            stmt.execute("CREATE TABLE SpecialGroupAdmins (" +
                    "groupID BIGINT NOT NULL, " +
                    "userID BIGINT NOT NULL, " +
                    "FOREIGN KEY (groupID) REFERENCES SpecialAccessGroups(groupID) ON DELETE CASCADE)");

            stmt.execute("CREATE TABLE SpecialGroupInstructors (" +
                    "groupID BIGINT NOT NULL, " +
                    "userID BIGINT NOT NULL, " +
                    "FOREIGN KEY (groupID) REFERENCES SpecialAccessGroups(groupID) ON DELETE CASCADE)");

            stmt.execute("CREATE TABLE SpecialGroupStudents (" +
                    "groupID BIGINT NOT NULL, " +
                    "userID BIGINT NOT NULL, " +
                    "FOREIGN KEY (groupID) REFERENCES SpecialAccessGroups(groupID) ON DELETE CASCADE)");
        }

        // Initialize the SpecialAccessGroupManager with the mock database connection
        specialAccessGroupManager = new SpecialAccessGroupManager(connection);
    }

    @Test
    public void testCreateAndDeleteGroup() throws SQLException {
        // Step 1: Create a new special access group
        String groupName = "Test Group";
        String description = "Test Group for JUnit Testing";
        long groupId = specialAccessGroupManager.createSpecialAccessGroup(groupName, description);

        // Step 2: Verify that the group was created
        assertTrue("Group should exist after creation", checkGroupExists(groupId));

        // Step 3: Delete the special access group
        deleteGroup(groupId);

        // Step 4: Verify that the group is deleted
        assertFalse("Group should not exist after deletion", checkGroupExists(groupId));
    }

    // Helper method to check if a group exists in the database
    private boolean checkGroupExists(long groupId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM SpecialAccessGroups WHERE groupID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, groupId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Helper method to delete a group by its ID
    private void deleteGroup(long groupId) throws SQLException {
        String sql = "DELETE FROM SpecialAccessGroups WHERE groupID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, groupId);
            pstmt.executeUpdate();
        }
    }
}
