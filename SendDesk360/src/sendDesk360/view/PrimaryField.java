package sendDesk360.view;

import javafx.beans.property.StringProperty;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class PrimaryField extends VBox {

    // VARIABLES
    private Label errorLabel;
    private boolean hasText = false;
    private final StringProperty userInput;
    private TextInputControl fieldComponent;

    // VARIANTS
    public enum FieldVariant {
        DEFAULT,
        PASSWORD,  
        ERROR,
        NUMBER_ONLY,     
    }

    // INITIALIZE VARIANT
    private void initializeFieldComponent(FieldVariant variant) {
        if (variant == FieldVariant.PASSWORD) {
            fieldComponent = new PasswordField();
        } 
        
        else if (variant == FieldVariant.NUMBER_ONLY) {
            fieldComponent = new TextField();
            
            // Restrict input to one digit
            fieldComponent.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.matches("\\d?")) {  // Only allow one digit (or empty)
                    fieldComponent.setText(newValue);  // Accept the input
                } else {
                    fieldComponent.setText(oldValue);  // Revert to previous value if more than one digit
                }
            });
        }
        
        else {
            fieldComponent = new TextField();
        }
        
        fieldComponent.setMaxWidth(Double.MAX_VALUE);
        getChildren().add(fieldComponent);

        
        // Add error label right after the field with padding
        errorLabel = new Label();
        errorLabel.setVisible(false);  // Hidden by default
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setTranslateY(4); // 4px space
        getChildren().add(errorLabel);

        fieldComponent.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                hasText = true;
                fieldComponent.getStyleClass().add("edited");  
            } else {
                hasText = false;
                fieldComponent.getStyleClass().remove("edited");  
            }
            if (errorLabel.isVisible()) {
                clearError(); // Clear error when the user starts typing
            }
        });
    }

    // Set the placeholder text
    public void setPlaceholder(String placeholder) {
        fieldComponent.setPromptText(placeholder);
    }

    // Set the variant (style) for the text field
    public void setVariant(FieldVariant variant) {
        fieldComponent.getStyleClass().clear();
        switch (variant) {
            case DEFAULT:
                fieldComponent.getStyleClass().add("primary-field-variant-default");
                break;
            case PASSWORD:
                fieldComponent.getStyleClass().add("primary-field-variant-default");
                break;
            case ERROR:
                fieldComponent.getStyleClass().add("primary-field-variant-error");
                errorLabel.setVisible(true);
                break;
            case NUMBER_ONLY:
                fieldComponent.getStyleClass().add("primary-field-variant-default");
                break;
            default: 
                fieldComponent.getStyleClass().add("primary-field-variant-default");
                break;
        }
    }

    // Display error message
    public void setErrorMessage(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        setVariant(FieldVariant.ERROR);
    }

    // Clear error message
    public void clearError() {
        errorLabel.setVisible(false);
        setVariant(FieldVariant.DEFAULT);
    }

    // Method to get the text property for continuous updates
    public StringProperty getUserInput() {
        return fieldComponent.textProperty();
    }
    
    // Method to get the field component
    public TextInputControl getFieldComponent() {
        return fieldComponent;
    }
    
    
    // MAIN constructor
    public PrimaryField(FieldVariant variant, String placeholder) {
        super();
    	
    	initializeFieldComponent(variant);
    	setPlaceholder(placeholder);
    	setVariant(variant);
    	
    	userInput = fieldComponent.textProperty();
    }
}