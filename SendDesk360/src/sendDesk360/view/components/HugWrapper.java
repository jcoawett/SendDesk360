package sendDesk360.view.components;

import javafx.scene.layout.Pane;

public class HugWrapper extends Pane {

    // Constructor to wrap any Pane (HBox, VBox, etc.) and force it to hug its content
    public HugWrapper(Pane pane) {
        // Ensure the Pane hugs its content by setting size constraints
        pane.setMaxWidth(USE_COMPUTED_SIZE);
        pane.setMaxHeight(USE_COMPUTED_SIZE);
        pane.setPrefWidth(USE_COMPUTED_SIZE);
        pane.setPrefHeight(USE_COMPUTED_SIZE);
        pane.setMinWidth(USE_COMPUTED_SIZE);
        pane.setMinHeight(USE_COMPUTED_SIZE);

        // Add the pane to this HugWrapper
        this.getChildren().add(pane);
    }
}