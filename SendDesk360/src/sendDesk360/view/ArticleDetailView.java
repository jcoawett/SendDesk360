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

public class ArticleDetailView extends VBox {

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

        if (viewModel.isAdmin()) {
            PrimaryButton editButton = new PrimaryButton(ButtonVariant.FILLED, "Edit", event -> System.out.println("Edit clicked"));
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

        VBox pageBody = new VBox(pageTitleContainer, articleBody);
        pageBody.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(pageBody, Priority.ALWAYS);

        HBox mainPageBody = new HBox(navBar, pageBody);
        mainPageBody.setAlignment(Pos.CENTER_LEFT);
        VBox.setVgrow(mainPageBody, Priority.ALWAYS);

        this.getChildren().add(mainPageBody);
        this.setStyle("-fx-background-color: #101011;");
    }
}