package sendDesk360.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sendDesk360.model.User;
import sendDesk360.model.User.Role;
import sendDesk360.SendDesk360;
import sendDesk360.model.database.UserManager;

public class LoginViewModel {

    // Properties for data binding
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final SendDesk360 mainApp;
    private final UserManager userManager;

    // Error message property
    private final StringProperty loginError = new SimpleStringProperty("");

    public LoginViewModel(SendDesk360 mainApp, UserManager userManager) {
        this.mainApp = mainApp;
        this.userManager = userManager;
    }

    // Getters and Setters for the properties
    public StringProperty usernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
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

    // Error property getter
    public StringProperty loginErrorProperty() {
        return loginError;
    }

    // Method to handle login logic
    public boolean login() {
        loginError.set("");

        if (getUsername().isEmpty() || getPassword().isEmpty()) {
            loginError.set("Username and password cannot be empty.");
            return false;
        }

        try {
            boolean isAuthenticated = userManager.authenticateUser(getUsername(), getPassword());
            if (isAuthenticated) {
            	System.out.println("Authenticated Username: " + getUsername() + "password: " + getPassword()); 
            }
            else {
            	System.out.println("Failed to authenticated Username: " + getUsername() + "password: " + getPassword()); 
            }
            if (isAuthenticated) {
                User authenticatedUser = userManager.getUserByUsername(getUsername());
                userManager.setCurrentUser(authenticatedUser);  // Set the current user
                mainApp.showRoleDropdownView();  // Navigate to RoleSelection
                return true;
            } else {
                loginError.set("Authentication failed. Invalid username or password.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginError.set("An error occurred during authentication.");
            return false;
        }
    }

    // Method to navigate to the sign-up page
    public void goToCreateAccount() {
        mainApp.showSignUpView();
    }
}