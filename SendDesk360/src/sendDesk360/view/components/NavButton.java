package sendDesk360.view.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NavButton extends Button {

    public NavButton(String iconName, String label, EventHandler<ActionEvent> actionHandler) {
        // Load the icon image
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/sendDesk360/view/assets/" + iconName + ".png")));
        icon.setFitWidth(22); // Set icon size
        icon.setPreserveRatio(true);

        // Set the icon and label
        this.setGraphic(icon);
        this.setText(label);
        this.setContentDisplay(ContentDisplay.LEFT); // Icon on the left of the text
        this.setGraphicTextGap(8); // Space between icon and text

        // Align text and icon to the left
        this.setStyle("-fx-alignment: center-left; -fx-padding: 8 16 8 16;"); // Align left and add padding

        // Set the action
        this.setOnAction(actionHandler);

        // Ensure the button fills the available horizontal space
        this.setMaxWidth(Double.MAX_VALUE); // Allow button to stretch fully
        this.setPrefHeight(USE_COMPUTED_SIZE); // Set consistent height for all buttons

        // Add the CSS style class for additional styling
        this.getStyleClass().add("navbar-button");
    }
}