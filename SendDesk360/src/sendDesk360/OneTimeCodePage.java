package sendDesk360;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sendDesk360.Styles.RadialGradientBackground;


// TODO:
// Make a clear button that clears the code
// Correct keystrokes that hop to the next field as soon as the digit is entered in one
// - Same with deleting
// Correct Error Handling
// - All or nothing fields. if one field is wrong all should have an error state

public class OneTimeCodePage extends VBox {
    private Stage currentView;
	
	public OneTimeCodePage(Stage primaryStage) {
        this.currentView = primaryStage;
        
        // WRAPPER FOR LOGO + TEXT WRAPPER
        // ----------------------------------------------//
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("Assets/Send Desk Logo.png")));
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);
        // ----------------------------------------------//

        // TEXT WRAPPER
        // ----------------------------------------------//
        Label heading = new Label("New Here?");
        heading.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");
        
        Label subtitle = new Label("Enter your one time code.");
        subtitle.setStyle("-fx-font-size: 16px; -fx-font-weight: 500; -fx-text-fill: #969799;");
        
        VBox textWrapper = new VBox(heading, subtitle);
        HBox.setHgrow(textWrapper, Priority.ALWAYS);
        textWrapper.setAlignment(Pos.CENTER);
        textWrapper.setSpacing(8);
        // ----------------------------------------------//

        VBox topContent = new VBox(logo, textWrapper);
        HBox.setHgrow(topContent, Priority.ALWAYS);
        topContent.setAlignment(Pos.CENTER);
        topContent.setSpacing(40);
        // ----------------------------------------------//
        
        
        // DIGIT FIELDS
        // ----------------------------------------------//
        HBox digitFieldContainer = new HBox(8);
        HBox.setHgrow(digitFieldContainer, Priority.ALWAYS);
        digitFieldContainer.setAlignment(Pos.CENTER);
        digitFieldContainer.setMaxWidth(464);

        // Create 6 digit fields
        PrimaryField[] digitFields = new PrimaryField[6];
        for (int i = 0; i < 6; i++) {
            digitFields[i] = new PrimaryField(PrimaryField.FieldVariant.NUMBER_ONLY, "-");
            digitFields[i].setMaxWidth(40);
            digitFields[i].setMaxHeight(56); 
            digitFieldContainer.getChildren().add(digitFields[i]); 
        }
        // ----------------------------------------------//

        // BUTTON WRAPPER
        // ----------------------------------------------//
        // Button to trigger the check/combine action
        PrimaryButton checkButton = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Continue", event -> { handleCombineInput(digitFields);  });
        PrimaryButton linkToLogin = new PrimaryButton(PrimaryButton.ButtonVariant.TEXT_ONLY, "Back to login", event -> { goToLogin(); });

        checkButton.setMaxWidth(464);
        linkToLogin.setMaxWidth(464);
        
        VBox buttonWrapper = new VBox(checkButton, linkToLogin);
        HBox.setHgrow(buttonWrapper, Priority.ALWAYS);
        buttonWrapper.setSpacing(16);
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setMaxWidth(464);
        
        

        this.setAlignment(Pos.CENTER); 
        this.setSpacing(40);
        RadialGradientBackground.applyLayeredRadialGradient(this);
        this.getChildren().addAll(topContent, digitFieldContainer, buttonWrapper);
        
        
	}
	
	
    private void handleCombineInput(PrimaryField[] digitFields) {
        String combinedInput = combineInput(digitFields);
        addDigitFieldKeyListeners(digitFields);

        if (!combinedInput.isEmpty()) {
            System.out.println("Combined Input: " + combinedInput);
            // Handle the valid combined input here (e.g., send it for validation)
        } else {
            System.out.println("Input invalid, show error states.");
        }
    }

    // Combine input from digit fields and show error if any field is empty
    private String combineInput(PrimaryField[] digitFields) {
        StringBuilder userInput = new StringBuilder();
        boolean allFieldsFilled = true;

        for (PrimaryField field : digitFields) {
            String input = field.getUserInput().get();

            // Check if the field is empty
            if (input.isEmpty()) {
                allFieldsFilled = false;
                field.setErrorMessage("");
            } else {
                userInput.append(input);
                field.clearError();
            }
        }

        return allFieldsFilled ? userInput.toString() : "";
    }

    // Add key listeners for digit fields to handle auto-move between fields
    private void addDigitFieldKeyListeners(PrimaryField[] digitFields) {
        for (int i = 0; i < digitFields.length; i++) {
            final int currentIndex = i;

            // Move forward when a digit is typed
            digitFields[i].getFieldComponent().addEventFilter(KeyEvent.KEY_TYPED, event -> {
                // Allow only digits and make sure only one digit can be typed in each field
                if (event.getCharacter().matches("[0-9]")) {
                    digitFields[currentIndex].getUserInput().set(event.getCharacter());  // Set the digit in the field
                    event.consume();  // Consume the event to prevent default behavior

                    // Move to the next field
                    if (currentIndex + 1 < digitFields.length) {
                        digitFields[currentIndex + 1].getFieldComponent().requestFocus();
                    }
                } else {
                    event.consume();  // Prevent non-digit characters from being entered
                }
            });

            // Move backward when backspace is pressed
            digitFields[i].getFieldComponent().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.BACK_SPACE) {
                    // Clear the current field
                    digitFields[currentIndex].getUserInput().set("");

                    // Move to the previous field if this is not the first one
                    if (currentIndex - 1 >= 0) {
                        digitFields[currentIndex - 1].getFieldComponent().requestFocus();
                    }
                }
            });
        }
    }

    // Navigate to login page
    private void goToLogin() {
        System.out.println("Navigating to Login Page");
        // Here you can add the logic to switch views/pages
    }
    
}
