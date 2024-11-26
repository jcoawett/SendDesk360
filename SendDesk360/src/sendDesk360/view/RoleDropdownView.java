package sendDesk360.view;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sendDesk360.SendDesk360;
import sendDesk360.view.components.PrimaryButton;
import sendDesk360.view.components.RadialGradientBackground;
import sendDesk360.viewModel.RoleDropdownViewModel;
import sendDesk360.view.components.PrimaryButton.ButtonVariant;

public class RoleDropdownView extends VBox {

	private RoleDropdownViewModel roleDropdownViewModel; 
    private ComboBox<String> roleDropdown;
    private SendDesk360 mainApp;

    public RoleDropdownView(RoleDropdownViewModel roleDropdownViewModel) {
    	this.roleDropdownViewModel = roleDropdownViewModel; 
        initializeUI();
    }
    
 // UI Components
    private Label heading;
    private VBox topContent;

    private void initializeUI() {
    	
    	// Logo and Heading
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/sendDesk360/view/assets/Send Desk Logo.png")));        
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);

        heading = new Label();
        heading.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

        topContent = new VBox(logo, heading);
        HBox.setHgrow(topContent, Priority.ALWAYS);
        topContent.setAlignment(Pos.CENTER);
        topContent.setSpacing(40);
        
    	 // Apply background gradient
        RadialGradientBackground.applyLayeredRadialGradient(this);

        // Title label with matching styling
        Label titleLabel = new Label("Login as:");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

        // Create and style the dropdown menu
        roleDropdown = new ComboBox<>(roleDropdownViewModel.getRoleNames());
        roleDropdown.setPromptText("Select role");
        roleDropdown.getItems().forEach(role -> System.out.println("Dropdown Role: " + role));
        
        roleDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Selected role: " + newVal);
            roleDropdownViewModel.setSelectedRole(newVal);
        });
        
        

        // Dropdown styling for a dark theme
        roleDropdown.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-background-color: #101011;" +
            "-fx-border-color: #5E5E5E;" +
            "-fx-border-radius: 5;" +
            "-fx-prompt-text-fill: #D3D3D3;" +
            "-fx-text-fill: #F8F8F8;" +
            "-fx-padding: 8px 16px;" +
            "-fx-background-radius: 5;" +
            "-fx-pref-width: 200px;"
        );

        // Set dark theme for dropdown list items
        roleDropdown.getStylesheets().add(getClass().getResource("/sendDesk360/view/styles.css").toExternalForm());

        // Login button styled as in SignUpView
        PrimaryButton loginButton = new PrimaryButton(ButtonVariant.FILLED, "Login", event -> roleDropdownViewModel.handleLogin(roleDropdown.getValue()));
        loginButton.setMaxWidth(200);

        // Layout setup
        VBox.setVgrow(roleDropdown, Priority.ALWAYS);
        this.setSpacing(16);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-padding: 40px;");

        // Add all elements to layout
        this.getChildren().addAll(topContent, titleLabel, roleDropdown, loginButton);
    }

}
