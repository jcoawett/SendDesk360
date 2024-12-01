package sendDesk360.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import sendDesk360.SendDesk360;
import sendDesk360.view.components.NavBar;
import sendDesk360.view.components.PrimaryButton;
import sendDesk360.view.components.PrimaryField;
import sendDesk360.view.components.PrimaryButton.ButtonVariant;
import sendDesk360.viewModel.ArticleViewModel;
import sendDesk360.viewModel.DashboardViewModel;
import sendDesk360.viewModel.ProfileViewModel;

public class ProfileView extends VBox {

    private final ProfileViewModel profileViewModel;
    private NavBar navBar;
    private Runnable onAccessGroupButtonClicked;

    public ProfileView(SendDesk360 mainApp, ProfileViewModel profileViewModel, DashboardViewModel dashboardViewModel) {
        this.profileViewModel = profileViewModel;

        Runnable onProfileClicked = () -> mainApp.showProfileView();
        initializeUI(profileViewModel, dashboardViewModel, mainApp, onProfileClicked);
    }

    private void initializeUI(ProfileViewModel profileViewModel, DashboardViewModel dashboardViewModel, SendDesk360 mainApp, Runnable onProfileClicked) {
        // Navigation bar
        navBar = new NavBar(mainApp, () -> mainApp.showDashboard(), onProfileClicked, dashboardViewModel);
        navBar.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(navBar, Priority.ALWAYS);
        navBar.setMaxWidth(300);
        navBar.setMaxHeight(Double.MAX_VALUE);
        navBar.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(navBar, Priority.ALWAYS);
        navBar.setMaxWidth(300);
        navBar.setMaxHeight(Double.MAX_VALUE);

        // Profile Header
        Label pageTitle = new Label("Edit Profile");
        pageTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

        // Fields for editing profile details
        VBox profileFields = new VBox();
        profileFields.setSpacing(16);
        profileFields.setStyle("-fx-padding: 16px;");

        PrimaryField firstNameField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "First Name");
        firstNameField.getUserInput().bindBidirectional(profileViewModel.firstNameProperty());

        PrimaryField lastNameField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Last Name");
        lastNameField.getUserInput().bindBidirectional(profileViewModel.lastNameProperty());

        PrimaryField emailField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Email");
        emailField.getUserInput().bindBidirectional(profileViewModel.emailProperty());

        PrimaryField preferredNameField = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Preferred Name");
        preferredNameField.getUserInput().bindBidirectional(profileViewModel.preferredNameProperty());

        profileFields.getChildren().addAll(
            new Label("First Name:"), firstNameField,
            new Label("Last Name:"), lastNameField,
            new Label("Email:"), emailField,
            new Label("Preferred Name:"), preferredNameField
        );

        // Save and Logout Buttons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(16);
        buttonBox.setStyle("-fx-padding: 16px;");
        buttonBox.setAlignment(Pos.CENTER);

        PrimaryButton saveButton = new PrimaryButton(ButtonVariant.FILLED, "Save", event -> {
            try {
                profileViewModel.saveProfile(); // Save profile changes
                System.out.println("Profile saved successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error saving profile: " + e.getMessage());
            }
        });

        PrimaryButton logoutButton = new PrimaryButton(ButtonVariant.TEXT_ONLY, "Logout", event -> {
            mainApp.logout(); // Log the user out
            System.out.println("User logged out.");
        });

        buttonBox.getChildren().addAll(saveButton, logoutButton);

        // Layout for the profile page
        VBox profileContent = new VBox(pageTitle, profileFields, buttonBox);
        profileContent.setAlignment(Pos.TOP_LEFT);
        profileContent.setSpacing(32);
        profileContent.setStyle("-fx-padding: 48px 16px;");

        // Main layout
        HBox mainLayout = new HBox(navBar, profileContent);
        mainLayout.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(mainLayout, Priority.ALWAYS);
        VBox.setVgrow(mainLayout, Priority.ALWAYS);

        this.getChildren().add(mainLayout);
        this.setStyle("-fx-background-color: #101011;");
    }
}