package sendDesk360;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.animation.FadeTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.scene.paint.Color;


public class PrimaryButton extends Button {
	
	
	// Variables
    private FadeTransition backgroundFadeIn;
    private FadeTransition backgroundFadeOut;
    private Timeline textColorTransitionIn;
    private Timeline textColorTransitionOut;
	
    
    // Button Variants
	public enum ButtonVariant {
		ACCENT,		 // Accent filled button
	    FILLED,      // Default filled button
	    TEXT_ONLY,   // Text-only button
	}
	

// MAIN OBJECT
// ----------------------------------- //
	public PrimaryButton(String label, ButtonVariant variant, EventHandler<ActionEvent> actionHandler) {
		
		// Set the label
		super(label);
		setVariant(variant);
		
        // Set the event handler for button actions (e.g., click event)
        if (actionHandler != null) {
            this.setOnAction(actionHandler);
        }

        // Set up fade transition for hover effect
        setupHoverEffects(this, variant);
		
	}
// ----------------------------------- //
	
	

// VARIANTS
// ----------------------------------- //
    public void setVariant(ButtonVariant variant) {
        this.getStyleClass().add("primary-button-variant-" + variant);
    }
// ----------------------------------- //

    
    
// FUNCTIONS TO SET PROPS
// ----------------------------------- //
    
    // Set the label
    public void setLabel(String label) {
        super.setText(label); 
    }
    
    // Set event handler
    public void setOnActionHandler(EventHandler<ActionEvent> actionHandler) {
        this.setOnAction(actionHandler);
    }

 // Hover Effect
    private void setupHoverEffects(Region button, ButtonVariant variant) {

        // Text color transitions for TEXT_ONLY variant
        if (variant == ButtonVariant.TEXT_ONLY) {

            textColorTransitionIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.textFillProperty(), Color.web("#969799"))),
                new KeyFrame(Duration.millis(200), new KeyValue(this.textFillProperty(), Color.web("#F8F8F8")))
            );
            
            textColorTransitionOut = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.textFillProperty(), Color.web("#F8F8F8"))),
                new KeyFrame(Duration.millis(200), new KeyValue(this.textFillProperty(), Color.web("#969799")))
            );
            
        } else {
            // Background fade transitions for FILLED and ACCENT variants
            backgroundFadeIn = new FadeTransition(Duration.millis(200), button);
            backgroundFadeIn.setFromValue(1.0);
            backgroundFadeIn.setToValue(0.84);

            backgroundFadeOut = new FadeTransition(Duration.millis(200), button);
            backgroundFadeOut.setFromValue(0.84);
            backgroundFadeOut.setToValue(1.0);
        }

        
        // Hover event handlers with null checks
        button.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            if (backgroundFadeIn != null) {
                backgroundFadeIn.play();
            }
            if (textColorTransitionIn != null) {
                textColorTransitionIn.play();
            }
        });

        button.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            if (backgroundFadeOut != null) {
                backgroundFadeOut.play();
            }
            if (textColorTransitionOut != null) {
                textColorTransitionOut.play();
            }
        });
    }
}    
 // ----------------------------------- //
