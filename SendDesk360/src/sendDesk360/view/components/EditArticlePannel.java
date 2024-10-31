package sendDesk360.view.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sendDesk360.viewModel.ArticleViewModel;


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
        inputSubtitle = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Subtitle");
        inputBody = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Body");
        inputKeywords = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Keywords");
        inputRelatedArticles = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Related Articles");
        inputLinks = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Reference Links");

        // Bind fields to the view model properties
        inputTitle.getUserInput().bindBidirectional(viewModel.titleProperty());
        inputDifficulty.getUserInput().bindBidirectional(viewModel.difficultyProperty());
        inputBody.getUserInput().bindBidirectional(viewModel.bodyProperty());
        inputKeywords.getUserInput().unbindBidirectional(viewModel.keywordsProperty());
        
        
        fieldWrapper.getChildren().addAll(inputTitle, inputDifficulty, inputSubtitle, inputBody, inputKeywords, inputRelatedArticles, inputLinks);
        
        // Save button with action to update or save article
        saveButton = new PrimaryButton(PrimaryButton.ButtonVariant.FILLED, "Save", event -> {
            try {
                // Check if it's a new article (no article ID) or an update
                if (viewModel.articleIDProperty().get() == 0) {
                    viewModel.saveNewArticle();  // Save as a new article
                } else {
                    viewModel.updateArticle();  // Update existing article
                }
                
                // Invoke the callback to notify DashboardView that the article was saved
                onSaveCallback.run();
            } catch (Exception e) {
                e.printStackTrace(); // Handle exception (e.g., show error message)
            }
        });
        

        // Add all UI elements to the VBox
        this.getChildren().addAll(heading, fieldWrapper, saveButton);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.setPrefWidth(400);
        this.setSpacing(16);
        this.getStyleClass().add("edit-pannel");
        this.setAlignment(Pos.TOP_LEFT);
    }
}