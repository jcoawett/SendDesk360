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
    private NavButton accessGroupsButton;
    private NavButton helpMessagesButton;
    private VBox navButtonWrapper; // Wrapper for the buttons
    
    private Runnable onAccessGroupButtonClicked; 
	
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
        
        
       // Create NavButton instances
        accessGroupsButton = new NavButton("user", "Access Groups", event -> {
            // Add the action handler for "Access Groups"
        	onAccessGroupButtonClicked(); 
            System.out.println("Access Groups button clicked");
        });
        
        helpMessagesButton = new NavButton("MessageIcon", "Help Messages", event -> {
            // Add the action handler for "Help Messages"
            System.out.println("Help Messages button clicked");
        });
        
        // Wrap the buttons in an HBox for proper layout
        navButtonWrapper = new VBox(accessGroupsButton, helpMessagesButton);
        navButtonWrapper.setSpacing(16); // Set spacing between buttons
        navButtonWrapper.setAlignment(Pos.CENTER_LEFT); // Align buttons to the left*/

        this.getChildren().addAll(logoWrapper, navButtonWrapper); 
        this.getStyleClass().add("navbar");
        this.setSpacing(24);
        this.setAlignment(Pos.TOP_CENTER);
       
	}
	
	public void onAccessGroupButtonClicked() {
		// This will notify the DashboardView to show the AccessGroupView
        if (onAccessGroupButtonClicked != null) {
            onAccessGroupButtonClicked.run();
        }
	}
	
	public void setOnAccessGroupButtonClicked(Runnable handler) {
        this.onAccessGroupButtonClicked = handler;
    }
	
}
