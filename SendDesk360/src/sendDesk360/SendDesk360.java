package sendDesk360;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class SendDesk360 extends Application {

    @Override
    public void start(Stage stage) {

        // Set up a layout
        VBox layout = new VBox();

        // Create a scene
        Scene scene = new Scene(layout, 400, 300);

        // Set the scene to the stage
        stage.setScene(scene);
        stage.setTitle("JavaFX WebView HTML Example");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}