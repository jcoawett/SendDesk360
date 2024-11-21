package sendDesk360.view.components;

import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sendDesk360.model.User;
import sendDesk360.viewModel.AccessGroupViewModel;

public class EditUserAccessPanel extends VBox {
    private final VBox userListContainer = new VBox();
    private Runnable onSaveCallback;

    public EditUserAccessPanel(List<User> users, List<String> accessGroups, AccessGroupViewModel viewModel) {
        setStyle("-fx-background-color: #2E2E2E; -fx-padding: 15px; -fx-spacing: 10px; -fx-border-color: #3A3A3A; -fx-border-width: 2px;");
        setPrefWidth(400);

        // Title
        Label title = new Label("Edit User Access");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: #F8F8F8; -fx-font-weight: bold;");
        getChildren().add(title);

        // Add user list container
        userListContainer.setSpacing(10);
        getChildren().add(userListContainer);

        // Populate the user list with access group checkboxes
        for (User user : users) {
            Label userLabel = new Label(String.format("%s %s (%s)", 
                user.getName().getFirst(), 
                user.getName().getLast(), 
                user.getUsername()
            ));
            userLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #D0D0D0;");

            VBox checkBoxContainer = new VBox();
            checkBoxContainer.setSpacing(5);

            for (String group : accessGroups) {
                CheckBox checkBox = new CheckBox(group);
                checkBox.setSelected(viewModel.isUserInAccessGroup(user, group, user.getRoles()));
                checkBox.setStyle("-fx-text-fill: #D0D0D0;");

                checkBox.setOnAction(event -> {
                    if (checkBox.isSelected()) {
                    	System.out.print("adding user to access group"); 
                        viewModel.addUserToAccessGroup(user, group);
                    } else {
                        viewModel.removeUserFromAccessGroup(user, group);
                    }
                });

                checkBoxContainer.getChildren().add(checkBox);
            }

            VBox userContainer = new VBox(userLabel, checkBoxContainer);
            userContainer.setSpacing(5);
            userContainer.setStyle("-fx-padding: 10px; -fx-background-color: #1E1E1F; -fx-border-color: #3A3A3A; -fx-border-width: 1;");

            userListContainer.getChildren().add(userContainer);
        }

        // Save Button
        SmallButton saveButton = new SmallButton("Save", event -> {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
        });
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px;");
        getChildren().add(saveButton);
    }

    public void setOnSaveCallback(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
    }
}
