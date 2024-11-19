package sendDesk360.view;

import sendDesk360.SendDesk360;
import sendDesk360.model.User;
import sendDesk360.model.database.UserManager;
import sendDesk360.view.components.PrimaryButton;
import sendDesk360.viewModel.AccessGroupViewModel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class AccessGroupView extends VBox {

    private final AccessGroupViewModel accessGroupViewModel;
    private VBox userListContainer;
    private PrimaryButton addGroupButton;
    private PrimaryButton removeGroupButton;
    private PrimaryButton createGroupButton;

    private User currentUser; 
    private UserManager userManager; 
    
    public AccessGroupView(SendDesk360 mainApp, AccessGroupViewModel accessGroupViewModel) {
    	this.currentUser = mainApp.getUserManager().getCurrentUser();
    	this.userManager = mainApp.getUserManager();
        this.accessGroupViewModel = accessGroupViewModel;
        initializeUI(mainApp);
    }

    private void initializeUI(SendDesk360 mainApp) {
        try {
            Label pageTitle = new Label("Access Groups");
            pageTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

            HBox pageTitleContainer = new HBox(pageTitle);
            pageTitleContainer.setStyle("-fx-padding: 48px 16px 16px 16px;");
            pageTitleContainer.setSpacing(16);

            if (accessGroupViewModel.isAdmin()) {
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                createGroupButton = new PrimaryButton(PrimaryButton.ButtonVariant.ACCENT, "Create Group", event -> openCreateGroupPanel(mainApp));
                addGroupButton = new PrimaryButton(PrimaryButton.ButtonVariant.TEXT_ONLY, "Add to Group", event -> openAddUserToGroupPanel(mainApp));
                removeGroupButton = new PrimaryButton(PrimaryButton.ButtonVariant.TEXT_ONLY, "Remove from Group", event -> openRemoveUserFromGroupPanel(mainApp));

                pageTitleContainer.getChildren().addAll(spacer, createGroupButton, addGroupButton, removeGroupButton);
            }

            userListContainer = new VBox();
            userListContainer.setSpacing(10);
            userListContainer.setStyle("-fx-padding: 16px;");
            refreshUserList();

            VBox pageBody = new VBox(pageTitleContainer, userListContainer);
            pageBody.setAlignment(Pos.TOP_LEFT);
            VBox.setVgrow(pageBody, Priority.ALWAYS);

            this.getChildren().add(pageBody);
            this.setStyle("-fx-background-color: #101011;");
        } catch (Exception e) {
            e.printStackTrace();
            displayError("Failed to load Access Groups view.");
        }
    }

    private void openCreateGroupPanel(SendDesk360 mainApp) {
        // Logic to open a panel for creating a new access group
        System.out.println("Open Create Group Panel");
        // Add your implementation here (similar to EditGroupPannel logic)
    }

    private void openAddUserToGroupPanel(SendDesk360 mainApp) {
        // Logic to open a panel for adding users to a group
        System.out.println("Open Add User to Group Panel");
        // Add your implementation here (similar to EditGroupPannel logic)
    }

    private void openRemoveUserFromGroupPanel(SendDesk360 mainApp) {
        // Logic to open a panel for removing users from a group
        System.out.println("Open Remove User from Group Panel");
        // Add your implementation here (similar to EditGroupPannel logic)
    }

    private void refreshUserList() {
        try {
            userListContainer.getChildren().clear();
            List<String> accessGroups = accessGroupViewModel.getAllAccessGroups();

            for (String group : accessGroups) {
                // Group title
                Label groupLabel = new Label(group);
                groupLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #F8F8F8;");

                // Container for the group's users
                VBox userInfoContainer = new VBox();
                userInfoContainer.setSpacing(5);
                userInfoContainer.setStyle("-fx-padding: 8px; -fx-background-color: #1E1E1F; -fx-border-color: #3A3A3A; -fx-border-width: 1;");

                // Add users to the container
                List<User> usersInGroup = accessGroupViewModel.getAllUsersInAccessGroup(group);
                for (User user : usersInGroup) {
                    // Format user info
                    String userInfo = String.format("%s %s (%s)", 
                        user.getName().getFirst(), 
                        user.getName().getLast(), 
                        user.getUsername()
                    );

                    Label userLabel = new Label(userInfo);
                    userLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #D0D0D0;");

                    userInfoContainer.getChildren().add(userLabel);
                }

                // Combine group title and user info
                VBox groupContainer = new VBox(groupLabel, userInfoContainer);
                groupContainer.setSpacing(5);
                groupContainer.setStyle("-fx-padding: 8px;");

                userListContainer.getChildren().add(groupContainer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayError("Failed to load user list.");
        }
    }


    private void displayError(String message) {
        this.getChildren().clear();

        VBox errorContainer = new VBox();
        errorContainer.setAlignment(Pos.CENTER);
        errorContainer.setStyle("-fx-background-color: #101011; -fx-padding: 50px;");

        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #F8F8F8;");
        errorContainer.getChildren().add(errorLabel);

        this.getChildren().add(errorContainer);
    }
}
