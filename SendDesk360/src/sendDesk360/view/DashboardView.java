package sendDesk360.view;

import sendDesk360.SendDesk360;
import sendDesk360.view.components.NavBar;
import sendDesk360.view.components.ArticleGroupDropdown;
import sendDesk360.view.components.ArticleGroupDropdown.ArticleDropdownVariant;
import sendDesk360.view.components.ArticlePreviewCard;
import sendDesk360.viewModel.DashboardViewModel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class DashboardView extends VBox {

    private final DashboardViewModel dashboardViewModel;
    private VBox articleList;

    // Constructor receiving the main app instance
    public DashboardView(SendDesk360 mainApp) {
        this.dashboardViewModel = new DashboardViewModel(
            mainApp, 
            mainApp.getArticleManager(), 
            mainApp.getUserManager()
        );
        initializeUI(); // Setup UI
        initializeArticleDropdowns(mainApp); // Populate dropdowns
    }

    // Initialize the UI components
    private void initializeUI() {
        NavBar navBar = new NavBar(); // Left navigation bar
        navBar.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(navBar, Priority.ALWAYS);
        navBar.setMaxWidth(300);
        navBar.setMaxHeight(Double.MAX_VALUE);

        Label pageTitle = new Label("Dashboard");
        pageTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

        // Article list container
        articleList = new VBox();
        articleList.setAlignment(Pos.TOP_LEFT);
        articleList.setSpacing(0); // Spacing between dropdowns
        HBox.setHgrow(articleList, Priority.ALWAYS); // Ensure it grows horizontally

        VBox pageTitleContainer = new VBox();
        pageTitleContainer.getChildren().add(pageTitle);
        pageTitleContainer.setStyle("-fx-padding: 48px 16px 16px 16px;");
        pageTitleContainer.setSpacing(0);
        HBox.setHgrow(pageTitleContainer, Priority.ALWAYS);
        
        // Page body container
        VBox pageBody = new VBox(pageTitleContainer, articleList);
        pageBody.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(pageBody, Priority.ALWAYS);

        // Main page body (nav bar + page content)
        HBox mainPageBody = new HBox(navBar, pageBody);
        mainPageBody.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(mainPageBody, Priority.ALWAYS);
        VBox.setVgrow(mainPageBody, Priority.ALWAYS);

        // Add everything to the root layout
        this.getChildren().add(mainPageBody);
        this.setStyle("-fx-background-color: #101011;");
    }

    // Initialize article dropdowns and populate them dynamically

    private void initializeArticleDropdowns(SendDesk360 mainApp) {
        try {
            articleList.getChildren().add(createDropdown(ArticleDropdownVariant.BEGINNER, mainApp));
            articleList.getChildren().add(createDropdown(ArticleDropdownVariant.INTERMEDIATE, mainApp));
            articleList.getChildren().add(createDropdown(ArticleDropdownVariant.ADVANCED, mainApp));
            articleList.getChildren().add(createDropdown(ArticleDropdownVariant.EXPERT, mainApp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

    // Create a dropdown for the specified difficulty level
    private ArticleGroupDropdown createDropdown(ArticleDropdownVariant variant, SendDesk360 mainApp) throws Exception {
        String difficulty = variant.name().toLowerCase();
        List<sendDesk360.model.Article> articles = dashboardViewModel.getArticlesByDifficulty(difficulty);

        List<ArticlePreviewCard> articleCards = articles.stream()
            .map(article -> new ArticlePreviewCard(article, mainApp))
            .toList();

        ArticleGroupDropdown dropdown = new ArticleGroupDropdown(variant, articleCards);
        HBox.setHgrow(dropdown, Priority.ALWAYS);

        return dropdown;
    }
}