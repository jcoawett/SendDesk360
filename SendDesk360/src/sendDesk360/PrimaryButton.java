package sendDesk360;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;


public class PrimaryButton extends Button {
	
	public enum ButtonVariant {
		ACCENT,		 // Accent filled button
	    FILLED,      // Default filled button
	    TEXT_ONLY,   // Text-only button
	    HOVERED,     // Hovered
	}


	public PrimaryButton(String label, ButtonVariant variant, EventHandler<ActionEvent> actionHandler) {
		
		super(label); // Set initial label
		setVariant(variant);
		
        // Set the event handler for button actions (e.g., click event)
        if (actionHandler != null) {
            this.setOnAction(actionHandler);
        }
		
	}
	

// VARIANTS
// ----------------------------------- //
    public void setVariant(ButtonVariant variant) {
        this.getStyleClass().add("primary-button-variant-" + variant);
    }
    
// ----------------------------------- //

    
    
// FUNCTIONS
// ----------------------------------- //
    public void setLabel(String label) {
        super.setText(label); // Update the button label
    }
    
    
    public void setOnActionHandler(EventHandler<ActionEvent> actionHandler) {
        this.setOnAction(actionHandler);
    }
 // ----------------------------------- //
}