package sendDesk360.view.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sendDesk360.model.Group;
import sendDesk360.viewModel.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditGroupPannel extends VBox {

    private Label heading;
    private PrimaryField inputGroupName;
    private PrimaryButton saveButton;
    private SmallButton addGroupButton;
    private VBox groupListContainer;
    private final ArticleViewModel viewModel;
    private final Runnable onSaveCallback;

    private List<String> newGroups = new ArrayList<>(); // Temporary list for unsaved groups

    public EditGroupPannel(ArticleViewModel viewModel, Runnable onSaveCallback) {
        this.viewModel = viewModel;
        this.onSaveCallback = onSaveCallback;
        initializeUI();
    }

    private void initializeUI() {
        // Panel heading
        heading = new Label("Manage Groups");
        heading.setStyle("-fx-text-fill: #F8F8F8; -fx-font-weight: 700; -fx-font-size: 18px;");

        // Input field for group name
        inputGroupName = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "New Group Name");
        inputGroupName.setPrefWidth(300);

        // Add group button
        addGroupButton = new SmallButton("Add Group", event -> addGroup());
        addGroupButton.setDisable(true); // Initially disabled

        // Save button for batch saving
        saveButton = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Save Groups", event -> saveGroups());
        saveButton.setDisable(true); // Initially disabled

        // Input and button container
        VBox inputContainer = new VBox(8, inputGroupName, addGroupButton);
        inputContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(inputContainer, Priority.ALWAYS);

        // Add listeners to inputGroupName for enabling/disabling the add button
        inputGroupName.getUserInput().addListener((observable, oldValue, newValue) -> updateAddButtonState());

        // Container for displaying existing and new groups
        groupListContainer = new VBox();
        groupListContainer.setSpacing(8);
        refreshGroupList();

        // Wrap save button in an HBox
        HBox saveButtonContainer = new HBox();
        saveButtonContainer.getChildren().add(saveButton);
        saveButton.setMaxWidth(Double.MAX_VALUE); // Allow button to grow
        HBox.setHgrow(saveButton, Priority.ALWAYS);
        saveButtonContainer.setAlignment(Pos.CENTER);

        // Add components to the panel
        this.getChildren().addAll(heading, inputContainer, groupListContainer, saveButtonContainer);
        this.setSpacing(16);
        this.setAlignment(Pos.TOP_LEFT);
        this.setPrefWidth(USE_COMPUTED_SIZE);;
        this.getStyleClass().add("pannel");
    }

    // Adds a group to the temporary list and refreshes the display
    private void addGroup() {
        String groupName = inputGroupName.getUserInput().get();
        if (!groupName.isEmpty() && !newGroups.contains(groupName)) {
            newGroups.add(groupName); // Add group to session list
            refreshGroupList(); // Refresh group list display
            inputGroupName.getUserInput().set(""); // Clear input field
        }
        updateSaveButtonState();
    }

    // Save all new groups and invoke the save callback
    private void saveGroups() {
        try {
            for (String groupName : newGroups) {
                viewModel.addNewGroup(groupName); // Add group via ViewModel
            }
            newGroups.clear(); // Clear the session list
            refreshGroupList(); // Refresh the display
            onSaveCallback.run(); // Invoke the callback
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    // Refresh the display of groups
    private void refreshGroupList() {
        groupListContainer.getChildren().clear();

        // Display existing groups
        List<Group> existingGroups = viewModel.getAvailableGroups();
        for (Group group : existingGroups) {
            Label groupLabel = new Label(group.getName());
            groupLabel.setStyle("-fx-text-fill: #F8F8F8;");
            groupListContainer.getChildren().add(groupLabel);
        }

        // Display new groups
        for (String newGroup : newGroups) {
            Label groupLabel = new Label(newGroup + " (Unsaved)");
            groupLabel.setStyle("-fx-text-fill: #4A6EF2;");
            groupListContainer.getChildren().add(groupLabel);
        }
    }

    // Update the state of the Add Group button
    private void updateAddButtonState() {
        boolean hasInput = !inputGroupName.getUserInput().get().isEmpty();
        addGroupButton.setDisable(!hasInput);
    }

    // Update the state of the Save button
    private void updateSaveButtonState() {
        boolean hasNewGroups = !newGroups.isEmpty();
        saveButton.setDisable(!hasNewGroups);
        saveButton.setVariant(hasNewGroups ? PrimaryButton.ButtonVariant.ACCENT : PrimaryButton.ButtonVariant.FILLED);
    }
}