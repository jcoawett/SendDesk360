package sendDesk360;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sendDesk360.Styles.RadialGradientBackground;


public class LoginPage extends VBox {
    private Stage primaryStage;

    public LoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // CONTAINER FOR LOGO + TITLE
        // ----------------------------------------------//
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("Assets/Send Desk Logo.png")));
        logo.setFitWidth(100); // Set the width of the logo
        logo.setPreserveRatio(true); // Preserve the aspect ratio of the image

        Label heading = new Label("Login to Send Desk");
        heading.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

        VBox topContent = new VBox(logo, heading);
        HBox.setHgrow(topContent, Priority.ALWAYS);
        topContent.setAlignment(Pos.CENTER);
        topContent.setSpacing(40);
        // ----------------------------------------------//

        // CONTENT WRAPPER (FIELDS AND BUTTONS)
        // ----------------------------------------------//
        PrimaryField emailField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Email");
        PrimaryField passwordField = new PrimaryField(PrimaryField.FieldVariant.PASSWORD, "Password");

        HBox.setHgrow(emailField, Priority.ALWAYS);
        HBox.setHgrow(passwordField, Priority.ALWAYS);

        // BUTTONS
        PrimaryButton loginButton = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Login", event -> handleLogin(emailField, passwordField));
        PrimaryButton createAccountButton = new PrimaryButton(PrimaryButton.ButtonVariant.TEXT_ONLY, "Create an account", event -> handleCreateAccount());

        HBox.setHgrow(loginButton, Priority.ALWAYS);
        HBox.setHgrow(createAccountButton, Priority.ALWAYS);
        
        loginButton.setMaxWidth(464);
        createAccountButton.setMaxWidth(464);

        // CONTENT WRAPPER LAYOUT (FIELDS + BUTTONS)
        VBox contentWrapper = new VBox(emailField, passwordField, loginButton, createAccountButton);
        HBox.setHgrow(contentWrapper, Priority.ALWAYS);
        contentWrapper.setSpacing(8); // 16px space between fields and buttons
        contentWrapper.setAlignment(Pos.CENTER);
        // ----------------------------------------------//
        
        // CONTAINER (TOP CONTENT + CONTENT WRAPPER)
        // ----------------------------------------------//
        VBox container = new VBox(topContent, contentWrapper);
        HBox.setHgrow(container, Priority.ALWAYS);
        container.setSpacing(40); // Spacing between different sections
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(464);
        // ----------------------------------------------//
        
        
        // MAIN TOP LEVEL OF PAGE
        // ----------------------------------------------//
        this.setAlignment(Pos.CENTER);
        RadialGradientBackground.applyLayeredRadialGradient(this);
        this.getChildren().addAll(container);
        // ----------------------------------------------//
    }
    

    // HANDLER METHODS
    private void handleLogin(PrimaryField emailField, PrimaryField passwordField) {
        String email = emailField.getUserInput().get();
        String password = passwordField.getUserInput().get();

        if (email.isEmpty()) {
        	
            emailField.setErrorMessage("Email is required.");
            
        } else if (password.isEmpty()) {
        	
            passwordField.setErrorMessage("Password is required.");
            
        } else if (email.isEmpty() && password.isEmpty()) {
        	
            emailField.setErrorMessage("Email is required.");
            passwordField.setErrorMessage("Password is required.");

        } else {
        	
            emailField.clearError();
            passwordField.clearError();
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);
        }
    }

    private void handleCreateAccount() {
        System.out.println("Switch to create account");
    }
}