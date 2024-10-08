package sendDesk360.view.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class NavButton extends Button {
    
    public NavButton(String iconName, String label, EventHandler<ActionEvent> actionHandler) {
        
        // Load the icon image
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/sendDesk360/view/assets/" + iconName + ".png")));
        icon.setFitWidth(22);
        icon.setPreserveRatio(true);
        
        // Set the icon and label
        this.setGraphic(icon);
        this.setText(label);
        this.setContentDisplay(ContentDisplay.LEFT);
        this.setGraphicTextGap(8);
        
        // Set the action
        this.setOnAction(actionHandler);
        HBox.setHgrow(this, Priority.ALWAYS);
        // Add the CSS style class
        this.getStyleClass().add("navbar-button");
    }
}