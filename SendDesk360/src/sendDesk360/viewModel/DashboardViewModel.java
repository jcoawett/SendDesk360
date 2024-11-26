package sendDesk360.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sendDesk360.SendDesk360;
import sendDesk360.model.Article;
import sendDesk360.model.Group;
import sendDesk360.model.User;
import sendDesk360.model.database.ArticleManager;
import sendDesk360.model.database.UserManager;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardViewModel {

    private final SendDesk360 mainApp;          // Reference to the main application
    private final ArticleManager articleManager; // ArticleManager instance
    private final UserManager userManager;       // UserManager instance

    // Properties for new article/user creation
    private StringProperty newArticleTitle = new SimpleStringProperty("");
    private StringProperty newArticleDescription = new SimpleStringProperty("");
    private StringProperty newArticleBody = new SimpleStringProperty("");
    private StringProperty newArticleDifficulty = new SimpleStringProperty("beginner");
    

    private StringProperty newUserEmail = new SimpleStringProperty("");
    private StringProperty newUserName = new SimpleStringProperty("");

    // Error messages for validation
    private StringProperty articleError = new SimpleStringProperty("");
    private StringProperty userError = new SimpleStringProperty("");

    // Constructor to receive dependencies from the main app
    public DashboardViewModel(SendDesk360 mainApp, ArticleManager articleManager, UserManager userManager) {
        this.mainApp = mainApp;
        this.articleManager = articleManager;
        this.userManager = userManager;
        
        
        if (userManager.getCurrentUser() == null) {
            System.err.println("Error: No user is logged in. Redirecting to login.");
            mainApp.showLoginView();  // Redirect to login if no user
            return;
        }

        // Debugging: Ensure articleManager is not null
        if (this.articleManager == null) {
            System.err.println("ArticleManager is NULL in DashboardViewModel!");
        }
    }
    // Fetch articles by difficulty from the database
    public List<Article> getArticlesByDifficulty(String difficulty) throws Exception {
        return articleManager.getArticlesByDifficulty(difficulty);
    }

    // Create a new article based on the current property values
    public boolean createNewArticle() {
        if (!validateArticleInput()) {
            return false;  // Return failure if input is invalid
        }

        try {
            Article newArticle = new Article();
            newArticle.setTitle(newArticleTitle.get());
            newArticle.setShortDescription(newArticleDescription.get());
            newArticle.setBody(newArticleBody.get());
            newArticle.setDifficulty(newArticleDifficulty.get());

            //Check that this article is not a duplicate article
            if (checkDuplicateArticle(newArticle)) {
            	articleManager.addArticle(newArticle); //Add article to the database 
            }
            else {
            	articleError.set("The Article you are trying to add already exists");
            	return false;
            }
            resetArticleInput();  // Clear input fields after success
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            articleError.set("Error adding article: " + e.getMessage());
            return false;
        }
    }
    
    public List<Article> getAllArticles() throws Exception {
        try {
            return articleManager.getAllArticles();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // Return an empty list in case of error
        }
    }

    // Validate article input
    private boolean validateArticleInput() {
        if (newArticleTitle.get().isEmpty() || newArticleBody.get().isEmpty()) {
            articleError.set("Title and body are required.");
            return false;
        }
        articleError.set(""); // Clear error message if validation passes
        return true;
    }

    // Reset article input fields
    private void resetArticleInput() {
        newArticleTitle.set("");
        newArticleDescription.set("");
        newArticleBody.set("");
        newArticleDifficulty.set("beginner");
    }
    

    // Create a new user with the provided name and email
    public boolean createNewUser() {
        if (!validateUserInput()) {
            return false;  // Return failure if input is invalid
        }

        try {
            User newUser = new User();
            newUser.setUsername(newUserName.get());
            newUser.setEmail(newUserEmail.get());

            userManager.addUser(newUser);  // Add user to the database
            resetUserInput();  // Clear input fields after success
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            userError.set("Error creating user: " + e.getMessage());
            return false;
        }
    }

    // Validate user input
    private boolean validateUserInput() {
        if (newUserName.get().isEmpty() || newUserEmail.get().isEmpty() || !newUserEmail.get().contains("@")) {
            userError.set("Valid username and email are required.");
            return false;
        }
        userError.set(""); // Clear error message if validation passes
        return true;
    }
    
    private boolean checkDuplicateArticle(Article newArticle) {
    	boolean result = true;
    	try {
			if (articleManager.getArticleByID(newArticle.getUniqueID()) != null){
				result = false;
			}
			if (articleManager.getArticleByTitle(newArticle.getTitle()) != null) {
				result = false; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    public List<Article> filterUnencryptedArticles(List<Article> articles) {
        User currentUser = userManager.getCurrentUser();

        if (currentUser == null) {
            System.err.println("Error: currentUser is null. Returning only unencrypted articles.");
            return articles.stream()
                    .filter(article -> !article.getEncrypted())  // Assuming 'getEncrypted' exists
                    .collect(Collectors.toList());
        }

        boolean canViewBodies = userManager.getAccessTagsForUser(currentUser).stream()
                .anyMatch(tag -> tag.equalsIgnoreCase("\"view-article-bodies\""));

        if (!canViewBodies) {
            return articles.stream()
                    .filter(article -> !article.getEncrypted())
                    .collect(Collectors.toList());
        }

        return articles;
    }


    // Reset user input fields
    private void resetUserInput() {
        newUserName.set("");
        newUserEmail.set("");
    }

    // Navigate to the login view
    public void goToLogin() {
        mainApp.showLoginView();
    }

    // Refresh the dashboard view
    public void refreshDashboard() {
        mainApp.showDashboard();
    }
    
    public void filterArticlesBasedOnGroup(List<Group> filteredGroups) {
    	
    }
    
    
    public String welcomeMessage() {
    	
    	String prefName = userManager.getCurrentUser().getName().getPref();
    	
    	String welcomeMessage = "Welcome " + prefName; 
    	
    	
    	if (prefName != null) {
    		
    		return welcomeMessage;
    		
    	} else {
    		
    		return "Dashboard";
    	}
    }
    
    public boolean isAdmin() {
        User currentUser = userManager.getCurrentUser();
        
        if(currentUser == null) {
        	System.err.println("Error: currentUser is null.");
        	return false;
        }
        
        return currentUser.getRoles().stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase("admin"));
    }

    // Properties for article input binding
    public StringProperty newArticleTitleProperty() {
        return newArticleTitle;
    }

    public StringProperty newArticleDescriptionProperty() {
        return newArticleDescription;
    }

    public StringProperty newArticleBodyProperty() {
        return newArticleBody;
    }

    public StringProperty newArticleDifficultyProperty() {
        return newArticleDifficulty;
    }

    // Properties for user input binding
    public StringProperty newUserEmailProperty() {
        return newUserEmail;
    }

    public StringProperty newUserNameProperty() {
        return newUserName;
    }

    // Properties for error messages binding
    public StringProperty articleErrorProperty() {
        return articleError;
    }

    public StringProperty userErrorProperty() {
        return userError;
    }
}