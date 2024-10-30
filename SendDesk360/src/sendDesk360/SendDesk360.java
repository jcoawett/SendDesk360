package sendDesk360;

import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sendDesk360.view.DashboardView;
import sendDesk360.view.LoginView;
import sendDesk360.view.TestPage;
import sendDesk360.view.SignUpView;
import sendDesk360.view.OneTimeCodeView;
import sendDesk360.viewModel.SignUpViewModel;
import sendDesk360.viewModel.LoginViewModel;
import sendDesk360.viewModel.OneTimeCodeViewModel;
import sendDesk360.model.User;
import sendDesk360.model.database.DatabaseManager;
import sendDesk360.model.database.UserManager;

public class SendDesk360 extends Application {

    private Stage primaryStage;
    private Scene scene;
    private SignUpViewModel signUpViewModel;
    private DatabaseManager dbManager;
    private UserManager userManager;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        initializeDatabase();
        initializeViewModel();
        initializeScene();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Send Desk 360");
        primaryStage.show();

        // Show the initial login page
        showLoginView();
        printAllUsers();
        
        // showTestView();
    }

    // Initialize DatabaseManager and UserManager
    private void initializeDatabase() {
        try {
            dbManager = new DatabaseManager();
            userManager = new UserManager(dbManager);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
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
        // Re-initialize the SignUpViewModel for a new user
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

    public void showTestView() {
        TestPage testView = new TestPage();
        scene.setRoot(testView);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Close database connections when the application stops
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