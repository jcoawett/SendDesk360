package sendDesk360;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SendDesk360 extends Application {

    @Override
    public void start(Stage stage) {
    	
        // PAGES
        // ----------------------------------------------//
        LoginPage loginPage = new LoginPage(stage);
        // ----------------------------------------------//
        
        
        // SCENE
        // ----------------------------------------------//
        Scene scene = new Scene(loginPage, 1600, 980);
        
        String cssPath = getClass().getResource("Styles/styles.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        
        
        stage.setScene(scene);
        stage.setTitle("Send Desk 360");
        stage.show();
        // ----------------------------------------------//
    }

    public static void main(String[] args) {
        launch(args);
    }
}