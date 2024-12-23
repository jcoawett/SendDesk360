package sendDesk360.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import sendDesk360.view.components.PrimaryButton;
import sendDesk360.view.components.PrimaryField;
import sendDesk360.view.components.RadialGradientBackground;
import sendDesk360.viewModel.LoginViewModel;

public class LoginView extends VBox {

    private final LoginViewModel loginViewModel;

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        initializeUI();
    }

    // Method to initialize the UI components
    private void initializeUI() {
        // LOGO AND TITLE
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/sendDesk360/view/assets/Send Desk Logo.png")));
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);

        Label heading = new Label("Login to Send Desk");
        heading.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

        VBox topContent = new VBox(logo, heading);
        topContent.setAlignment(Pos.CENTER);
        topContent.setSpacing(40);

        // CONTENT WRAPPER (FIELDS AND BUTTONS)
        PrimaryField usernameField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Username");
        PrimaryField passwordField = new PrimaryField(PrimaryField.FieldVariant.PASSWORD, "Password");

        // Bind fields to ViewModel properties
        usernameField.getUserInput().bindBidirectional(loginViewModel.usernameProperty());
        passwordField.getUserInput().bindBidirectional(loginViewModel.passwordProperty());

        // Error message label
        Label errorMessageLabel = new Label();
        errorMessageLabel.setStyle("-fx-text-fill: red;");
        errorMessageLabel.textProperty().bind(loginViewModel.loginErrorProperty());

        // BUTTONS
        PrimaryButton loginButton = new PrimaryButton(
            PrimaryButton.ButtonVariant.FILLED, "Login",
            event -> handleLogin()
        );
        PrimaryButton createAccountButton = new PrimaryButton(
            PrimaryButton.ButtonVariant.TEXT_ONLY, "Create an account",
            event -> loginViewModel.goToCreateAccount()
        );
        // Layout adjustments
        loginButton.setMaxWidth(464);
        createAccountButton.setMaxWidth(464);

        VBox contentWrapper = new VBox(usernameField, passwordField, errorMessageLabel, loginButton, createAccountButton);
        contentWrapper.setSpacing(8);
        contentWrapper.setAlignment(Pos.CENTER);

        // CONTAINER (TOP CONTENT + CONTENT WRAPPER)
        VBox container = new VBox(topContent, contentWrapper);
        container.setSpacing(40);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(464);

        // MAIN TOP LEVEL OF PAGE
        this.setAlignment(Pos.CENTER);
        RadialGradientBackground.applyLayeredRadialGradient(this);
        this.getChildren().addAll(container);
    }

    private void handleLogin() {
        if (!loginViewModel.login()) {
            // Here you can handle login failure, e.g., show error messages.
            System.out.println("Login failed. Please check your credentials.");
        } else {
            System.out.println("Login successful!");
            // Logic to transition to another page or dashboard if needed.
        }
    }
}