package sendDesk360.model.database;

import sendDesk360.model.User;
import sendDesk360.model.User.Role;
import sendDesk360.model.encryption.EncryptionHelper;
import sendDesk360.model.encryption.EncryptionUtils;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Vector;

/**
 * Manages user-related database operations, including adding, retrieving,
 * updating, authenticating, and deleting users, as well as handling user roles.
 */
public class UserManager {
    private Connection connection;
    private EncryptionHelper encryptionHelper;
    private User currentUser; // Store the current logged-in user

    /**
     * Constructs a UserManager with a given DatabaseManager.
     *
     * @param dbManager the DatabaseManager instance to use for database connections
     * @throws Exception if an error occurs while establishing a database connection
     */
    public UserManager(DatabaseManager dbManager) throws Exception {
        this.connection = dbManager.getConnection();
        this.encryptionHelper = new EncryptionHelper();
        
        
        /*
        Role instructorRole = new Role();
        instructorRole.setName("instructor");
        instructorRole.setPrivilege(1);
        Vector<Role> roles = new Vector<Role>(); 
        roles.add(instructorRole);
        User dummyInstructor = new User("instructor-middle-last-teach", "instructor1", "instructor@asu.edu", "Happy123!", false, roles);
        addUser(dummyInstructor);
       */
        
    }
   
    // Set the current user after successful login
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    // USER FUNCTIONS

