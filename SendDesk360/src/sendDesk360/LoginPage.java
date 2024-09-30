package sendDesk360;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

public class LoginPage extends VBox {

    public LoginPage() {
        
        setSpacing(8);
        setAlignment(Pos.CENTER);
        
        // FIELDS
        PrimaryField emailField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Email");
        PrimaryField passwordField = new PrimaryField(PrimaryField.FieldVariant.PASSWORD, "Password");
        PrimaryField digitTest = new PrimaryField(PrimaryField.FieldVariant.NUMBER_ONLY, "0");
        
        VBox.setVgrow(emailField, Priority.ALWAYS);
        VBox.setVgrow(passwordField, Priority.ALWAYS);
        
        emailField.setMaxWidth(464);
        passwordField.setMaxWidth(464);
        digitTest.setMaxWidth(88);
        
        // OnClick Actions
        EventHandler<ActionEvent> login = event -> {
            String email = emailField.getUserInput().get();
            String password = passwordField.getUserInput().get();
            String digit = digitTest.getUserInput().get();
            if (email.isEmpty() || password.isEmpty() || digit.isEmpty()) {
                emailField.setErrorMessage("Email is required.");
                passwordField.setErrorMessage("Password is required.");
                digitTest.setErrorMessage("BOY ENTER A NUMBer");
            } else {
                emailField.clearError();
                passwordField.clearError();
                digitTest.clearError();
                System.out.println("Email: " + email);
                System.out.println("Password: " + password);
                System.out.println("Digit: " + digit);
            }
        };
        
        EventHandler<ActionEvent> switchScreen = event -> {
        	System.out.println("Switch to create account");
        };
       

        // BUTTONS
        PrimaryButton loginButton = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Login", login);
        PrimaryButton createAccountButton = new PrimaryButton(PrimaryButton.ButtonVariant.TEXT_ONLY, "createAccount", switchScreen);

        VBox.setVgrow(loginButton, Priority.ALWAYS);
        VBox.setVgrow(createAccountButton, Priority.ALWAYS);
        loginButton.setMaxWidth(464);
        createAccountButton.setMaxWidth(464);

        getChildren().addAll(emailField, passwordField, digitTest, loginButton, createAccountButton);
    }
}