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
    	
    	PrimaryButton buttonFilled = new PrimaryButton("label test", PrimaryButton.ButtonVariant.FILLED, customHandler);
    	PrimaryButton buttonAccent = new PrimaryButton("Label Test", PrimaryButton.ButtonVariant.ACCENT, customHandler);
    	PrimaryButton buttonTextOnly = new PrimaryButton("label test", PrimaryButton.ButtonVariant.TEXT_ONLY, customHandler);

 
        // Set up a layout
    	VBox layout = new VBox(buttonFilled, buttonAccent, buttonTextOnly); 
        VBox.setVgrow(buttonFilled, Priority.ALWAYS);
        VBox.setVgrow(buttonTextOnly, Priority.ALWAYS);
        
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(16);
        
        buttonFilled.setMaxWidth(464);
        buttonAccent.setMaxWidth(464);
        buttonTextOnly.setMaxWidth(464);
        
        
        

        // Create a scene
        Scene scene = new Scene(layout, 1600, 980);

        // Set the scene to the stage
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("Send Desk");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}