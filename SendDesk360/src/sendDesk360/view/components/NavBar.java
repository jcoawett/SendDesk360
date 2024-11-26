package sendDesk360.view.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import sendDesk360.SendDesk360;
import sendDesk360.viewModel.ArticleViewModel;

public class NavBar extends VBox {

    // UI Components
    private ImageView logo;
    private Label heading;
    private HBox logoWrapper;
    private NavButton accessGroupsButton;
    private NavButton helpMessagesButton;
    private NavButton manageProfileButton;
    private NavButton manageUsersButton;
    private NavButton homeButton;
    private VBox navButtonWrapper; // Wrapper for the buttons
    private ArticleViewModel viewModel;

    private Runnable onAccessGroupButtonClicked;
    

 // Constructor
    public NavBar(SendDesk360 mainApp, Runnable onHomeClicked, Runnable onProfileClicked, ArticleViewModel viewModel) {
        this.viewModel = viewModel;

        // Logo & Heading
        logo = new ImageView(new Image(getClass().getResourceAsStream("/sendDesk360/view/assets/Send Desk Logo.png")));
        logo.setFitWidth(40);
        logo.setPreserveRatio(true);

        heading = new Label("SendDesk 360");
        heading.setStyle("-fx-text-fill: #F8F8F8; -fx-font-weight: 700; -fx-font-size: 24px;");

        logoWrapper = new HBox(logo, heading);
        HBox.setHgrow(logoWrapper, Priority.ALWAYS);
        logoWrapper.setAlignment(Pos.CENTER_LEFT);
        logoWrapper.setSpacing(8);

        // Common Buttons
        homeButton = new NavButton("home", "Home", event -> onHomeClicked.run());
        manageProfileButton = new NavButton("user", "Profile", event -> onProfileClicked.run());

        // Button Wrapper
        navButtonWrapper = new VBox(homeButton, manageProfileButton);
        navButtonWrapper.setSpacing(0);
        navButtonWrapper.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(navButtonWrapper, Priority.ALWAYS);

        // Admin Role: Add Access Groups Button
        if (viewModel.isAdmin()) {
            accessGroupsButton = new NavButton("groups", "Access Groups", event -> {
                if (onAccessGroupButtonClicked != null) {
                    onAccessGroupButtonClicked.run();
                }
            });
            navButtonWrapper.getChildren().add(accessGroupsButton);
        }

        // Instructor Role: Add Manage Users Button
        if (viewModel.isInstructor()) {
            manageUsersButton = new NavButton("manageUsers", "Manage Users", event -> System.out.println("Manage Users clicked"));
            navButtonWrapper.getChildren().add(manageUsersButton);
        }

        this.getChildren().addAll(logoWrapper, navButtonWrapper);
        this.getStyleClass().add("navbar");
        this.setSpacing(24);
        this.setAlignment(Pos.TOP_LEFT);
    }

    public void setOnAccessGroupButtonClicked(Runnable handler) {
        this.onAccessGroupButtonClicked = handler;
    }

    // Access Groups Button Action
    public void onAccessGroupButtonClicked() {
        if (onAccessGroupButtonClicked != null) {
            onAccessGroupButtonClicked.run();
        }
    }


}