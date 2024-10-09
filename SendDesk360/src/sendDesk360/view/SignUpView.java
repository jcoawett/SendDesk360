package sendDesk360.view;

import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sendDesk360.SendDesk360;
import sendDesk360.view.components.PrimaryButton;
import sendDesk360.view.components.PrimaryField;
import sendDesk360.view.components.RadialGradientBackground;
// VIEWMODELS
import sendDesk360.viewModel.SignUpViewModel;
import sendDesk360.viewModel.SignUpViewModel.SignUpStep;

public class SignUpView extends VBox {

    private final SignUpViewModel signUpViewModel;

    // UI Components
    private Label heading;
    private VBox topContent;
    private VBox fieldWrapper;
    private PrimaryField textField;
    private PrimaryField passwordField;
    private PrimaryField confirmPasswordField;
    private PrimaryButton continueButton;
    private PrimaryButton linkToLogin;
    private PrimaryButton linkToOTC;

    public SignUpView(SendDesk360 mainApp, SignUpViewModel viewModel) {

        this.signUpViewModel = viewModel;
        initializeUI();

        // Update the view when the collection step changes
        signUpViewModel.collectionStepProperty().addListener((obs, oldStep, newStep) -> updateView(newStep));

        // Update the view for the first time
        updateView(signUpViewModel.getCollectionStep());
    }

    // Method to initialize static UI components
    public void initializeUI() {
        // Logo and Heading
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/sendDesk360/view/assets/Send Desk Logo.png")));        
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);

        heading = new Label();
        heading.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

        topContent = new VBox(logo, heading);
        HBox.setHgrow(topContent, Priority.ALWAYS);
        topContent.setAlignment(Pos.CENTER);
        topContent.setSpacing(40);

        // Field Wrapper
        fieldWrapper = new VBox();
        fieldWrapper.setAlignment(Pos.CENTER);
        fieldWrapper.setSpacing(16);

        // Buttons
        continueButton = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Continue", event -> {
            signUpViewModel.processInput();
        });
        linkToLogin = new PrimaryButton(PrimaryButton.ButtonVariant.TEXT_ONLY, "Back to login", event -> {
            signUpViewModel.goToLogin();
        });
        linkToOTC = new PrimaryButton(
	    	PrimaryButton.ButtonVariant.TEXT_ONLY, "Use one time code", event -> {
	    	signUpViewModel.goToOTC();
	    });


        continueButton.setMaxWidth(464);
        linkToLogin.setMaxWidth(464);
        
        VBox linkWrapper = new VBox(linkToLogin, linkToOTC);
        HBox.setHgrow(linkWrapper, Priority.ALWAYS);
        linkWrapper.setSpacing(0);
        linkWrapper.setAlignment(Pos.CENTER);

        VBox buttonWrapper = new VBox(continueButton, linkWrapper);
        HBox.setHgrow(buttonWrapper, Priority.ALWAYS);
        buttonWrapper.setSpacing(16);
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setMaxWidth(464);

        // Main Layout
        this.getChildren().addAll(topContent, fieldWrapper, buttonWrapper);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(40);
        RadialGradientBackground.applyLayeredRadialGradient(this);
    }

    // Helper method to bind error messages
    private void bindErrorMessage(PrimaryField field, StringProperty errorProperty) {
        errorProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                field.setErrorMessage(newVal);
            } else {
                field.clearError();
            }
        });
    }

    // Method to update the view based on the current step
    private void updateView(SignUpStep step) {
        fieldWrapper.getChildren().clear();

        switch (step) {
            case USERNAME:
                heading.setText("Choose a Username");
                textField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Choose a username...");
                textField.getUserInput().bindBidirectional(signUpViewModel.usernameProperty());
                fieldWrapper.getChildren().add(textField);
                bindErrorMessage(textField, signUpViewModel.usernameErrorProperty());
                break;

            case PASSWORD:
                heading.setText("Create a Password");
                passwordField = new PrimaryField(PrimaryField.FieldVariant.PASSWORD, "Create a password...");
                confirmPasswordField = new PrimaryField(PrimaryField.FieldVariant.PASSWORD, "Confirm your password...");
                passwordField.getUserInput().bindBidirectional(signUpViewModel.passwordProperty());
                confirmPasswordField.getUserInput().bindBidirectional(signUpViewModel.confirmPasswordProperty());
                fieldWrapper.getChildren().addAll(passwordField, confirmPasswordField);
                bindErrorMessage(passwordField, signUpViewModel.passwordErrorProperty());
                bindErrorMessage(confirmPasswordField, signUpViewModel.confirmPasswordErrorProperty());
                break;

            case FIRSTNAME:
                heading.setText("What's your first name?");
                textField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Enter your first name...");
                textField.getUserInput().bindBidirectional(signUpViewModel.firstNameProperty());
                fieldWrapper.getChildren().add(textField);
                bindErrorMessage(textField, signUpViewModel.firstNameErrorProperty());
                break;

            case MIDDLENAME:
                heading.setText("What's your middle name?");
                textField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Enter your middle name...");
                textField.getUserInput().bindBidirectional(signUpViewModel.middleNameProperty());
                fieldWrapper.getChildren().add(textField);
                bindErrorMessage(textField, signUpViewModel.middleNameErrorProperty());
                break;

            case LASTNAME:
                heading.setText("What's your last name?");
                textField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Enter your last name...");
                textField.getUserInput().bindBidirectional(signUpViewModel.lastNameProperty());
                fieldWrapper.getChildren().add(textField);
                bindErrorMessage(textField, signUpViewModel.lastNameErrorProperty());
                break;

            case PREFNAME:
                heading.setText("What's your preferred name?");
                textField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Enter your preferred name...");
                textField.getUserInput().bindBidirectional(signUpViewModel.preferredNameProperty());
                fieldWrapper.getChildren().add(textField);
                bindErrorMessage(textField, signUpViewModel.preferredNameErrorProperty());
                break;

            case EMAIL:
                heading.setText("What's your email?");
                textField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Enter your email address...");
                textField.getUserInput().bindBidirectional(signUpViewModel.emailProperty());
                fieldWrapper.getChildren().add(textField);
                bindErrorMessage(textField, signUpViewModel.emailErrorProperty());
                break;

            case COMPLETED:
                heading.setText("Sign Up Complete!");
                continueButton.setText("Go to Dashboard");
                continueButton.setVariant(PrimaryButton.ButtonVariant.ACCENT);
                continueButton.setOnAction(event -> {
                    signUpViewModel.proceedToDashboard();
                });
                break;

            default:
                break;
        }

        fieldWrapper.setMaxWidth(464);
    }
}