package sendDesk360.view.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import sendDesk360.model.Group;
import sendDesk360.viewModel.ArticleViewModel;


import java.util.List;

public class EditGroupPannel extends VBox {

    private Label heading;
    private PrimaryField inputGroupName;
    private Button addGroupButton;
    private PrimaryButton saveButton; 
    private VBox groupListContainer;
    private final ArticleViewModel viewModel;
    private final Runnable onSaveCallback;

    public EditGroupPannel(ArticleViewModel viewModel, Runnable onSaveCallback) {
        this.viewModel = viewModel;
        this.onSaveCallback = onSaveCallback;
        initializeUI();
    }

    private void initializeUI() {
    	// Panel heading
        heading = new Label("Manage Groups");
        heading.setStyle("-fx-text-fill: #F8F8F8; -fx-font-weight: 700; -fx-font-size: 18px;");

        // Input field for group name (styled as PrimaryField)
        inputGroupName = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "New Group Name");
        inputGroupName.setPrefWidth(300);

        // Add group button and save button 
        addGroupButton = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Add Group", event -> addGroup());
        saveButton = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Save", event -> {
            onSaveCallback.run();  // Invoke the callback to handle exit
        });
        
        
        // Layout for input and button
        HBox inputContainer = new HBox(10, inputGroupName, addGroupButton);
        inputContainer.setAlignment(Pos.CENTER_LEFT);

        // Container to display existing groups
        groupListContainer = new VBox();
        groupListContainer.setSpacing(8);  // Larger spacing for readability
        refreshGroupList();

        // Add all components to the panel
        this.getChildren().addAll(heading, inputContainer, groupListContainer, saveButton);
        this.setSpacing(20);  // Increased spacing between sections
        this.setAlignment(Pos.TOP_LEFT);
        this.setPrefWidth(400);  // Set a fixed width for consistency
        this.getStyleClass().add("edit-group-pannel");
    }

    // Adds a new group to the ViewModel and refreshes the display
    private void addGroup() {
        String groupName = inputGroupName.getUserInput().get();
        System.out.print("Adding groupName: " + groupName);
        if (!groupName.isEmpty()) {
            viewModel.addNewGroup(groupName);  // Add group via ViewModel; 
            refreshGroupList();  // Refresh group list display
        }
    }

    private void refreshGroupList() {
        groupListContainer.getChildren().clear();
        List<Group> groups = viewModel.getAvailableGroups();  // Fetch groups from ViewModel
        for (Group group : groups) {
            Label groupLabel = new Label(group.getName().toString());
            groupLabel.setStyle("-fx-text-fill: #F8F8F8;");
            groupListContainer.getChildren().add(groupLabel);
        }
    }
}