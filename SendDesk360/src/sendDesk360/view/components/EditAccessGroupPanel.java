
package sendDesk360.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sendDesk360.viewModel.AccessGroupViewModel;

public class EditAccessGroupPanel extends VBox {

    private final Runnable onSaveCallback;
    private PrimaryField inputGroupName;
    private CheckBox userCheckBox;
    private CheckBox adminCheckBox;
    private CheckBox instructorCheckBox;
    private SmallButton saveButton;

    public EditAccessGroupPanel(Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
        initializeUI();
    }

    private void initializeUI() {
        // Panel heading
        Label heading = new Label("Add Access Group");
        heading.setStyle("-fx-text-fill: #F8F8F8; -fx-font-weight: 700; -fx-font-size: 18px;");

        // Input field for access group name
        inputGroupName = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Access Group Name");
        inputGroupName.setPrefWidth(300);

        // Role checkboxes
        Label rolesLabel = new Label("Assign Roles:");
        rolesLabel.setStyle("-fx-text-fill: #F8F8F8;");

        userCheckBox = new CheckBox("User");
        adminCheckBox = new CheckBox("Admin");
        instructorCheckBox = new CheckBox("Instructor");

        VBox rolesBox = new VBox(10, userCheckBox, adminCheckBox, instructorCheckBox);
        rolesBox.setAlignment(Pos.TOP_LEFT);

        // Save button
        saveButton = new SmallButton("Save Group", event -> {
            String groupName = inputGroupName.getUserInput().get();
            boolean isUser = userCheckBox.isSelected();
            boolean isAdmin = adminCheckBox.isSelected();
            boolean isInstructor = instructorCheckBox.isSelected();

            // Save group logic
            saveAccessGroup(groupName, isUser, isAdmin, isInstructor);

            // Invoke the callback to indicate saving is complete
            onSaveCallback.run();
        });
        
        saveButton.setDisable(true); // Initially disabled

        // Add listeners to enable/disable save button based on input
        inputGroupName.getUserInput().addListener((observable, oldValue, newValue) -> {
            boolean hasInput = !newValue.isEmpty();
            saveButton.setDisable(!hasInput);
        });

        
        // Cancel button
        SmallButton cancelButton = new SmallButton("Cancel", event -> onSaveCallback.run());

        // Buttons container
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // Input layout
        VBox inputContainer = new VBox(15, inputGroupName, rolesLabel, rolesBox, buttonBox);
        inputContainer.setAlignment(Pos.TOP_LEFT);
        inputContainer.setSpacing(16);

        // Main layout
        this.getChildren().addAll(heading, inputContainer);
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_LEFT);
        this.getStyleClass().add("pannel");
        this.setPrefWidth(USE_COMPUTED_SIZE);
    }

    private void saveAccessGroup(String groupName, boolean isUser, boolean isAdmin, boolean isInstructor) {
        // Logic to save the group (e.g., call ViewModel methods or update the database)
        System.out.printf("Group '%s' saved with roles: User=%b, Admin=%b, Instructor=%b%n",
                groupName, isUser, isAdmin, isInstructor);
    }
    

    private void closePanel() {
        if (this.getParent() != null) {
            ((VBox) this.getParent()).getChildren().remove(this);
        }
    }

    // Getters
    public String getAccessGroupName() {
        return inputGroupName.getUserInput().get();
    }

    public boolean isUserRoleSelected() {
        return userCheckBox.isSelected();
    }

    public boolean isAdminRoleSelected() {
        return adminCheckBox.isSelected();
    }

    public boolean isInstructorRoleSelected() {
        return instructorCheckBox.isSelected();
    }

    // Setters
    public void setAccessGroupName(String groupName) {
        inputGroupName.setAccessibleText(groupName);;
    }

    public void setUserRoleSelected(boolean selected) {
        userCheckBox.setSelected(selected);
    }

    public void setAdminRoleSelected(boolean selected) {
        adminCheckBox.setSelected(selected);
    }

    public void setInstructorRoleSelected(boolean selected) {
        instructorCheckBox.setSelected(selected);
    }
       
}
