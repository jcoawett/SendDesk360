package sendDesk360;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sendDesk360.Styles.RadialGradientBackground;


public class SignUpPage extends VBox {
	
    private SendDesk360 mainApp;

    // ENUM FOR THE STAGE IN SIGN UP
    public enum SignUpStep {
        USERNAME,
        PASSWORD,
        FIRSTNAME,
        LASTNAME,
        EMAIL,
        COMPLETED
    }

    // Class-level variables
    private User newUser;
    private SignUpStep collectionStep;

    // UI Components
    private Label heading;
    private VBox topContent;
    private VBox fieldWrapper;
    private PrimaryField textField;
    private PrimaryField passwordField;
    private PrimaryButton continueButton;
    private PrimaryButton linkToLogin;

    public SignUpPage(SendDesk360 mainApp) {
    	
    	this.mainApp = mainApp;
    	
        // Initialize the new User
    	
    	//TODO: User class
        newUser = new User();

        // Start with the USERNAME step
        collectionStep = SignUpStep.USERNAME;

        // Initialize UI Components
        initializeUI();

        // Update the view for the first time
        updateView();
    }

    // Method to initialize static UI components
    private void initializeUI() {
        // Logo and Heading
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("Assets/Send Desk Logo.png")));
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
            processInput();
        });
        linkToLogin = new PrimaryButton(PrimaryButton.ButtonVariant.TEXT_ONLY, "Back to login", event -> {
            goToLogin();
        });

        continueButton.setMaxWidth(464);
        linkToLogin.setMaxWidth(464);

        VBox buttonWrapper = new VBox(continueButton, linkToLogin);
        HBox.setHgrow(buttonWrapper, Priority.ALWAYS);
        buttonWrapper.setSpacing(16);
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setMaxWidth(464);

        // Main Layout
        this.setAlignment(Pos.CENTER);
        this.setSpacing(40);
        RadialGradientBackground.applyLayeredRadialGradient(this);
        this.getChildren().addAll(topContent, fieldWrapper, buttonWrapper);
    }

    // Method to update the view based on the current step
    private void updateView() {
        // Clear previous inputs
        fieldWrapper.getChildren().clear();

        switch (collectionStep) {
            case USERNAME:
                heading.setText("Choose a Username");
                textField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Choose a username...");
                fieldWrapper.getChildren().add(textField);
                break;

            case PASSWORD:
                heading.setText("Create a Password");
                passwordField = new PrimaryField(PrimaryField.FieldVariant.PASSWORD, "Create a password...");
                fieldWrapper.getChildren().add(passwordField);
                break;

            case FIRSTNAME:
                heading.setText("What's your first name?");
                textField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Enter your first name...");
                fieldWrapper.getChildren().add(textField);
                break;

            case LASTNAME:
                heading.setText("Enter Your last name?");
                textField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Enter your last name...");
                fieldWrapper.getChildren().add(textField);
                break;

            case EMAIL:
                heading.setText("What's your email");
                textField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Enter your email address...");
                fieldWrapper.getChildren().add(textField);
                break;

            case COMPLETED:
                heading.setText("Sign Up Complete!");
                // You can add additional UI components here
                continueButton.setText("Go to Dashboard");
                break;

            default:
                break;
        }
        
       fieldWrapper.setMaxWidth(464);
    }

    // TODO: Setters are in here
    // Method to process input and advance to the next step
    private void processInput() {
        boolean validInput = false;

        switch (collectionStep) {
            case USERNAME:
                String username = textField.getUserInput().get();
                if (!username.isEmpty()) {
                    newUser.setUsername(username); //TODO: Set username
                    validInput = true;
                } else {
                    // Show error message (implement as needed)
                    System.out.println("Username cannot be empty.");
                }
                break;

            case PASSWORD:
                String password = passwordField.getUserInput().get();
                if (!password.isEmpty()) {
                    newUser.setPassword(password); //TODO: Set password
                    validInput = true;
                } else {
                    System.out.println("Password cannot be empty.");
                }
                break;

            case FIRSTNAME:
                String firstName = textField.getUserInput().get();
                if (!firstName.isEmpty()) {
                    newUser.setFirstName(firstName); //TODO: Set first Name
                    validInput = true;
                } else {
                    System.out.println("First name cannot be empty.");
                }
                break;

            case LASTNAME:
                String lastName = textField.getUserInput().get();
                if (!lastName.isEmpty()) {
                    newUser.setLastName(lastName); //TODO: Set lastname
                    validInput = true;
                } else {
                    System.out.println("Last name cannot be empty.");
                }
                break;

            case EMAIL:
                String email = textField.getUserInput().get();
                if (!email.isEmpty() && email.contains("@")) {
                    newUser.setEmail(email); //TODO: Set email
                    validInput = true;
                } else {
                    System.out.println("Please enter a valid email address.");
                }
                break;
                
            //TODO: you can add cases here if i missed other fields just make sure you create another Instance of UI to handle it, and another Enum for the step.

            case COMPLETED:
                // Proceed to the next part of your application
                goToDashboard();
                validInput = true; // To prevent blocking
                break;

            default:
                break;
        }

        if (validInput) {
            advanceToNextStep();
        }
    }

    // Method to advance to the next sign-up step
    private void advanceToNextStep() {
        switch (collectionStep) {
            case USERNAME:
                collectionStep = SignUpStep.PASSWORD;
                break;

            case PASSWORD:
                collectionStep = SignUpStep.FIRSTNAME;
                break;

            case FIRSTNAME:
                collectionStep = SignUpStep.LASTNAME;
                break;

            case LASTNAME:
                collectionStep = SignUpStep.EMAIL;
                break;

            case EMAIL:
                collectionStep = SignUpStep.COMPLETED;
                break;

            default:
                break;
        }

        updateView();
    }

    // Event Functions
    private void goToLogin() {
        System.out.println("Navigating to Login Page");
        mainApp.showLoginPage();
    }

    private void goToDashboard() {
        System.out.println("User registration complete!");
        System.out.println("User Details: " + newUser);
    }
}