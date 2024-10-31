package sendDesk360.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import sendDesk360.viewModel.ArticleViewModel;
import sendDesk360.SendDesk360;
import sendDesk360.view.components.NavBar;
import sendDesk360.view.components.PrimaryButton;
import sendDesk360.view.components.PrimaryButton.ButtonVariant;
import sendDesk360.view.components.EditArticlePannel;

public class ArticleDetailView extends VBox {

    private EditArticlePannel editPanel;
    private HBox mainPageBody;
    private VBox pageBody;

    public ArticleDetailView(ArticleViewModel viewModel, SendDesk360 mainApp) {
        initializeUI(viewModel, mainApp);
    }

    private void initializeUI(ArticleViewModel viewModel, SendDesk360 mainApp) {
        // Navigation bar setup
        NavBar navBar = new NavBar();
        navBar.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(navBar, Priority.ALWAYS);
        navBar.setMaxWidth(300);
        navBar.setMaxHeight(Double.MAX_VALUE);

        // Bind title and body directly to the view model's properties
        Label pageTitle = new Label();
        pageTitle.textProperty().bind(viewModel.titleProperty());
        pageTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

        Label articleBody = new Label();
        articleBody.textProperty().bind(viewModel.bodyProperty());
        articleBody.setWrapText(true);
        articleBody.setStyle("-fx-font-size: 14px; -fx-text-fill: #D3D3D3; -fx-padding: 0 16px 0 16px;");

        // Navigation and control buttons
        HBox buttonWrapper = new HBox();
        PrimaryButton backButton = new PrimaryButton(ButtonVariant.TEXT_ONLY, "Back", event -> mainApp.showDashboard());

        // Add edit and delete buttons for admin users
        if (viewModel.isAdmin()) {
            PrimaryButton editButton = new PrimaryButton(ButtonVariant.FILLED, "Edit", event -> toggleEditPanel(viewModel));
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
        pageTitleContainer.setStyle("-fx-padding: 48px 16px 16px 16px;");

        pageBody = new VBox(pageTitleContainer, articleBody);
        pageBody.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(pageBody, Priority.ALWAYS);

        // Create main layout with nav bar, page body, and an empty space for edit panel
        mainPageBody = new HBox(navBar, pageBody);
        mainPageBody.setAlignment(Pos.CENTER_LEFT);
        VBox.setVgrow(mainPageBody, Priority.ALWAYS);

        // Initialize Edit Panel (hidden by default)
        editPanel = new EditArticlePannel(viewModel, () -> onArticleSaved(viewModel));
        editPanel.setVisible(false); // Hide by default

        // Add editPanel to mainPageBody without making it visible initially
        mainPageBody.getChildren().add(editPanel);

        this.getChildren().add(mainPageBody);
        this.setStyle("-fx-background-color: #101011;");
    }

    // Toggle the visibility of the EditArticlePannel on the right side of the screen
    private void toggleEditPanel(ArticleViewModel viewModel) {
        editPanel.setVisible(!editPanel.isVisible());

        // Adjust layout priority when panel is shown or hidden
        if (editPanel.isVisible()) {
            HBox.setHgrow(pageBody, Priority.ALWAYS);
            VBox.setVgrow(editPanel, Priority.ALWAYS);
        } else {
            HBox.setHgrow(pageBody, Priority.ALWAYS);
            VBox.setVgrow(editPanel, Priority.ALWAYS);
        }
    }

    // Callback to refresh article view after saving
    private void onArticleSaved(ArticleViewModel viewModel) {
        editPanel.setVisible(false);
        viewModel.loadArticle(viewModel.buildArticleFromProperties()); // Reload updated article properties
    }
}