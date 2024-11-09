package sendDesk360;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// VIEWS
import sendDesk360.view.DashboardView;
import sendDesk360.view.LoginView;
import sendDesk360.view.TestPage;
import sendDesk360.view.SignUpView;
import sendDesk360.view.OneTimeCodeView;
import sendDesk360.view.ArticleDetailView;
import sendDesk360.view.RoleDropdownView; 

// VIEW MODELS
import sendDesk360.viewModel.SignUpViewModel;
import sendDesk360.viewModel.LoginViewModel;
import sendDesk360.viewModel.OneTimeCodeViewModel;
import sendDesk360.viewModel.ArticleViewModel;
import sendDesk360.viewModel.RoleDropdownViewModel; 


// MODELS
import sendDesk360.model.Article;
import sendDesk360.model.User;
import sendDesk360.model.database.ArticleManager;
import sendDesk360.model.database.DatabaseManager;
import sendDesk360.model.database.UserManager;

public class SendDesk360 extends Application {

    private Stage primaryStage;
    private Scene scene;
    private SignUpViewModel signUpViewModel;
    private DatabaseManager dbManager;  // DatabaseManager instance
    private UserManager userManager;
    private ArticleManager articleManager;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        
        // Ensure the database is initialized before anything else
        initializeDatabase();
        initializeViewModel();
        initializeScene();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Send Desk 360");
        primaryStage.show();

        showLoginView();  // Start with login view
    }

    // Initialize DatabaseManager and UserManager

	private void initializeDatabase() {
	    try {
	        dbManager = new DatabaseManager();  // Initialize DatabaseManager
	        userManager = new UserManager(dbManager);  // Initialize UserManager
	        articleManager = new ArticleManager(dbManager);  // Initialize ArticleManager
	
	        // Debugging: Ensure ArticleManager is initialized
	        System.out.println("ArticleManager initialized: " + (articleManager != null));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	

	
	// DATA MANAGERS
	
	// Getter for ArticleManager
	public ArticleManager getArticleManager() {
	    return articleManager;
	}

	// Getter for UserManager
	public UserManager getUserManager() {
	    return userManager;
	}

    // Add a getter for the DatabaseManager
    public DatabaseManager getDatabaseManager() {
        return dbManager;
    }
    
    
    

    // Initialize the SignUpViewModel with a new User and UserManager
    private void initializeViewModel() {
        this.signUpViewModel = new SignUpViewModel(this, new User(), userManager);
    }

    // Initialize the scene with an empty VBox as root
    private void initializeScene() {
        scene = new Scene(new VBox(), 1600, 980);
        applyStyles(scene);
    }

    // Apply styles to the scene
    private void applyStyles(Scene scene) {
        String cssPath = getClass().getResource("/sendDesk360/view/styles.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
    }

    // PAGE ROUTING METHODS
    public void showLoginView() {
        LoginViewModel loginViewModel = new LoginViewModel(this, userManager);
        LoginView loginView = new LoginView(loginViewModel);
        scene.setRoot(loginView);
    }

    public void showSignUpView() {
        this.signUpViewModel = new SignUpViewModel(this, new User(), userManager);
        SignUpView signUpView = new SignUpView(signUpViewModel);
        scene.setRoot(signUpView);
    }

    public void showOneTimeCodeView(String generatedCode) {
        OneTimeCodeViewModel viewModel = new OneTimeCodeViewModel(this, generatedCode);
        OneTimeCodeView oneTimeCodeView = new OneTimeCodeView(viewModel);
        scene.setRoot(oneTimeCodeView);
    }

    public void showDashboard() {
        DashboardView dashboardView = new DashboardView(this);
        scene.setRoot(dashboardView);
    }
    
    public void showRoleDropdownView() {
    	RoleDropdownViewModel roleDropdownViewModel = new RoleDropdownViewModel(this, new User(), userManager); 
    	RoleDropdownView roleDropdownView = new RoleDropdownView(roleDropdownViewModel);
    	scene.setRoot(roleDropdownView);
    }

    public void showTestView() {
        TestPage testView = new TestPage();
        scene.setRoot(testView);
    }
    
    public void showArticleDetailView(Article article) {
        User currentUser = userManager.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Error: No current user found. Redirecting to login.");
            showLoginView();
            return;
        }

        // Create the ArticleViewModel, load the article, and set up the view
        ArticleViewModel viewModel = new ArticleViewModel(articleManager, currentUser);
        viewModel.loadArticle(article);  // Load article details here
        ArticleDetailView articleDetailView = new ArticleDetailView(viewModel, this);

        // Set the article detail view as the root of the existing scene
        scene.setRoot(articleDetailView);
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (dbManager != null) {
            dbManager.close();
            System.out.println("Database connection closed.");
        }
    }

    // Method to print all users (for testing purposes)
    public void printAllUsers() {
        try {
            userManager.printAllUsersTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}