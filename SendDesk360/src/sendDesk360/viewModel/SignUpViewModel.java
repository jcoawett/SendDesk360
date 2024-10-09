package sendDesk360.viewModel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sendDesk360.model.Admin;
import sendDesk360.model.User;
import sendDesk360.model.User.Role;
import sendDesk360.SendDesk360;
import sendDesk360.model.PasswordValidator;

public class SignUpViewModel {
    private final User user;
    private final Admin admin;
    private final SendDesk360 mainApp;
    private ObjectProperty<SignUpStep> collectionStep = new SimpleObjectProperty<>(SignUpStep.USERNAME);

    // Properties for binding with the UI
    private StringProperty username = new SimpleStringProperty("");
    private StringProperty password = new SimpleStringProperty("");
    private StringProperty confirmPassword = new SimpleStringProperty("");
    private StringProperty firstName = new SimpleStringProperty("");
    private StringProperty middleName = new SimpleStringProperty("");
    private StringProperty lastName = new SimpleStringProperty("");
    private StringProperty preferredName = new SimpleStringProperty("");
    private StringProperty email = new SimpleStringProperty("");

    // Error message properties
    private StringProperty usernameError = new SimpleStringProperty("");
    private StringProperty passwordError = new SimpleStringProperty("");
    private StringProperty confirmPasswordError = new SimpleStringProperty("");
    private StringProperty firstNameError = new SimpleStringProperty("");
    private StringProperty middleNameError = new SimpleStringProperty("");
    private StringProperty lastNameError = new SimpleStringProperty("");
    private StringProperty preferredNameError = new SimpleStringProperty("");
    private StringProperty emailError = new SimpleStringProperty("");

    // Observable list for roles
    private ObservableList<Role> roles = FXCollections.observableArrayList();

    public SignUpViewModel(SendDesk360 mainApp, User user) {
    	
    	this.admin = new Admin();
		this.mainApp = mainApp;
        this.user = user;
        roles.addAll(user.getRoles());
        
    }

    public String getUserDetails() {
        StringBuilder userDetails = new StringBuilder();
        userDetails.append("Username: ").append(user.getUsername()).append("\n")
            .append("Password: ").append(user.getPassword()).append("\n")
            .append("First Name: ").append(user.getName().getFirst()).append("\n")
            .append("Middle Name: ").append(user.getName().getMiddle()).append("\n")
            .append("Last Name: ").append(user.getName().getLast()).append("\n")
            .append("Preferred Name: ").append(user.getName().getPref()).append("\n")
            .append("Email: ").append(user.getEmail()).append("\n")
            .append("Roles: ");
        // TODO: GET ROLES
        return userDetails.toString();
    }
    
    // Enum for signup steps
    public enum SignUpStep {
        USERNAME,
        PASSWORD,
        FIRSTNAME,
        MIDDLENAME,
        LASTNAME,
        PREFNAME,
        EMAIL,
        ROLES,
        COMPLETED
    }

    // Getter and setter for collection step
    public ObjectProperty<SignUpStep> collectionStepProperty() {
        return collectionStep;
    }

    public SignUpStep getCollectionStep() {
        return collectionStep.get();
    }

    public void setCollectionStep(SignUpStep step) {
        this.collectionStep.set(step);
    }

