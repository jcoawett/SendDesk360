package sendDesk360.view.components;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import sendDesk360.SendDesk360;
import sendDesk360.model.Article;
import sendDesk360.model.Group;
import sendDesk360.viewModel.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchBar extends StackPane {

    private TextField searchField;
    private VBox searchResultsContainer; // Container for displaying search results
    private final List<Article> articles; // List of articles to search from
    private Popup resultsPopup; // For dropdown behavior
    private Popup filtersPopup; 
    private final SendDesk360 mainApp;
    private ArticleViewModel viewModel; 
    private Group selectedGroup; 
    
    SmallButton clearButton;
    SmallButton filterButton; 

    public SearchBar(List<Article> articles, SendDesk360 mainApp, ArticleViewModel viewModel) {
        this.articles = articles;
        this.mainApp = mainApp;
        this.viewModel = viewModel; 
        initializeUI();
    }

    private void initializeUI() {
        // Search field with placeholder
        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setStyle("-fx-font-size: 16px; -fx-text-fill: #F8F8F8; -fx-background-color: #0F1011; -fx-border-color: #28292E; -fx-border-radius: 5px; -fx-padding: 10px;");
        searchField.setPrefWidth(400);

        // Search icon
        ImageView searchIcon = new ImageView(new Image(getClass().getResourceAsStream("/sendDesk360/view/assets/search.png")));
        searchIcon.setFitHeight(24);
        searchIcon.setPreserveRatio(true);

        // Create a container for the search field and icon
        HBox searchFieldContainer = new HBox(8, searchIcon, searchField);
        searchFieldContainer.setAlignment(Pos.CENTER_LEFT);

        // Buttons container
        clearButton = new SmallButton("Clear", event -> clearSearch());
        filterButton = new SmallButton("Filters", event -> showFiltersPopup());
        HBox actionButtons = new HBox(8, filterButton, clearButton);
        actionButtons.setAlignment(Pos.CENTER);

        // Initialize popup for search results
        resultsPopup = new Popup();
        resultsPopup.setAutoHide(true);
        
     // Initialize popup for filters
        filtersPopup = new Popup();
        filtersPopup.setAutoHide(true);

        searchResultsContainer = new VBox();
        searchResultsContainer.setSpacing(8);
        searchResultsContainer.setStyle("-fx-background-color: #17181A; -fx-border-radius: 8px; -fx-padding: 8px;");
        resultsPopup.getContent().add(searchResultsContainer);

        // Listen for text input and update the results
        searchField.textProperty().addListener((observable, oldValue, newValue) -> updateSearchResults(newValue));

        // Combine all elements
        HBox searchBarWithButtons = new HBox(16, searchFieldContainer, actionButtons);
        searchBarWithButtons.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().add(searchBarWithButtons);
    }

    private void clearSearch() {
        searchField.clear();
        searchResultsContainer.getChildren().clear();
        resultsPopup.hide();
    }

    private void updateSearchResults(String query) {
        searchResultsContainer.getChildren().clear();

        if (query.isEmpty()) {
            resultsPopup.hide();
            return;
        }

        List<Article> matchingArticles = articles.stream()
                .filter(article -> article.getTitle().toLowerCase().contains(query.toLowerCase())
                        || article.getShortDescription().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        for (Article article : matchingArticles) {
            HBox resultItem = new HBox();
            resultItem.setStyle("-fx-background-color: #28292E; -fx-padding: 10px; -fx-border-radius: 5px;");
            resultItem.setAlignment(Pos.CENTER_LEFT);
            resultItem.setOnMouseClicked(event -> openArticle(article)); // Add click behavior

            Label resultText = new Label(article.getTitle() + ": " + article.getShortDescription());
            resultText.setStyle("-fx-text-fill: #F8F8F8; -fx-font-size: 14px;");
            resultItem.getChildren().add(resultText);

            searchResultsContainer.getChildren().add(resultItem);
        }

        if (!resultsPopup.isShowing()) {
            Window window = this.getScene().getWindow();
            resultsPopup.setWidth(searchField.getWidth()); // Match the dropdown width with the search field
            resultsPopup.show(window,
                    window.getX() + searchField.localToScene(0, 0).getX() + searchField.getScene().getX(),
                    window.getY() + searchField.localToScene(0, 0).getY() + searchField.getScene().getY() + searchField.getHeight());
        }
    }

    private void updateSearchResultsFilter(Group selectedGroup) {
    	filtersPopup.hide(); 
        searchResultsContainer.getChildren().clear();
        
        List<Article> matchingArticles = new ArrayList<Article>(); 

        System.out.println("Checking for group: " + selectedGroup.getName()); 
        if (selectedGroup != null) {
            matchingArticles = articles.stream()
                .filter(article -> viewModel.isArticleInGroup(article.getArticleID(), selectedGroup.getGroupID()))
                .collect(Collectors.toList());
            
            // Display or use the matching articles as needed
            matchingArticles.forEach(article -> System.out.println("Matched Article: " + article.getTitle()));
        }


        for (Article article : matchingArticles) {
            HBox resultItem = new HBox();
            resultItem.setStyle("-fx-background-color: #28292E; -fx-padding: 10px; -fx-border-radius: 5px;");
            resultItem.setAlignment(Pos.CENTER_LEFT);
            resultItem.setOnMouseClicked(event -> openArticle(article)); // Add click behavior

            Label resultText = new Label(article.getTitle() + ": " + article.getShortDescription());
            resultText.setStyle("-fx-text-fill: #F8F8F8; -fx-font-size: 14px;");
            resultItem.getChildren().add(resultText);

            searchResultsContainer.getChildren().add(resultItem);
        }

        if (!resultsPopup.isShowing()) {
            Window window = this.getScene().getWindow();
            resultsPopup.setWidth(searchField.getWidth()); // Match the dropdown width with the search field
            resultsPopup.show(window,
                    window.getX() + searchField.localToScene(0, 0).getX() + searchField.getScene().getX(),
                    window.getY() + searchField.localToScene(0, 0).getY() + searchField.getScene().getY() + searchField.getHeight());
        }
    }
    
    
    private void openArticle(Article article) {
        System.out.println("Navigating to article: " + article.getTitle());
        mainApp.showArticleDetailView(article); // Navigate to the article detail view
        resultsPopup.hide(); // Close the dropdown
    }
    
    private void showFiltersPopup() {
    	VBox filterContent = new VBox();
        filterContent.setSpacing(8);
        filterContent.setStyle("-fx-background-color: #17181A; -fx-border-radius: 8px; -fx-padding: 8px;");

        // Create dropdown for groups
        ComboBox<String> groupSelector = new ComboBox<>();
        List<Group> currentGroups = viewModel.getAvailableGroups(); 
        for (Group g: currentGroups) {
        	groupSelector.getItems().add(g.getName()); 
        }
        
        
        groupSelector.setPromptText("Select a group...");
        groupSelector.setStyle("-fx-font-size: 14px;");

        // Set the width of the dropdown to be smaller
        groupSelector.setPrefWidth(200);
        
     // Set the width of the items in the dropdown list
        groupSelector.setCellFactory(param -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);
                    }
                }
            };
            // Set a fixed width for the items in the dropdown
            cell.setPrefWidth(200);  // Set the width for the options in the dropdown
            return cell;
        });
        
        
        filterContent.getChildren().add(groupSelector);

        filtersPopup.getContent().clear();
        filtersPopup.getContent().add(filterContent);

        if (!filtersPopup.isShowing()) {
            // Calculate position relative to the filter button
            Window window = this.getScene().getWindow();
            double buttonX = filterButton.localToScene(0, 0).getX() + filterButton.getScene().getX();
            double buttonY = filterButton.localToScene(0, 0).getY() + filterButton.getScene().getY() + filterButton.getHeight();

            // Show popup underneath the Filter button
            filtersPopup.show(window, window.getX() + buttonX, window.getY() + buttonY);
        }
        
     // Set on action for group selection
        groupSelector.setOnAction(event -> {
            String selectedGroupName = groupSelector.getValue();
            if (selectedGroupName != null) {
                currentGroups.stream()
                    .filter(group -> group.getName().equals(selectedGroupName))
                    .findFirst()
                    .ifPresent(group -> {
                        selectedGroup = group; // Update the instance variable selectedGroup
                        System.out.println("Selected Group Updated: " + group.getName());
                        updateSearchResultsFilter(selectedGroup); 
                    });
            }
        });
        
       
    }        
            
    }
        
       