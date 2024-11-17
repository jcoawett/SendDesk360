package sendDesk360.view.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;



public class SmallButton extends Button {
	
	
	
	public SmallButton(String label, EventHandler<ActionEvent> actionHandler) {
		
		this.setText(label);
		this.setOnAction(actionHandler);
		
		this.getStyleClass().add("small-button");		
	}
}
