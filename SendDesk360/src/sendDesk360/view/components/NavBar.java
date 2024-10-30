package sendDesk360.view.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class NavBar extends VBox {
	
	// UI Components
	private ImageView logo;
	private Label heading;
	private HBox logoWrapper;
	
	public NavBar() {
		
		// LOGO & HEADING
        logo = new ImageView(new Image(getClass().getResourceAsStream("/sendDesk360/view/assets/Send Desk Logo.png")));
        logo.setFitWidth(40);
        logo.setPreserveRatio(true);
        
        heading = new Label();
        heading.setText("SendDesk 360");
        heading.setStyle(" -fx-text-fill: #F8F8F8; -fx-font-weight: 700; -fx-font-size: 24px; ");
        
        logoWrapper = new HBox(logo, heading);
        HBox.setHgrow(logoWrapper, Priority.ALWAYS);
        logoWrapper.setAlignment(Pos.CENTER_LEFT);
        logoWrapper.setSpacing(8);
        
        
        // ADD NAV BUTTON WRAPPER HERE
        	// ADD NAV BUTTONS HERE TOO

        this.getChildren().addAll(logoWrapper); 
        this.getStyleClass().add("navbar");
        this.setSpacing(24);
        this.setAlignment(Pos.TOP_CENTER);
	}
}
