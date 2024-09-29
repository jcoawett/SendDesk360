package sendDesk360;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;

public class SendDesk360 extends Application {

    @Override
    public void start(Stage stage) {

        EventHandler<ActionEvent> customHandler = event -> {
            System.out.println("Button clicked!");
        };
    	
    	PrimaryButton button = new PrimaryButton("label test", PrimaryButton.ButtonVariant.FILLED, customHandler);
    	PrimaryButton seconadryButton = new PrimaryButton("label test", PrimaryButton.ButtonVariant.ACCENT, customHandler);
    	PrimaryButton textOnly = new PrimaryButton("label test", PrimaryButton.ButtonVariant.TEXT_ONLY, customHandler);


    	

 
        // Set up a layout
    	VBox layout = new VBox(button, seconadryButton, textOnly); // textOnly is being used here
        VBox.setVgrow(button, Priority.ALWAYS);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(48);
        button.setMaxWidth(464);
        seconadryButton.setMaxWidth(464);// Let the button fill the available width

        // Create a scene
        Scene scene = new Scene(layout, 1600, 980);




        // Set the scene to the stage
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("PP poop check");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}