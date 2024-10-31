package sendDesk360.view;

import sendDesk360.SendDesk360;
import sendDesk360.model.Article;
import sendDesk360.view.components.NavBar;
import sendDesk360.view.components.PrimaryButton;
import sendDesk360.view.components.ArticleGroupDropdown;
import sendDesk360.view.components.ArticleGroupDropdown.ArticleDropdownVariant;
import sendDesk360.view.components.PrimaryButton.ButtonVariant;
import sendDesk360.view.components.ArticlePreviewCard;
import sendDesk360.view.components.EditArticlePannel;
import sendDesk360.viewModel.ArticleViewModel;
import sendDesk360.viewModel.DashboardViewModel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class DashboardView extends VBox {

    private final DashboardViewModel dashboardViewModel;
    private VBox articleList;
    private PrimaryButton createNewArticle;
    private EditArticlePannel editPannel;

    public DashboardView(SendDesk360 mainApp) {
        this.dashboardViewModel = new DashboardViewModel(
            mainApp, 
            mainApp.getArticleManager(), 
            mainApp.getUserManager()
        );
        initializeUI(mainApp);
        initializeArticleDropdowns(mainApp);
    }

    private void initializeUI(SendDesk360 mainApp) {
        NavBar navBar = new NavBar();
        navBar.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(navBar, Priority.ALWAYS);
        navBar.setMaxWidth(300);
        navBar.setMaxHeight(Double.MAX_VALUE);

        Label pageTitle = new Label("Dashboard");
        pageTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

        articleList = new VBox();
        articleList.setAlignment(Pos.TOP_LEFT);
        articleList.setSpacing(0);
        HBox.setHgrow(articleList, Priority.ALWAYS);

        HBox pageTitleContainer = new HBox(pageTitle);
        pageTitleContainer.setStyle("-fx-padding: 48px 16px 16px 16px;");
        HBox.setHgrow(pageTitleContainer, Priority.ALWAYS);

        if (dashboardViewModel.isAdmin()) {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            createNewArticle = new PrimaryButton(ButtonVariant.ACCENT, "Create New", event -> openEditPanelForNewArticle(mainApp));
            pageTitleContainer.getChildren().addAll(spacer, createNewArticle);
        }
        
        pageTitleContainer.setAlignment(Pos.CENTER);

        VBox pageBody = new VBox(pageTitleContainer, articleList);
        pageBody.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(pageBody, Priority.ALWAYS);

        HBox mainPageBody = new HBox(navBar, pageBody);
        mainPageBody.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(mainPageBody, Priority.ALWAYS);
        VBox.setVgrow(mainPageBody, Priority.ALWAYS);

        this.getChildren().add(mainPageBody);
        this.setStyle("-fx-background-color: #101011;");
    }

    private void onArticleSaved(SendDesk360 mainApp, ArticleViewModel viewModel) {
        // Close the edit panel
        if (editPannel != null) {
            HBox mainPageBody = (HBox) this.getChildren().get(0);
            mainPageBody.getChildren().remove(editPannel);
            editPannel = null;
        }

        try {
            Article newArticle = viewModel.buildArticleFromProperties();
            articleList.getChildren().clear();
            initializeArticleDropdowns(mainApp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void openEditPanelForNewArticle(SendDesk360 mainApp) {
        if (dashboardViewModel.isAdmin()) {
            ArticleViewModel viewModel = new ArticleViewModel(mainApp.getArticleManager(), mainApp.getUserManager().getCurrentUser());
            viewModel.resetProperties();

            editPannel = new EditArticlePannel(viewModel, () -> onArticleSaved(mainApp, viewModel));
            HBox mainPageBody = (HBox) this.getChildren().get(0); 
            mainPageBody.getChildren().add(editPannel);
        }
    }

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

    private ArticleGroupDropdown createDropdown(ArticleDropdownVariant variant, SendDesk360 mainApp) throws Exception {
        String difficulty = variant.name().toLowerCase();
        List<Article> articles = dashboardViewModel.getArticlesByDifficulty(difficulty);

        List<ArticlePreviewCard> articleCards = articles.stream()
            .map(article -> new ArticlePreviewCard(article, mainApp))
            .toList();

        ArticleGroupDropdown dropdown = new ArticleGroupDropdown(variant, articleCards);
        HBox.setHgrow(dropdown, Priority.ALWAYS);

        return dropdown;
    }
}