    /**
     * Adds a new user to the database.
     *
     * @param user the User object containing the user's information
     * @throws Exception if an error occurs while adding the user to the database
     */
    public void addUser(User user) throws Exception {
        System.out.println("Attempting to add user: " + user.getUsername());
        String sql = "INSERT INTO Users (username, password, password_iv, email, flag, expireTime, firstName, middleName, lastName, preferredName) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());

            byte[] passwordIV = EncryptionUtils.generateRandomIV();
            byte[] encryptedPassword = encryptionHelper.encrypt(user.getPassword().getBytes("UTF-8"), passwordIV);
            pstmt.setString(2, Base64.getEncoder().encodeToString(encryptedPassword));
            pstmt.setString(3, Base64.getEncoder().encodeToString(passwordIV));

            pstmt.setString(4, user.getEmail());
            pstmt.setBoolean(5, user.isFlag());
            pstmt.setLong(6, user.getExpireTime());
            pstmt.setString(7, user.getName().getFirst());
            pstmt.setString(8, user.getName().getMiddle());
            pstmt.setString(9, user.getName().getLast());
            pstmt.setString(10, user.getName().getPref());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            if (rowsAffected > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        long userID = keys.getLong(1);
                        user.setUserID(userID);
                        addUserRoles(user);  // Save roles to UserRoles table
                        System.out.println("User added successfully with ID: " + userID);
                    }
                }
            } else {
                System.out.println("User was not added. Check constraints or data.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rethrow for higher-level handling.
        }
    }
    
    /**
     * Retrieves the user ID for a given username.
     *
     * @param username the username of the user to retrieve
     * @return the userID of the user, or -1 if not found
     * @throws SQLException if an error occurs while querying the database
     */
    private long getUserIDByUsername(String username) throws SQLException {
        String sql = "SELECT userID FROM Users WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("userID");
                }
            } catch (SQLException e) {
                System.err.println("Error retreiving userID from database " + e.getMessage());
                e.printStackTrace();
                throw e; // Rethrow for higher-level handling.
            }
        }
        return -1;
    }
    
    
    /**
     * Retrieves user information by username.
     *
     * @param username the username of the user to retrieve
     * @return the User object containing user information, or null if not found
     * @throws Exception if an error occurs while retrieving the user information
     */
    public User getUserByUsername(String username) throws Exception {
        String sql = "SELECT * FROM Users WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getLong("userID"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setFlag(rs.getBoolean("flag"));
                    user.setExpireTime(rs.getLong("expireTime"));

                    // Retrieve name fields directly
                    User.FullName fullName = new User.FullName();
                    fullName.setFirst(rs.getString("firstName"));
                    fullName.setMiddle(rs.getString("middleName"));
                    fullName.setLast(rs.getString("lastName"));
                    fullName.setPref(rs.getString("preferredName"));
                    user.setName(fullName);

                    // Retrieve roles
                    Vector<Role> roles = getRolesForUser(user.getUserID());
                    user.setRoles(roles);

                    return user;
                }
            } catch (SQLException e) {
                System.err.println("Error retreiving user from database " + e.getMessage());
                e.printStackTrace();
                throw e; // Rethrow for higher-level handling.
            }
        }
        return null;
    }
    
    /**
     * Updates the information of an existing user.
     *
     * @param user the User object containing the updated user information
     * @throws Exception if an error occurs while updating the user information
     */
    public void updateUser(User user) throws Exception {
        String sql = "UPDATE Users SET password = ?, password_iv = ?, email = ?, flag = ?, expireTime = ?, firstName = ?, middleName = ?, lastName = ?, preferredName = ? WHERE userID = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Encrypt the password
            byte[] passwordIV = EncryptionUtils.generateRandomIV();
            byte[] encryptedPasswordBytes = encryptionHelper.encrypt(user.getPassword().getBytes("UTF-8"), passwordIV);
            String encryptedPassword = Base64.getEncoder().encodeToString(encryptedPasswordBytes);
            String passwordIVBase64 = Base64.getEncoder().encodeToString(passwordIV);

            pstmt.setString(1, encryptedPassword);
            pstmt.setString(2, passwordIVBase64);
            pstmt.setString(3, user.getEmail());
            pstmt.setBoolean(4, user.isFlag());
            pstmt.setLong(5, user.getExpireTime());
            pstmt.setString(6, user.getName().getFirst());
            pstmt.setString(7, user.getName().getMiddle());
            pstmt.setString(8, user.getName().getLast());
            pstmt.setString(9, user.getName().getPref());
            pstmt.setLong(10, user.getUserID());

            pstmt.executeUpdate();

            // Update user roles
            changeUserRoles(user.getUsername(), user.getRoles());
        } catch (SQLException e) {
            System.err.println("Error updating user information " + e.getMessage());
            e.printStackTrace();
            throw e; // Rethrow for higher-level handling.
        }
    }
    
    /**
     * Checks if there are any users in the database.
     *
     * @return true if this is the first user, false otherwise
     * @throws SQLException if an error occurs while querying the database
     */
    public boolean isFirstUser() throws SQLException {
        String sql = "SELECT COUNT(*) AS user_count FROM Users;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int count = rs.getInt("user_count");
                return count == 0;
            }
        }
        return false;
    }
    
    
    /**
     * Authenticates a user by verifying the username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return true if authentication is successful, false otherwise
     * @throws Exception if an error occurs during authentication
     */
    public boolean authenticateUser(String username, String password) throws Exception {
        String sql = "SELECT password, password_iv FROM Users WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    String encryptedStoredPassword = resultSet.getString("password");
                    String passwordIVBase64 = resultSet.getString("password_iv");

                    // Decrypt the stored password
                    String decryptedStoredPassword = decryptPassword(encryptedStoredPassword, passwordIVBase64);

                    // Compare passwords
                    boolean success =  password.equals(decryptedStoredPassword);
                    if (!success) {
                    	System.out.println("Password: " + password + " did not match stored password: " + decryptedStoredPassword);
                    }
                    else {return success;} 
                }
                else {
                	System.out.println("No users with the given username found, are we sure it's in the database?"); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Database error during authentication", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error during password decryption", e);
        }
        return false;
    }
    
    /**
     * Decrypts an encrypted password using the provided initialization vector (IV).
     *
     * @param encryptedPassword The encrypted password in Base64 format.
     * @param ivBase64 The initialization vector (IV) used for decryption, also in Base64 format.
     * @return The decrypted password as a String, or null if decryption fails.
     * @throws Exception if there is an error during decryption.
     */
    private String decryptPassword(String encryptedPassword, String ivBase64) throws Exception {
    	try {
            byte[] encryptedPasswordBytes = Base64.getDecoder().decode(encryptedPassword);
            byte[] passwordIV = Base64.getDecoder().decode(ivBase64);
            byte[] decryptedPasswordBytes = encryptionHelper.decrypt(encryptedPasswordBytes, passwordIV);
            return new String(decryptedPasswordBytes, "UTF-8");
        } catch (IllegalArgumentException e) {
            System.err.println("Error decoding Base64: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Decryption error: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Deletes a user from the database based on the provided username.
     *
     * @param username The username of the user to be deleted.
     * @throws SQLException if there is an error during the deletion process.
     */
    public void deleteUser(String username) throws SQLException {
        String sql = "DELETE FROM Users WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }
   
    
    
    
    public void addUserRoles(User user) throws SQLException {
        String sql = "INSERT INTO UserRoles (userID, roleName) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Role role : user.getRoles()) {
            	System.out.println("Adding user role " + role.getName() + " for user " + user.getUserID()); 
                getOrCreateRole(role); // Ensure the role exists in the Roles table
                pstmt.setLong(1, user.getUserID());
                pstmt.setString(2, role.getName()); // Use roleName instead of roleID
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Error adding user roles: " + e.getMessage());
            throw e; // Rethrow exception for better visibility
        }
    }

    public void getOrCreateRole(Role role) throws SQLException {
        String selectSql = "SELECT name FROM Roles WHERE name = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSql)) {
            pstmt.setString(1, role.getName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Role exists, no need to insert again
                return;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving role: " + e.getMessage());
        }

        // If role does not exist, create it
        String insertSql = "INSERT INTO Roles (name, privilege) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
            pstmt.setString(1, role.getName());
            pstmt.setInt(2, role.getPrivilege());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating role: " + e.getMessage());
            throw e; // Rethrow exception if role creation fails
        }
    }
        
        
    /**
     * Changes the roles of a user identified by the given username.
     * This method deletes the user's existing roles and assigns new roles from the provided vector.
     *
     * @param username The username of the user whose roles are to be changed.
     * @param newRoles A vector of Role objects representing the new roles to be assigned to the user.
     * @throws SQLException if there is an error during the process of changing user roles.
     */
    public void changeUserRoles(String username, Vector<Role> newRoles) throws SQLException {
        try {
            long userID = getUserIDByUsername(username);
            if (userID == -1) {
                throw new SQLException("User not found");
            }

            // Delete existing roles
            String deleteSql = "DELETE FROM UserRoles WHERE userID = ?;";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
                pstmt.setLong(1, userID);
                pstmt.executeUpdate();
            }

            // Add new roles
            User user = new User();
            user.setUserID(userID);
            user.setRoles(newRoles);
            addUserRoles(user);
        } catch (SQLException e) {
            System.err.println("Error changing user roles: " + e.getMessage());
            throw e;
        }
    }

    public Vector<Role> getRolesForUser(long userID) throws SQLException {
    String sql = "SELECT Roles.name AS roleName, Roles.privilege " +
                 "FROM Roles " +
                 "JOIN UserRoles ON Roles.name = UserRoles.roleName " +
                 "WHERE UserRoles.userID = ?;";
    Vector<Role> roles = new Vector<>();
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setLong(1, userID); // Set the userID parameter
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String roleName = rs.getString("roleName"); // Correctly retrieve roleName
            System.out.print("user has role: " + roleName); 
            int privilege = rs.getInt("privilege");
            Role tempRole = new Role(); 
            tempRole.setPrivilege(privilege);
            tempRole.setName(roleName);
            roles.add(tempRole);// Add the Role object to the list
        }
    } catch (SQLException e) {
        System.err.println("Error retrieving roles for user: " + e.getMessage());
        throw e; // Rethrow exception for better visibility
    }
    return roles;
}
    
    
    // DEBUGGING
    
    /**
     * Prints a table of all users in the system, displaying user details and their roles.
     *
     * @throws Exception if there is an error during the retrieval or printing of users.
     */
    /*public void printAllUsersTable() throws Exception {
        List<User> users = getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        // Define table headers
        String leftAlignFormat = "| %-5s | %-15s | %-15s | %-10s | %-30s | %-15s |%n";

        System.out.format("+-------+-----------------+-----------------+------------+--------------------------------+-----------------+%n");
        System.out.format("| ID    | Username        | Email           | Privilege  | Name                           | Roles           |%n");
        System.out.format("+-------+-----------------+-----------------+------------+--------------------------------+-----------------+%n");

        for (User user : users) {
            String fullName = String.format("%s %s %s (%s)",
                    user.getName().getFirst(),
                    user.getName().getMiddle(),
                    user.getName().getLast(),
                    user.getName().getPref());

            String roles = "";
            for (Role role : user.getRoles()) {
                roles += role.getName() + "(" + role.getPrivilege() + ") ";
            }

            System.out.format(leftAlignFormat,
                    user.getUserID(),
                    truncate(user.getUsername(), 15),
                    truncate(user.getEmail(), 15),
                    getHighestPrivilege(user.getRoles()),
                    truncate(fullName, 30),
                    truncate(roles.trim(), 15));
        }

        System.out.format("+-------+-----------------+-----------------+------------+--------------------------------+-----------------+%n");
    }*/

    /**
     * Helper method to determine the highest privilege level from a vector of roles.
     *
     * @param roles A vector of Role objects from which to determine the highest privilege.
     * @return A string representation of the highest privilege level.
     */
    private String getHighestPrivilege(Vector<Role> roles) {
        int highestPrivilege = 0;
        for (Role role : roles) {
            if (role.getPrivilege() > highestPrivilege) {
                highestPrivilege = role.getPrivilege();
            }
        }
        return String.valueOf(highestPrivilege);
    }

    /**
     * Helper method to truncate a string to a specified length.
     * If the string exceeds the length, it will be shortened and appended with "..." at the end.
     *
     * @param value The string value to be truncated.
     * @param length The maximum length for the string.
     * @return The truncated string if it exceeds the specified length; otherwise, the original string.
     */
    private String truncate(String value, int length) {
        if (value.length() <= length) {
            return value;
        } else {
            return value.substring(0, length - 3) + "...";
        }
    }
    
    
    public List<String> getCommentsForArticle(long articleID) throws SQLException {
        String query = "SELECT Users.username, commentText, timestamp FROM Comments " +
                       "INNER JOIN Users ON Comments.userID = Users.userID " +
                       "WHERE articleID = ? ORDER BY timestamp ASC";
        List<String> comments = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, articleID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String comment = "[" + rs.getTimestamp("timestamp") + "] "
                            + rs.getString("username") + ": " + rs.getString("commentText");
                    comments.add(comment);
                }
            }
        }
        return comments;
    }
    
    
    public void addCommentToArticle(long articleID, long userID, String commentText) throws SQLException {
        String insertSQL = "INSERT INTO Comments (articleID, userID, commentText) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setLong(1, articleID);
            pstmt.setLong(2, userID);
            pstmt.setString(3, commentText);
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves a list of all users from the database.
     *
     * @return A list of User objects representing all users in the system.
     * @throws Exception if there is an error during the retrieval process.
     */
    public List<User> getAllUsers() throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
               while (rs.next()) {
                   User user = new User();
                   user.setUserID(rs.getLong("userID"));
                   user.setUsername(rs.getString("username"));
                   user.setEmail(rs.getString("email"));
                   user.setFlag(rs.getBoolean("flag"));
                   user.setExpireTime(rs.getLong("expireTime"));

                   // Retrieve name fields directly
                   User.FullName fullName = new User.FullName();
                   fullName.setFirst(rs.getString("firstName"));
                   fullName.setMiddle(rs.getString("middleName"));
                   fullName.setLast(rs.getString("lastName"));
                   fullName.setPref(rs.getString("preferredName"));
                   user.setName(fullName);

                   // Retrieve roles
                   Vector<Role> roles = getRolesForUser(user.getUserID());
                   user.setRoles(roles);

                   users.add(user);
               }
           } catch (SQLException e) {
               System.err.println("Error retrieving all users: " + e.getMessage());
           }
        return users;
    }
    
    
    public List<String> getAccessTagsForUser(User user, Role role) throws Exception {
        ArrayList<String> tags = new ArrayList<>();
        String query = "SELECT tagName FROM UserAccessTags WHERE userID = ? AND roleName = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, user.getUserID());
            stmt.setString(2, role.getName());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tags.add(rs.getString("tagName"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving access tags for user: " + e.getMessage());
            throw e;
        }

        return tags;
    }

    public List<String> getAccessTagsForRole(Role role) throws SQLException {
        String query = "SELECT tagName FROM AccessTags WHERE roleName = ?";
        List<String> accessTags = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, role.getName());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                accessTags.add(rs.getString("tagName"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving access tags for role: " + e.getMessage());
            throw e;
        }

        return accessTags;
    }
    
    public List<String> getAccessTagsForUser(User user){
    	List<String> result = new ArrayList<String>(); 
    	for (Role r: user.getRoles()) {
    		try {
				result.addAll(getAccessTagsForUser(user,r));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    	}
    	return result;
    }

    public void createAccessTag(String tagName, String roleName) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM AccessTags WHERE tagName = ? AND roleName = ?";
        String insertSQL = "INSERT INTO AccessTags (tagName, roleName) VALUES (?, ?)";
        
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSQL)) {
            checkStmt.setString(1, tagName);
            checkStmt.setString(2, roleName);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Tag already exists for this role, no need to insert
                    System.out.println("Access tag already exists: " + tagName + " for role " + roleName);
                    return;
                }
            }
        }

        try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
            insertStmt.setString(1, tagName);
            insertStmt.setString(2, roleName);
            insertStmt.executeUpdate();
            System.out.println("Access tag created: " + tagName + " for role " + roleName);
        }
    }

    public void addAccessTagForUser(String tag, User user, Role role) throws Exception {
    	System.out.println("Checking access tag : " + tag + " for role " + role.getName()); 
        String checkTagQuery = "SELECT COUNT(*) AS count FROM AccessTags WHERE tagName = ? AND roleName = ?";
        String addTagQuery = "INSERT INTO UserAccessTags (userID, roleName, tagName) VALUES (?, ?, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkTagQuery);
             PreparedStatement addStmt = connection.prepareStatement(addTagQuery)) {

            checkStmt.setString(1, tag);
            checkStmt.setString(2, role.getName());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt("count") == 0) {
                throw new IllegalArgumentException(
                    "Access tag '" + tag + "' does not exist for role '" + role.getName() + "'."
                );
            }

            addStmt.setLong(1, user.getUserID());
            addStmt.setString(2, role.getName());
            addStmt.setString(3, tag);
            addStmt.executeUpdate();

            System.out.println("Access tag '" + tag + "' added for user ID " + user.getUserID() + " under role '" + role.getName() + "'.");
        } catch (SQLException e) {
            System.err.println("Error adding user access tag: " + e.getMessage());
            throw e;
        }
    }

    public boolean userHasAccessTag(long userID, String tagName, Role role) throws SQLException {
        String query = "SELECT COUNT(*) AS tagCount FROM UserAccessTags WHERE userID = ? AND tagName = ? AND roleName = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, userID);
            stmt.setString(2, tagName);
            stmt.setString(3, role.getName());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("tagCount") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking user access tag: " + e.getMessage());
            throw e;
        }

        return false;
    }

    public void removeAccessTagForUser(String tag, User user, Role role) throws Exception {
        String query = "DELETE FROM UserAccessTags WHERE userID = ? AND tagName = ? AND roleName = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, user.getUserID());
            stmt.setString(2, tag);
            stmt.setString(3, role.getName());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Access tag '" + tag + "' removed for user ID " + user.getUserID() + " under role '" + role.getName() + "'.");
            } else {
                System.out.println("No matching access tag found for user ID " + user.getUserID() + " with tag '" + tag + "' under role '" + role.getName() + "'.");
            }
        } catch (SQLException e) {
            System.err.println("Error removing user access tag: " + e.getMessage());
            throw e;
        }
    }

    public List<User> getUsersWithAccessTag(String tagName) throws SQLException {
        String query = 
            "SELECT u.userID, u.firstName, u.lastName, u.username " +
            "FROM Users u " +
            "JOIN UserAccessTags ua ON u.userID = ua.userID " +
            "WHERE ua.tagName = ?";

        List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tagName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    long userID = rs.getLong("userID");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String username = rs.getString("username");

                    
                    User user = getUserByUsername(username); 
                    users.add(user);
                }
             System.out.print("fetched " + rs.getFetchSize() + " users for access group " + tagName);
            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } catch (SQLException e) {
            System.err.println("Error fetching users with access tag '" + tagName + "': " + e.getMessage());
            throw e;
        }

        return users;
    }

    public List<String> getAllAccessTags() throws SQLException {
        String query = "SELECT DISTINCT tagName FROM AccessTags";
        List<String> tags = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String tagName = rs.getString("tagName");
                tags.add(tagName); // Add the tag to the list
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all access tags: " + e.getMessage());
            throw e;
        }

        return tags;
    }


}