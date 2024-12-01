package sendDesk360.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.awt.TextArea;

import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import sendDesk360.viewModel.ArticleViewModel;
import sendDesk360.viewModel.DashboardViewModel;
import sendDesk360.SendDesk360;
import sendDesk360.view.components.NavBar;
import sendDesk360.view.components.PrimaryButton;
import sendDesk360.view.components.PrimaryField;
import sendDesk360.view.components.SmallButton;
import sendDesk360.view.components.PrimaryButton.ButtonVariant;
import sendDesk360.view.components.EditArticlePannel;
import sendDesk360.view.components.PrimaryField;

public class ArticleDetailView extends VBox {

    private EditArticlePannel editPanel;
    private HBox mainPageBody;
    private VBox pageBody;
    private VBox commentWrapper; // Class-level declaration for comments section

    public ArticleDetailView(ArticleViewModel viewModel, SendDesk360 mainApp, DashboardViewModel dashboardViewModel) {
        initializeUI(viewModel, mainApp, dashboardViewModel);
    }

    private void initializeUI(ArticleViewModel viewModel, SendDesk360 mainApp, DashboardViewModel dashboardViewModel) {
        // Navigation bar setup
        NavBar navBar = new NavBar(mainApp, () -> mainApp.showDashboard(), () -> mainApp.showProfileView(), dashboardViewModel);
        navBar.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(navBar, Priority.ALWAYS);
        navBar.setMaxWidth(300);
        navBar.setMaxHeight(Double.MAX_VALUE);

        // Page title and body
        Label pageTitle = new Label();
        pageTitle.textProperty().bind(viewModel.titleProperty());
        pageTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

        Label articleBody = new Label();
        articleBody.textProperty().bind(viewModel.bodyProperty());
        articleBody.setWrapText(true);
        articleBody.setStyle("-fx-font-size: 14px; -fx-text-fill: #D3D3D3; -fx-padding: 0px 16px 0 16px;");

        // Buttons
        HBox buttonWrapper = new HBox();
        PrimaryButton backButton = new PrimaryButton(ButtonVariant.TEXT_ONLY, "Back", event -> mainApp.showDashboard());
        if (viewModel.isAdmin()) {
            PrimaryButton editButton = new PrimaryButton(ButtonVariant.FILLED, "Edit", event -> toggleEditPanel(viewModel, mainApp));
            PrimaryButton deleteButton = new PrimaryButton(ButtonVariant.TEXT_ONLY, "Delete", event -> {
                try {
                    viewModel.deleteArticle();
                    mainApp.showDashboard();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            buttonWrapper.getChildren().addAll(editButton, deleteButton);
        }
        buttonWrapper.getChildren().add(backButton);
        buttonWrapper.setSpacing(16);

        // Layout containers
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox pageTitleContainer = new HBox(pageTitle, spacer, buttonWrapper);
        pageTitleContainer.setStyle("-fx-padding: 48px 16px 0px 16px;");
        HBox.setHgrow(pageTitleContainer, Priority.ALWAYS);

        Region Vspacer = new Region();
        VBox.setVgrow(Vspacer, Priority.ALWAYS);

        // COMMENTS SECTION
        commentWrapper = new VBox(); // Initialize the commentWrapper
        commentWrapper.setStyle("-fx-padding: 16px;");
        commentWrapper.setSpacing(8);

        Label commentHeader = new Label("Student Questions");
        commentHeader.setStyle("-fx-font-size: 24px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");
        commentWrapper.getChildren().add(commentHeader);

        // Comment Input Field
        PrimaryField commentInput = new PrimaryField(PrimaryField.FieldVariant.DEFAULT, "Enter your question...");
        commentInput.setMaxWidth(600);
        HBox.setHgrow(commentInput, Priority.ALWAYS);

        SmallButton postButton = new SmallButton("Post", event -> {
            String commentText = commentInput.getUserInput().get();
            if (commentText != null && !commentText.trim().isEmpty()) {
                viewModel.postComment(commentText); // Save comment to the database
                commentInput.getUserInput().set(""); // Clear the input field
                refreshComments(viewModel); // Refresh comments UI
            }
        });

        HBox commentFieldWrapper = new HBox(commentInput, postButton);
        commentFieldWrapper.setSpacing(8);
        commentWrapper.getChildren().add(commentFieldWrapper);

        // Article Title and Body
        VBox articleTitleNBody = new VBox(pageTitleContainer, articleBody);
        articleTitleNBody.setAlignment(Pos.TOP_LEFT);
        articleTitleNBody.setSpacing(16);
        HBox.setHgrow(articleTitleNBody, Priority.ALWAYS);

        VBox articleAndComments = new VBox();
        articleAndComments.getChildren().addAll(articleTitleNBody, Vspacer, commentWrapper);
        articleAndComments.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(articleAndComments, Priority.ALWAYS);
        HBox.setHgrow(articleAndComments, Priority.ALWAYS);

        pageBody = new VBox(articleAndComments);
        pageBody.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(pageBody, Priority.ALWAYS);

        // Main layout
        mainPageBody = new HBox(navBar, pageBody);
        mainPageBody.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(mainPageBody, Priority.ALWAYS);
        VBox.setVgrow(mainPageBody, Priority.ALWAYS);

        this.getChildren().add(mainPageBody);
        this.setStyle("-fx-background-color: #101011;");

        // Load and display comments on initialization
        viewModel.loadComments(); // Load comments from the database
        refreshComments(viewModel); // Refresh the comments in the UI
    }

    private void refreshComments(ArticleViewModel viewModel) {
        // Clear existing comments (except header and input field)
        commentWrapper.getChildren().removeIf(node -> !(node instanceof HBox || node instanceof Label && ((Label) node).getText().equals("Student Questions")));

        // Add comments from the view model
        for (String comment : viewModel.commentsProperty()) {
            Label commentLabel = new Label(comment);
            commentLabel.setWrapText(true);
            commentLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-padding: 5;");
            commentWrapper.getChildren().add(commentLabel);
        }
    }


    // Toggle the visibility of the EditArticlePannel on the right side of the screen
    private void toggleEditPanel(ArticleViewModel viewModel, SendDesk360 mainApp) {
        // Check if the panel is already open
        if (editPanel != null) {
            // Remove the edit panel
            mainPageBody.getChildren().remove(editPanel);
            editPanel = null; // Clear reference
        } else {
            // Create the panel dynamically
            editPanel = new EditArticlePannel(viewModel, () -> {
                onArticleSaved(viewModel);
                // Close the panel after saving
                mainPageBody.getChildren().remove(editPanel);
                editPanel = null;
            });

            // Add the panel to the main layout
            mainPageBody.getChildren().add(editPanel);
            HBox.setHgrow(editPanel, Priority.ALWAYS); // Ensure proper resizing
        }
    }

    // Callback to refresh article view after saving
    private void onArticleSaved(ArticleViewModel viewModel) {
        editPanel.setVisible(false);
        viewModel.loadArticle(viewModel.buildArticleFromProperties()); // Reload updated article properties
    }
} 