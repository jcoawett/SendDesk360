package sendDesk360.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sendDesk360.SendDesk360;

public class LoginViewModel {

    // Properties for data binding
	private final StringProperty email = new SimpleStringProperty("");
	private final StringProperty password = new SimpleStringProperty("");
    private final SendDesk360 mainApp;

    public LoginViewModel(SendDesk360 mainApp) {
        this.mainApp = mainApp;
    }

    // Getters and Setters for the properties
    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    // Method to handle login logic
    public boolean login() {
        if (getEmail().isEmpty() || getPassword().isEmpty()) {
            // Ideally, notify the view to show error (could use a callback or observer)
            return false;
        }

        // Placeholder logic for successful login
        System.out.println("Logging in with email: " + getEmail() + " and password: " + getPassword());
        // You can add logic here to check credentials

        return true;
    }

    // Method to navigate to the sign-up page
    public void goToCreateAccount() {
        mainApp.showSignUpView();
    }
}