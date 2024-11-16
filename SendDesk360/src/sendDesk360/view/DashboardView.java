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
        
        
        searchBar = new SearchBar();
        searchBar.setPrefWidth(400);
        searchBar.setPrefHeight(getPrefWidth());        
        

        HBox pageTitleContainer = new HBox(pageTitle, searchBar);
        pageTitleContainer.setStyle("-fx-padding: 48px 16px 16px 16px;");
        HBox.setHgrow(pageTitleContainer, Priority.ALWAYS);

        if (dashboardViewModel.isAdmin()) {
        	 Region spacer = new Region();
        	 HBox.setHgrow(spacer, Priority.ALWAYS);

        	 createNewArticle = new PrimaryButton(ButtonVariant.ACCENT, "Create New", event -> openEditPanelForNewArticle(mainApp));
        	 addNewArticleGroup = new PrimaryButton(ButtonVariant.ACCENT, "Manage Groups", event -> openEditPanelForNewGrouping(mainApp));

        	  // Set spacing and alignment for better layout
        	  pageTitleContainer.setSpacing(10); 
        	  pageTitleContainer.setAlignment(Pos.CENTER_RIGHT);  // Align items to the left

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