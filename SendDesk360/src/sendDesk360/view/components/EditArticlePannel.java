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
    private VBox groupCheckboxContainer;
    
    private List<Group> groupsToAddArticleTo = new ArrayList<Group>();
    private List<Group> groupsToRemoveArticleFrom = new ArrayList<Group>(); 

    public EditArticlePannel(ArticleViewModel viewModel, Runnable onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
        initializeUI(viewModel);
    }

    private void initializeUI(ArticleViewModel viewModel) {
        // Heading label
        heading = new Label("Edit Article");
        heading.setStyle("-fx-text-fill: #F8F8F8; -fx-font-weight: 700; -fx-font-size: 16px;");
        
        VBox fieldWrapper = new VBox();
       
        // Initialize fields with appropriate variants and placeholders
        inputTitle = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Title");
        inputDifficulty = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Difficulty");
        inputSubtitle = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Short Description");
        inputBody = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Body");
        inputKeywords = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Keywords");
        inputRelatedArticles = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Related Articles");
        inputLinks = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Reference Links");

        // Bind fields to the view model properties
        inputTitle.getUserInput().bindBidirectional(viewModel.titleProperty());
        inputDifficulty.getUserInput().bindBidirectional(viewModel.difficultyProperty());
        inputBody.getUserInput().bindBidirectional(viewModel.bodyProperty());
        inputKeywords.getUserInput().unbindBidirectional(viewModel.keywordsProperty());
        inputSubtitle.getUserInput().bindBidirectional(viewModel.shortDescriptionProperty()); 
        
        
        fieldWrapper.getChildren().addAll(inputTitle, inputDifficulty, inputSubtitle, inputBody, inputKeywords, inputRelatedArticles, inputLinks);
        
        // Group Checkboxes
        groupCheckboxContainer = new VBox();
        Label groupHeading = new Label("Assign Groups:");
        groupHeading.setStyle("-fx-font-weight: 700; -fx-font-size: 14px; -fx-text-fill: #F8F8F8;");
        groupCheckboxContainer.getChildren().add(groupHeading);

        // Add checkboxes dynamically for each group in the system
        List<Group> availableGroups = viewModel.getAvailableGroups();  // Fetch groups from ViewModel
        for (Group group : availableGroups) {
            CheckBox groupCheckBox = new CheckBox(group.getName());
            groupCheckBox.setStyle("-fx-text-fill: #F8F8F8;");
            
            // Initialize checkbox based on whether the article is already in the group
            groupCheckBox.setSelected(viewModel.isArticleInGroup(group.getGroupID()));

            // Add listener to handle adding/removing article from group
            groupCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                	System.out.println("Adding group: " + group.getName() + " to the list of groups to add this article to"); 
                	groupsToAddArticleTo.add(group); 
                } else {
                	System.out.println("Adding group: " + group.getName() + " to the list of groups to remove this article from"); 
                	groupsToRemoveArticleFrom.add(group); 
                }
            });

            groupCheckboxContainer.getChildren().add(groupCheckBox);
        }

        // Save button with action to update or save article
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
                
                for (Group group: groupsToRemoveArticleFrom) {
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

        // Add all UI elements to the VBox
        this.getChildren().addAll(heading, fieldWrapper, groupCheckboxContainer, saveButton);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.setPrefWidth(400);
        this.setSpacing(16);
        this.getStyleClass().add("edit-pannel");
        this.setAlignment(Pos.TOP_LEFT);
    }
}
