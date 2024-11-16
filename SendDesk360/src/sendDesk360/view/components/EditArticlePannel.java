package sendDesk360.view.components;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import sendDesk360.model.Group;
import sendDesk360.viewModel.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditArticlePannel extends VBox {

    private Label heading;
    private PrimaryField inputDifficulty;
    private PrimaryField inputTitle;
    private PrimaryField inputSubtitle;
    private PrimaryField inputBody;
    private PrimaryField inputKeywords;
    private PrimaryField inputRelatedArticles;
    private PrimaryField inputLinks;
    private PrimaryButton saveButton;

    private final Runnable onSaveCallback;
    private VBox bottomContent;
    private VBox articleGroupItemContainer;
    
    private List<Group> groupsToAddArticleTo = new ArrayList<Group>();
    private List<Group> groupsToRemoveArticleFrom = new ArrayList<Group>(); 

    public EditArticlePannel(ArticleViewModel viewModel, Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
        initializeUI(viewModel);
    }

    private void initializeUI(ArticleViewModel viewModel) {
        // Heading label
        heading = new Label("Create/Edit Article");
        heading.setStyle("-fx-text-fill: #F8F8F8; -fx-font-weight: 700; -fx-font-size: 16px;");
        
        VBox fieldWrapper = new VBox();
       
        // Initialize fields with appropriate variants and placeholders
        inputTitle = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Title");
        
        DropdownField difficultyDropdown = new DropdownField("Select Difficulty...", 
                List.of("beginner", "intermediate", "advanced", "expert"));

        // Bind to ViewModel
        viewModel.difficultyProperty().addListener((obs, oldValue, newValue) -> {
            difficultyDropdown.setSelectedValue(newValue);
        });

        difficultyDropdown.selectedValueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                viewModel.difficultyProperty().set(newValue);
            }
        });

        
        inputSubtitle = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Short Description");
        inputBody = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Body");
        inputKeywords = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Keywords");
        inputRelatedArticles = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Related Articles");
        inputLinks = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Reference Links");
        
        

        // Bind fields to the view model properties
        inputTitle.getUserInput().bindBidirectional(viewModel.titleProperty());

        
        inputBody.getUserInput().bindBidirectional(viewModel.bodyProperty());
        inputKeywords.getUserInput().unbindBidirectional(viewModel.keywordsProperty());
        inputSubtitle.getUserInput().bindBidirectional(viewModel.shortDescriptionProperty()); 
        
        

        
        
        fieldWrapper.getChildren().addAll(inputTitle, difficultyDropdown, inputSubtitle, inputBody, inputKeywords, inputRelatedArticles, inputLinks);
        
        // Group Checkboxes
        bottomContent = new VBox();
        articleGroupItemContainer = new VBox();
        

        

        
        // Save button with action to update or save article
     // Save button with initial configuration
        saveButton = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Save", event -> {
            try {
                // Check if it's a new article (no article ID) or an update
                if (viewModel.articleIDProperty().get() == 0) {
                    viewModel.saveNewArticle();  // Save as a new article
                } else {
                    viewModel.updateArticle();  // Update existing article
                }

                for (Group group : groupsToAddArticleTo) {
                    viewModel.addArticleToGroup(group.getGroupID());
                }

                for (Group group : groupsToRemoveArticleFrom) {
                    viewModel.removeArticleFromGroup(group.getGroupID());
                }

                groupsToAddArticleTo.clear();
                groupsToRemoveArticleFrom.clear();

                // Invoke the callback to notify DashboardView that the article was saved
                onSaveCallback.run();
            } catch (Exception e) {
                e.printStackTrace(); // Handle exception (e.g., show error message)
            }
        });

        // Initially, the save button is disabled and uses the FILLED variant
        saveButton.setDisable(true);


        // Add listeners to input fields to dynamically control the save button's state
        inputTitle.getUserInput().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        inputSubtitle.getUserInput().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        inputBody.getUserInput().addListener((observable, oldValue, newValue) -> updateSaveButtonState());

        // Wrap the save button in an HBox and ensure it fills the horizontal space
     // Wrap the save button in an HBox and ensure it fills the horizontal space
        HBox saveButtonContainer = new HBox();
        saveButtonContainer.getChildren().add(saveButton);

        // Ensure the button stretches horizontally
        saveButton.setMaxWidth(Double.MAX_VALUE); // Allow button to grow
        HBox.setHgrow(saveButton, Priority.ALWAYS); // Set growth priority for button

        // Ensure the container also stretches horizontally
        saveButtonContainer.setAlignment(Pos.CENTER);
        saveButtonContainer.setFillHeight(true);
        saveButtonContainer.setMaxWidth(Double.MAX_VALUE);
        saveButtonContainer.setPrefWidth(Double.MAX_VALUE);

        // Add the save button container to the bottom content

        
        
        List<Group> availableGroups = viewModel.getAvailableGroups();// Fetch groups from ViewModel
        

        // Group Checkboxes
        Label groupHeading = new Label("Assign Groups:");
        groupHeading.setStyle("-fx-font-weight: 700; -fx-font-size: 14px; -fx-text-fill: #F8F8F8;");

        for (Group group : availableGroups) {
            CheckBox groupCheckBox = new CheckBox(group.getName());
            groupCheckBox.getStyleClass().add("accent-checkbox");
            groupCheckBox.setStyle("-fx-text-fill: #F8F8F8;"); // Update text color to #F8F8F8

            groupCheckBox.setSelected(viewModel.isArticleInGroup(group.getGroupID()));

            groupCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    groupsToAddArticleTo.add(group);
                } else {
                    groupsToRemoveArticleFrom.add(group);
                }
            });

            articleGroupItemContainer.getChildren().add(groupCheckBox);
        }
        articleGroupItemContainer.setSpacing(8);

        // Add all content to bottomContent
        bottomContent.getChildren().addAll(groupHeading, articleGroupItemContainer, saveButtonContainer);
        bottomContent.setSpacing(16);
        bottomContent.setFillWidth(true); // Ensure VBox stretches children horizontally
        bottomContent.setMaxWidth(Double.MAX_VALUE);




        // Add all UI elements to the VBox
        this.getChildren().addAll(heading, fieldWrapper, bottomContent);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.setPrefWidth(400);
        this.setSpacing(16);
        this.getStyleClass().add("pannel");
        this.setAlignment(Pos.TOP_LEFT);
    }
    
    
    
    
    // Utility method to update the save button state
    private void updateSaveButtonState() {
        boolean isReady = !inputTitle.getUserInput().get().isEmpty()
                && !inputSubtitle.getUserInput().get().isEmpty()
                && !inputBody.getUserInput().get().isEmpty();

        saveButton.setDisable(!isReady); // Disable the button if required fields are empty
        saveButton.setVariant(isReady ? PrimaryButton.ButtonVariant.ACCENT : PrimaryButton.ButtonVariant.FILLED);
    }

}



