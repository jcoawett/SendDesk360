package sendDesk360.viewModel;

import javafx.beans.property.*;
import sendDesk360.SendDesk360;

public class OneTimeCodeViewModel {

    private final SendDesk360 mainApp;
    private final StringProperty[] digitProperties;
    private final StringProperty errorProperty;
    private String generatedCode;
    private String userEmail;

    public OneTimeCodeViewModel(SendDesk360 mainApp, String userEmail, String generatedCode) {
        this.mainApp = mainApp;
        this.userEmail = userEmail;
        this.generatedCode = generatedCode;

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
        String inputCode = getCombinedInput();
        if (inputCode.equals(generatedCode)) {
            // Code is correct, proceed to reset password
            return true;
        } else {
            errorProperty.set("Invalid code. Please try again.");
            return false;
        }
    }

    // Method to navigate to the password reset view
    public void proceedToResetPassword() {
//        mainApp.showPasswordResetView(userEmail);
    }

    // Method to handle back to login
    public void goToLogin() {
        mainApp.showLoginView();
    }

    // Getter for userEmail
    public String getUserEmail() {
        return userEmail;
    }
}