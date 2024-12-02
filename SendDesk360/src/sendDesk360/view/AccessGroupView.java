package sendDesk360.view;

import sendDesk360.SendDesk360;
import sendDesk360.model.User;
import sendDesk360.model.User.Role;
import sendDesk360.model.database.UserManager;
import sendDesk360.view.components.EditAccessGroupPanel;
import sendDesk360.view.components.EditUserAccessPanel;
import sendDesk360.view.components.NavBar;
import sendDesk360.view.components.PrimaryButton;
import sendDesk360.view.components.SmallButton;
import sendDesk360.viewModel.AccessGroupViewModel;
import sendDesk360.viewModel.ArticleViewModel;
import sendDesk360.viewModel.DashboardViewModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AccessGroupView extends VBox {

    private final AccessGroupViewModel accessGroupViewModel;
    private VBox userListContainer;
    private PrimaryButton editGroupButton;
    private PrimaryButton createGroupButton;
    private PrimaryButton dashboardButton; 

    EditAccessGroupPanel editAccessGroupPanel;
    EditUserAccessPanel editUserAccessPanel; 
    
    private User currentUser; 
    private UserManager userManager; 
    
    public AccessGroupView(SendDesk360 mainApp, AccessGroupViewModel accessGroupViewModel, DashboardViewModel dashboardViewModel) {
        this.currentUser = mainApp.getUserManager().getCurrentUser();
        this.userManager = mainApp.getUserManager();
        this.accessGroupViewModel = accessGroupViewModel;
        initializeUI(mainApp, dashboardViewModel);
    }
    


    private void initializeUI(SendDesk360 mainApp, DashboardViewModel dashboardViewModel) {
        try {
            // Initialize the NavBar
            NavBar navBar = new NavBar(mainApp, () -> mainApp.showDashboard(), () -> mainApp.showProfileView(), dashboardViewModel);
            navBar.setAlignment(Pos.TOP_LEFT);
            VBox.setVgrow(navBar, Priority.ALWAYS);
            navBar.setMaxWidth(300);
            navBar.setMaxHeight(Double.MAX_VALUE);

            // Page title
            Label pageTitle = new Label("Access Groups");
            pageTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

            HBox pageTitleContainer = new HBox(pageTitle);
            pageTitleContainer.setStyle("-fx-padding: 48px 16px 16px 16px;");
            pageTitleContainer.setSpacing(16);

            Region spacer = new Region();

            // Admin-specific buttons
            if (accessGroupViewModel.isAdmin()) {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                createGroupButton = new PrimaryButton(PrimaryButton.ButtonVariant.ACCENT, "Create Group", event -> openCreateGroupPanel(mainApp));
                editGroupButton = new PrimaryButton(PrimaryButton.ButtonVariant.TEXT_ONLY, "Add to Group", event -> openAddUserToGroupPanel(mainApp));

                pageTitleContainer.getChildren().addAll(spacer, createGroupButton, editGroupButton);
            } else {
                pageTitleContainer.getChildren().add(spacer);
            }

            // User list container
            userListContainer = new VBox();
            userListContainer.setSpacing(10);
            userListContainer.setStyle("-fx-padding: 16px;");
            refreshUserList();

            VBox pageBody = new VBox(pageTitleContainer, userListContainer);
            pageBody.setAlignment(Pos.TOP_LEFT);
            VBox.setVgrow(pageBody, Priority.ALWAYS);

            // Main layout with NavBar and page content
            HBox mainLayout = new HBox(navBar, pageBody);
            mainLayout.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(pageBody, Priority.ALWAYS);
            VBox.setVgrow(mainLayout, Priority.ALWAYS);

            // Add the main layout to the root container
            this.getChildren().add(mainLayout);
            this.setStyle("-fx-background-color: #101011;");
        } catch (Exception e) {
            e.printStackTrace();
            displayError("Failed to load Access Groups view.");
        }
    }
    


    private void openCreateGroupPanel(SendDesk360 mainApp) {
        System.out.print("openCreateGroupPanel");
        if (accessGroupViewModel.isAdmin()) {
            HBox mainPageBody = (HBox) this.getChildren().get(0); // Fixed to match the actual container type

            // Check if the edit panel is already open
            if (editAccessGroupPanel != null) {
                // Panel is already open, so close it
                mainPageBody.getChildren().remove(editAccessGroupPanel);
                editAccessGroupPanel = null; // Set to null to indicate it's closed
            } else {
                // Create and open the panel
                editAccessGroupPanel = new EditAccessGroupPanel(() -> {
                    // Define the save callback
                    String groupName = editAccessGroupPanel.getAccessGroupName();
                    boolean isUser = editAccessGroupPanel.isUserRoleSelected();
                    boolean isAdmin = editAccessGroupPanel.isAdminRoleSelected();
                    boolean isInstructor = editAccessGroupPanel.isInstructorRoleSelected();

                    List<Role> selectedRoles = new ArrayList<>();
                    if (isUser) {
                        Role role = new Role();
                        role.setName("user");
                        role.setPrivilege(0);
                        selectedRoles.add(role);
                    }
                    if (isAdmin) {
                        Role role = new Role();
                        role.setName("admin");
                        role.setPrivilege(2);
                        selectedRoles.add(role);
                    }
                    if (isInstructor) {
                        Role role = new Role();
                        role.setName("instructor");
                        role.setPrivilege(1);
                        selectedRoles.add(role);
                    }

                    // Add the access group to the ViewModel
                    for (Role role : selectedRoles) {
                        accessGroupViewModel.addAccessGroup(groupName, role);
                    }

                    // Close the panel after saving
                    mainPageBody.getChildren().remove(editAccessGroupPanel);
                    refreshUserList();
                    editAccessGroupPanel = null;
                });

                mainPageBody.getChildren().add(editAccessGroupPanel);
            }
        }
    }
         


    private void openAddUserToGroupPanel(SendDesk360 mainApp) {
    	//this is true as expected 
        if (accessGroupViewModel.isAdmin() ) {
        	 HBox mainPageBody = (HBox) this.getChildren().get(0);

            if (editUserAccessPanel != null) {
            	System.out.println("editUserAccessPanel was determined to be already open"); 
                // Close the panel if already open
                Node scrollPane = editUserAccessPanel.getParent(); // Get the ScrollPane wrapping the panel
                if (scrollPane != null) {
                    mainPageBody.getChildren().remove(scrollPane);
                }
                editUserAccessPanel = null;
            } else {
                // Get users and access groups
                List<User> allUsers = accessGroupViewModel.getAllUsers();
                List<String> allAccessGroups = accessGroupViewModel.getAllAccessGroups();

                // Create the panel
                editUserAccessPanel = new EditUserAccessPanel(allUsers, allAccessGroups, accessGroupViewModel);
                
                // Wrap the panel in a ScrollPane
                ScrollPane scrollPane = new ScrollPane(editUserAccessPanel);
                scrollPane.setFitToWidth(true); // Ensures the panel stretches to fit the width of the VBox
                scrollPane.setPrefHeight(400); // Set a reasonable height for the scroll area
                scrollPane.getStyleClass().add("scroll-pane"); // Optional: Style it with CSS


                // Define save behavior
                editUserAccessPanel.setOnSaveCallback(() -> {
                    // Remove the panel and refresh user list
                	System.out.println("on save callback called for editUserAcessPanel"); 
                    mainPageBody.getChildren().remove(scrollPane);
                    refreshUserList();
                    editUserAccessPanel = null;
                });

                mainPageBody.getChildren().add(scrollPane);
            }
        }
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

                 // Create the "Remove" button
                    SmallButton removeButton = new SmallButton("Remove", event ->{
                        // Handle removal logic
                        try {
							accessGroupViewModel.removeUserFromAccessGroup(user, group);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        refreshUserList(); // Refresh the list after removal
                    });
                    
                    //only admins and instructors of this access group can add and remove users from group 
                    if (accessGroupViewModel.isAdmin() || 
                    		(accessGroupViewModel.isUserInAccessGroup(currentUser, "\"admin-rights\"", currentUser.getRoles())) 
                    			&& accessGroupViewModel.isUserInAccessGroup(currentUser, group, currentUser.getRoles())) {
                    	
                    	 removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 5px;");

                         // Create an HBox to hold both the user label and remove button
                         HBox userContainer = new HBox(userLabel, removeButton);
                         userContainer.setSpacing(10);
                         userContainer.setStyle("-fx-alignment: center-left;");

                         userInfoContainer.getChildren().add(userContainer);
                    }
                    else {
                    	HBox userContainer = new HBox(userLabel);
                    	userContainer.setSpacing(10);
                        userContainer.setStyle("-fx-alignment: center-left;");
                    	System.out.println("User was not deemed to be an admin or an instructor with special access rights");
                    }
                }

                // Combine group title and user info
                VBox groupContainer = new VBox(groupLabel, userInfoContainer);
                groupContainer.setSpacing(5);
                groupContainer.setStyle("-fx-padding: 8px;");
                
                userListContainer.getChildren().add(groupContainer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayError("Oops! You're not supposed to be here, you don't have admin-rights");
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
