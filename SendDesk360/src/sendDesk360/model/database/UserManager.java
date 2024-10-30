package sendDesk360.model.database;

import sendDesk360.model.User;
import sendDesk360.model.User.Role;
import sendDesk360.model.encryption.EncryptionHelper;
import sendDesk360.model.encryption.EncryptionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Vector;

public class UserManager {
    private Connection connection;
    private EncryptionHelper encryptionHelper;

    public UserManager(DatabaseManager dbManager) throws Exception {
        this.connection = dbManager.getConnection();
        this.encryptionHelper = new EncryptionHelper();
    }
    
    // USER FUNCTIONS

    // Add a new user
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
                        addUserRoles(user);
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
    
    // Get userID by username
    private long getUserIDByUsername(String username) throws SQLException {
        String sql = "SELECT userID FROM Users WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("userID");
                }
            }
        }
        return -1;
    }
    
    
    // Retrieve user information
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
            }
        }
        return null;
    }
    
    // Update the user info
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
        }
    }
    
    // Check if its the first user
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
    
    
    // Authenticate user
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
                    return password.equals(decryptedStoredPassword);
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
    
    private String decryptPassword(String encryptedPassword, String ivBase64) throws Exception {
        byte[] encryptedPasswordBytes = Base64.getDecoder().decode(encryptedPassword);
        byte[] passwordIV = Base64.getDecoder().decode(ivBase64);
        byte[] decryptedPasswordBytes = encryptionHelper.decrypt(encryptedPasswordBytes, passwordIV);
        return new String(decryptedPasswordBytes, "UTF-8");
    }
    
    // Delete a user
    public void deleteUser(String username) throws SQLException {
        String sql = "DELETE FROM Users WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }
    }
   
    
    
    
    // ROLES

    // Add roles for a user
    public void addUserRoles(User user) throws SQLException {
        String sql = "INSERT INTO UserRoles (userID, roleID) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Role role : user.getRoles()) {
                long roleID = getOrCreateRole(role);
                pstmt.setLong(1, user.getUserID());
                pstmt.setLong(2, roleID);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    // Get or create a role, return the roleID
    public long getOrCreateRole(Role role) throws SQLException {
        String selectSql = "SELECT roleID FROM Roles WHERE name = ? AND privilege = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSql)) {
            pstmt.setString(1, role.getName());
            pstmt.setInt(2, role.getPrivilege());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("roleID");
            }
        }
        // Role does not exist, create it
        String insertSql = "INSERT INTO Roles (name, privilege) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, role.getName());
            pstmt.setInt(2, role.getPrivilege());
            pstmt.executeUpdate();
            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            }
        }
        throw new SQLException("Failed to insert role");
    }

    // Change user roles
    public void changeUserRoles(String username, Vector<Role> newRoles) throws SQLException {
        // Get userID
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
    }

    // Get User roles
    private Vector<Role> getRolesForUser(long userID) throws SQLException {
        String sql = "SELECT r.name, r.privilege FROM Roles r JOIN UserRoles ur ON r.roleID = ur.roleID WHERE ur.userID = ?;";
        Vector<Role> roles = new Vector<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Role role = new Role();
                    role.setName(rs.getString("name"));
                    role.setPrivilege(rs.getInt("privilege"));
                    roles.add(role);
                }
            }
        }
        return roles;
    }
    
    
    // DEBUGGING
    
    
    public void printAllUsersTable() throws Exception {
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
    }

    // Helper method to get the highest privilege from roles
    private String getHighestPrivilege(Vector<Role> roles) {
        int highestPrivilege = 0;
        for (Role role : roles) {
            if (role.getPrivilege() > highestPrivilege) {
                highestPrivilege = role.getPrivilege();
            }
        }
        return String.valueOf(highestPrivilege);
    }

    // Helper method to truncate strings to a certain length
    private String truncate(String value, int length) {
        if (value.length() <= length) {
            return value;
        } else {
            return value.substring(0, length - 3) + "...";
        }
    }

    // Existing method to get all users
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
        }
        return users;
    }
}