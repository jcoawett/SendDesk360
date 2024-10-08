package sendDesk360;

import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sendDesk360.view.LoginView;
import sendDesk360.view.SignUpView;
import sendDesk360.view.OneTimeCodeView;
import sendDesk360.viewModel.SignUpViewModel;
import sendDesk360.viewModel.OneTimeCodeViewModel;
import sendDesk360.model.User;

public class SendDesk360 extends Application {

    private Stage primaryStage;
    private Scene scene;
    private SignUpViewModel signUpViewModel;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        initializeViewModel();
        initializeScene();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Send Desk 360");
        primaryStage.show();

        // Show the initial login page
        showLoginView();
    }

    // Initialize the SignUpViewModel with a new User
    private void initializeViewModel() {
        this.signUpViewModel = new SignUpViewModel(this, new User());
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
        LoginView loginView = new LoginView(this);
        scene.setRoot(loginView);
    }

    public void showSignUpView() {
        SignUpView signUpView = new SignUpView(this, signUpViewModel);
        scene.setRoot(signUpView);
    }

    public void showOneTimeCodeView(String userEmail, String generatedCode) {
        OneTimeCodeViewModel viewModel = new OneTimeCodeViewModel(this, userEmail, generatedCode);
        OneTimeCodeView oneTimeCodeView = new OneTimeCodeView(viewModel);
        scene.setRoot(oneTimeCodeView);
    }


    public void showDashboard() {
        // Implement the logic for showing the dashboard here
    }

    public static void main(String[] args) {
        launch(args);
    }
}