package sendDesk360;

import sendDesk360.model.User;
import sendDesk360.model.User.Role;
import sendDesk360.model.Article;
import sendDesk360.model.database.DatabaseManager;
import sendDesk360.model.database.UserManager;
import sendDesk360.model.database.ArticleManager;

import java.util.*;
import java.sql.*;

public class DatabaseConsoleApp {

    private static DatabaseManager dbManager;
    private static UserManager userManager;
    private static ArticleManager articleManager;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Initialize the DatabaseManager, UserManager, and ArticleManager
            dbManager = new DatabaseManager();
            userManager = new UserManager(dbManager);
            articleManager = new ArticleManager(dbManager);

            mainMenu();

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the database connection
            if (dbManager != null) {
                try {
                    dbManager.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    System.err.println("Error closing database connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("Exiting the application...");
        }
    }
    
    
    // MAIN TEST LOOP
    private static void mainMenu() {
        boolean exit = false;
        while (!exit) {
            displayMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    manageUsers();
                    break;
                case "2":
                    manageArticles();
                    break;
                case "3":
                    backupArticles();
                    break;
                case "4":
                    restoreArticles();
                    break;
                case "5":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // DISPLAY MENU
    private static void displayMenu() {
        System.out.println("=================================");
        System.out.println("Select an option:");
        System.out.println("1. Manage Users");
        System.out.println("2. Manage Articles");
        System.out.println("3. Backup Articles");
        System.out.println("4. Restore Articles");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }
    // ------------------------------------------------------------------------------- //
    
    // MANAGE USERS
    private static void manageUsers() {
        while (true) {
            System.out.println("====== Manage Users ======");
            System.out.println("1. Create a new User");
            System.out.println("2. Authenticate a User");
            System.out.println("3. Update a User");
            System.out.println("4. Delete a User");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    createUser();
                    break;
                case "2":
                    authenticateUser();
                    break;
                case "3":
                    updateUser();
                    break;
                case "4":
                    deleteUser();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    

    // CREATE USER
    private static void createUser() {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter middle name: ");
            String middleName = scanner.nextLine();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter preferred name: ");
            String prefName = scanner.nextLine();

            User.FullName fullName = new User.FullName();
            fullName.setFirst(firstName);
            fullName.setMiddle(middleName);
            fullName.setLast(lastName);
            fullName.setPref(prefName);

            // Create roles
            Vector<Role> roles = new Vector<>();
            boolean addingRoles = true;
            while (addingRoles) {
                System.out.print("Add a role? (yes/no): ");
                String addRoleChoice = scanner.nextLine();
                if (addRoleChoice.equalsIgnoreCase("yes")) {
                    System.out.print("Enter role name: ");
                    String roleName = scanner.nextLine();
                    System.out.print("Enter privilege level (integer): ");
                    int privilege = Integer.parseInt(scanner.nextLine());
                    Role role = new Role();
                    role.setName(roleName);
                    role.setPrivilege(privilege);
                    roles.add(role);
                } else {
                    addingRoles = false;
                }
            }

            // Create the user
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setFlag(false);
            user.setExpireTime(System.currentTimeMillis() + (60000 * 10)); // Example expiration time
            user.setName(fullName);
            user.setRoles(roles);

            userManager.addUser(user);
            System.out.println("User added successfully.");

        } catch (Exception e) {
            System.out.println("An error occurred while creating the user.");
            e.printStackTrace();
        }
    }
    	

    // AUTHENTICATE USER
    private static void authenticateUser() {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            boolean isAuthenticated = userManager.authenticateUser(username, password);
            if (isAuthenticated) {
                System.out.println("User authenticated successfully.");
            } else {
                System.out.println("Authentication failed. Invalid username or password.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred during authentication.");
            e.printStackTrace();
        }
    }
    
    // UPDATE USER
    private static void updateUser() {
        try {
            System.out.print("Enter the username of the user to update: ");
            String username = scanner.nextLine();

            // Retrieve the user
            User user = userManager.getUserByUsername(username);
            if (user == null) {
                System.out.println("User not found.");
                return;
            }

            boolean updating = true;
            while (updating) {
                System.out.println("Select the field to update:");
                System.out.println("1. Password");
                System.out.println("2. Email");
                System.out.println("3. Full Name");
                System.out.println("4. Manage Roles");
                System.out.println("5. Save and Exit");
                System.out.println("6. Cancel");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        user.setPassword(newPassword);
                        break;
                    case "2":
                        System.out.print("Enter new email: ");
                        String newEmail = scanner.nextLine();
                        user.setEmail(newEmail);
                        break;
                    case "3":
                        System.out.print("Enter new first name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Enter new middle name: ");
                        String middleName = scanner.nextLine();
                        System.out.print("Enter new last name: ");
                        String lastName = scanner.nextLine();
                        System.out.print("Enter new preferred name: ");
                        String prefName = scanner.nextLine();
                        User.FullName fullName = new User.FullName();
                        fullName.setFirst(firstName);
                        fullName.setMiddle(middleName);
                        fullName.setLast(lastName);
                        fullName.setPref(prefName);
                        user.setName(fullName);
                        break;
                    case "4":
                        manageUserRoles(user);
                        break;
                    case "5":
                        // Save changes
                        userManager.updateUser(user);
                        System.out.println("User updated successfully.");
                        updating = false;
                        break;
                    case "6":
                        System.out.println("Update cancelled.");
                        updating = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the user.");
            e.printStackTrace();
        }
    }
    
    // MANAGE USER ROLES
    private static void manageUserRoles(User user) {
        boolean managingRoles = true;
        while (managingRoles) {
            System.out.println("Current roles:");
            for (User.Role role : user.getRoles()) {
                System.out.println("- " + role.getName() + " (Privilege: " + role.getPrivilege() + ")");
            }
            System.out.println("Select an option:");
            System.out.println("1. Add Role");
            System.out.println("2. Remove Role");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter role name: ");
                    String roleName = scanner.nextLine();
                    System.out.print("Enter privilege level (integer): ");
                    try {
                        int privilege = Integer.parseInt(scanner.nextLine());
                        User.Role newRole = new User.Role();
                        newRole.setName(roleName);
                        newRole.setPrivilege(privilege);
                        user.getRoles().add(newRole);
                        System.out.println("Role added.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid privilege level.");
                    }
                    break;
                case "2":
                    System.out.print("Enter role name to remove: ");
                    String removeRoleName = scanner.nextLine();
                    boolean removed = user.getRoles().removeIf(role -> role.getName().equalsIgnoreCase(removeRoleName));
                    if (removed) {
                        System.out.println("Role removed.");
                    } else {
                        System.out.println("Role not found.");
                    }
                    break;
                case "3":
                    managingRoles = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    	

    // DELETE USER
    private static void deleteUser() {
        try {
            System.out.print("Enter the username of the user to delete: ");
            String username = scanner.nextLine();

            // Confirm deletion
            System.out.print("Are you sure you want to delete user '" + username + "'? (yes/no): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                userManager.deleteUser(username);
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the user.");
            e.printStackTrace();
        }
    }
	    
    
    
    // ------------------------------------------------------------------------------- //

    
    // MANAGE ARTICLES
    
    private static void manageArticles() {
        while (true) {
            System.out.println("====== Manage Articles ======");
            System.out.println("1. Create a new Article");
            System.out.println("2. View an Article");
            System.out.println("3. Update an Article");
            System.out.println("4. Delete an Article");
            System.out.println("5. List all Articles");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    createArticle();
                    break;
                case "2":
                    viewArticle();
                    break;
                case "3":
                    updateArticle();
                    break;
                case "4":
                    deleteArticle();
                    break;
                case "5":
                    listArticles();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    	
    // CREATE ARTICLE
    private static void createArticle() {
        try {
            System.out.print("Enter article title: ");
            String title = scanner.nextLine();

            System.out.print("Enter short description: ");
            String shortDescription = scanner.nextLine();

            System.out.print("Enter difficulty (beginner, intermediate, advanced, expert): ");
            String difficulty = scanner.nextLine();

            System.out.print("Enter article body: ");
            String body = scanner.nextLine();

            System.out.print("Enter keywords (comma-separated): ");
            String keywordsInput = scanner.nextLine();
            List<String> keywords = Arrays.asList(keywordsInput.split(",\\s*"));

            System.out.print("Enter reference links (comma-separated): ");
            String refsInput = scanner.nextLine();
            List<String> referenceLinks = Arrays.asList(refsInput.split(",\\s*"));

            System.out.print("Enter related article IDs (comma-separated): ");
            String relatedIdsInput = scanner.nextLine();
            List<Long> relatedArticleIDs = new ArrayList<>();
            for (String idStr : relatedIdsInput.split(",\\s*")) {
                try {
                    relatedArticleIDs.add(Long.parseLong(idStr));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid article ID: " + idStr);
                }
            }

            Article article = new Article();
            article.setUniqueID(System.currentTimeMillis());
            article.setTitle(title);
            article.setShortDescription(shortDescription);
            article.setDifficulty(difficulty);
            article.setBody(body);
            article.setKeywords(keywords);
            article.setReferenceLinks(referenceLinks);
            article.setRelatedArticleIDs(relatedArticleIDs);

            articleManager.addArticle(article);
            System.out.println("Article added successfully.");

        } catch (Exception e) {
            System.out.println("An error occurred while creating the article.");
            e.printStackTrace();
        }
    }
    
    // VIEW ARTICLE
    private static void viewArticle() {
        try {
            System.out.print("Enter the article ID to view: ");
            long articleId = Long.parseLong(scanner.nextLine());

            // Retrieve the article
            Article article = articleManager.getArticleByID(articleId);
            if (article == null) {
                System.out.println("Article not found.");
                return;
            }

            // Display the article details
            displayArticle(article);

        } catch (NumberFormatException e) {
            System.out.println("Invalid article ID. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An error occurred while viewing the article.");
            e.printStackTrace();
        }
    }
    
   

    // DISPLAY ARTICLE DETAILS
    private static void displayArticle(Article article) {
        System.out.println("====== Article Details ======");
        System.out.println("Article ID: " + article.getArticleID());
        System.out.println("Unique ID: " + article.getUniqueID());
        System.out.println("Title: " + article.getTitle());
        System.out.println("Short Description: " + article.getShortDescription());
        System.out.println("Difficulty: " + article.getDifficulty());
        System.out.println("Body: " + article.getBody());
        System.out.println("Keywords: " + String.join(", ", article.getKeywords()));
        System.out.println("Reference Links: " + String.join(", ", article.getReferenceLinks()));
        System.out.println("Related Articles IDs: " + article.getRelatedArticleIDs());
    }
    
    // UPDATE ARTICLE
    private static void updateArticle() {
        try {
            System.out.print("Enter the article ID to update: ");
            long articleId = Long.parseLong(scanner.nextLine());

            // Retrieve the article
            Article article = articleManager.getArticleByID(articleId);
            if (article == null) {
                System.out.println("Article not found.");
                return;
            }

            boolean updating = true;
            while (updating) {
                System.out.println("Select the field to update:");
                System.out.println("1. Title");
                System.out.println("2. Short Description");
                System.out.println("3. Difficulty");
                System.out.println("4. Body");
                System.out.println("5. Keywords");
                System.out.println("6. Reference Links");
                System.out.println("7. Related Article IDs");
                System.out.println("8. Save and Exit");
                System.out.println("9. Cancel");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.print("Enter new title: ");
                        String newTitle = scanner.nextLine();
                        article.setTitle(newTitle);
                        break;
                    case "2":
                        System.out.print("Enter new short description: ");
                        String newShortDesc = scanner.nextLine();
                        article.setShortDescription(newShortDesc);
                        break;
                    case "3":
                        System.out.print("Enter new difficulty (beginner, intermediate, advanced, expert): ");
                        String newDifficulty = scanner.nextLine();
                        article.setDifficulty(newDifficulty);
                        break;
                    case "4":
                        System.out.print("Enter new body: ");
                        String newBody = scanner.nextLine();
                        article.setBody(newBody);
                        break;
                    case "5":
                        System.out.print("Enter new keywords (comma-separated): ");
                        String keywordsInput = scanner.nextLine();
                        List<String> newKeywords = Arrays.asList(keywordsInput.split(",\\s*"));
                        article.setKeywords(newKeywords);
                        break;
                    case "6":
                        System.out.print("Enter new reference links (comma-separated): ");
                        String refsInput = scanner.nextLine();
                        List<String> newRefs = Arrays.asList(refsInput.split(",\\s*"));
                        article.setReferenceLinks(newRefs);
                        break;
                    case "7":
                        System.out.print("Enter new related article IDs (comma-separated): ");
                        String relatedIdsInput = scanner.nextLine();
                        List<Long> newRelatedIds = new ArrayList<>();
                        for (String idStr : relatedIdsInput.split(",\\s*")) {
                            try {
                                newRelatedIds.add(Long.parseLong(idStr));
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid article ID: " + idStr);
                            }
                        }
                        article.setRelatedArticleIDs(newRelatedIds);
                        break;
                    case "8":
                        // Save changes
                        articleManager.updateArticle(article);
                        System.out.println("Article updated successfully.");
                        updating = false;
                        break;
                    case "9":
                        System.out.println("Update cancelled.");
                        updating = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid article ID. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An error occurred while updating the article.");
            e.printStackTrace();
        }
    }
    	
    
    // DELETE ARTICLE
    private static void deleteArticle() {
        try {
            System.out.print("Enter the article ID to delete: ");
            long articleId = Long.parseLong(scanner.nextLine());

            // Confirm deletion
            System.out.print("Are you sure you want to delete article ID '" + articleId + "'? (yes/no): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                articleManager.deleteArticle(articleId);
                System.out.println("Article deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid article ID. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the article.");
            e.printStackTrace();
        }
    }
    
    

    // LIST ARTICLES
    private static void listArticles() {
        try {
            List<Article> articles = articleManager.getAllArticles();
            if (articles.isEmpty()) {
                System.out.println("No articles found.");
                return;
            }
            for (Article article : articles) {
                displayArticleBrief(article);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while listing articles.");
            e.printStackTrace();
        }
    }

    
    
    // DISPLAY ARTICLE BRIEF
    private static void displayArticleBrief(Article article) {
        System.out.println("-------------------------------------------------");
        System.out.println("Article ID: " + article.getArticleID());
        System.out.println("Title: " + article.getTitle());
        System.out.println("Short Description: " + article.getShortDescription());
        System.out.println("Difficulty: " + article.getDifficulty());
    }
   
    
    
    // ------------------------------------------------------------------------------- //

    
    // BACKUP ARTICLES
    private static void backupArticles() {
        System.out.println("Backup articles functionality is not implemented yet.");
        // You can implement this method to backup articles to a file.
    }

    // RESTORE ARTICLES
    private static void restoreArticles() {
        System.out.println("Restore articles functionality is not implemented yet.");
        // You can implement this method to restore articles from a backup file.
    }

    
    // ------------------------------------------------------------------------------- //

    

}