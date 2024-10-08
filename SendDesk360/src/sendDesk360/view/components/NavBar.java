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
	private NavButton inviteUser;
	private NavButton deleteUser;
	private NavButton resetUser;
	private NavButton addRole;
	private NavButton removeRole;
	private NavButton logOutButton;
	private VBox navButtonWrapper;
	
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
        
        
        inviteUser = new NavButton("user", "Invite user", event -> { System.out.println("CONNECT DASHBOARD VIEW MODEL: Invite User"); });
        
        deleteUser = new NavButton("user", "Delete user", event -> { System.out.println("CONNECT DASHBOARD VIEW MODEL: Delete User"); });

        resetUser = new NavButton("user", "Reset user", event -> { System.out.println("CONNECT DASHBOARD VIEW MODEL: Reset User"); });

        addRole = new NavButton("user", "Add role", event -> { System.out.println("CONNECT DASHBOARD VIEW MODEL: Add Role"); });

        removeRole = new NavButton("user", "Remove role", event -> { System.out.println("CONNECT DASHBOARD VIEW MODEL: Remove Role"); });

        logOutButton = new NavButton("user", "Logout", event -> { System.out.println("CONNECT DASHBOARD VIEW MODEL: Logout User"); });

        
        // BUTTON WRAPPER
        navButtonWrapper = new VBox(inviteUser, deleteUser, resetUser, addRole, removeRole, logOutButton);
        HBox.setHgrow(navButtonWrapper, Priority.ALWAYS);

        navButtonWrapper.setAlignment(Pos.TOP_LEFT);
        navButtonWrapper.setSpacing(0);
        
        this.getChildren().addAll(logoWrapper, navButtonWrapper);
        this.getStyleClass().add("navbar");
        this.setSpacing(24);
        this.setAlignment(Pos.TOP_CENTER);
	}
}
