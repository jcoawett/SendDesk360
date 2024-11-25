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
import sendDesk360.view.components.EditGroupPannel;
import sendDesk360.view.components.SearchBar;
import sendDesk360.view.components.SpecialGroupTag;
import sendDesk360.viewModel.ArticleViewModel;
import sendDesk360.viewModel.DashboardViewModel;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;


public class DashboardView extends VBox {

    private final DashboardViewModel dashboardViewModel;
    private VBox articleList;
    private PrimaryButton createNewArticle;
    private PrimaryButton addNewArticleGroup; 
    private EditArticlePannel editPannel;
    private EditGroupPannel editGroupPannel;
    private SearchBar searchBar;

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
        try {
            NavBar navBar = new NavBar();
            navBar.setAlignment(Pos.TOP_LEFT);
            VBox.setVgrow(navBar, Priority.ALWAYS);
            navBar.setMaxWidth(300);
            navBar.setMaxHeight(Double.MAX_VALUE);
            
            // Set the action for the Access Group button in NavBar
            navBar.setOnAccessGroupButtonClicked(() -> openAccessGroupView(mainApp));

            Label pageTitle = new Label("Dashboard");
            pageTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: 700; -fx-text-fill: #F8F8F8;");

            articleList = new VBox();
            articleList.setAlignment(Pos.TOP_LEFT);
            articleList.setSpacing(0);
            HBox.setHgrow(articleList, Priority.ALWAYS);


            List<Article> allArticles = dashboardViewModel.getAllArticles();
           
            
            
            searchBar = new SearchBar(allArticles, mainApp, new ArticleViewModel(mainApp.getArticleManager(), mainApp.getUserManager().getCurrentUser())); //I am not sure about this line


            searchBar.setPrefWidth(USE_COMPUTED_SIZE);

            HBox pageTitleContainer = new HBox(pageTitle, searchBar);
            pageTitleContainer.setStyle("-fx-padding: 48px 16px 16px 16px;");
            pageTitleContainer.setSpacing(16);
            
            HBox.setHgrow(pageTitleContainer, Priority.ALWAYS);

            if (dashboardViewModel.isAdmin()) {
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                createNewArticle = new PrimaryButton(ButtonVariant.ACCENT, "Create New", event -> openEditPanelForNewArticle(mainApp));
                addNewArticleGroup = new PrimaryButton(ButtonVariant.TEXT_ONLY, "Manage Groups", event -> openEditPanelForNewGrouping(mainApp));

                pageTitleContainer.setSpacing(8);
                pageTitleContainer.setAlignment(Pos.CENTER_RIGHT);

                pageTitleContainer.getChildren().addAll(spacer, createNewArticle, addNewArticleGroup);
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
        } catch (Exception e) {
            e.printStackTrace();
            display403Error();
        }
    }
    
    
    private void display403Error() {
        // Clear the current UI
        this.getChildren().clear();

        // Create a new VBox for the error message
        VBox errorContainer = new VBox();
        errorContainer.setStyle("-fx-background-color: #101011; -fx-padding: 50px;");
        errorContainer.setAlignment(Pos.CENTER);

        // Create the error label
        Label errorLabel = new Label("403 - Access Denied");
        errorLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #F8F8F8;");

        // Add the label to the container
        errorContainer.getChildren().add(errorLabel);

        // Add the error container to the dashboard
        this.getChildren().add(errorContainer);
    }

    private void onArticleSaved(SendDesk360 mainApp, ArticleViewModel viewModel) {
    	System.out.println("Called onArticleSaved");
        // Close the edit panel
        if (editPannel != null) {
            HBox mainPageBody = (HBox) this.getChildren().get(0);
            mainPageBody.getChildren().remove(editPannel);
            editPannel = null;
        }

        try {
            articleList.getChildren().clear();
            initializeArticleDropdowns(mainApp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void onGroupSaved(SendDesk360 mainApp, ArticleViewModel viewModel) {
    	System.out.println("On Group Saved Called");
    	if (editGroupPannel != null) {
    		 HBox mainPageBody = (HBox) this.getChildren().get(0);
             mainPageBody.getChildren().remove(editGroupPannel);
             editGroupPannel = null;
    	}
    	
    	try {
    		articleList.getChildren().clear(); 
    		initializeArticleDropdowns(mainApp);
    	} catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void openEditPanelForNewArticle(SendDesk360 mainApp) {
        if (dashboardViewModel.isAdmin()) {
            HBox mainPageBody = (HBox) this.getChildren().get(0);

            // Check if the edit panel is already open
            if (editPannel != null) {
                // Panel is already open, so close it
                mainPageBody.getChildren().remove(editPannel);
                editPannel = null; // Set to null to indicate it's closed
            } else {
                // Panel is not open, so create and open it
                ArticleViewModel viewModel = new ArticleViewModel(mainApp.getArticleManager(), mainApp.getUserManager().getCurrentUser());
                viewModel.resetProperties();

                editPannel = new EditArticlePannel(viewModel, () -> onArticleSaved(mainApp, viewModel));
                mainPageBody.getChildren().add(editPannel);
            }
        }
    }

    private void openEditPanelForNewGrouping(SendDesk360 mainApp) {
        if (dashboardViewModel.isAdmin()) {
            HBox mainPageBody = (HBox) this.getChildren().get(0);

            // Check if the group edit panel is already open
            if (editGroupPannel != null) {
                // Panel is already open, so close it
                mainPageBody.getChildren().remove(editGroupPannel);
                editGroupPannel = null; // Set to null to indicate it's closed
            } else {
                // Panel is not open, so create and open it
                ArticleViewModel viewModel = new ArticleViewModel(mainApp.getArticleManager(), mainApp.getUserManager().getCurrentUser());
                viewModel.resetProperties();

                editGroupPannel = new EditGroupPannel(viewModel, () -> onGroupSaved(mainApp, viewModel));
                mainPageBody.getChildren().add(editGroupPannel);
            }
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
        // Get difficulty as lowercase string
        String difficulty = variant.name().toLowerCase();

        // Get articles for the specified difficulty
        List<Article> articles = dashboardViewModel.getArticlesByDifficulty(difficulty);
        articles = dashboardViewModel.filterUnencryptedArticles(articles); 

        // Map articles to preview cards
        List<ArticlePreviewCard> articleCards = articles.stream()
            .map(article -> new ArticlePreviewCard(article, mainApp))
            .toList();

        // Pass the article count to the dropdown
        int articleCount = articles.size();

        // Create and return the ArticleGroupDropdown with the count
        ArticleGroupDropdown dropdown = new ArticleGroupDropdown(variant, articleCards, articleCount);
        HBox.setHgrow(dropdown, Priority.ALWAYS);

        return dropdown;
    }
    
    // Open AccessGroupView when button is clicked
    private void openAccessGroupView(SendDesk360 mainApp) {
        // Remove any existing content
        this.getChildren().clear();
        
        // Create the AccessGroupView
        mainApp.showAccessGroupView();
    }
}