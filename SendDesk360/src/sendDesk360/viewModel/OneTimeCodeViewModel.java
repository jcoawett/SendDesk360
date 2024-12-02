package sendDesk360.viewModel;

import javafx.beans.property.*;
import sendDesk360.SendDesk360;
import sendDesk360.model.User;
import sendDesk360.model.database.UserManager;

public class OneTimeCodeViewModel {

    private final SendDesk360 mainApp;
    private final StringProperty[] digitProperties;
    private final StringProperty errorProperty;
    private String generatedCode;
    private UserManager userManager;
    private User user;
    
    public OneTimeCodeViewModel(SendDesk360 mainApp, String generatedCode) {
        this.mainApp = mainApp;
        this.generatedCode = generatedCode;
        this.userManager = mainApp.getUserManager(); // Initialize userManager
        this.user = userManager.getCurrentUser();    // Initialize user

        // Initialize properties for each digit
        digitProperties = new StringProperty[6];
        for (int i = 0; i < 6; i++) {
            digitProperties[i] = new SimpleStringProperty("");
        }

        errorProperty = new SimpleStringProperty("");
    }

    public StringProperty digitProperty(int index) {
        return digitProperties[index];
    }

    public StringProperty errorProperty() {
        return errorProperty;
    }

    // Method to combine input from digit fields
    public String getCombinedInput() {
        StringBuilder input = new StringBuilder();
        for (StringProperty digitProp : digitProperties) {
            input.append(digitProp.get());
        }
        return input.toString();
    }

    // Method to verify the OTC
    public boolean verifyCode() {
    	
    	//TODO: Implement Lillis method for OTC here 
        String inputCode = getCombinedInput();
        if (inputCode.equals(generatedCode)) {
            // Code is correct, proceed to dashboard
        	
        	
            proceedToDashboard();
            return true;
        } else {
            errorProperty.set("Invalid code. Please try again.");
            return false;
        }
    }

    // Method to navigate to the dashboard
    public void proceedToDashboard() {
        if (user == null) {
            user = userManager.getCurrentUser();
        }
        if (user == null) {
            System.err.println("Error: No user is logged in. Redirecting to login.");
            mainApp.showLoginView();
            return;
        }
        userManager.setCurrentUser(user);
        mainApp.showDashboard();
    }
    // Method to handle back to login
    public void goToLogin() {
        mainApp.showLoginView();
    }
}