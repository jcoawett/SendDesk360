package sendDesk360;

import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;


// TODO: Page routing and logic

public class SendDesk360 extends Application {

    private Stage primaryStage;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Initialize the scene with an empty root
        scene = new Scene(new VBox(), 1600, 980);
        applyStyles(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Send Desk 360");
        primaryStage.show();

        // Show the initial page
        showLoginPage();
    }
    

    // PAGE ROUTING METHODS
    private void applyStyles(Scene scene) {
        String cssPath = getClass().getResource("Styles/styles.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
    }

    public void showLoginPage() {
        LoginPage loginPage = new LoginPage(this);
        scene.setRoot(loginPage);
    }

    public void showSignUpPage() {
        SignUpPage signUpPage = new SignUpPage(this);
        scene.setRoot(signUpPage);
    }

    // Implement other navigation methods as needed

    public static void main(String[] args) {
        launch(args);
    }
}