    // Validation and data processing methods
    public boolean processInput() {
        boolean validInput = false;

        switch (getCollectionStep()) {
            case USERNAME:
                usernameError.set("");
                if (username.get() != null && !username.get().isEmpty()) {
                    user.setUsername(username.get());
                    validInput = true;
                } else {
                    usernameError.set("Username cannot be empty.");
                }
                break;

            case PASSWORD:
                passwordError.set("");
                confirmPasswordError.set("");
                if (password.get() != null && !password.get().isEmpty()) {
                    if (password.get().equals(confirmPassword.get())) {
                        String passwordValidationError = PasswordValidator.validatePassword(password.get());
                        if (passwordValidationError.isEmpty()) {
                            user.setPassword(password.get());
                            validInput = true;
                        } else {
                            passwordError.set(passwordValidationError);
                        }
                    } else {
                        confirmPasswordError.set("Passwords do not match.");
                    }
                } else {
                    passwordError.set("Password cannot be empty.");
                }
                break;

            case FIRSTNAME:
                firstNameError.set("");
                if (firstName.get() != null && !firstName.get().isEmpty()) {
                    user.getName().setFirst(firstName.get());
                    validInput = true;
                } else {
                    firstNameError.set("First name cannot be empty.");
                }
                break;

            case MIDDLENAME:
                middleNameError.set("");
                if (middleName.get() != null && !middleName.get().isEmpty()) {
                    user.getName().setMiddle(middleName.get());
                    validInput = true;
                } else {
                    middleNameError.set("Middle name cannot be empty.");
                }
                break;

            case LASTNAME:
                lastNameError.set("");
                if (lastName.get() != null && !lastName.get().isEmpty()) {
                    user.getName().setLast(lastName.get());
                    validInput = true;
                } else {
                    lastNameError.set("Last name cannot be empty.");
                }
                break;

            case PREFNAME:
                preferredNameError.set("");
                if (preferredName.get() != null && !preferredName.get().isEmpty()) {
                    user.getName().setPref(preferredName.get());
                    validInput = true;
                } else {
                    preferredNameError.set("Preferred name cannot be empty.");
                }
                break;

            case EMAIL:
                emailError.set("");
                if (email.get() != null && !email.get().isEmpty() && email.get().contains("@")) {
                    user.setEmail(email.get());
                    validInput = true;
                } else {
                    emailError.set("Please enter a valid email address.");
                }
                break;

            case COMPLETED:
                validInput = true;
                break;

            default:
                break;
        }

        if (validInput) {
            advanceToNextStep();
        }

        return validInput;
    }

    // Advances to the next step
    private void advanceToNextStep() {
        switch (getCollectionStep()) {
            case USERNAME:
                setCollectionStep(SignUpStep.PASSWORD);
                break;
            case PASSWORD:
                setCollectionStep(SignUpStep.FIRSTNAME);
                break;
            case FIRSTNAME:
                setCollectionStep(SignUpStep.MIDDLENAME);
                break;
            case MIDDLENAME:
                setCollectionStep(SignUpStep.LASTNAME);
                break;
            case LASTNAME:
                setCollectionStep(SignUpStep.PREFNAME);
                break;
            case PREFNAME:
                setCollectionStep(SignUpStep.EMAIL);
                break;
            case EMAIL:
                setCollectionStep(SignUpStep.COMPLETED);
                break;
            default:
                break;
        }
    }

    
    // NAVIGATION
    public void goToLogin() {
    	mainApp.showLoginView();
    }
    
    public void goToOTC() {
    	mainApp.showOneTimeCodeView("101010");
    }
    
    public void proceedToDashboard() {
        mainApp.showDashboard();
    }

    // Properties for binding with the UI
    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty confirmPasswordProperty() {
        return confirmPassword;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty middleNameProperty() {
        return middleName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty preferredNameProperty() {
        return preferredName;
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Error property getters
    public StringProperty usernameErrorProperty() {
        return usernameError;
    }

    public StringProperty passwordErrorProperty() {
        return passwordError;
    }

    public StringProperty confirmPasswordErrorProperty() {
        return confirmPasswordError;
    }

    public StringProperty firstNameErrorProperty() {
        return firstNameError;
    }

    public StringProperty middleNameErrorProperty() {
        return middleNameError;
    }

    public StringProperty lastNameErrorProperty() {
        return lastNameError;
    }

    public StringProperty preferredNameErrorProperty() {
        return preferredNameError;
    }

    public StringProperty emailErrorProperty() {
        return emailError;
    }
}